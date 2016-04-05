package yourselvs;

import java.text.ParseException;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import yourselvs.dungeons.DungeonRecord;

public class Messenger {
	private String prefix = "[" + ChatColor.RED + ChatColor.BOLD + "DGN" + ChatColor.RESET + "]";
	private String unformattedPrefix = "[DGN]";
	private DungeonTracker plugin;
	
	public Messenger(DungeonTracker plugin){
		this.plugin = plugin;
	}
	
	public void commandNotAllowed(String dungeon, String command, Player player){
		sendMessage(player, "You are not allowed to use the command \"" + ChatColor.YELLOW + command + ChatColor.RESET + "\" because you are in dungeon \"" + ChatColor.YELLOW + dungeon + ChatColor.RESET + "\"." );
		sendPlayerLog(player, "tried to use command \"" + command + "\" while in dungeon \"" + dungeon + "\" and was stopped.");
	}
	
	public void commandNotAllowed(Player player){
		sendMessage(player, "You are not allowed to use that commmand." );
	}
	
	public void commandNotAllowed(CommandSender player){
		sendMessage(player, "You are not allowed to use that commmand." );
	}
	
	public void resumeDungeon(Player player, DungeonRecord record){
		String time = plugin.subtractTime(record.startTime, new Date());
		sendMessage(player,  "Resuming dungeon \"" + ChatColor.YELLOW + record.dungeon + ChatColor.RESET + "\" with a time of " + ChatColor.YELLOW + time + ChatColor.RESET + "." );
		sendPlayerLog(player, "rejoined and resumed dungeon \"" + record.dungeon + "\" with a time of " + time + ".");
	}
	
	public void quitDungeon(Player player, DungeonRecord record){
		String time = plugin.subtractTime(record.startTime, new Date());
		sendPlayerLog(player, "quit the server and left dungeon \"" + record.dungeon + "\" with a time of " + time + ".");
	}
	
	public void startDungeon(Player player, DungeonRecord record){
		String time = plugin.getFormatter().format(record.startTime);
		sendMessage(player, "Starting dungeon \"" + ChatColor.YELLOW + record.dungeon + ChatColor.RESET + "\" at time " + ChatColor.YELLOW + time + ChatColor.RESET + ".");
		sendPlayerLog(player, "started the dungeon \"" + record.dungeon + "\" at the time " + time + ".");
	}
	
	public void finishDungeon(Player player, DungeonRecord record){
		String time = plugin.subtractTime(record.startTime, record.finishTime);
		sendMessage(player, "Dungeon \"" + ChatColor.YELLOW + record.dungeon + ChatColor.RESET + "\" finished with a time of " + ChatColor.YELLOW + time + ChatColor.RESET + ".");
		sendPlayerLog(player, "finished the dungeon \"" + record.dungeon + "\" with a time of " + time + ".");
	}
	
	public void beatDungeonPR(Player player, DungeonRecord oldPR, DungeonRecord newPR){
		String oldTime = plugin.subtractTime(oldPR.startTime, oldPR.finishTime);
		String newTime = plugin.subtractTime(newPR.startTime, newPR.finishTime);
		
		Date oldDate = null;
		Date newDate = null;
		try {
			oldDate = plugin.getFormatter().parse(oldTime);
			newDate = plugin.getFormatter().parse(newTime);
		} catch (ParseException e) {e.printStackTrace();}
		
		String difference = plugin.subtractTime(newDate, oldDate);
		
		sendMessage(player, "You beat your old time by " + ChatColor.YELLOW + difference + ChatColor.RESET + ".");
	}
	
	public void beatDungeonWR(Player player, DungeonRecord oldPR, DungeonRecord newPR){
		String oldTime = plugin.subtractTime(oldPR.startTime, oldPR.finishTime);
		String newTime = plugin.subtractTime(newPR.startTime, newPR.finishTime);
		
		Date oldDate = null;
		Date newDate = null;
		try {
			oldDate = plugin.getFormatter().parse(oldTime);
			newDate = plugin.getFormatter().parse(newTime);
		} catch (ParseException e) {e.printStackTrace();}
		
		String difference = plugin.subtractTime(newDate, oldDate);
		
		sendServerMessage(ChatColor.DARK_PURPLE + "NEW WORLD RECORD");
		sendServerMessage(ChatColor.YELLOW + player.getName() + " beat the world record time in \"" + ChatColor.YELLOW + newPR.dungeon + ChatColor.RESET + " with a time of " + ChatColor.YELLOW + newTime + ChatColor.RESET + ", beating the world record by " + ChatColor.YELLOW + difference + ChatColor.RESET + ".");
	}
	
	public void commandNotFound(Player player, String command){
		sendMessage(player, "Command not recognized: " + ChatColor.YELLOW + command);
	}
	
	public void commandNotFound(CommandSender player, String command){
		sendMessage(player, "Command not recognized: " + command);
	}
	
	public void dungeonNotFound(Player player, String dungeon){
		sendMessage(player, "Dungeon not found: " + ChatColor.YELLOW + dungeon);
	}
	
	public void dungeonNotFound(CommandSender player, String dungeon){
		sendMessage(player, "Dungeon not found: " + dungeon);
	}
	
	public void playerNotFound(Player sender, String player) {
		sendMessage(sender, "Player not found: " + ChatColor.YELLOW + player);
	}
	
	public void playerNotFound(CommandSender sender, String player) {
		sendMessage(sender, "Player not found: " + player);
	}
	
	public void mustBePlayer(CommandSender player){
		sendMessage(player, "You must be a player to do this.");
	}
	
	public void mustIncludeDungeon(Player player){
		sendMessage(player, "You must include a dungeon.");
	}
	
	public void mustIncludeDungeon(CommandSender player){
		sendMessage(player, "You must include a dungeon.");
	}
	
	public void mustIncludePlayer(Player player) {
		sendMessage(player, "You must include a player.");
	}
	
	public void mustIncludePlayer(CommandSender player) {
		sendMessage(player, "You must include a player.");
	}	
	
	public void mustBeInDungeon(Player player){
		sendMessage(player, "You must be in a dungeon to do this.");
	}
	
	public void mustBeInDungeon(CommandSender player){
		sendMessage(player, "The player must be in a dungeon to do this.");
	}
	
	public void invalidPageNum(Player player) {
		sendMessage(player, "That is an invalid page.");
	}
	
	public void mustIncludeCreator(Player player){
		sendMessage(player, "You must include a creator.");
	}
	
	public void mustIncludeParam(Player player){
		sendMessage(player, "You must include a parameter.");
	}
	
	public void mustIncludeCommand(Player player){
		sendMessage(player, "You must include a command.");
	}
	
	public void mustIncludeDifficulty(Player player){
		sendMessage(player, "You must include a difficulty.");
	}
	
	public void argumentNotFound(Player player, String arg){
		sendMessage(player, "Argument not found: " + ChatColor.YELLOW + arg);
	}
	
	public void paramNotFound(Player player, String string) {
		sendMessage(player, "Parameter not found: " + ChatColor.YELLOW + string);
	}
	
	public void difficultyNotFound(Player player, String string) {
		sendMessage(player, "Difficulty not found: " + ChatColor.YELLOW + string);
	}
	
	private void sendPlayerLog(Player player, String message){
		plugin.getLogger().info("Player \"" + player.getName() + "\" " + message);
	}
	
	private void sendMessage(Player player, String message){
		player.sendMessage(prefix + " " + ChatColor.RESET + message + ChatColor.RESET);
	}
	
	private void sendMessage(CommandSender player, String message){
		player.sendMessage(unformattedPrefix + " " + message);
	}
	
	private void sendServerMessage(String message){
		for(Player player : plugin.getServer().getOnlinePlayers())
			sendMessage(player, message);
	}	
}
