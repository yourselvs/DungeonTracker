package yourselvs.dungeontracker.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import yourselvs.dungeontracker.DungeonTracker;
import yourselvs.dungeontracker.events.PlayerFinishDungeonEvent;
import yourselvs.dungeontracker.events.PlayerStartDungeonEvent;

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
		// TODO Show a player a dungeon's leaderboard
	}
	
	public void viewDungeonHistory(Player player, String dungeon){
		// TODO Show a player a dungeon's completion history
	}
	
	public void viewPlayerHistory(Player player, String dungeon, String playerName){
		// TODO Show a player another player's completion history in a dungeon
	}
	
	public void viewRecord(Player player, String dungeon){
		// TODO Show a player the world record for a dungeon 
	}
	
	public void viewRecord(CommandSender sender, String dungeon){
		// TODO Show a non-player the world record for a dungeon
	}
	
	public void viewPlayerRecord(Player player, String dungeon, String playerName){
		// TODO Show a player a specific player's record for a dungeon
	}
	
	public void viewPlayerRecord(CommandSender sender, String dungeon, String playerName){
		// TODO Show a non-player a specific player's record for a dungeon
	}
	
	public void createDungeon(Player sender, String dungeon, String difficulty, String creator){
		// TODO Create a dungeon with specific parameters
	}
	
	public void deleteDungeon(Player sender, String dungeon){
		// TODO Delete a dungeon by name
	}
	
	public void setParam(Player player, String dungeon, String param, boolean bool){
		// TODO Set a specific parameter value of a dungeon
	}
	
	public void viewParam(Player player, String dungeon, String param){
		// TODO Show a player the value of a param
	}
	
	public void allowCommand(Player player, String dungeon, String command){
		// TODO Set a command to be allowed in a dungeon
	}
	
	public void blockCommand(Player player, String dungeon, String command){
		// TODO Set a command to be blocked in a dungeon
	}
	
	public void viewCommand(Player player, String dungeon, String command){
		// TODO Show a player the value of a command
	}
	
	public void viewDungeon(Player player, String dungeon){
		// TODO Show a player info about a dungeon
	}
	
	public void viewInfo(Player player){
		// TODO Show a player general plugin info
	}
}
