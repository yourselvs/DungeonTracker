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
		plugin.getLogger().info("Player \"" + player.getName() + "\" tried to use command \"" + command + "\" while in dungeon \"" + dungeon + "\" and was stopped.");
	}
	
	public void dungeonResume(Player player, DungeonRecord record){
		String time = plugin.subtractTime(record.startTime, new Date());
		sendMessage(player,  "Resuming dungeon \"" + ChatColor.YELLOW + record.dungeon + ChatColor.RESET + "\" with a time of " + ChatColor.YELLOW + time + ChatColor.RESET + "." );
		plugin.getLogger().info("Player \"" + player.getName() + "\" rejoined and resumed dungeon \"" + record.dungeon + "\" with a time of " + time + ".");
	}
	
	public void dungeonQuit(Player player, DungeonRecord record){
		String time = plugin.subtractTime(record.startTime, new Date());
		plugin.getLogger().info("Player \"" + player.getName() + "\" quit the server and left dungeon \"" + record.dungeon + "\" with a time of " + time + ".");
	}
	
	public void dungeonStart(Player player, DungeonRecord record){
		String time = plugin.getFormatter().format(record.startTime);
		sendMessage(player, "Starting dungeon \"" + ChatColor.YELLOW + record.dungeon + ChatColor.RESET + "\" at time " + ChatColor.YELLOW + time + ChatColor.RESET + ".");
		plugin.getLogger().info("Player \"" + player.getName() + "\" started the dungeon \"" + record.dungeon + "\" at the time " + time + ".");
	}
	
	public void dungeonFinish(Player player, DungeonRecord record){
		String time = plugin.subtractTime(record.startTime, record.finishTime);
		sendMessage(player, "Dungeon \"" + ChatColor.YELLOW + record.dungeon + ChatColor.RESET + "\" finished with a time of " + ChatColor.YELLOW + time + ChatColor.RESET + ".");
		plugin.getLogger().info("Player \"" + player.getName() + "\" finished the dungeon \"" + record.dungeon + "\" with a time of " + time + ".");
	}
	
	public void dungeonBeatPR(Player player, DungeonRecord oldPR, DungeonRecord newPR){
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
	
	public void dungeonBeatWR(Player player, DungeonRecord oldPR, DungeonRecord newPR){
		String oldTime = plugin.subtractTime(oldPR.startTime, oldPR.finishTime);
		String newTime = plugin.subtractTime(newPR.startTime, newPR.finishTime);
		
		Date oldDate = null;
		Date newDate = null;
		try {
			oldDate = plugin.getFormatter().parse(oldTime);
			newDate = plugin.getFormatter().parse(newTime);
		} catch (ParseException e) {e.printStackTrace();}
		
		String difference = plugin.subtractTime(newDate, oldDate);
		
		sendServer(ChatColor.DARK_PURPLE + "NEW WORLD RECORD");
		sendServer(ChatColor.YELLOW + player.getName() + " beat the world record time in \"" + ChatColor.YELLOW + newPR.dungeon + ChatColor.RESET + " with a time of " + ChatColor.YELLOW + newTime + ChatColor.RESET + ", beating the world record by " + ChatColor.YELLOW + difference + ChatColor.RESET + ".");
	}
	
	private void sendMessage(Player player, String message){
		player.sendMessage(prefix + " " + ChatColor.RESET + message);
	}
	
	private void sendServer(String message){
		for(Player player : plugin.getServer().getOnlinePlayers())
			sendMessage(player, message);
	}	
}
