package hung.com.test.index;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bson.BsonDocument;
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
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
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
public class App72_indexBson_FieldArray {

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
			MongoCollection<Document> collection = database.getCollection("sampleColFieldArray");
			
			collection.insertMany(Arrays.asList(
			        Document.parse("{ item: 'journal', qty: 25, tags: ['blue', 'red','ping'], dim_cm: [ 14, 21 ] }"),
			        Document.parse("{ item: 'notebook', qty: 50, tags: ['red', 'blank','grey'], dim_cm: [ 14, 21 ] }"),
			        Document.parse("{ item: 'paper', qty: 100, tags: ['red', 'yellow', 'plain','white'], dim_cm: [ 14, 21 ] }"),
			        Document.parse("{ item: 'planner', qty: 75, tags: ['blank', 'green'], dim_cm: [ 22.85, 30 ] }"),
			        Document.parse("{ item: 'postcard', qty: 45, tags: ['blue','cyan','black'], dim_cm: [ 10, 15.25 ] }")
			));

			//====================== Index Array ====================
			//dùng cú pháp json hay hơn dùng thư viện java. Vì nó cho phép dùng với Java, PHP, NodeJs,Shell command... đều ok.
			//1: ascending
			//-1: descending
			String json = "{tags:1}";


			Bson bson =  BasicDBObject.parse( json );
			collection.createIndex(bson);
			
			//======================= find ============================
			String jsonFind = "{tags:'red'}";
			Bson bsonFind =  BasicDBObject.parse( jsonFind );
			
			FindIterable<Document> iterDoc = collection.find(bsonFind);
			Iterator it = iterDoc.iterator(); 
			Document doc;
			while (it.hasNext()) { 
				doc = (Document)it.next();
				System.out.println(doc);
			}
			
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
