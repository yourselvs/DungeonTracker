package yourselvs.dungeons;

import java.text.SimpleDateFormat;
import java.util.Date;

import yourselvs.BukkitHandler;
import yourselvs.Messenger;
import yourselvs.database.MongoHandler;
import yourselvs.listeners.CommandListener;
import yourselvs.listeners.EventListener;
import yourselvs.listeners.PlayerListener;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hello world!
 *
 */
public class DungeonTracker extends JavaPlugin
{
	private MongoHandler mongo;
	private Messenger messenger;
	private BukkitHandler bukkit;
	private SimpleDateFormat format;
    
    public void onEnable() {
    	new CommandListener(this);
    	new EventListener(this);
    	new PlayerListener(this);
    	mongo = new MongoHandler();
    	messenger = new Messenger(this);
    	bukkit = new BukkitHandler(this);
    	format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
    }
    
    public MongoHandler getMongo() {return mongo;}
    public Messenger getMessenger() {return messenger;}
    public BukkitHandler getBukkit() {return bukkit;}
    public SimpleDateFormat getFormatter() {return format;}
    
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
}
