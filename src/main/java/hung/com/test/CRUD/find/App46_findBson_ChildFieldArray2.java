package hung.com.test.CRUD.find;


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
public class App46_findBson_ChildFieldArray2 {

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
			MongoCollection<Document> collection = database.getCollection("sampleColChildFieldArray");
			
			collection.insertMany(Arrays.asList(
			        Document.parse("{country: 'Vietnam', cities: [{name: 'Hanoi', code: 111}, {name: 'Hai Phong'},{name: null,code: 000}]}"),
			        Document.parse("{country: 'Thailand', cities: [{name: 'ChiengMai', code: 111}, {name: 'Bang Coc'},{code: 000}]}")
			));
			
			// $or: operator OR
			// $eq: equals
			//dùng cú pháp json hay hơn dùng thư viện java. Vì nó cho phép dùng với Java, PHP, NodeJs,Shell command... đều ok.

			String json = "{'cities.name':'Hanoi'}"; //show child field
//			String json = "{'cities.name': null}";
			
			Bson bson =  BasicDBObject.parse( json );
			
			FindIterable<Document> iterDoc = collection.find(bson);

			// Getting the iterator 
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
