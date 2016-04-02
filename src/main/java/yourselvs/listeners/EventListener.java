package yourselvs.listeners;

import org.bukkit.entity.Player;
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

import yourselvs.DungeonTracker;

public class EventListener implements Listener{
	private DungeonTracker plugin;
	
	public EventListener(DungeonTracker plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPickupItem(PlayerPickupItemEvent event) {
		String dungeon = getDungeon(event.getPlayer());
		if(dungeon != null){
			if(!hasPermission(dungeon, "canPickupItem"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onManipulateArmorStand(PlayerArmorStandManipulateEvent event){
		String dungeon = getDungeon(event.getPlayer());
		if(dungeon != null){
			if(!hasPermission(dungeon, "canManipulateArmorStand"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEnterBed(PlayerBedEnterEvent event) {
		String dungeon = getDungeon(event.getPlayer());
		if(dungeon != null){
			if(!hasPermission(dungeon, "canEnterBed"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onUseBucket(PlayerBucketEvent event) {
		String dungeon = getDungeon(event.getPlayer());
		if(dungeon != null){
			if(!hasPermission(dungeon, "canUseBucket"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDropItem(PlayerDropItemEvent event) {
		String dungeon = getDungeon(event.getPlayer());
		if(dungeon != null){
			if(!hasPermission(dungeon, "canDropItem"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPickupArrow(PlayerPickupArrowEvent event) {
		String dungeon = getDungeon(event.getPlayer());
		if(dungeon != null){
			if(!hasPermission(dungeon, "canPickupArrow"))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChangeExp(PlayerExpChangeEvent event) {
		String dungeon = getDungeon(event.getPlayer());
		if(dungeon != null){
			if(!hasPermission(dungeon, "canChangeExperience"))
				event.setAmount(0);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onToggleFlight(PlayerToggleFlightEvent event) {
		String dungeon = getDungeon(event.getPlayer());
		if(dungeon != null){
			if(!hasPermission(dungeon, "canFly")){
				event.setCancelled(true);
				event.getPlayer().setFlying(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onToggleSneak(PlayerToggleSneakEvent event) {
		String dungeon = getDungeon(event.getPlayer());
		if(dungeon != null){
			if(!hasPermission(dungeon, "canSneak")){
				event.setCancelled(true);
				event.getPlayer().setSneaking(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onToggleSprint(PlayerToggleSprintEvent event) {
		String dungeon = getDungeon(event.getPlayer());
		if(dungeon != null){
			if(!hasPermission(dungeon, "canSprint")){
				event.setCancelled(true);
				event.getPlayer().setSprinting(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onRespawn(PlayerRespawnEvent event) {
		String dungeon = getDungeon(event.getPlayer());
		if(dungeon != null){
			event.setRespawnLocation(plugin.getMongo().getDungeonSpawn(dungeon));
		}
	}
	
	private String getDungeon(Player player){
		return plugin.getMongo().getPlayerDungeon(player);
	}
	
	private boolean hasPermission(String dungeon, String param) {
		return plugin.getMongo().getParamValue(dungeon, param);
	}

}
