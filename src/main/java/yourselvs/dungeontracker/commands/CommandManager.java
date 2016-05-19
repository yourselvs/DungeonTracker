package yourselvs.dungeontracker.commands;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.bukkit.Location;
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
		plugin.getMongo().createRecord(event.getDungeon(), event.getPlayer(), new Date());
		
		event.getPlayer().setFlying(false);
		event.getPlayer().teleport(plugin.getMongo().getDungeonSpawn(event.getDungeon()));
		plugin.getMessenger().startDungeon(event.getPlayer(), plugin.getMongo().getCurrentRecord(event.getPlayer().getName()));
	}
	
	public void leaveDungeon(Player player){
		// Process having a player leave a dungeon
		player.teleport(plugin.getMongo().getStartLocation(player.getName()));
		DungeonRecord record = plugin.getMongo().getCurrentRecord(player.getName());
		plugin.getMessenger().leaveDungeon(player, record);
		plugin.getMongo().deleteRecord(player);
	}
	
	public void finishDungeon(PlayerFinishDungeonEvent event){
		// Process having a player finish a dungeon
		event.getPlayer().teleport(plugin.getMongo().getStartLocation(event.getPlayer().getName()));
		DungeonRecord record = plugin.getMongo().finishRecord(event.getPlayer().getName(), event.getTime());
		plugin.getMessenger().finishDungeon(event.getPlayer(), record);
		
		DungeonRecord pr = plugin.getMongo().getFastestTime(event.getDungeon(), event.getPlayer().getName());
		DungeonRecord wr = plugin.getMongo().getFastestTime(event.getDungeon());
		
		boolean beatPr = false;
		boolean beatWr = false;
		
		if(pr != null && wr != null){
			Date prDate = null;
			Date wrDate = null;
			try {
				prDate = plugin.getLongFormatter().parse(plugin.subtractTime(pr.getStartTime(), pr.getFinishTime()));
				wrDate = plugin.getLongFormatter().parse(plugin.subtractTime(wr.getStartTime(), wr.getFinishTime()));
			} catch (ParseException e) {e.printStackTrace();}
			
			if(event.getTime().getTime() < prDate.getTime()) // if the player beats their personal record
				beatPr = true;
			
			if(event.getTime().getTime() < wrDate.getTime()) // if the player beats the world record
				beatWr = true;
		}
		if(pr == null)
			beatPr = true;
		if(wr == null)
			beatWr = true;
		
		if(beatPr);
			//plugin.getMessenger().beatDungeonPR(event.getPlayer(), record);
		
		if(beatWr);
			//plugin.getMessenger().beatDungeonWR(event.getPlayer(), record);
	}
	
	public void createDungeon(Player player, String dungeon, String difficulty, String creator){
		difficulty = difficulty.toUpperCase();
		plugin.getMongo().createDungeon(dungeon, player.getLocation(), difficulty, creator);
		plugin.getMessenger().confirmCreateDungeon(dungeon, player, difficulty);
	}
	
	@SuppressWarnings("deprecation")
	public void deleteDungeon(Player sender, String dungeon){
		Map<String, Location> locations = plugin.getMongo().getStartLocations(dungeon);
		for(String name : locations.keySet()){
			Player player = plugin.getServer().getPlayer(name);
			if(player == null)
				player = (Player) plugin.getServer().getOfflinePlayer(name);
			Location loc = locations.get(name);
			
			player.teleport(loc);
		}
		
		long longNum = plugin.getMongo().deleteDungeon(dungeon);
		if(longNum > 0)
			plugin.getMessenger().confirmDeleteDungeon(sender, dungeon, longNum);
	}
	
	public void setParam(Player player, String dungeon, String param, boolean bool){
		plugin.getMongo().updateParamValue(dungeon, param, bool);
		
		if(plugin.getMongo().getParamValue(dungeon, param) == bool)
			plugin.getMessenger().confirmUpdateParam(player, dungeon, param, bool);
		else
			plugin.getMessenger().errorUpdateParam(player, dungeon, param, bool);
	}	
	
	public void setCommand(Player player, String dungeon, String command, boolean bool){
		plugin.getMongo().updateCommandValue(dungeon, command, bool, player);
		
		if(plugin.getMongo().getCommandValue(dungeon, command) == bool)
			plugin.getMessenger().confirmUpdateCommand(player, dungeon, command, bool);
		else
			plugin.getMessenger().errorUpdateCommand(player, dungeon, command, bool);
	}
}
