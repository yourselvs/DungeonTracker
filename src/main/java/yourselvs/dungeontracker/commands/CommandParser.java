package yourselvs.dungeontracker.commands;

import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import yourselvs.dungeontracker.DungeonTracker;
import yourselvs.dungeontracker.events.PlayerFinishDungeonEvent;
import yourselvs.dungeontracker.events.PlayerStartDungeonEvent;

public class CommandParser implements CommandExecutor{
	private DungeonTracker plugin;
	
	public CommandParser(DungeonTracker plugin){
		this.plugin = plugin;
		plugin.getServer().getPluginManager();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("dungeon") || cmd.getName().equalsIgnoreCase("dgn") ||
				cmd.getName().equalsIgnoreCase("dt") || cmd.getName().equalsIgnoreCase("dungeontracker")){
			if(sender.hasPermission("dungeon")){
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
					else {
						new Thread(new Runnable() {
							public void run(){
								if(subcmd.equalsIgnoreCase("create")){
									processCreate(command);
								}
								else if(subcmd.equalsIgnoreCase("delete")){
									processDelete(command);
								}
										/*else if(subcmd.equalsIgnoreCase("param")){
											processParam(command);
										}
										else if(subcmd.equalsIgnoreCase("command")){
											processCommand(command);
										}*/
								else if(subcmd.equalsIgnoreCase("view")){
									processView(command);
								}
								else if(subcmd.equalsIgnoreCase("list")){
									processList(command);
								}
								else if(subcmd.equalsIgnoreCase("help")){
									processHelp(command);
								}
								else
									processCommandNotFound(command);
							}
						}).start();
					}
				}
				else
					processDungeon(command);
			}
			else{
				sender.sendMessage("You are not allowed to use this plugin.");
			}
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
			plugin.getMessenger().commandNotAllowed((Player) command.sender);
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
						plugin.getMessenger().viewLeaderboard(player, command.args[1], page);
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
			if(sender.hasPermission("dungeon.history")){ // if the sender has permission 
				if(command.args.length > 1){ // if the sender included a dungeon
					String dungeon = command.args[1];
					if(plugin.getMongo().dungeonExists(dungeon)){ // if the dungeon exists
						if(command.args.length > 2){// if the sender included a player or page number
							Player player = plugin.getBukkit().playerExistsOnline(command.args[2]);
							if(player == null) // if the player doesn't exist online check offline
								player = (Player) plugin.getBukkit().playerExistsOffline(command.args[2]);
							if(player != null){ // if the player exists
								int page = 1;
								if(command.args.length > 3 && isParsableInt(command.args[3])) // if the number included is parsable and if the player included a page number
									page = Integer.parseInt(command.args[3]);
								plugin.getMessenger().viewPlayerHistory(sender, player.getName(), dungeon, page);
							}
							else if(isParsableInt(command.args[2])){
								int page = Integer.parseInt(command.args[2]);
								plugin.getMessenger().viewDungeonHistory(sender, dungeon, page);
							}
							else
								plugin.getMessenger().playerNotFound(command.sender, command.args[2]);
						}
						else{
							int page = 1;
							plugin.getMessenger().viewDungeonHistory(sender, dungeon, page);
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

	private void processRecord(DungeonCommand command) {
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
								plugin.getMessenger().viewPlayerRecord(sender, command.args[1], player.getName());
							else
								plugin.getMessenger().playerNotFound(sender, command.args[2]);
						}
						else
							plugin.getMessenger().viewRecord(sender, command.args[1]);	
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
							plugin.getMessenger().viewPlayerRecord(command.sender, command.args[1], player.getName());
						
						else
							plugin.getMessenger().playerNotFound(command.sender, command.args[2]);
					}
					else
						plugin.getMessenger().viewRecord(command.sender, command.args[1]);
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
					if(!plugin.getMongo().dungeonExists(command.args[1])){ // if the dungeon doesnt already exist
						if(command.args.length > 2){ // if the player included a difficulty
							if(isParsableDifficulty(command.args[2])){ // if the difficulty is parsable
								if(command.args.length > 3){ // if the player included a creator name
									String creator = "";
									for(int i = 3; i < command.args.length; i++)
										creator = creator + command.args[i] + " ";
									plugin.getManager().createDungeon(player, command.args[1], command.args[2].toUpperCase(), creator);
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
						plugin.getMessenger().dungeonAlreadyExists(player);
				}
				else
					plugin.getMessenger().mustIncludeDungeonName(player);
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
							if(plugin.getParams().containsKey(command.args[2])){ // if the param exists
								if(command.args.length > 3){ // if the player included a bool
									if(isParsableBool(command.args[3])){ // if the bool can be parsed
										boolean bool = parseBool(command.args[3]);
										plugin.getManager().setParam(player, command.args[1], command.args[2], bool);
									}
									else
										plugin.getMessenger().argumentNotFound(player, command.args[3]);
								}
								else
									plugin.getMessenger().viewParam(player, command.args[1], command.args[2]);
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
								if(isParsableBool(command.args[3])){ // if the command can be parsed
									boolean bool = parseCommand(command.args[3]);
									if(bool)
										plugin.getManager().setCommand(player, command.args[1], command.args[2], bool);
									else
										plugin.getManager().setCommand(player, command.args[1], command.args[2], bool);
								}
								else
									plugin.getMessenger().argumentNotFound(player, command.args[3]);
							}
							else
								plugin.getMessenger().viewCommand(player, command.args[1], command.args[2]);
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
				plugin.getMessenger().viewInfo(player);
			}
			else
				plugin.getMessenger().commandNotAllowed(player);
		}
		else
			plugin.getMessenger().mustBePlayer(command.sender);
	}
	
	private void processView(DungeonCommand command) {
		if(command.sender instanceof Player){
			Player player = (Player) command.sender;
			if(player.hasPermission("dungeon.info")){
				if(plugin.getMongo().dungeonExists(command.args[1])){
					
					if(player.hasPermission("dungeon.info")){
						plugin.getMessenger().viewDungeon(player, command.args[1]);
					}
					else
						plugin.getMessenger().commandNotAllowed(player);
				}
				else
					plugin.getMessenger().dungeonNotFound(player, command.args[1]);
			}
			else
				plugin.getMessenger().commandNotAllowed(player);
		}
		else
			plugin.getMessenger().mustBePlayer(command.sender);
	}
	
	private void processList(DungeonCommand command) {
		if(command.sender instanceof Player){
			Player player = (Player) command.sender;
			if(player.hasPermission("dungeon.list")){
				if(command.args.length > 1 && isParsableInt(command.args[1])){
					int page = Integer.parseInt(command.args[1]);
					plugin.getMessenger().listDungeons(player, page);
				}
				else
					plugin.getMessenger().listDungeons(player, 1);
			}
			else
				plugin.getMessenger().commandNotAllowed(player);
		}
		else
			plugin.getMessenger().mustBePlayer(command.sender);
	}
	
	private void processHelp(DungeonCommand command) {
		if(command.sender instanceof Player){
			Player player = (Player) command.sender;
			plugin.getMessenger().viewHelp(player);
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
