package yourselvs.dungeontracker.database;

import java.util.List;

import org.bson.Document;

public interface IMongo {
	String textUri = "mongodb://anotheruser:AnotherPassword!@ds056288.mongolab.com:56288/minecraft";
	MongoDBStorage mongoStorage = new MongoDBStorage(textUri,"minecraft","suggestions");
	
	public List<Document> findDocuments(String propertiesToFindJson);
	public List<Document> findDocuments(Document propertiesToFind);
	public Document findDocument(Document propertiesToFind);
	public Document insertDocument(Document newDocument);
	public Document insertDocument(String newDocumentJson);
	public boolean updateDocument(Document docToFind, Document propertiesToUpdate);
	public boolean updateDocument(String docToFind, String propertiesToUpdate);
	public long deleteDocuments(String docToDeleteJson);
	public long deleteDocuments(Document docToDelete);
}
