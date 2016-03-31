package yourselvs.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import yourselvs.dungeons.DungeonTracker;

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
			String dungeonName = plugin.getMongo().getPlayerDungeon(event.getPlayer());
			if(!plugin.getMongo().getCommandValue(dungeonName, command)){
				event.setCancelled(true);
				plugin.getMessenger().commandNotAllowed(dungeonName, command, event.getPlayer());
			}
		}
	}
}
