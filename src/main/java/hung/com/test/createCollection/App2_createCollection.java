package hung.com.test.createCollection;


import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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
public class App2_createCollection {

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
			database.createCollection("sampleCollection"); // default là autoindex ở server "_Id" sẽ tạo ở server.
			
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
