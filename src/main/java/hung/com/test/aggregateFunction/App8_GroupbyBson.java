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
public class App8_GroupbyBson {

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
			
			//_id là trường bắt buộc để nhóm Group. _id = null nghĩa là tất cả 
			// num_tutorial: là tên đại diện hiển thị ứng với Alias trong SQL
			// $sum:  các toán tử function đếu kết thúc với dấu “:” và bắt đầu với $ => tuân theo Json
			// $by_user  là field name trong Json Document. Cùng tên đc nhóm vào 1 Group  
			// {$sum: 1} chính là hàm count mỗi lần +1 vào
			// {$sum: “$money”}  tìm các field money và cộng lại với nhau.
			//chú ý cú pháp đều tuân thủ Json rất chặt chẽ mặc dù có các function của MongoDB


//			String json = "{$group:{_id:null, \"total likes\":{$sum:\"$likes\"}}}";
//			String json = "{$group:{_id:\"$title\", \"total likes\":{$sum:\"$likes\"}}}";
			String json = "{$group:{_id:\"$title\", \"total likes\":{$sum:\"$likes\"}, \"total id\":{$sum:\"$id\"} }}";
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
