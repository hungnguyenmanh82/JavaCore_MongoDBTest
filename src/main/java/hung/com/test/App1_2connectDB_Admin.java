package hung.com.test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.event.ServerClosedEvent;
import com.mongodb.event.ServerDescriptionChangedEvent;
import com.mongodb.event.ServerListener;
import com.mongodb.event.ServerOpeningEvent;

/**
 * create an MongoDB user with root:
 * 
		use admin
		db.createUser(
		  {
		    user: "admin",
		    pwd: "123",
		    roles: [ { role: "root", db: "admin" } ]
		  }
		);

 */
public class App1_2connectDB_Admin {
	
	private static final String address = "localhost";
	private static final int port = 27017;
	//
	private static final String user = "admin";
	private static final String password = "123";
	private static final String databaseName = "admin";
	
	public static void main(String[] args) {
		try {
			MongoCredential credential = MongoCredential.createCredential(user,databaseName,password.toCharArray());
			MongoClientOptions options = MongoClientOptions.builder()											
											.addServerListener(serverListener)
											.build();
			MongoClient mongo = new MongoClient(new ServerAddress(address,port),credential, options); 
			
			MongoDatabase database = mongo.getDatabase("Mydb"); 
		
			mongo.close();
		} catch (MongoException  e) {
			e.printStackTrace();
			System.out.println("=======");
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
