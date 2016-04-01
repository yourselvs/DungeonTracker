package yourselvs.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import yourselvs.dungeons.DungeonTracker;

public class EventListener implements Listener{
	private DungeonTracker plugin;
	
	public EventListener(DungeonTracker plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocess(PlayerPickupItemEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			if(!plugin.getMongo().getParamValue(dungeon, "canPickupItem"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocess(PlayerArmorStandManipulateEvent event){
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			if(!plugin.getMongo().getParamValue(dungeon, "canManipulateArmorStand"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocess(PlayerBedEnterEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			if(!plugin.getMongo().getParamValue(dungeon, "canEnterBed"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocess(PlayerBucketEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			if(!plugin.getMongo().getParamValue(dungeon, "canUseBucket"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocess(PlayerDropItemEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			if(!plugin.getMongo().getParamValue(dungeon, "canDropItem"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocess(PlayerPickupArrowEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			if(!plugin.getMongo().getParamValue(dungeon, "canPickupArrow"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocess(PlayerExpChangeEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			if(!plugin.getMongo().getParamValue(dungeon, "canChangeExperience"))
				event.setAmount(0);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocess(PlayerToggleFlightEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			if(!plugin.getMongo().getParamValue(dungeon, "canFly"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocess(PlayerToggleSneakEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			if(!plugin.getMongo().getParamValue(dungeon, "canSneak"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocess(PlayerToggleSprintEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			if(!plugin.getMongo().getParamValue(dungeon, "canSprint"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocess(PlayerRespawnEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			event.setRespawnLocation(plugin.getMongo().getDungeonSpawn(dungeon));
		}
	}
}
