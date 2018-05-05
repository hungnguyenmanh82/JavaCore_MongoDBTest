package hung.com.test.CRUD.insert;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

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
public class App3_insertDocumentArray {

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
			MongoCollection<Document> collection = database.getCollection("sampleColArray");
			//cách này cho performance tốt hơn là dùng Json
			//1
			Document journal = new Document("item", "journal")
			        .append("qty", 25)
			        .append("tags", Arrays.asList("blank", "red"));

			Document journalSize = new Document("h", 14)
			        .append("w", 21)
			        .append("uom", "cm");
			journal.append("size", journalSize); // https://stackoverflow.com/questions/6570088/mongodb-java-api-put-vs-append
			
			//2
			Document mat = new Document("item", "mat")
			        .append("qty", 85)
			        .append("tags", Arrays.asList("gray"));

			Document matSize = new Document("h", 27.9)
			        .append("w", 35.5)
			        .append("uom", "cm");
			mat.put("size", matSize);  // // https://stackoverflow.com/questions/6570088/mongodb-java-api-put-vs-append
			//3
			Document mousePad = new Document("item", "mousePad")
			        .append("qty", 25)
			        .append("tags",  Arrays.asList("gel", "blue"));

			Document mousePadSize = new Document("h", 19)
			        .append("w", 22.85)
			        .append("uom", "cm");
			mousePad.put("size", mousePadSize); // // https://stackoverflow.com/questions/6570088/mongodb-java-api-put-vs-append

			collection.insertMany( Arrays.asList(journal, mat, mousePad));
			
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
