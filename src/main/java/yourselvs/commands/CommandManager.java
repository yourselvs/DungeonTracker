package yourselvs.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import yourselvs.DungeonTracker;
import yourselvs.events.PlayerFinishDungeonEvent;
import yourselvs.events.PlayerStartDungeonEvent;

public class CommandManager {
	private DungeonTracker plugin;
	
	public CommandManager(DungeonTracker plugin){
		this.plugin = plugin;
	}
	
	public void joinDungeon(PlayerStartDungeonEvent event){
		// TODO Process having a player join a dungeon
	}
	
	public void leaveDungeon(Player player){
		// TODO Process having a player leave a dungeon
	}
	
	public void finishDungeon(PlayerFinishDungeonEvent event){
		// TODO Process having a player finish a dungeon
	}

	public void viewLeaderboard(Player player, String dungeon, int page) {
		// TODO Auto-generated method stub
	}
	
	public void viewDungeonHistory(Player player, String dungeon){
		
	}
	
	public void viewPlayerHistory(Player player, String dungeon, String playerName){
		
	}
	
	public void viewRecord(Player player, String dungeon){
		
	}
	
	public void viewRecord(CommandSender sender, String dungeon){
		
	}
	
	public void viewPlayerRecord(Player player, String dungeon, String playerName){
		
	}
	
	public void viewPlayerRecord(CommandSender sender, String dungeon, String playerName){
		
	}
	
	public void createDungeon(Player sender, String dungeon, String difficulty, String creator){
		
	}
	
	public void deleteDungeon(Player sender, String dungeon){
		
	}
	
	public void setParam(Player player, String dungeon, String param, boolean bool){
		
	}
	
	public void viewParam(Player player, String dungeon, String param){
		
	}
	
	public void allowCommand(Player player, String dungeon, String command){
		
	}
	
	public void blockCommand(Player player, String dungeon, String command){
		
	}
	
	public void viewCommand(Player player, String dungeon, String command){
		
	}
	
	public void viewDungeon(Player player, String dungeon){
		
	}
	
	public void viewInfo(Player player){
		
	}
}
