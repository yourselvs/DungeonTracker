package yourselvs.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import yourselvs.dungeons.DungeonTracker;
import yourselvs.events.PlayerFinishDungeonEvent;
import yourselvs.events.PlayerStartDungeonEvent;

public class PlayerListener implements Listener{
	private DungeonTracker plugin;
	
	public PlayerListener(DungeonTracker plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public void preprocess(PlayerQuitEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			// TODO Save player data on quit
			event.getPlayer();
		}
	}
	
	public void preprocess(PlayerJoinEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			// TODO Load player data on join
			event.getPlayer();
		}
	}
	
	public void preprocess(PlayerFinishDungeonEvent event) {
		 	
	}
	
	public void preprocess(PlayerStartDungeonEvent event) {
		plugin.getMongo().createRecord(event.getDungeon(), event.getPlayer(), event.getTime());
	}
}
