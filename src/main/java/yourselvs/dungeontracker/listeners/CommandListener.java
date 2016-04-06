package yourselvs.dungeontracker.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import yourselvs.dungeontracker.DungeonTracker;

public class CommandListener implements Listener{
	private DungeonTracker plugin;
	
	public CommandListener(DungeonTracker plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocess(PlayerCommandPreprocessEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null && event.getMessage().startsWith("/")){
			String command = event.getMessage().substring(1);
			if(!plugin.getMongo().getCommandValue(dungeon, command) && !command.equalsIgnoreCase("dungeon leave")){ // if command is not allowed and its not "/dungeon leave"
				event.setCancelled(true);
				plugin.getMessenger().commandNotAllowed(dungeon, command, event.getPlayer());
			}
		}
	}
}
