package yourselvs.dungeontracker.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import yourselvs.dungeontracker.DungeonTracker;
import yourselvs.dungeontracker.dungeons.Dungeon;
import yourselvs.dungeontracker.dungeons.DungeonRecord;

public class MongoHandler {
	private MongoVars v = new MongoVars();
	private DungeonTracker plugin;
	private IMongo db;
	
	public MongoHandler(DungeonTracker plugin){
		this.plugin = plugin;
	}
	
	public void setDB(IMongo db){
		this.db = db;
	}
	
	public List<String> getCommandsAllowed(String dungeon){
		List<String> commands = new ArrayList<String>();
		
		List<Document> docs = null;
		docs = db.findDocuments(new Document(v.type, v.commandType)
				.append(v.dungeon, dungeon)
				.append(v.value, v.allowedValue));
		for(Document doc : docs)
			commands.add(doc.getString(v.name));
		return commands;
	}
	
	public String getPlayerDungeon(Player player){
		Document doc = new Document(v.type, v.recordType)
				.append(v.player, player.getName())
				.append(v.status, v.incompleteStatus);
		Document findDoc = db.findDocument(doc);
		
		if(findDoc != null)
			return findDoc.getString(v.dungeon);
		return null;
	}
	
	public boolean getCommandValue(String dungeon, String command){
		Document doc = db.findDocument(new Document(v.type, v.commandType)
				.append(v.dungeon, dungeon)
				.append(v.name, command));
		
		if(doc != null)
			return doc.getBoolean(v.value);
		return false;
	}
	
	public void updateCommandValue(String dungeon, String command, boolean value, Player player){
		Document docToFind = new Document(v.type, v.commandType)
				.append(v.dungeon, dungeon)
				.append(v.name, command);
		Document doc = db.findDocument(docToFind);
		Document update = new Document(v.set, new Document(v.value, value));
		
		if(doc != null){
				if(!db.updateDocument(doc, update))
					plugin.getMessenger().updateFailed(player);
				else
					plugin.getMessenger().updateSuccesful(player);
		}
		else{
				db.insertDocument(docToFind.append(v.value, value));
				plugin.getMessenger().newCommandRegistered(player);
		}
	}
	
	public boolean getParamValue(String dungeon, String param){
		Document doc = db.findDocument(new Document(v.type, v.paramType)
				.append(v.dungeon, dungeon)
				.append(v.name, param));
		
		if(doc != null)
			return doc.getBoolean(v.value);
		return false;
	}
	
	public void updateParamValue(String dungeon, String param, boolean value){
		Document doc = db.findDocument(new Document(v.type, v.paramType)
				.append(v.dungeon, dungeon)
				.append(v.name, param));
		
		Document update = new Document(v.set, new Document(v.value, value));
		
		db.updateDocument(doc, update);
	}

	public Location getDungeonSpawn(String dungeon) {
		Document doc = db.findDocument(new Document(v.type, v.dungeon)
				.append(v.name, dungeon));
		
		if(doc != null){
			double x = doc.getDouble(v.locX);
			double y = doc.getDouble(v.locY);
			double z = doc.getDouble(v.locZ);
			World world = plugin.getServer().getWorld(doc.getString(v.world));
			Location loc = new Location(world, x, y, z);
			return loc;
		}
		return null;
	}
	
	public boolean dungeonExists(String dungeon) {
		Document doc = db.findDocument(new Document(v.type, v.dungeon)
			.append(v.name, dungeon));
		
		if(doc != null)
			return true;
		return false;
	}
	
	public void createDungeon(String dungeon, Location location, String difficulty, String creator){
		db.insertDocument(new Document(v.type, v.dungeonType)
					.append(v.name, dungeon)
					.append(v.locX, location.getX())
					.append(v.locY, location.getY())
					.append(v.locZ, location.getZ())
					.append(v.world, location.getWorld().getName())
					.append(v.difficulty, difficulty)
					.append(v.creator, creator));
			
			for(String key : plugin.getParams().keySet()){
				db.insertDocument(new Document(v.type, v.paramType)
						.append(v.difficulty, dungeon)
						.append(v.name, key)
						.append(v.value, plugin.getParams().get(key)));
			}
	}
	
	public DungeonRecord getFastestTime(String dungeon, String playerName){
		// TODO Get the fastest record of a player in a dungeon
		return null;
	}
	
	public DungeonRecord getFastestTime(String dungeon){
		// TODO Get the fastest record in a dungeon
		return null;
	}
	
	public List<DungeonRecord> getRecords(String dungeon) {
		List<Document> docs = db.findDocuments(new Document(v.type, v.recordType)
				.append(v.name, dungeon)
				.append(v.status, v.completeStatus));
			
		List<DungeonRecord> records = new ArrayList<DungeonRecord>();
		for(Document doc : docs){
			String player = doc.getString(v.player);
			Date startTime = new Date(doc.getLong(v.startTime));
			Date finishTime = new Date(doc.getLong(v.finishTime));
			
			DungeonRecord record = null;
			try {
				record = new DungeonRecord(plugin, dungeon, player, startTime, finishTime);
			} catch (Exception e) {e.printStackTrace();}
			
			records.add(record);
		}
		
		return records;
	}
	
	public List<DungeonRecord> getRecords(String dungeon, Player player) {
		List<Document> docs = db.findDocuments(new Document(v.type, v.recordType)
				.append(v.name, dungeon)
				.append(v.status, v.completeStatus)
				.append(v.player, player.getName()));
		List<DungeonRecord> records = new ArrayList<DungeonRecord>();
		for(Document doc : docs){
			Date startTime = new Date(doc.getLong(v.startTime));
			Date finishTime = new Date(doc.getLong(v.finishTime));
			
			DungeonRecord record = null;
			try {
				record = new DungeonRecord(plugin, dungeon, player.getName(), startTime, finishTime);
			} catch (Exception e) {e.printStackTrace();}
			
			records.add(record);
		}
		
		return records;
	}
	
	public Map<String, Location> getStartLocations(String dungeon) {
		Map<String, Location> locations = new HashMap<String, Location>();
		List<Document> docs = db.findDocuments(new Document(v.type, v.recordType)
				.append(v.name, dungeon)
				.append(v.status, v.incompleteStatus));
		
		for(Document doc : docs){
			Location loc = getStartLocation(doc.getString(v.player));
			
			locations.put(doc.getString(v.player), loc);
		}
		
		return locations;
	}
	
	public Dungeon getDungeon(String name){
		Document doc = db.findDocument(new Document(v.type, v.dungeonType)
				.append(v.name, name));
		
		return getDungeon(doc);
	}
	
	public Dungeon getDungeon(Document doc){
		String name = doc.getString(v.name);
		String creator = doc.getString(v.creator);
		
		double x = doc.getDouble(v.locX);
		double y = doc.getDouble(v.locY);
		double z = doc.getDouble(v.locZ);
		World world = plugin.getServer().getWorld(doc.getString(v.world));
		Location location = new Location(world, x, y, z);
		
		// TODO return the actual record
		DungeonRecord record = null;
		try {
			record = new DungeonRecord(plugin, name, "jimmy", new Date(10), new Date(20));
		} catch (Exception e) {e.printStackTrace();}
		
		int timesCompleted = doc.getInteger(v.timesCompleted, 0);
		String difficulty = doc.getString(v.difficulty);
		
		Dungeon dungeon = new Dungeon(name, creator, location, record, timesCompleted, difficulty);
		
		return dungeon;
	}
	
	public void createRecord(String dungeon, Player player, Date startTime){
		Location location = player.getLocation();
		db.insertDocument(new Document(v.type, v.recordType)
				.append(v.dungeon, dungeon)
				.append(v.player, player.getName())
				.append(v.startTime, startTime.getTime())
				.append(v.status, v.incompleteStatus)
				.append(v.locX, location.getX())
				.append(v.locY, location.getY())
				.append(v.locZ, location.getZ())
				.append(v.world, location.getWorld().getName()));
	}
	
	public void checkpointRecord(Player player, String checkpoint){
		// TODO Update a player record when they go through a checkpoint
	}
	
	public DungeonRecord getCurrentRecord(String player){
		Document doc = db.findDocument(new Document(v.type, v.recordType)
				.append(v.player, player)
				.append(v.status, v.incompleteStatus));
		Date startTime = new Date(doc.getLong(v.startTime));
		String dungeon = doc.getString(v.dungeon);
		return new DungeonRecord(dungeon, player, startTime);
	}
	
	public Location getStartLocation(String player){
		Document doc = db.findDocument(new Document(v.type, v.recordType)
				.append(v.player, player)
				.append(v.status, v.incompleteStatus));
		if(doc != null){
			double x = doc.getDouble(v.locX);
			double y = doc.getDouble(v.locY);
			double z = doc.getDouble(v.locZ);
			World world = plugin.getServer().getWorld(doc.getString(v.world));
			Location loc = new Location(world, x, y, z);
			
			return loc;
		}
		return null;
	}
	
	public DungeonRecord finishRecord(String player, Date finishTime){
		DungeonRecord record = getCurrentRecord(player);

		Date completionTime = new Date(finishTime.getTime() - record.getStartTime().getTime());
		
		Document docToDelete = new Document(v.type, v.recordType)
				.append(v.status, v.incompleteStatus);
		
		db.deleteDocuments(docToDelete);
		
		Document doc = new Document(v.type, v.recordType)
				.append(v.finishTime, finishTime.getTime())
				.append(v.player, player)
				.append(v.completionTime, completionTime.getTime())
				.append(v.status, v.completeStatus);
		
		db.insertDocument(doc);
		
		record.setFinishTime(finishTime);
		record.setCompletionTime(completionTime);
		
		return record;
	}
	
	public void deleteRecord(Player player){
		Document doc = db.findDocument(new Document(v.type, v.recordType)
				.append(v.player, player.getName())
				.append(v.status, v.incompleteStatus));
		db.deleteDocuments(doc);
	}
	
	
	public long deleteDungeon(String dungeon){
		long num = 0;
		num += db.deleteDocuments(new Document(v.type, v.paramType).append(v.dungeon, dungeon));
		num += db.deleteDocuments(new Document(v.type, v.commandType).append(v.dungeon, dungeon));
		num += db.deleteDocuments(new Document(v.type, v.recordType).append(v.dungeon, dungeon));
		num += db.deleteDocuments(new Document(v.type, v.dungeonType).append(v.name, dungeon));
		return num;
	}
	
	public int timesCompleted(String dungeon, Player player){
		Document doc = db.findDocument(new Document(v.type, v.dungeonType)
				.append(v.name, dungeon));
		
		return doc.getInteger(v.timesCompleted, 0);
	}
	
	public List<Dungeon> getDungeons(){
		List<Document> docs = db.findDocuments(new Document(v.type, v.dungeonType));
		List<Dungeon> dungeons = new ArrayList<Dungeon>();
		
		for(Document doc : docs)
			dungeons.add(getDungeon(doc));
		
		return dungeons;
	}
}
