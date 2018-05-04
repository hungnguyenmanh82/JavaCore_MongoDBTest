package hung.com.test.index;


import java.util.Arrays;
import java.util.List;

import org.bson.Document;
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
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.event.ServerClosedEvent;
import com.mongodb.event.ServerDescriptionChangedEvent;
import com.mongodb.event.ServerListener;
import com.mongodb.event.ServerOpeningEvent;
import com.mongodb.util.JSON;

/**
 * 
 * 
		>db.mycol.creatIndex ({"title":1})
		>db.mycol.creatIndex ({"title":1,"description":-1})

 */
public class App7_indexUniqueBson {

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
			MongoCollection<Document> collection = database.getCollection("sampleCollection");
			/**
			   {
			     _id=5aeadf6432ff4031fcc89550, 
			     title=MongoDB, 
			     id=1, 
			     description=database, 
			     likes=100, 
			     url=http://www.tutorialspoint.com/mongodb/, 
			     by=tutorials point
			   }
			 */
			//dùng Bson tiện hơn
			String json = "{\"likes\":1}";
			//1: ascending
			//-1: descending
//			String json = "{\"likes\":1,\"title\":-1}";
			Bson bson =  BasicDBObject.parse( json );

			//các lệnh index, find, update, insert, delete đều làm tương tự dùng Bson
			IndexOptions indexOptions = new IndexOptions();
			indexOptions.unique(true);
			collection.createIndex(bson, indexOptions);

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
