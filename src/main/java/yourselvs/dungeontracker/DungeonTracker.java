package yourselvs.dungeontracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.plugin.java.JavaPlugin;

import yourselvs.dungeontracker.commands.CommandManager;
import yourselvs.dungeontracker.commands.CommandParser;
import yourselvs.dungeontracker.database.MongoHandler;
import yourselvs.dungeontracker.dungeons.DungeonRecord;
import yourselvs.dungeontracker.listeners.CommandListener;
import yourselvs.dungeontracker.listeners.EventListener;
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
	
	private static SimpleDateFormat format;
    
    @Override
	public void onEnable() {
    	new CommandListener(this);
    	new EventListener(this);
    	new PlayerListener(this);
    	
    	mongo = new MongoHandler(this);
    	messenger = new Messenger(this);
    	bukkit = new BukkitHandler(this);
    	parser = new CommandParser(this);
    	manager = new CommandManager(this);
    	config = new ConfigHandler(this);
    	
    	format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
    	
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
    public ConfigHandler getConfigHandler() {return config;}
    public SimpleDateFormat getFormatter() {return format;}
    public int getPageSize() {return pageSize;}
    
    public String subtractTime(Date date1, Date date2){
		boolean negative = false;
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long difference = time2 - time1;
		if(difference < 0){
			negative = true;
			difference = 0 - difference;
		}
		String time = getFormatter().format(difference);
		if(negative)
			time = "-" + time;
		
		return time;
	}
    
    public ArrayList<DungeonRecord> sortRecordsCompletion(ArrayList<DungeonRecord> data){
		if (data == null)
			return data;
		if (data.size() == 0 || data.size() == 1)
			return data;

		int smallestIndex;
		long smallest;

		for (int curIndex = 0; curIndex < data.size(); curIndex++) {
			smallest = data.get(curIndex).getCompletionTime().getTime();
			smallestIndex = curIndex;

			for (int i = curIndex + 1; i < data.size(); i++) {
				if (smallest > data.get(i).getCompletionTime().getTime()) {
					smallest = data.get(i).getCompletionTime().getTime();
					smallestIndex = i;
				}
			}
			
			if (!(smallestIndex == curIndex)) {
				DungeonRecord temp = data.get(curIndex);
				data.set(curIndex, data.get(smallestIndex));
				data.set(smallestIndex, temp);
			}
		}
		return data;
    }
    
    public ArrayList<DungeonRecord> sortRecordsStart(ArrayList<DungeonRecord> data){
		if (data == null)
			return data;
		if (data.size() == 0 || data.size() == 1)
			return data;

		int smallestIndex;
		long smallest;

		for (int curIndex = 0; curIndex < data.size(); curIndex++) {
			smallest = data.get(curIndex).getCompletionTime().getTime();
			smallestIndex = curIndex;

			for (int i = curIndex + 1; i < data.size(); i++) {
				if (smallest > data.get(i).getStartTime().getTime()) {
					smallest = data.get(i).getStartTime().getTime();
					smallestIndex = i;
				}
			}

			if (!(smallestIndex == curIndex)) {
				DungeonRecord temp = data.get(curIndex);
				data.set(curIndex, data.get(smallestIndex));
				data.set(smallestIndex, temp);
			}
		}
		return data;
    }
}
