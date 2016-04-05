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
		
		pluginManager.addPermission(new Permission("dungeon.info")); // view info about the plugin and each dungeon
		pluginManager.addPermission(new Permission("dungeon.join")); // join/leave a dungeon
		pluginManager.addPermission(new Permission("dungeon.forcejoin"));; // force a player to join/leave a dungeon
		pluginManager.addPermission(new Permission("dungeon.leaders")); // view the leaderboards of a dungeon
		pluginManager.addPermission(new Permission("dungeon.history")); // view the completion history of a dungeon or a specific player in a dungeon
		pluginManager.addPermission(new Permission("dungeon.record")); // view dungeon world record, personal record, and other players records
		pluginManager.addPermission(new Permission("dungeon.create")); // create a dungeon
		pluginManager.addPermission(new Permission("dungeon.param")); // view/change a parameter of a dungeon
		pluginManager.addPermission(new Permission("dungeon.command")); // view/change a commmand value of a dungeon
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
					parseRecord(command);
				}
				else if(subcmd.equalsIgnoreCase("create")){
					processCreate(command);
				}
				else if(subcmd.equalsIgnoreCase("delete")){
					processDelete(command);
				}
				else if(subcmd.equalsIgnoreCase("param")){
					processParam(command);
				}
				else if(subcmd.equalsIgnoreCase("command")){
					processCommand(command);
				}
				else if(plugin.getMongo().dungeonExists(subcmd)){
					processViewDungeon(command);
				}
				else
					processCommandNotFound(command);
			}
			else
				processDungeon(command);
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
			if(player.hasPermission("dungeon.leaders")){ // if the player has permission to view leaderboards
				if(command.args.length > 1){ // if the player included a dungeon
					if(plugin.getMongo().dungeonExists(command.args[1])){ // if the dungeon exists
						int page = 1;
						if(command.args.length > 2 && isParsableInt(command.args[2])) // if the number included is parsable and if the player included a page number
							page = Integer.parseInt(command.args[2]);					
						plugin.getManager().viewLeaderboard(player, command.args[1], page);
					}
					else
						plugin.getMessenger().dungeonNotFound(player, command.args[1]);
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
		if(command.sender instanceof Player){
			Player sender = (Player) command.sender;
			if(sender.hasPermission("dungeon.history")){
				if(command.args.length > 1){
					String dungeon = command.args[1];
					if(plugin.getMongo().dungeonExists(dungeon)){ // if the dungeon exists
						if(command.args.length > 2){// if the sender included a player
							Player player = plugin.getBukkit().playerExistsOnline(command.args[2]);
							if(player == null) // if the player doesn't exist online check offline
								player = (Player) plugin.getBukkit().playerExistsOffline(command.args[2]);
							if(player != null){ // if the player exists
								plugin.getManager().getPlayerHistory(sender, player.getName(), dungeon);
							}
							else
								plugin.getMessenger().playerNotFound(command.sender, command.args[2]);
						}
						else{
							plugin.getManager().getDungeonHistory(sender, dungeon);
						}
					}
					else
						plugin.getMessenger().dungeonNotFound(command.sender, dungeon);
				}
				else
					plugin.getMessenger().mustIncludeDungeon(sender);
			}
			else
				plugin.getMessenger().commandNotAllowed(sender);
		}
		else
			plugin.getMessenger().mustBePlayer(command.sender);
	}

	private void parseRecord(DungeonCommand command) {
		if(command.sender instanceof Player){ // if the sender is a player
			Player sender = (Player) command.sender;
			if(sender.hasPermission("dungeon.record")){ // if the sender has permission
				if(command.args.length > 1){ // if the sender included a dungeon
					if(plugin.getMongo().dungeonExists(command.args[1])){ // if the dungeon exists
						if(command.args.length > 2){ // if the sender included a player
							Player player = plugin.getBukkit().playerExistsOnline(command.args[2]);
							if(player == null) // if the player doesn't exist online check offline
								player = (Player) plugin.getBukkit().playerExistsOffline(command.args[2]);
							if(player != null) // if the player exists
								plugin.getManager().getPlayerRecord(sender, command.args[1], player.getName());
							else
								plugin.getMessenger().playerNotFound(sender, command.args[2]);
						}
						else
							plugin.getManager().getRecord(sender, command.args[1]);	
					}
					else
						plugin.getMessenger().dungeonNotFound(sender, command.args[1]);
				}
				else
					plugin.getMessenger().mustIncludeDungeon(sender);
			}
			else
				plugin.getMessenger().commandNotAllowed(sender);
		}
		else{
			if(command.args.length > 1){
				if(plugin.getMongo().dungeonExists(command.args[1])){
					if(command.args.length > 2){
						Player player = plugin.getBukkit().playerExistsOnline(command.args[2]);
						if(player == null) // if the player doesn't exist online check offline
							player = (Player) plugin.getBukkit().playerExistsOffline(command.args[2]);
						if(player != null) // if the player exists
							plugin.getManager().getPlayerRecord(command.sender, command.args[1], player.getName());
						
						else
							plugin.getMessenger().playerNotFound(command.sender, command.args[2]);
					}
					else
						plugin.getManager().getRecord(command.sender, command.args[1]);
				}
				else
					plugin.getMessenger().dungeonNotFound(command.sender, command.args[1]);
			}
			else
				plugin.getMessenger().mustIncludeDungeon(command.sender);
		}
	}

	private void processCreate(DungeonCommand command) {
		if(command.sender instanceof Player){ // if the sender is a player
			Player player = (Player) command.sender;
			if(player.hasPermission("dungeon.create")){ // if the player has permission to create a dungeon
				if(command.args.length > 1){ // if the player included a dungeon name
					if(command.args.length > 2){ // if the player included a difficulty
						if(isParsableDifficulty(command.args[2])){ // if the difficulty is parsable
							if(command.args.length > 3){ // if the player included a creator name
								String creator = "";
								for(int i = 3; i < command.args.length; i++)
									creator = creator + command.args[i];
								plugin.getManager().createDungeon(player, command.args[1], command.args[2], creator);
							}
							else
								plugin.getMessenger().mustIncludeCreator(player);
						}
						else
							plugin.getMessenger().difficultyNotFound(player, command.args[2]);
					}
					else
						plugin.getMessenger().mustIncludeDifficulty(player);
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
	
	private void processDelete(DungeonCommand command) {
		if(command.sender instanceof Player){ // if the sender is a player
			Player player = (Player) command.sender;
			if(player.hasPermission("dungeon.create")){ // if the player has permission to create a dungeon
				if(command.args.length > 1){ // if the player included a dungeon name
					if(plugin.getMongo().dungeonExists(command.args[1])) // if the dungeon exists
						plugin.getManager().deleteDungeon(player, command.args[1]);
					else
						plugin.getMessenger().dungeonNotFound(player, command.args[1]);
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

	private void processParam(DungeonCommand command) {
		if(command.sender instanceof Player){ // if the sender is a player
			Player player = (Player) command.sender;
			if(player.hasPermission("dungeon.param")){ // if the player has permission
				if(command.args.length > 1){ // if the player included a dungeon
					if(plugin.getMongo().dungeonExists(command.args[1])){ // if the dungeon exists
						if(command.args.length > 2){ // if the player included a param
							if(plugin.getConfigHandler().getParams().containsKey(command.args[2])){ // if the param exists
								if(command.args.length > 3){ // if the player included a bool
									if(isParsableBool(command.args[3])){ // if the bool can be parsed
										boolean bool = parseBool(command.args[3]);
										plugin.getManager().setParam(player, command.args[1], command.args[2], bool);
									}
									else
										plugin.getMessenger().argumentNotFound(player, command.args[3]);
								}
								else
									plugin.getManager().getParam(player, command.args[1], command.args[2]);
							}
							else
								plugin.getMessenger().paramNotFound(player, command.args[2]);
						}
						else
							plugin.getMessenger().mustIncludeParam(player);
					}
					else
						plugin.getMessenger().dungeonNotFound(player, command.args[1]);
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

	private void processCommand(DungeonCommand command) {
		if(command.sender instanceof Player){ // if the sender is a player
			Player player = (Player) command.sender;
			if(player.hasPermission("dungeon.command")){ // if the player has permission
				if(command.args.length > 1){ // if the player included a dungeon
					if(plugin.getMongo().dungeonExists(command.args[1])){ // if the dungeon exists
						if(command.args.length > 2){ // if the player included a command
							if(command.args.length > 3){ // if the player included a command
								if(isParsableCommand(command.args[3])){ // if the command can be parsed
									boolean bool = parseCommand(command.args[3]);
									if(bool)
										plugin.getManager().allowCommand(player, command.args[1], command.args[2]);
									else
										plugin.getManager().blockCommand(player, command.args[1], command.args[2]);
								}
								else
									plugin.getMessenger().argumentNotFound(player, command.args[3]);
							}
							else
								plugin.getManager().getCommand(player, command.args[1], command.args[2]);
						}
						else
							plugin.getMessenger().mustIncludeCommand(player);
					}
					else
						plugin.getMessenger().dungeonNotFound(player, command.args[1]);
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

	private void processDungeon(DungeonCommand command) {
		if(command.sender instanceof Player){ // if the sender is a player
			Player player = (Player) command.sender;
			if(player.hasPermission("dungeon.info")){ // if the player has permission
				plugin.getManager().viewInfo(player);
			}
			else
				plugin.getMessenger().commandNotAllowed(player);
		}
		else
			plugin.getMessenger().mustBePlayer(command.sender);
	}
	
	private void processViewDungeon(DungeonCommand command) {
		if(command.sender instanceof Player){
			Player player = (Player) command.sender;
			if(player.hasPermission("dungeon.info")){
				plugin.getManager().viewDungeon(player, command.args[0]);
			}
			else
				plugin.getMessenger().commandNotAllowed(player);
		}
		else
			plugin.getMessenger().mustBePlayer(command.sender);
	}
	
	private void processCommandNotFound(DungeonCommand command) {
		String subcmd = command.args[0];
		if(command.sender instanceof Player){
			Player player = (Player) command.sender;
			plugin.getMessenger().commandNotFound(player, subcmd);
		}
		else
			plugin.getMessenger().commandNotFound(command.sender, subcmd);
	}
	
	public boolean isParsableInt(String input){
	    boolean parsable = true;
	    try{
	        Integer.parseInt(input);
	    }catch(NumberFormatException e){
	        parsable = false;
	    }
	    return parsable;
	}
	
	public boolean isParsableBool(String input){
		if(input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false"))
			return true;
		return false;
	}
	
	public boolean parseBool(String input){
	    if(input.equalsIgnoreCase("true"))
	    	return true;
	    return false;
	}
	
	public boolean isParsableCommand(String input){
		if(input.equalsIgnoreCase("allow") || input.equalsIgnoreCase("block"))
			return true;
		return false;
	}
	
	public boolean parseCommand(String input){
	    if(input.equalsIgnoreCase("allow"))
	    	return true;
	    return false;
	}
	
	public boolean isParsableDifficulty(String input){
		if(input.equalsIgnoreCase("easy") || input.equalsIgnoreCase("medium") ||
				input.equalsIgnoreCase("hard") || input.equalsIgnoreCase("insane"))
			return true;
		return false;
	}
}
