package yourselvs.dungeontracker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.RED + ChatColor.BOLD + "DGN" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET;
	private String linkPrefix = ChatColor.AQUA + "[" + ChatColor.RED + ChatColor.BOLD + "DGN" + ChatColor.RESET + ChatColor.AQUA + "]" + ChatColor.RESET;
	private String unformattedPrefix = "[DGN]";
	private DungeonTracker plugin;
	
	public Messenger(DungeonTracker plugin){
		this.plugin = plugin;
	}
	public void commandNotAllowed(String dungeon, String command, Player player){
		sendMessage(player, "You are not allowed to use the command " + ChatColor.YELLOW + command + ChatColor.RESET + " because you are in dungeon " + ChatColor.YELLOW + dungeon + ChatColor.RESET + "." );
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
		sendMessage(player,  "Resuming dungeon " + ChatColor.YELLOW + record.getDungeon() + ChatColor.RESET + " with a time of " + ChatColor.YELLOW + time + ChatColor.RESET + "." );
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
		String time = plugin.getLongFormatter().format(record.getStartTime());
		sendMessage(player, "Starting dungeon " + ChatColor.YELLOW + record.getDungeon() + ChatColor.RESET + " at time " + ChatColor.YELLOW + time + ChatColor.RESET + ".");
		sendPlayerLog(player, "started the dungeon \"" + record.getDungeon() + "\" at the time " + time + ".");
	}
	
	public void finishDungeon(Player player, DungeonRecord record){
		String time = plugin.getShortFormatter().format(record.getCompletionTime());
		sendMessage(player, "Dungeon " + ChatColor.YELLOW + record.getDungeon() + ChatColor.RESET + " finished with a time of " + ChatColor.YELLOW + time + ChatColor.RESET + ".");
		sendPlayerLog(player, "finished the dungeon \"" + record.getDungeon() + "\" with a time of " + time + ".");
	}
	
	public void beatDungeonPR(Player player, DungeonRecord oldPR){
		String time = plugin.subtractTime(oldPR.getStartTime(), oldPR.getFinishTime());
		
		Date date = null;
		try {
			date = plugin.getLongFormatter().parse(time);
		} catch (ParseException e) {e.printStackTrace();}
		
		plugin.getShortFormatter().format(date);
		
		sendMessage(player, "You set a new best time!");
	}
	
	public void beatDungeonWR(Player player, DungeonRecord wr){
		Date date = new Date(wr.getFinishTime().getTime() - wr.getStartTime().getTime());
		plugin.getShortFormatter().format(date);
		String time = plugin.getShortFormatter().format(date);
		
		sendServerMessage(ChatColor.DARK_PURPLE + "NEW WORLD RECORD");
		sendServerMessage(ChatColor.YELLOW + player.getName() + ChatColor.RESET + " set the world record time in " + ChatColor.YELLOW + wr.getDungeon() + ChatColor.RESET + " with a time of " + ChatColor.YELLOW + time + ".");
	}
	
	public void viewLeaderboard(Player player, String dungeon, int page) {
		List<DungeonRecord> records = plugin.getMongo().getRecords(dungeon);
		records = plugin.sortRecordsCompletion(records);
		int pageSize = plugin.getPageSize();
		int maxPage = (records.size() / pageSize) + 1;
		
		if(!records.isEmpty()){
			if(page > 0 && page <= maxPage){
				sendMessage(player, ChatColor.AQUA + dungeon + ChatColor.RESET + " records. Page " + ChatColor.YELLOW + page + ChatColor.RESET + " of " + ChatColor.YELLOW + maxPage);
				for(int i = (page - 1) * pageSize; i < ((page - 1) * pageSize) + 6; i++){
					if(i < records.size()){
						DungeonRecord record = records.get(i);
						sendMessage(player, "" + ChatColor.YELLOW + ChatColor.ITALIC + record.getPlayer() + ChatColor.RESET + " | " + plugin.subtractTime(record.getStartTime(), record.getFinishTime()));
					}
				}
				if(page > 1)
					sendClickMessage(player, ChatColor.YELLOW + "Previous page", "Click to view previous page", "/sh leaders " + dungeon + (page - 1));
				if(page < maxPage)
					sendClickMessage(player, ChatColor.YELLOW + "Next page", "Click to view next page", "/sh leaders " + dungeon +  (page + 1));
			}
		}
		else
			sendMessage(player, "Nobody has completed " + ChatColor.YELLOW + dungeon + ChatColor.RESET + " yet.");
			
	}
	
	public void viewDungeonHistory(Player player, String dungeon, int page){
		List<DungeonRecord> records = plugin.getMongo().getRecords(dungeon);
		records = plugin.sortRecordsStart(records);
		int pageSize = plugin.getPageSize();
		int maxPage = (records.size() / pageSize) + 1;
		
		if(!records.isEmpty()){
			if(page > 0 && page <= maxPage){
				sendMessage(player, ChatColor.AQUA + dungeon + ChatColor.RESET + " records. Page " + ChatColor.YELLOW + page + ChatColor.RESET + " of " + ChatColor.YELLOW + maxPage);
				for(int i = (page - 1) * pageSize; i < ((page - 1) * pageSize) + 6; i++){
					if(i < records.size()){
						DungeonRecord record = records.get(i);
						sendMessage(player, "" + ChatColor.YELLOW + ChatColor.ITALIC + record.getPlayer() + ChatColor.RESET + " | " + plugin.subtractTime(record.getStartTime(), record.getFinishTime()));
					}
				}
				if(page > 1)
					sendClickMessage(player, ChatColor.YELLOW + "Previous page", "Click to view previous page", "/dungeon leaders " + dungeon + (page - 1));
				if(page < maxPage)
					sendClickMessage(player, ChatColor.YELLOW + "Next page", "Click to view next page", "/dungeon leaders " + dungeon +  (page + 1));
			}
		}
		else
			sendMessage(player, "Nobody has completed " + ChatColor.YELLOW + dungeon + ChatColor.RESET + " yet.");
	}
	
	@SuppressWarnings("deprecation")
	public void viewPlayerHistory(Player player, String playerName, String dungeon, int page){
		Player searchPlayer = plugin.getServer().getPlayer(playerName);
		List<DungeonRecord> records = plugin.getMongo().getRecords(dungeon, searchPlayer);
		records = plugin.sortRecordsStart(records);
		int pageSize = plugin.getPageSize();
		int maxPage = (records.size() / pageSize) + 1;
		
		if(!records.isEmpty()){
			if(page > 0 && page <= maxPage){
				sendMessage(player, ChatColor.AQUA + dungeon + ChatColor.RESET + " records (" + ChatColor.AQUA + playerName + "). Page " + ChatColor.YELLOW + page + ChatColor.RESET + " of " + ChatColor.YELLOW + maxPage);
				for(int i = (page - 1) * pageSize; i < ((page - 1) * pageSize) + 6; i++){
					if(i < records.size()){
						DungeonRecord record = records.get(i);
						sendMessage(player, "" + ChatColor.YELLOW + ChatColor.ITALIC + record.getPlayer() + ChatColor.RESET + " | " + plugin.subtractTime(record.getStartTime(), record.getFinishTime()));
					}
				}
				if(page > 1)
					sendClickMessage(player, ChatColor.YELLOW + "Previous page", "Click to view previous page", "/dungeon leaders " + dungeon + playerName + (page - 1));
				if(page < maxPage)
					sendClickMessage(player, ChatColor.YELLOW + "Next page", "Click to view next page", "/dungeon leaders " + dungeon + playerName + (page + 1));
			}
		}
		else
			sendMessage(player, "Nobody has completed " + ChatColor.YELLOW + dungeon + ChatColor.RESET + " yet.");
	}
	
	public void viewRecord(Player player, String dungeon){
		DungeonRecord record = plugin.getMongo().getFastestTime(dungeon);
		
		if(record != null)
			sendMessage(player, "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "WR" + ChatColor.RESET + " | " + 
					ChatColor.YELLOW + ChatColor.ITALIC + record.getPlayer() + ChatColor.RESET + " | " + 
					plugin.getShortFormatter().format(record.getCompletionTime()));
		else
			sendMessage(player, ChatColor.YELLOW + player.getName() + ChatColor.RESET + " has not completed " + ChatColor.YELLOW + dungeon + ChatColor.RESET + " yet.");
	}
	
	public void viewRecord(CommandSender sender, String dungeon){
		DungeonRecord record = plugin.getMongo().getFastestTime(dungeon);
		
		if(record != null)
			sendMessage(sender, "WR | " + record.getPlayer() + " | " + plugin.getShortFormatter().format(record.getCompletionTime()));
		else
			sendMessage(sender, "Nobody has completed " + ChatColor.YELLOW + dungeon + ChatColor.RESET + " yet.");
	}
	
	public void viewPlayerRecord(Player player, String dungeon, String playerName){
		DungeonRecord record = plugin.getMongo().getFastestTime(dungeon, playerName);
		
		if(record != null)
			sendMessage(player, "" + ChatColor.YELLOW + ChatColor.ITALIC + record.getPlayer() + ChatColor.RESET + " | " + 
					plugin.getShortFormatter().format(record.getCompletionTime()));
		else
			sendMessage(player, "" + ChatColor.YELLOW + playerName + ChatColor.RESET + " has not completed " + ChatColor.YELLOW + dungeon + ChatColor.RESET + " yet.");
	}
	
	public void viewPlayerRecord(CommandSender sender, String dungeon, String playerName){
		DungeonRecord record = plugin.getMongo().getFastestTime(dungeon, playerName);
		
		if(record != null)
			sendMessage(sender, record.getPlayer() + " | " + plugin.getShortFormatter().format(record.getCompletionTime()));
		else
			sendMessage(sender, "\"" + playerName + "\" has not completed \"" + dungeon + "\" yet.");
	}
	
	public void confirmCreateDungeon(String dungeon, Player player, String difficulty) {
		sendMessage(player, "Dungeon successfully created | Name " + ChatColor.YELLOW + dungeon + ChatColor.RESET + 
				" | Difficulty " + ChatColor.YELLOW + difficulty);
	}
	
	public void confirmDeleteDungeon(Player player, String dungeon, long longNum) {
		sendMessage(player, "Dungeon successfully deleted: " + ChatColor.YELLOW + dungeon + ChatColor.RESET + " (" + longNum + ")");
	}
	
	public void confirmUpdateParam(Player player, String dungeon, String param, boolean value) {
		sendMessage(player, "Parameter updated | Dungeon " + ChatColor.YELLOW + dungeon + ChatColor.RESET +
				" | Parameter " + ChatColor.YELLOW + param + ChatColor.RESET +
				" | Value " + ChatColor.YELLOW + value + ChatColor.RESET);
	}
	
	public void errorUpdateParam(Player player, String dungeon, String param, boolean value) {
		sendMessage(player, "Parameter not updated | Dungeon " + ChatColor.YELLOW + dungeon + ChatColor.RESET +
				" | Parameter " + ChatColor.YELLOW + param + ChatColor.RESET +
				" | Value " + ChatColor.YELLOW + value + ChatColor.RESET);
	}
	
	public void confirmUpdateCommand(Player player, String dungeon, String command, boolean value) {
		sendMessage(player, "Parameter updated | Dungeon " + ChatColor.YELLOW + dungeon + ChatColor.RESET + 
				" | Command " + ChatColor.YELLOW + command + ChatColor.RESET + 
				" | Value " + ChatColor.YELLOW + value + ChatColor.RESET);
	}
	
	public void errorUpdateCommand(Player player, String dungeon, String command, boolean value) {
		sendMessage(player, "Parameter not updated | Dungeon " + ChatColor.YELLOW + dungeon + ChatColor.RESET + 
				" | Command " + ChatColor.YELLOW + command + ChatColor.RESET + 
				" | Value " + ChatColor.YELLOW + value + ChatColor.RESET);
	}
	
	public void viewCommand(Player player, String dungeon, String command){
		boolean value = plugin.getMongo().getCommandValue(dungeon, command);
		String allowed = value ? "Yes" : "No";
		sendMessage(player, "Dungeon " + ChatColor.YELLOW + dungeon + ChatColor.RESET + 
				" | Command " + ChatColor.YELLOW + command + ChatColor.RESET + 
				" | Allowed " + ChatColor.YELLOW + allowed + ChatColor.RESET); 
	}
	
	public void viewParam(Player player, String dungeon, String param){
		boolean value = plugin.getMongo().getParamValue(dungeon, param);
		sendMessage(player, "Dungeon " + ChatColor.YELLOW + dungeon + ChatColor.RESET + 
				" | Parameter " + ChatColor.YELLOW + param + ChatColor.RESET + 
				" | Value " + ChatColor.YELLOW + value + ChatColor.RESET);
	}
	
	public void viewDungeon(Player player, String dungeonName){
		Dungeon dungeon = plugin.getMongo().getDungeon(dungeonName);
		ArrayList<String> messages = new ArrayList<String>();
		
		String difficulty = dungeon.getDifficulty();
		if(difficulty.equalsIgnoreCase("easy"))
			difficulty = ChatColor.GREEN + difficulty;
		else if(difficulty.equalsIgnoreCase("medium"))
			difficulty = ChatColor.YELLOW + difficulty;
		else if(difficulty.equalsIgnoreCase("hard"))
			difficulty = ChatColor.RED + difficulty;
		else if(difficulty.equalsIgnoreCase("insane"))
			difficulty = ChatColor.DARK_RED + difficulty;
		
		messages.add("Dungeon: " + ChatColor.YELLOW + dungeon.getName());
		messages.add("Creator: " + ChatColor.YELLOW + dungeon.getCreator());
		messages.add("Difficulty: " + difficulty);
		if(dungeon.getRecord() == null)
			messages.add("Has not been completed");
		else
			messages.add("Fastest time: " + ChatColor.YELLOW + plugin.getShortFormatter().format(dungeon.getRecord().getCompletionTime()));
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
	
	public void listDungeons(Player player, int page){
		List<Dungeon> dungeons = plugin.getMongo().getDungeons();		
		if(dungeons.size() > 0){
	
			int pageSize = plugin.getPageSize();
			int maxPage = (dungeons.size() / pageSize) + 1;
			
			if(page > 0 && page <= maxPage){
				sendMessage(player, "Dungeons. Page " + ChatColor.YELLOW + page + ChatColor.RESET + " of " + ChatColor.YELLOW + maxPage);
				for(int i = (page - 1) * pageSize; i < ((page - 1) * pageSize) + 6; i++){
					if(i < dungeons.size()){
						Dungeon dungeon = dungeons.get(i);
						String difficulty = dungeon.getDifficulty();
						if(difficulty.equalsIgnoreCase("easy"))
							difficulty = ChatColor.GREEN + difficulty;
						else if(difficulty.equalsIgnoreCase("medium"))
							difficulty = ChatColor.YELLOW + difficulty;
						else if(difficulty.equalsIgnoreCase("hard"))
							difficulty = ChatColor.RED + difficulty;
						else if(difficulty.equalsIgnoreCase("insane"))
							difficulty = ChatColor.DARK_RED + difficulty;
						
						sendClickMessage(player, dungeon.getName() + " | " + difficulty, "Click to view dungeon info", "/dungeon view " + dungeon.getName());
					}
				}
				if(page > 1)
					sendClickMessage(player, ChatColor.YELLOW + "Previous page", "Click to view previous page", "/dungeon list " + (page - 1));
				if(page < maxPage)
					sendClickMessage(player, ChatColor.YELLOW + "Next page", "Click to view next page", "/dungeon list " + (page + 1));
			}
		}
		else
			sendMessage(player, "There are no dungeons available.");
	}
	
	public void viewHelp(Player player){
		List<String> messages = new ArrayList<String>();
		Map<String, String> cmds = new HashMap<String, String>();
		if(player.hasPermission("dungeon.join")){
			cmds.put("join [dungeon]", "Join a dungeon");
			cmds.put("leave", "Leave a dungeon");
		}
		if(player.hasPermission("dungeon.forcejoin")){
			cmds.put("forcejoin [dungeon] [player]", "Force a player to join a dungeon");
			cmds.put("forceleave [player]", "Force a player to leave a dungeon");
		}
		if(player.hasPermission("dungeon.list"))
			cmds.put("list", "Lists the dungeons available");
		/*if(player.hasPermission("dungeon.leaders"))
			cmds.put("leaders [dungeon] (page)", "View the fastest times of a dungeon");
		if(player.hasPermission("dungeon.history"))
			cmds.put("history [dungeon] (player/page) (page)", "View the history of a dungeon or specific player");
		if(player.hasPermission("dungeon.record"))
			cmds.put("record [dungeon] (player)", "View the record of a dungeon or specific player");*/
		if(player.hasPermission("dungeon.create")){
			cmds.put("create [dungeon] [difficulty] [creator]", "Creates a dungeon at the spot where you stand");
			cmds.put("delete [dungeon]", "Deletes all records of a dungeon");
		}
		/*if(player.hasPermission("dungeon.param"))
			cmds.put("param [dungeon] [parameter] (boolean)", "Views the value of a parameter or sets the value of a parameter");
		if(player.hasPermission("dungeon.command"))
			cmds.put("command [dungeon] [command] (boolean)", "Views the value of a command or sets the value of a command");*/
		
		for(String key : cmds.keySet())
			messages.add(ChatColor.YELLOW + "/dungeon " + key + ChatColor.RESET + " | " + cmds.get(key));
		
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
	
	public void mustIncludeDungeonName(Player player){
		sendMessage(player, "You must include a dungeon name.");
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
	
	public void newCommandRegistered(Player player){
		sendMessage(player, "Registered a new command.");
	}
	
	public void updateFailed(Player player){
		sendMessage(player, "Update failed.");
	}
	
	public void updateSuccesful(Player player){
		sendMessage(player, "Update successful.");
	}
	
	private void sendPlayerLog(Player player, String message){
		plugin.getLogger().info("Player \"" + player.getName() + "\" " + message);
	}
	
	private void sendMessage(Player player, String message){
		player.sendMessage(prefix + " " + ChatColor.RESET + message + ChatColor.RESET);
	}
	
	private void sendBlankMessage(Player player, String message){
		player.sendMessage(message + ChatColor.RESET);
	}
	
	private void sendMessage(Player player, List<String> messages){
		player.sendMessage(prefix + " - - - - - - - ");
		
		for(String message : messages)
			sendBlankMessage(player, message);
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
