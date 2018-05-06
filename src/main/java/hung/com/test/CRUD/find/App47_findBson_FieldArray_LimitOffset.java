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
public class App47_findBson_FieldArray_LimitOffset {

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
			MongoCollection<Document> collection = database.getCollection("ColArrayElementLimitOffset");
			
			String json1 = "{ article: 'xem Tử Vi', author: 'Hungbeo',"+ 
							 "comments: ["+ 
					                      "{user:'Thao',comment:'tuyet voi'},"+
					                      "{user:'Lam',comment:'quite good'},"+
					                      "{user:'HungPV',comment:'that is great'},"+
					                      "{user:'CuongPt',comment:'fair enough'},"+
					                      "{user:'HiepDD',comment:'not bad'},"+
					                      "{user:'CongTH',comment:'beautiful'},"+
							              "{user:'Thi',comment:'bullshit...'}"+
					                   "]"+
					       "}";
			
			String json2 = "{ article: 'xem Tử Vi', author: 'Boob',"+ 
					 "comments: ["+ 
			                      "{user:'Thao',comment:'what a jerk'},"+
			                      "{user:'Lam',comment:'I hate it'},"+
			                      "{user:'HungPV',comment:'a Boob'},"+
			                      "{user:'CuongPt',comment:'fair enough'},"+
			                      "{user:'HiepDD',comment:'not bad'},"+
			                      "{user:'CongTH',comment:'beautiful'},"+
					              "{user:'Thi',comment:'bullshit...'}"+
			                   "]"+
			       "}";
			
			collection.insertMany(Arrays.asList(
			        Document.parse(json1),
			        Document.parse(json2)
			));
			
			// $or: operator OR
			// $eq: equals
			//dùng cú pháp json hay hơn dùng thư viện java. Vì nó cho phép dùng với Java, PHP, NodeJs,Shell command... đều ok.

			
			String json = "{author:'Hungbeo'}";
			Bson bson =  BasicDBObject.parse( json );
			
			// $Project
			// {$slice:[2,3]: offset (skip) = 2, Limit = 3
			String jsonProject = "{comments:{$slice:[2,3]} }";
//			String jsonProject = "{comments:{$slice:3} }";           //show the first 3 items (positive means the fist )
//			String jsonProject = "{comments:{$slice:-3} }";          //show the last 3 items. (negative means the last)
			Bson bsonProject =  BasicDBObject.parse( jsonProject );
			
			FindIterable<Document> iterDoc = collection.find(bson).projection(bsonProject);

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
