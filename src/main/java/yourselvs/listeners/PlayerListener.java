package yourselvs.listeners;

import java.text.ParseException;
import java.util.Date;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import yourselvs.DungeonTracker;
import yourselvs.dungeons.DungeonRecord;
import yourselvs.events.PlayerFinishDungeonEvent;
import yourselvs.events.PlayerStartDungeonEvent;

public class PlayerListener implements Listener{
	private DungeonTracker plugin;
	
	public PlayerListener(DungeonTracker plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){
			plugin.getMessenger().quitDungeon(event.getPlayer(), plugin.getMongo().getCurrentRecord(event.getPlayer()));
		}
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		String dungeon = plugin.getMongo().getPlayerDungeon(event.getPlayer());
		if(dungeon != null){ // if the joining player is in a dungeon
			plugin.getMessenger().resumeDungeon(event.getPlayer(), plugin.getMongo().getCurrentRecord(event.getPlayer()));
		}
	}
	
	public void onFinishDungeon(PlayerFinishDungeonEvent event) {		
		DungeonRecord record = plugin.getMongo().finishRecord(event.getPlayer());
		plugin.getMessenger().finishDungeon(event.getPlayer(), record);
		
		DungeonRecord pr = plugin.getMongo().getFastestTime(event.getDungeon(), event.getPlayer());
		DungeonRecord wr = plugin.getMongo().getFastestTime(event.getDungeon());
		
		Date prDate = null;
		Date wrDate = null;
		try {
			prDate = plugin.getFormatter().parse(plugin.subtractTime(pr.startTime, pr.finishTime));
			wrDate = plugin.getFormatter().parse(plugin.subtractTime(wr.startTime, wr.finishTime));
		} catch (ParseException e) {e.printStackTrace();}
		
		if(event.getTime().getTime() < prDate.getTime()) // if the player beats their personal record
			plugin.getMessenger().beatDungeonPR(event.getPlayer(), pr, record);
		
		if(event.getTime().getTime() < wrDate.getTime()) // if the player beats the world record
			plugin.getMessenger().beatDungeonWR(event.getPlayer(), wr, record);
	}
	
	public void onStartDungeon(PlayerStartDungeonEvent event) {
		plugin.getMongo().createRecord(event.getDungeon(), event.getPlayer());
		plugin.getMessenger().startDungeon(event.getPlayer(), plugin.getMongo().getCurrentRecord(event.getPlayer()));
	}
}
