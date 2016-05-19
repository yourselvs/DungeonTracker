package yourselvs.dungeontracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.kingdoms.main.Kingdoms;
import org.kingdoms.manager.game.GameManagement;

import yourselvs.dungeontracker.commands.CommandManager;
import yourselvs.dungeontracker.commands.CommandParser;
import yourselvs.dungeontracker.database.IMongo;
import yourselvs.dungeontracker.database.MongoDBAsync;
import yourselvs.dungeontracker.database.MongoDBStorage;
import yourselvs.dungeontracker.database.MongoHandler;
import yourselvs.dungeontracker.dungeons.Dungeon;
import yourselvs.dungeontracker.dungeons.DungeonRecord;
import yourselvs.dungeontracker.listeners.CommandListener;
import yourselvs.dungeontracker.listeners.PlayerListener;

/**
 * Hello world!
 *
 */
public class DungeonTracker extends JavaPlugin
{
	private MongoHandler mongo;
	private Messenger messenger;
	private BukkitHandler bukkit;
	private CommandParser parser;
	private CommandManager manager;
	private ConfigHandler config;
	private int pageSize = 6;
	private Map<String, Boolean> params;
	private List<Dungeon> dungeons;
	private static SimpleDateFormat longFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss.SS");
	private static SimpleDateFormat shortFormat = new SimpleDateFormat("mm:ss.SS");
	
	public static boolean useLeaderboardFunction = false;
    
    @Override
	public void onEnable() {
    	new CommandListener(this);
    	new PlayerListener(this);
    	
    	mongo = new MongoHandler(this);
    	mongo.setDB(new MongoDBStorage(IMongo.textUri, "minecraft", "dungeon"));
    	messenger = new Messenger(this);
    	bukkit = new BukkitHandler(this);
    	parser = new CommandParser(this);
    	manager = new CommandManager(this);
    	config = new ConfigHandler(this);
    	dungeons = mongo.getDungeons();
    	getKingdoms();
    	
    	params = new HashMap<String, Boolean>();
    	params.put("canPickupItem", false);
    	params.put("canManipulateArmorStand", false);
    	params.put("canEnterBed", false);
    	params.put("canUseBucket", false);
    	params.put("canDropItem", false);
    	params.put("canChangeExperience", true);
    	params.put("canFly", false);
    	params.put("canSneak", true);
    	params.put("canSprint", true);
    	
    	
    	
    	getCommand("dungeon").setExecutor(parser);
    	getCommand("dgn").setExecutor(parser);
    	getCommand("dt").setExecutor(parser);
    	getCommand("dungeontracker").setExecutor(parser);
    }
    
    public MongoHandler getMongo() {return mongo;}
    public Messenger getMessenger() {return messenger;}
    public BukkitHandler getBukkit() {return bukkit;}
    public CommandParser getParser() {return parser;}
    public CommandManager getManager() {return manager;}
    public List<Dungeon> getDungeons() {return dungeons;}
    public ConfigHandler getConfigHandler() {return config;}
    public SimpleDateFormat getLongFormatter() {return longFormat;}
    public SimpleDateFormat getShortFormatter() {return shortFormat;}
    public int getPageSize() {return pageSize;}
    public Map<String, Boolean> getParams() {return params;}
    
    public String subtractTime(Date date1, Date date2){
		boolean negative = false;
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long difference = time2 - time1;
		if(difference < 0){
			negative = true;
			difference = 0 - difference;
		}
		String time = longFormat.format(difference);
		if(negative)
			time = "-" + time;
		
		return time;
	}
    
    public List<DungeonRecord> sortRecordsCompletion(List<DungeonRecord> records){
		if (records == null)
			return records;
		if (records.size() == 0 || records.size() == 1)
			return records;

		int smallestIndex;
		long smallest;

		for (int curIndex = 0; curIndex < records.size(); curIndex++) {
			smallest = records.get(curIndex).getCompletionTime().getTime();
			smallestIndex = curIndex;

			for (int i = curIndex + 1; i < records.size(); i++) {
				if (smallest > records.get(i).getCompletionTime().getTime()) {
					smallest = records.get(i).getCompletionTime().getTime();
					smallestIndex = i;
				}
			}
			
			if (!(smallestIndex == curIndex)) {
				DungeonRecord temp = records.get(curIndex);
				records.set(curIndex, records.get(smallestIndex));
				records.set(smallestIndex, temp);
			}
		}
		return records;
    }
    
    public List<DungeonRecord> sortRecordsStart(List<DungeonRecord> records){
		if (records == null)
			return records;
		if (records.size() == 0 || records.size() == 1)
			return records;

		int smallestIndex;
		long smallest;

		for (int curIndex = 0; curIndex < records.size(); curIndex++) {
			smallest = records.get(curIndex).getCompletionTime().getTime();
			smallestIndex = curIndex;

			for (int i = curIndex + 1; i < records.size(); i++) {
				if (smallest > records.get(i).getStartTime().getTime()) {
					smallest = records.get(i).getStartTime().getTime();
					smallestIndex = i;
				}
			}

			if (!(smallestIndex == curIndex)) {
				DungeonRecord temp = records.get(curIndex);
				records.set(curIndex, records.get(smallestIndex));
				records.set(smallestIndex, temp);
			}
		}
		return records;
    }
    
    @SuppressWarnings("static-access")
	private GameManagement getKingdoms(){
    	GameManagement kingdoms = null;
    	if(Bukkit.getPluginManager().getPlugin("Kingdoms") != null){
    		try{
    			kingdoms = ((Kingdoms) Bukkit.getPluginManager().getPlugin("Kingdoms")).getManagers();
    		} catch (NoClassDefFoundError e) {
                Kingdoms.logInfo("Kingdoms: NoClassDefFoundError");
            } catch (Exception e){
                Kingdoms.logInfo("Kingdoms: Exception");
            }
    	}
    	
    	return kingdoms;
    }
}
