package yourselvs.dungeontracker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mythicacraft.voteroulette.utils.InteractiveMessageAPI.FormattedText;
import com.mythicacraft.voteroulette.utils.InteractiveMessageAPI.InteractiveMessage;
import com.mythicacraft.voteroulette.utils.InteractiveMessageAPI.InteractiveMessageElement;
import com.mythicacraft.voteroulette.utils.InteractiveMessageAPI.InteractiveMessageElement.ClickEvent;
import com.mythicacraft.voteroulette.utils.InteractiveMessageAPI.InteractiveMessageElement.HoverEvent;

import yourselvs.dungeontracker.dungeons.Dungeon;
import yourselvs.dungeontracker.dungeons.DungeonRecord;

public class Messenger {
	private String prefix = "[" + ChatColor.RED + ChatColor.BOLD + "DGN" + ChatColor.RESET + "]";
	private String linkPrefix = ChatColor.AQUA + "[" + ChatColor.RED + ChatColor.BOLD + "DGN" + ChatColor.RESET + ChatColor.AQUA + "]" + ChatColor.RESET;
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
		String time = plugin.subtractTime(record.getStartTime(), new Date());
		sendMessage(player,  "Resuming dungeon \"" + ChatColor.YELLOW + record.getDungeon() + ChatColor.RESET + "\" with a time of " + ChatColor.YELLOW + time + ChatColor.RESET + "." );
		sendPlayerLog(player, "rejoined and resumed dungeon \"" + record.getDungeon() + "\" with a time of " + time + ".");
	}
	
	public void quitDungeon(Player player, DungeonRecord record){
		String time = plugin.subtractTime(record.getStartTime(), new Date());
		sendPlayerLog(player, "quit the server and left dungeon \"" + record.getDungeon() + "\" with a time of " + time + ".");
	}
	
	public void leaveDungeon(Player player, DungeonRecord record){
		String time = plugin.subtractTime(record.getStartTime(), new Date());
		sendPlayerLog(player, "left the dungeon \"" + record.getDungeon() + "\" with a time of " + time + ".");
	}
	
	public void startDungeon(Player player, DungeonRecord record){
		String time = plugin.getFormatter().format(record.getStartTime());
		sendMessage(player, "Starting dungeon \"" + ChatColor.YELLOW + record.getDungeon() + ChatColor.RESET + "\" at time " + ChatColor.YELLOW + time + ChatColor.RESET + ".");
		sendPlayerLog(player, "started the dungeon \"" + record.getDungeon() + "\" at the time " + time + ".");
	}
	
	public void finishDungeon(Player player, DungeonRecord record){
		String time = plugin.subtractTime(record.getStartTime(), record.getFinishTime());
		sendMessage(player, "Dungeon \"" + ChatColor.YELLOW + record.getDungeon() + ChatColor.RESET + "\" finished with a time of " + ChatColor.YELLOW + time + ChatColor.RESET + ".");
		sendPlayerLog(player, "finished the dungeon \"" + record.getDungeon() + "\" with a time of " + time + ".");
	}
	
	public void beatDungeonPR(Player player, DungeonRecord oldPR, DungeonRecord newPR){
		String oldTime = plugin.subtractTime(oldPR.getStartTime(), oldPR.getFinishTime());
		String newTime = plugin.subtractTime(newPR.getStartTime(), newPR.getFinishTime());
		
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
		String oldTime = plugin.subtractTime(oldPR.getStartTime(), oldPR.getFinishTime());
		String newTime = plugin.subtractTime(newPR.getStartTime(), newPR.getFinishTime());
		
		Date oldDate = null;
		Date newDate = null;
		try {
			oldDate = plugin.getFormatter().parse(oldTime);
			newDate = plugin.getFormatter().parse(newTime);
		} catch (ParseException e) {e.printStackTrace();}
		
		String difference = plugin.subtractTime(newDate, oldDate);
		
		sendServerMessage(ChatColor.DARK_PURPLE + "NEW WORLD RECORD");
		sendServerMessage(ChatColor.YELLOW + player.getName() + " beat the world record time in \"" + ChatColor.YELLOW + newPR.getDungeon() + ChatColor.RESET + " with a time of " + ChatColor.YELLOW + newTime + ChatColor.RESET + ", beating the world record by " + ChatColor.YELLOW + difference + ChatColor.RESET + ".");
	}
	
	public void viewLeaderboard(Player player, String dungeon, int page) {
		ArrayList<DungeonRecord> records = plugin.getMongo().getRecords(dungeon);
		records = plugin.sortRecordsCompletion(records);
		int pageSize = plugin.getPageSize();
		int maxPage = (records.size() / pageSize) + 1;
		boolean proceed = true;
		
		if(proceed && page > 0 && page <= maxPage){
			sendMessage(player, ChatColor.AQUA + dungeon + ChatColor.RESET + " records. Page " + ChatColor.YELLOW + page + ChatColor.RESET + " of " + ChatColor.YELLOW + maxPage);
			for(int i = (page - 1) * pageSize; i < ((page - 1) * pageSize) + 6; i++){
				if(i < records.size()){
					DungeonRecord record = records.get(i);
					sendMessage(player, "" + ChatColor.YELLOW + ChatColor.ITALIC + record.getPlayer().getName() + ChatColor.RESET + " | " + plugin.subtractTime(record.getStartTime(), record.getFinishTime()));
				}
			}
			if(page > 1)
				sendClickMessage(player, ChatColor.YELLOW + "Previous page", "Click to view previous page", "/sh leaders " + dungeon + (page - 1));
			if(page < maxPage)
				sendClickMessage(player, ChatColor.YELLOW + "Next page", "Click to view next page", "/sh leaders " + dungeon +  (page + 1));
		}
	}
	
	public void viewDungeonHistory(Player player, String dungeon, int page){
		ArrayList<DungeonRecord> records = plugin.getMongo().getRecords(dungeon);
		records = plugin.sortRecordsStart(records);
		int pageSize = plugin.getPageSize();
		int maxPage = (records.size() / pageSize) + 1;
		boolean proceed = true;
		
		if(proceed && page > 0 && page <= maxPage){
			sendMessage(player, ChatColor.AQUA + dungeon + ChatColor.RESET + " records. Page " + ChatColor.YELLOW + page + ChatColor.RESET + " of " + ChatColor.YELLOW + maxPage);
			for(int i = (page - 1) * pageSize; i < ((page - 1) * pageSize) + 6; i++){
				if(i < records.size()){
					DungeonRecord record = records.get(i);
					sendMessage(player, "" + ChatColor.YELLOW + ChatColor.ITALIC + record.getPlayer().getName() + ChatColor.RESET + " | " + plugin.subtractTime(record.getStartTime(), record.getFinishTime()));
				}
			}
			if(page > 1)
				sendClickMessage(player, ChatColor.YELLOW + "Previous page", "Click to view previous page", "/sh leaders " + dungeon + (page - 1));
			if(page < maxPage)
				sendClickMessage(player, ChatColor.YELLOW + "Next page", "Click to view next page", "/sh leaders " + dungeon +  (page + 1));
		}
	}
	
	@SuppressWarnings("deprecation")
	public void viewPlayerHistory(Player player, String playerName, String dungeon, int page){
		Player searchPlayer = plugin.getServer().getPlayer(playerName);
		ArrayList<DungeonRecord> records = plugin.getMongo().getRecords(dungeon, searchPlayer);
		records = plugin.sortRecordsStart(records);
		int pageSize = plugin.getPageSize();
		int maxPage = (records.size() / pageSize) + 1;
		boolean proceed = true;
		
		if(proceed && page > 0 && page <= maxPage){
			sendMessage(player, ChatColor.AQUA + dungeon + ChatColor.RESET + " records (" + ChatColor.AQUA + playerName + "). Page " + ChatColor.YELLOW + page + ChatColor.RESET + " of " + ChatColor.YELLOW + maxPage);
			for(int i = (page - 1) * pageSize; i < ((page - 1) * pageSize) + 6; i++){
				if(i < records.size()){
					DungeonRecord record = records.get(i);
					sendMessage(player, "" + ChatColor.YELLOW + ChatColor.ITALIC + record.getPlayer().getName() + ChatColor.RESET + " | " + plugin.subtractTime(record.getStartTime(), record.getFinishTime()));
				}
			}
			if(page > 1)
				sendClickMessage(player, ChatColor.YELLOW + "Previous page", "Click to view previous page", "/sh leaders " + dungeon + playerName + (page - 1));
			if(page < maxPage)
				sendClickMessage(player, ChatColor.YELLOW + "Next page", "Click to view next page", "/sh leaders " + dungeon + playerName + (page + 1));
		}
	}
	
	public void viewRecord(Player player, String dungeon){
		DungeonRecord record = plugin.getMongo().getFastestTime(dungeon);
		sendMessage(player, "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "WR" + ChatColor.RESET + " | " + 
				ChatColor.YELLOW + ChatColor.ITALIC + record.getPlayer().getName() + ChatColor.RESET + " | " + 
				plugin.getFormatter().format(record.getCompletionTime()));
	}
	
	public void viewRecord(CommandSender sender, String dungeon){
		DungeonRecord record = plugin.getMongo().getFastestTime(dungeon);
		sendMessage(sender, "WR | " + record.getPlayer().getName() + " | " +	plugin.getFormatter().format(record.getCompletionTime()));
	}
	
	public void viewPlayerRecord(Player player, String dungeon, String playerName){
		DungeonRecord record = plugin.getMongo().getFastestTime(dungeon, playerName);
		sendMessage(player, "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "WR" + ChatColor.RESET + " | " + 
				ChatColor.YELLOW + ChatColor.ITALIC + record.getPlayer().getName() + ChatColor.RESET + " | " + 
				plugin.getFormatter().format(record.getCompletionTime()));
	}
	
	public void viewPlayerRecord(CommandSender sender, String dungeon, String playerName){
		DungeonRecord record = plugin.getMongo().getFastestTime(dungeon, playerName);
		sendMessage(sender, "WR | " + record.getPlayer().getName() + " | " +	plugin.getFormatter().format(record.getCompletionTime()));
	}
	
	public void confirmCreateDungeon(String dungeon, Player player, String difficulty) {
		sendMessage(player, "Dungeon successfully created | Name: " + dungeon + " | Difficulty: " + difficulty);
	}
	
	public void confirmDeleteDungeon(Player player, String dungeon) {
		sendMessage(player, "Dungeon successfully deleted: " + dungeon);
	}
	
	public void confirmUpdateParam(Player player, String dungeon, String param, boolean value) {
		sendMessage(player, "Parameter updated | Dungeon: " + dungeon + 
				" | Parameter: " + param + 
				" | Value: " + value);
	}
	
	public void confirmUpdateCommand(Player player, String dungeon, String command, boolean value) {
		sendMessage(player, "Parameter updated | Dungeon: " + ChatColor.YELLOW + dungeon + ChatColor.RESET + 
				" | Command: " + ChatColor.YELLOW + command + ChatColor.RESET + 
				" | Value: " + ChatColor.YELLOW + value + ChatColor.RESET);
	}
	
	public void viewCommand(Player player, String dungeon, String command){
		boolean value = plugin.getMongo().getCommandValue(dungeon, command);
		String allowed = value ? "Yes" : "No";
		sendMessage(player, "Dungeon: " + ChatColor.YELLOW + dungeon + ChatColor.RESET + 
				" | Command: " + ChatColor.YELLOW + command + ChatColor.RESET + 
				" | Allowed: " + ChatColor.YELLOW + allowed + ChatColor.RESET); 
	}
	
	public void viewParam(Player player, String dungeon, String param){
		boolean value = plugin.getMongo().getParamValue(dungeon, param);
		sendMessage(player, "Dungeon: " + ChatColor.YELLOW + dungeon + ChatColor.RESET + 
				" | Parameter: " + ChatColor.YELLOW + param + ChatColor.RESET + 
				" | Value: " + ChatColor.YELLOW + value + ChatColor.RESET);
	}
	
	public void viewDungeon(Player player, String dungeonName){
		Dungeon dungeon = plugin.getMongo().getDungeon(dungeonName);
		ArrayList<String> messages = new ArrayList<String>();
		
		messages.add("Dungeon: " + ChatColor.YELLOW + dungeon.getName());
		messages.add("Creator: " + ChatColor.YELLOW + dungeon.getCreator());
		messages.add("Difficulty: " + ChatColor.YELLOW + dungeon.getDifficulty());
		messages.add("Fastest time: " + ChatColor.YELLOW + plugin.getFormatter().format(dungeon.getRecord().getCompletionTime()));
		messages.add("Times completed: " + ChatColor.YELLOW + dungeon.getTimesCompleted());
		
		sendMessage(player, messages);
	}
	
	public void viewInfo(Player player){
		ArrayList<String> messages = new ArrayList<String>();
		
		messages.add("DungeonTracker " + ChatColor.YELLOW + "v 1.0");
		messages.add("Plugin made by " + ChatColor.YELLOW + "yourselvs");
		messages.add("Use " + ChatColor.YELLOW + "/dungeon help" + ChatColor.RESET + " to view commands.");
		
		sendMessage(player, messages);
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
	
	public void dungeonAlreadyExists(Player player) {
		sendMessage(player, "There already exists a dungeon with that name.");
	}
	
	private void sendPlayerLog(Player player, String message){
		plugin.getLogger().info("Player \"" + player.getName() + "\" " + message);
	}
	
	private void sendMessage(Player player, String message){
		player.sendMessage(prefix + " " + ChatColor.RESET + message + ChatColor.RESET);
	}
	
	private void sendMessage(Player player, ArrayList<String> messages){
		player.sendMessage(prefix + " " + ChatColor.RESET + messages.get(0) + ChatColor.RESET);
		messages.remove(0);
		for(String message : messages)
			sendMessage(player, message);
	}
	
	private void sendMessage(CommandSender player, String message){
		player.sendMessage(unformattedPrefix + " " + message);
	}
	
	private void sendServerMessage(String message){
		for(Player player : plugin.getServer().getOnlinePlayers())
			sendMessage(player, message);
	}
	
	public void sendClickMessage(Player player, String line, String hoverMessage, String command){
		InteractiveMessage message = new InteractiveMessage(new InteractiveMessageElement(new FormattedText(linkPrefix + " " + line), HoverEvent.SHOW_TEXT, new FormattedText(hoverMessage), ClickEvent.RUN_COMMAND, command));	
		message.sendTo(player);
	}
	
	public void sendClickMessage(Player player, String line, String command){
		InteractiveMessage message = new InteractiveMessage(new InteractiveMessageElement(new FormattedText(linkPrefix + " " + line), HoverEvent.NONE, new FormattedText(""), ClickEvent.RUN_COMMAND, command));	
		message.sendTo(player);
	}
	
	public void sendSuggestMessage(Player player, String line, String hoverMessage, String command){
		InteractiveMessage message = new InteractiveMessage(new InteractiveMessageElement(new FormattedText(linkPrefix + " " + line), HoverEvent.SHOW_TEXT, new FormattedText(hoverMessage), ClickEvent.SUGGEST_COMMAND, command));	
		message.sendTo(player);
	}

	public void sendSuggestMessage(Player player, String line, String command){
		InteractiveMessage message = new InteractiveMessage(new InteractiveMessageElement(new FormattedText(linkPrefix + " " + line), HoverEvent.NONE, new FormattedText(""), ClickEvent.SUGGEST_COMMAND, command));
		message.sendTo(player);
	}
}
