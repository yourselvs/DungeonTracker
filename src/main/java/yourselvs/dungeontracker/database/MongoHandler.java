package yourselvs.dungeontracker.database;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import yourselvs.dungeontracker.DungeonTracker;
import yourselvs.dungeontracker.dungeons.Dungeon;
import yourselvs.dungeontracker.dungeons.DungeonRecord;

public class MongoHandler {
	private DungeonTracker plugin;
	private MongoDBStorage db;
	
	public MongoHandler(DungeonTracker plugin){
		this.plugin = plugin;
	}
	
	public String[] getCommandsAllowed(String dungeon){
		// TODO Get list of allowed commands in a dungeon
		return null;
	}
	
	public String getPlayerDungeon(Player player){
		// TODO Check if a player is in a dungeon. Return the dungeon name. If not in a dungeon, return null.
		return null;
	}
	
	public boolean getCommandValue(String dungeon, String command){
		// TODO Get the value of a command for a dungeon
		return false;
	}
	
	public void updateCommandValue(String dungeon, String command, boolean value){
		// TODO Update the value of a command for a dungeon
	}
	
	public boolean getParamValue(String dungeon, String param){
		// TODO Get the value of a parameter for a dungeon
		return false;
	}
	
	public void updateParamValue(String dungeon, String param, boolean value){
		// TODO Update a param value for a dungeon
	}

	public Location getDungeonSpawn(String dungeon) {
		// TODO Get the spawn point of a single dungeon
		return null;
	}
	
	public boolean dungeonExists(String dungeon) {
		// TODO Find whether or not a dungeon exists
		return false;
	}
	
	public void createDungeon(String dungeon, Location location){
		// TODO Create a dungeon
	}
	
	public DungeonRecord getFastestTime(String dungeon, Player player){
		// TODO Get the fastest record of a player in a dungeon
		return null;
	}
	
	public DungeonRecord getFastestTime(String dungeon){
		// TODO Get the fastest record in a dungeon
		return null;
	}
	
	public ArrayList<DungeonRecord> getRecords(String dungeon){
		return null;
	}
	
	public ArrayList<DungeonRecord> getRecords(String dungeon, Player player){
		return null;
	}
	
	public Dungeon getDungeon(String dungeon){
		// TODO Get information about a dungeon
		return null;
	}
	
	public void createRecord(String dungeon, Player player){
		// TODO Create a player record when entering a dungeon
	}
	
	public void checkpointRecord(Player player, String checkpoint){
		// TODO Update a player record when they go through a checkpoint
	}
	
	public DungeonRecord getCurrentRecord(Player player){
		// TODO Get the record of a player in progress
		return null;
	}
	
	public DungeonRecord finishRecord(Player player){
		// TODO Update a player record when they finish a dungeon
		return null;
	}
	
	public void deleteDungeon(String dungeon){
		// TODO Delete all data concerning a specific dungeon
	}
	
	public int timesCompleted(String dungeon, Player player){
		// TODO Get the number of times a dungeon has been completed by a player
		return 0;
	}
}
