package hung.com.test.function;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.BsonJavaScript;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.event.ServerClosedEvent;
import com.mongodb.event.ServerDescriptionChangedEvent;
import com.mongodb.event.ServerListener;
import com.mongodb.event.ServerOpeningEvent;
import com.mongodb.util.JSON;

/**
 * create an MongoDB user with root:
 * 
		use Mydb
		db.createUser({user:"MydbUser",pwd:"123",roles:[{role:"readWrite",db:"Mydb"}]})

 */
public class App91_ObjectId {

	private static final String address = "localhost";
	private static final int port = 27017;
	//phải dùng quyền admin thì mới tạo đc function
	private static final String user = "admin";
	private static final String password = "123";
	private static final String databaseName = "admin";


	public static void main(String[] args) {
		// http://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/authentication/ 

		try {
			MongoCredential credential = MongoCredential.createCredential(user,databaseName,password.toCharArray());
			MongoClientOptions options = MongoClientOptions.builder()											
					.addServerListener(serverListener)
					.build();
			MongoClient mongo = new MongoClient(new ServerAddress(address,port),credential, options); 
			MongoDatabase database = mongo.getDatabase(databaseName); 
			//==========================================create Function and save in database
			BsonDocument echoFunction = new BsonDocument("value",
			        new BsonJavaScript("function() { return ObjectId().str}"));

			
			// "system.js" là collection của MongoDB chứa javascript
			database.getCollection("system.js").updateOne(
			        new Document("_id", "echoFunction"),
			        new Document("$set", echoFunction),
			        new UpdateOptions().upsert(true));  //nếu ko có thì insert
		

			//====================================================================
			//create new collection if not find
			database.runCommand(new Document("$eval", "db.loadServerScripts()"));
			Document returnDoc1 =  database.runCommand(new Document("$eval", "echoFunction()"));		
			System.out.println("---------------------------------");
			System.out.println(returnDoc1);  //lưu ý  key = "retval"
			System.out.println("echoFunction(9) = " + returnDoc1.get("retval"));
			

			
			//====================================================================
			mongo.close();
		} catch (MongoException  e) {

			System.out.println("=========================================");
			e.printStackTrace();
		}


	}

	private static ServerListener serverListener = new ServerListener() {

		public void serverOpening(ServerOpeningEvent event) {
//			System.out.println("*****************"+ event);

		}

		public void serverDescriptionChanged(ServerDescriptionChangedEvent event) {
//			System.out.println("++++++"+ event);

		}

		public void serverClosed(ServerClosedEvent event) {
//			System.out.println("----------------"+ event);
		}
	};



}
