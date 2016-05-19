package yourselvs.dungeontracker.database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import yourselvs.dungeontracker.boxes.Box;
import yourselvs.dungeontracker.boxes.ListBox;
import yourselvs.dungeontracker.boxes.LongBox;

import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClientURI;


import java.util.List;

import java.util.ArrayList;
import org.bson.Document;

public class MongoDBAsync implements IMongo {

	MongoClient client = null;
	MongoDatabase db = null;
	MongoCollection<Document> coll = null;
	
	public MongoDBAsync (String connectionString, String dbName, String collectionName)
	{
		new Thread(new Runnable() {
	        public void run(){
	        	client = new MongoClient(new MongoClientURI(connectionString));
				db = client.getDatabase(dbName);
				coll = db.getCollection(collectionName);
	        }
	    }).start();
	}
	
	public List<Document> findDocuments(String propertiesToFindJson) {
		return findDocuments(Document.parse(propertiesToFindJson));
	}
	
	@SuppressWarnings("unchecked")
	public List<Document> findDocuments(Document propertiesToFind) {
		ListBox box = new ListBox();
		new Thread(new Runnable() {
	        public void run(){
	        	box.cargo = coll.find(propertiesToFind).into(new ArrayList<Document>());
	        }
	    }).start();
		return box.cargo;
	}
	
	public Document findDocument(Document propertiesToFind) {
		Box box = new Box();
		new Thread(new Runnable() {
			public void run(){
				box.cargo = coll.find(propertiesToFind).first();
			}
		}).start();
		return box.cargo;
	}
	
	public Document insertDocument(Document newDocument) {
		new Thread(new Runnable() {
			public void run(){
				coll.insertOne(newDocument);
			}
		}).start();
		return newDocument;
	}
	
	public Document insertDocument(String newDocumentJson) {
		return insertDocument( Document.parse(newDocumentJson));
	}
	
	public boolean updateDocument(Document docToFind, Document propertiesToUpdate) {
		new Thread(new Runnable() {
			public void run(){
				coll.updateMany(docToFind, propertiesToUpdate);
			}
		}).start();
		return true;
	}
	
	public boolean updateDocument(String docToFind, String propertiesToUpdate) {
		return updateDocument(Document.parse(docToFind), Document.parse(propertiesToUpdate));
	}
	
	public long deleteDocuments(String docToDeleteJson) {
		return deleteDocuments(Document.parse(docToDeleteJson));
	}

	public long deleteDocuments(Document docToDelete) {
		LongBox box = new LongBox();		
		new Thread(new Runnable() {
			public void run(){
				box.cargo = coll.deleteMany(docToDelete).getDeletedCount();
			}
		}).start();
		return box.cargo;
	}

}
