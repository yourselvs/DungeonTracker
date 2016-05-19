package yourselvs.dungeontracker.database;

public class MongoVars {
	public MongoVars(){
		
	}
	
	// Database commands
	public final String set = "$set";
	
	// Variable names
	public final String type = "type";
	public final String dungeon = "dungeon";
	public final String value = "value";
	public final String name = "name"; 
	public final String player = "player";
	public final String status = "status";
	public final String world = "world";
	public final String difficulty = "difficulty";
	public final String creator = "creator";
	public final String startTime = "startTime";
	public final String finishTime = "finishTime";
	public final String completionTime = "completionTime";
	public final String timesCompleted = "timesCompleted";
	public final String locX = "locationX";
	public final String locY = "locationY";
	public final String locZ = "locationZ";
	
	// Values
	public final String allowedValue = "allowed";
	
	// Types
	public final String commandType = "command";
	public final String recordType = "record";
	public final String paramType = "param";
	public final String dungeonType = "dungeon";
	
	// Statuses
	public final String completeStatus = "complete";
	public final String incompleteStatus = "incomplete";
}
