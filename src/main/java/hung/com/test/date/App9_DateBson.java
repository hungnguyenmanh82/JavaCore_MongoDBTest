package hung.com.test.date;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.event.ServerClosedEvent;
import com.mongodb.event.ServerDescriptionChangedEvent;
import com.mongodb.event.ServerListener;
import com.mongodb.event.ServerOpeningEvent;

/**
 * create an MongoDB user with root:
 * 
	{
		"_id":"5aeadf6432ff4031fcc89550",
		"title":"MongoDB",
		"id":1,
		"description":"database",
		"likes":120,
		"url":"http://www.tutorialspoint.com/mongodb/",
		"by":"tutorials point"
	}
 */
public class App9_DateBson {

	private static final String address = "localhost";
	private static final int port = 27017;
	//
	private static final String user = "MydbUser";
	private static final String password = "123";
	private static final String databaseName = "Mydb";

	public static void main(String[] args) {
		// http://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/authentication/ 

		try {
			MongoCredential credential = MongoCredential.createCredential(user,databaseName,password.toCharArray());
			MongoClientOptions options = MongoClientOptions.builder()											
					.addServerListener(serverListener)
					.build();
			MongoClient mongo = new MongoClient(new ServerAddress(address,port),credential, options); 
			MongoDatabase database = mongo.getDatabase("Mydb"); 

			//====================================================================
			MongoCollection<Document> collection = database.getCollection("myCollection");
			
			
			String json = "{" +
					"\"_id\":1, "+
					"\"item\":\"database\","+
					"\"price\":140,"+
					"\"quantity\":22,"+
					"\"date\": new Date()"+
					"}";
			Document doc = Document.parse(json);
			collection.insertOne(doc);
			
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
