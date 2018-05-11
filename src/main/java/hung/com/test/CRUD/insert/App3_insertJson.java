package hung.com.test.CRUD.insert;


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
public class App3_insertJson {

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
			//create new collection if not find
			MongoCollection<Document> collection = database.getCollection("sampleCollection");
			
			//Double quote is Json standard
			//MongoDB accepts single quote in Json (but single quote is not Json standard)
			//dùng cú pháp json hay hơn dùng thư viện java. Vì nó cho phép dùng với Java, PHP, NodeJs,Shell command... đều ok.
			String json = "{" +
					"'title':'MySQL', "+
					"'id':3,"+
					"'description':'database',"+
					"'likes':140,"+
					"'url':'http://www.tuvi.com',"+
					"'by':'hungbeo'"+
					"}";
/*			String json = "{" +
					"title:'MySQL', "+
					"'id':3,"+
					"'description':'database',"+
					"'likes':140,"+
					"'url':'http://www.tuvi.com',"+
					"'by':'hungbeo'"+
					"}";*/
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
