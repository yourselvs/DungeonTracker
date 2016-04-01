package yourselvs;

import java.text.ParseException;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import yourselvs.dungeons.DungeonRecord;
import yourselvs.dungeons.DungeonTracker;

public class Messenger {
	private String prefix = "[" + ChatColor.RED + ChatColor.BOLD + "DGN" + ChatColor.RESET + "]";
	private DungeonTracker plugin;
	
	public Messenger(DungeonTracker plugin){
		this.plugin = plugin;
	}
	
	public void commandNotAllowed(String dungeon, String command, Player player){
		sendMessage(player, "You are not allowed to use the command \"" + ChatColor.YELLOW + command + ChatColor.RESET + "\" because you are in dungeon \"" + ChatColor.YELLOW + dungeon + ChatColor.RESET + "\"." );
		sendPlayerLog(player, "tried to use command \"" + command + "\" while in dungeon \"" + dungeon + "\" and was stopped.");
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
	
	private void sendPlayerLog(Player player, String message){
		plugin.getLogger().info("Player \"" + player.getName() + "\" " + message);
	}
	
	private void sendMessage(Player player, String message){
		player.sendMessage(prefix + " " + ChatColor.RESET + message);
	}
	
	private void sendServerMessage(String message){
		for(Player player : plugin.getServer().getOnlinePlayers())
			sendMessage(player, message);
	}	
}
