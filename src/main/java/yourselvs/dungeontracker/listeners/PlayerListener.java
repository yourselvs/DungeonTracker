package yourselvs.dungeontracker.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import yourselvs.dungeontracker.DungeonTracker;

public class PlayerListener implements Listener{
	private DungeonTracker plugin;
	
	public PlayerListener(DungeonTracker plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			plugin.getMessenger().quitDungeon(event.getPlayer(), plugin.getMongo().getCurrentRecord(event.getPlayer().getName()));
		}
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){ // if the joining player is in a dungeon
			plugin.getMessenger().resumeDungeon(event.getPlayer(), plugin.getMongo().getCurrentRecord(event.getPlayer().getName()));
		}
	}
}
