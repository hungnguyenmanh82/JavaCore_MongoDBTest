package hung.com.test.CRUD.insert;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

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
public class App3_insertDocument {

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
			MongoDatabase database = mongo.getDatabase(databaseName); 

			//====================================================================
			//create new collection if not find
			MongoCollection<Document> collection = database.getCollection("sampleCollection");
			//cách này cho performance tốt hơn là dùng Json
			Document document = new Document("title", "MongoDB") 
					.append("id", 1)
					.append("description", "database") 
					.append("likes", 100) 
					.append("url", "http://www.tutorialspoint.com/mongodb/") 
					.append("by", "tutorials point");  
			collection.insertOne(document);
			
			//ObjectId đc gen ở Client tự động khi tạo 1 document nếu ta ko chủ động thêm vào(ko phải trên MongoDB server)
			ObjectId id = (ObjectId)document.get( "_id" );
			System.out.println(document.get("_id"));
			//cách 2:
//			document.append("_id", new ObjectId());
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
