package hung.com.test.date;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.DateCodec;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.event.ServerClosedEvent;
import com.mongodb.event.ServerDescriptionChangedEvent;
import com.mongodb.event.ServerListener;
import com.mongodb.event.ServerOpeningEvent;

/**
 * create an MongoDB user with root:
 * 
		use Mydb
		db.createUser({user:"MydbUser",pwd:"123",roles:[{role:"readWrite",db:"Mydb"}]})

 */
public class App9_date {

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
			
			//===========================================================================
			CreateCollectionOptions collectionOptions = new CreateCollectionOptions();
			collectionOptions.autoIndex(false);
//			collectionOptions.capped(true);
			
			database.getCollection("myCollection").drop();
			database.createCollection("myCollection",collectionOptions); 
			
			//==================== Index Unique===========================
			String json = "{\"_id\":1}";
			//1: ascending
			//-1: descending
//			String json = "{\"likes\":1,\"title\":-1}";
			Bson bson =  BasicDBObject.parse( json );

			//các lệnh index, find, update, insert, delete đều làm tương tự dùng Bson
			IndexOptions indexOptions = new IndexOptions();
//			indexOptions.unique(true);
			
			MongoCollection<Document> collection = database.getCollection("myCollection");
			collection.createIndex(bson, indexOptions);
			//========================================================================
			
			
			String stDate = "2018-04-22 12:30:45.333";  //333 millisecond
			java.util.Date javaDate = null;
			
			try {
				 javaDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(stDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Document document = new Document() 
					.append("_id", 1)
					.append("item", "abc") 
					.append("price", 100) 
					.append("quantity", 3) 
					.append("date", javaDate);  
			collection.insertOne(document); 
			
			
			
			mongo.close();
		} catch (MongoException  e) {

			System.out.println("=========================================");
			e.printStackTrace();
		}


	}
	
	private static ServerListener serverListener = new ServerListener() {
		
		public void serverOpening(ServerOpeningEvent event) {
			System.out.println("*****************"+ event);
			
		}
		
		public void serverDescriptionChanged(ServerDescriptionChangedEvent event) {
			System.out.println("++++++"+ event);
			
		}
		
		public void serverClosed(ServerClosedEvent event) {
			System.out.println("----------------"+ event);
		}
	};



}
