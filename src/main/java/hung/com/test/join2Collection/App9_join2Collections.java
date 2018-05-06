package hung.com.test.join2Collection;


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
public class App9_join2Collections {

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
			MongoCollection<Document> collectionOrder = database.getCollection("orders");
			
			collectionOrder.insertMany(Arrays.asList(
			        Document.parse("{ '_id' : 1, 'item' : 'almonds', 'price' : 12, 'quantity' : 2 }"),
			        Document.parse("{ '_id' : 2, 'item' : 'pecans', 'price' : 20, 'quantity' : 1 }"),
			        Document.parse("{ '_id' : 3}")
			));
			//
			MongoCollection<Document> collectionInventory = database.getCollection("inventory");
			
			collectionInventory.insertMany(Arrays.asList(
			        Document.parse("{ '_id' : 1, 'sku' : 'almonds', description: 'product 1', 'instock' : 120 }"),
			        Document.parse("{ '_id' : 2, 'sku' : 'bread', description: 'product 2', 'instock' : 80 }"),
			        Document.parse("{ '_id' : 3, 'sku' : 'cashews', description: 'product 3', 'instock' : 60 }"),
			        Document.parse("{ '_id' : 4, 'sku' : 'pecans', description: 'product 4', 'instock' : 70 }"),
			        Document.parse("{ '_id' : 5, 'sku': null, description: 'Incomplete' }"),
			        Document.parse("{ '_id' : 6 }")
			));
			
			//============================ find (or $match with Aggregation fucntion ============
			String jsonFind = "{$match:{'_id':1}}";
			Bson bsonFind =  BasicDBObject.parse( jsonFind );
			
			//=========================== $lookup =================================		
			String jsonLookup = "{ $lookup: {"+
				                              "from: 'inventory'," +
				                              "localField: 'item'," +
				                              "foreignField: 'sku'," +
				                              "as: 'inventory_docs'	} } ";
			Bson bsonLookup =  BasicDBObject.parse( jsonLookup );
			
			List<Bson> listBson = new ArrayList<Bson>();
			listBson.add(bsonFind);    //filter first
			listBson.add(bsonLookup);  // filter second
			
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
