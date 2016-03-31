package yourselvs.dungeons;

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
	public MongoHandler mongo;
	public Messenger messenger;
	public BukkitHandler bukkit;
	
    public static void main(String[] args) {
		
    }
    
    public void onEnable() {
    	new CommandListener(this);
    	new EventListener(this);
    	new PlayerListener(this);
    	mongo = new MongoHandler();
    	messenger = new Messenger(this);
    	bukkit = new BukkitHandler(this);
    }
    
    public MongoHandler getMongo() {return mongo;}
    public Messenger getMessenger() {return messenger;}
    public BukkitHandler getBukkit() {return bukkit;}
}
