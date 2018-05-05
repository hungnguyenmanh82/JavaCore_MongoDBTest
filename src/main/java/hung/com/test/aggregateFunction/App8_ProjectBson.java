package hung.com.test.aggregateFunction;


import java.util.ArrayList;
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
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
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
public class App8_ProjectBson {

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
			
			// [] vòng lặp 
			// {$project} : 1 Document trong Collection => duyệt tuần tự trong vòng lặp
			// {_id:0}   :  0 nghía là ko hiển thị field “_id” trong json Document
			// {item:1}  : 1 là hiển thị field “item” trong json Document
			// 0 = false, 1 = true
			// {field: <expression>} : expression là biểu thức true/false trả về là 0 or 1

			//{"title":1,_id:0}: chỉ hiển thị title mà ko hiển thị column Id
			// 1: hiển thị field
			// 0: ko hiển thị field này
			// _id: là trường đặc biệt nếu ko để là 0 thì sẽ tự động hiển thị
			String json = "{$project:{_id:0,title:1,likes:1}}";
			
			Bson bson =  BasicDBObject.parse( json );

			List<Bson> listBson = new ArrayList<Bson>();
			listBson.add(bson);
			
			AggregateIterable<Document> output  = collection.aggregate(listBson);

			// Getting the iterator 
			Iterator it = output.iterator(); 
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
