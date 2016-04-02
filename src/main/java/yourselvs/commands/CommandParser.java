package yourselvs.commands;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

import yourselvs.DungeonTracker;
import yourselvs.events.PlayerFinishDungeonEvent;
import yourselvs.events.PlayerStartDungeonEvent;

public class CommandParser implements CommandExecutor{
	private DungeonTracker plugin;
	private PluginManager pluginManager;
	
	public CommandParser(DungeonTracker plugin){
		this.plugin = plugin;
		this.pluginManager = plugin.getServer().getPluginManager();
		
		pluginManager.addPermission(new Permission("dungeon.viewinfo")); // view info about the plugin and each dungeon
		pluginManager.addPermission(new Permission("dungeon.join")); // join/leave a dungeon
		pluginManager.addPermission(new Permission("dungeon.forcejoin"));; // force a player to join a dungeon
		pluginManager.addPermission(new Permission("dungeon.forceleave")); // force a player to leave a dungeon
		pluginManager.addPermission(new Permission("dungeon.viewleaders")); // view the leaderboards of a dungeon
		pluginManager.addPermission(new Permission("dungeon.viewhistory")); // view the completion history of a dungeon
		pluginManager.addPermission(new Permission("dungeon.viewplayerhistory")); // view the completion history of a specific player in a dungeon
		pluginManager.addPermission(new Permission("dungeon.viewrecord")); // view dungeon world record
		pluginManager.addPermission(new Permission("dungeon.viewplayerrecord")); // view player record in a dungeon
		pluginManager.addPermission(new Permission("dungeon.viewselfrecord")); // view your own record in a dungeon
		pluginManager.addPermission(new Permission("dungeon.create")); // create a dungeon
		pluginManager.addPermission(new Permission("dungeon.viewparam")); // view a parameter of a dungeon
		pluginManager.addPermission(new Permission("dungeon.changeparam")); // change a paramter of a dungeon
		pluginManager.addPermission(new Permission("dungeon.viewcommand")); // view a commmand value of a dungeon
		pluginManager.addPermission(new Permission("dungeon.changecommand")); // change whether a command is allowed in a dungeon
		pluginManager.addPermission(new Permission("dungeon.changedescription")); // change the description of a dungeon
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("dungeon") || cmd.getName().equalsIgnoreCase("dgn") ||
				cmd.getName().equalsIgnoreCase("dt") || cmd.getName().equalsIgnoreCase("dungeontracker")){
			DungeonCommand command = new DungeonCommand(sender, cmd, label, args);
			if(command.args.length > 0){
				String subcmd = args[0];

				if(subcmd.equalsIgnoreCase("join")){
					processJoin(command);
				}
				else if(subcmd.equalsIgnoreCase("leave")){
					processLeave(command);
				}
				else if(subcmd.equalsIgnoreCase("forcejoin")){
					processForceJoin(command);
				}
				else if(subcmd.equalsIgnoreCase("forceleave")){
					processForceLeave(command);
				}
				else if(subcmd.equalsIgnoreCase("complete")){
					processComplete(command);
				}
				else if(subcmd.equalsIgnoreCase("leaders")){
					processLeaders(command);
				}
				else if(subcmd.equalsIgnoreCase("history")){
					processHistory(command);
				}
				else if(subcmd.equalsIgnoreCase("record")){
					processRecord(command);
				}
				else if(subcmd.equalsIgnoreCase("create")){
					processCreate(command);
				}
				else if(subcmd.equalsIgnoreCase("param")){
					processParam(command);
				}
				else if(subcmd.equalsIgnoreCase("command")){
					processCommand(command);
				}
				else if(command.sender instanceof Player)
					processCommandNotFound(command);
			}
			else
				processViewDungeon(command);
			return true;
		}
		return false;
	}

	private void processJoin(DungeonCommand command) {
		if(command.sender instanceof Player){ // if sender is a player
			Player player = (Player) command.sender;	
			if(player.hasPermission("dungeon.join")){ // if the player has permission to join a dungeon
				if(command.args.length > 1){ // if the player included a dungeon
					String dungeon = command.args[1];
					if(plugin.getMongo().dungeonExists(dungeon)){ // if the dungeon exists
						PlayerStartDungeonEvent event = new PlayerStartDungeonEvent(player, new Date(), dungeon);
						Bukkit.getServer().getPluginManager().callEvent(event);
						
						if(!event.isCancelled())
							plugin.getManager().joinDungeon(event);
					}
					else
						plugin.getMessenger().dungeonNotFound(player, dungeon);
				}
				else
					plugin.getMessenger().mustIncludeDungeon(player);
			}
			else{
				String fullCommand = "";
				for(String string: command.args)
					fullCommand = fullCommand + string;
				plugin.getMessenger().commandNotAllowed(player);
			}
		}
		else
			plugin.getMessenger().mustBePlayer(command.sender);
	}

	private void processLeave(DungeonCommand command) {
		if(command.sender instanceof Player){ // if the sender is a player
			Player player = (Player) command.sender;
			if(plugin.getMongo().getPlayerDungeon(player) != null) // if the player is in a dungeon
				plugin.getManager().leaveDungeon(player);
			else
				plugin.getMessenger().mustBeInDungeon(player);
		}
		else
			plugin.getMessenger().mustBePlayer(command.sender);
	}

	private void processForceJoin(DungeonCommand command) {
		if(command.sender instanceof Player){ // if sender is a player
			Player sender = (Player) command.sender;	
			if(sender.hasPermission("dungeon.forcejoin")){ // if a player has permission to force a player to join a dungeon
				if(command.args.length > 1){ // if the sender included a dungeon
					String dungeon = command.args[1];
					if(plugin.getMongo().dungeonExists(dungeon)){ // if the dungeon exists
						if(command.args.length > 2){ // if the sender included a player
							Player player = plugin.getBukkit().playerExistsOnline(command.args[2]);
							if(player != null){ // if the player exists online
								PlayerStartDungeonEvent event = new PlayerStartDungeonEvent(player, new Date(), dungeon);
								Bukkit.getServer().getPluginManager().callEvent(event);
								
								if(!event.isCancelled())
									plugin.getManager().joinDungeon(event);
							}
							else
								plugin.getMessenger().playerNotFound(sender, command.args[2]);
						}
						else
							plugin.getMessenger().mustIncludePlayer(sender);
					}
					else
						plugin.getMessenger().dungeonNotFound(sender, dungeon);
				}
				else
					plugin.getMessenger().mustIncludeDungeon(sender);
			}
			else
				plugin.getMessenger().commandNotAllowed(sender);
		}
		else{ // if the sender is not a player
			if(command.args.length > 1){ // if the sender included a dungeon
				String dungeon = command.args[1];
				if(plugin.getMongo().dungeonExists(dungeon)){ // if the dungeon exists
					if(command.args.length > 2){// if the sender included a player
						Player player = plugin.getBukkit().playerExistsOnline(command.args[2]);
						if(player != null){ // if the player exists online
							PlayerStartDungeonEvent event = new PlayerStartDungeonEvent(player, new Date(), dungeon);
							Bukkit.getServer().getPluginManager().callEvent(event);
							
							if(!event.isCancelled())
								plugin.getManager().joinDungeon(event);
						}
						else
							plugin.getMessenger().playerNotFound(command.sender, command.args[2]);
					}
					else
						plugin.getMessenger().mustIncludePlayer(command.sender);
				}
				else
					plugin.getMessenger().dungeonNotFound(command.sender, dungeon);
			}
			else
				plugin.getMessenger().mustIncludeDungeon(command.sender);
		}
	}

	private void processForceLeave(DungeonCommand command) {
		if(command.sender instanceof Player){ // if the sender is a player
			Player sender = (Player) command.sender;
			if(sender.hasPermission("dungeon.forceleave")){ // if the sender has permission to force a player to leave
				if(command.args.length > 1){// if the sender included a player
					Player player = plugin.getBukkit().playerExistsOnline(command.args[1]);
					if(player != null) // if the player exists online
						plugin.getManager().leaveDungeon(player);
					else
						plugin.getMessenger().playerNotFound(sender, command.args[2]);
				}
				else
					plugin.getMessenger().mustIncludePlayer(sender);
			}
		}
		else{
			if(command.args.length > 1){// if the sender included a player
				Player player = plugin.getBukkit().playerExistsOnline(command.args[1]);
				if(player != null) // if the player exists online
					plugin.getManager().leaveDungeon(player);
				else
					plugin.getMessenger().playerNotFound(command.sender, command.args[2]);
			}
			else
				plugin.getMessenger().mustIncludePlayer(command.sender);
		}
	}

	private void processComplete(DungeonCommand command) {
		if(!(command.sender instanceof Player)){ // if the sender is not a player
			if(command.args.length > 1){ // if the sender included a player
				Player player = plugin.getBukkit().playerExistsOnline(command.args[1]);
				if(player != null){ // if the player exists online
					String dungeon = plugin.getMongo().getPlayerDungeon(player);
					if(dungeon != null){ // if the player is in a dungeon
						PlayerFinishDungeonEvent event = new PlayerFinishDungeonEvent(player, new Date(), dungeon);
						Bukkit.getServer().getPluginManager().callEvent(event);
						
						if(!event.isCancelled())
							plugin.getManager().finishDungeon(event);
					}
					else
						plugin.getMessenger().mustBeInDungeon(command.sender);
				}
				else
					plugin.getMessenger().playerNotFound(command.sender, command.args[1]);
			}
			else
				plugin.getMessenger().mustIncludePlayer(command.sender);
		}
		else
			plugin.getMessenger().commandNotAllowed(command.sender);
	}

	private void processLeaders(DungeonCommand command) {
		if(command.sender instanceof Player){ // if the sender is a player
			Player player = (Player) command.sender;
			if(player.hasPermission("dungeon.viewleaders")){ // if the player has permission to view leaderboards
				if(command.args.length > 1){ // if the player included a dungeon
					int page = 1;
					if(command.args.length > 2 && isParsable(command.args[2])) // if the number included is parsable and if the player included a page number
						page = Integer.parseInt(command.args[2]);					
					plugin.getManager().viewLeaderboard(command, page);
				}
				else
					plugin.getMessenger().mustIncludeDungeon(player);
			}
			else
				plugin.getMessenger().commandNotAllowed(player);
		}
		else
			plugin.getMessenger().mustBePlayer(command.sender);
	}

	private void processHistory(DungeonCommand command) {
		
	}

	private void processRecord(DungeonCommand command) {
		// TODO Auto-generated method stub
		
	}

	private void processCreate(DungeonCommand command) {
		// TODO Auto-generated method stub
		
	}

	private void processParam(DungeonCommand command) {
		// TODO Auto-generated method stub
		
	}

	private void processCommand(DungeonCommand command) {
		// TODO Auto-generated method stub
		
	}

	private void processCommandNotFound(DungeonCommand command) {
		String subcmd = command.args[0];
		Player player = (Player) command.sender;
		plugin.getMessenger().commandNotFound(player, subcmd);
	}

	private void processViewDungeon(DungeonCommand command) {
		// TODO Auto-generated method stub
		
	}
	
	public static boolean isParsable(String input){
	    boolean parsable = true;
	    try{
	        Integer.parseInt(input);
	    }catch(NumberFormatException e){
	        parsable = false;
	    }
	    return parsable;
	}
}
