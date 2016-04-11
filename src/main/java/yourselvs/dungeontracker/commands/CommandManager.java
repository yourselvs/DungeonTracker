package yourselvs.dungeontracker.commands;

import java.text.ParseException;
import java.util.Date;

import org.bukkit.entity.Player;

import yourselvs.dungeontracker.DungeonTracker;
import yourselvs.dungeontracker.dungeons.DungeonRecord;
import yourselvs.dungeontracker.events.PlayerFinishDungeonEvent;
import yourselvs.dungeontracker.events.PlayerStartDungeonEvent;

public class CommandManager {
	private DungeonTracker plugin;
	
	public CommandManager(DungeonTracker plugin){
		this.plugin = plugin;
	}
	
	public void joinDungeon(PlayerStartDungeonEvent event){
		// Process having a player join a dungeon
		plugin.getMongo().createRecord(event.getDungeon(), event.getPlayer());
		event.getPlayer().teleport(plugin.getMongo().getDungeonSpawn(event.getDungeon()));
		plugin.getMessenger().startDungeon(event.getPlayer(), plugin.getMongo().getCurrentRecord(event.getPlayer()));
	}
	
	public void leaveDungeon(Player player){
		// Process having a player leave a dungeon
		player.teleport(plugin.getMongo().getStartLocation(player));
		DungeonRecord record = plugin.getMongo().getCurrentRecord(player);
		plugin.getMessenger().leaveDungeon(player, record);
		plugin.getMongo().deleteRecord(player);
	}
	
	public void finishDungeon(PlayerFinishDungeonEvent event){
		// Process having a player finish a dungeon
		event.getPlayer().teleport(plugin.getMongo().getStartLocation(event.getPlayer()));
		DungeonRecord record = plugin.getMongo().finishRecord(event.getPlayer());
		plugin.getMessenger().finishDungeon(event.getPlayer(), record);
		
		DungeonRecord pr = plugin.getMongo().getFastestTime(event.getDungeon(), event.getPlayer().getName());
		DungeonRecord wr = plugin.getMongo().getFastestTime(event.getDungeon());
		
		Date prDate = null;
		Date wrDate = null;
		try {
			prDate = plugin.getFormatter().parse(plugin.subtractTime(pr.getStartTime(), pr.getFinishTime()));
			wrDate = plugin.getFormatter().parse(plugin.subtractTime(wr.getStartTime(), wr.getFinishTime()));
		} catch (ParseException e) {e.printStackTrace();}
		
		if(event.getTime().getTime() < prDate.getTime()) // if the player beats their personal record
			plugin.getMessenger().beatDungeonPR(event.getPlayer(), pr, record);
		
		if(event.getTime().getTime() < wrDate.getTime()) // if the player beats the world record
			plugin.getMessenger().beatDungeonWR(event.getPlayer(), wr, record);
	}
	
	public void createDungeon(Player player, String dungeon, String difficulty, String creator){
		plugin.getMongo().createDungeon(dungeon, player.getLocation(), difficulty, creator);
		plugin.getMessenger().confirmCreateDungeon(dungeon, player, difficulty);
	}
	
	public void deleteDungeon(Player sender, String dungeon){
		plugin.getMongo().deleteDungeon(dungeon);
		plugin.getMessenger().confirmDeleteDungeon(sender, dungeon);
	}
	
	public void setParam(Player player, String dungeon, String param, boolean bool){
		plugin.getMongo().updateParamValue(dungeon, param, bool);
		plugin.getMessenger().confirmUpdateParam(player, dungeon, param, bool);
	}	
	
	public void setCommand(Player player, String dungeon, String command, boolean bool){
		plugin.getMongo().updateCommandValue(command, dungeon, bool);
	}
}
