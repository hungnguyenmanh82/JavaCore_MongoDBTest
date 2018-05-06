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
public class App8_Match_find {

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
			// https://docs.mongodb.com/manual/reference/operator/aggregation/lookup/
			MongoCollection<Document> collectionOrder = database.getCollection("orders1");
			
			collectionOrder.insertMany(Arrays.asList(
			        Document.parse("{ '_id' : 1, 'item' : 'almonds', 'price' : 12, 'quantity' : 2 }"),
			        Document.parse("{ '_id' : 2, 'item' : 'pecans', 'price' : 20, 'quantity' : 1 }"),
			        Document.parse("{ '_id' : 3}")
			));

			
			//== find
			String jsonFind = "{$match:{'_id':1}}";
			Bson bsonFind =  BasicDBObject.parse( jsonFind );	
			
			List<Bson> listBson = new ArrayList<Bson>();
			listBson.add(bsonFind);

			
			AggregateIterable<Document> output  = collectionOrder.aggregate(listBson);

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
