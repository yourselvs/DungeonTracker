package yourselvs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import yourselvs.dungeons.DungeonTracker;

public class Messenger {
	private String prefix = "[" + ChatColor.RED + ChatColor.BOLD + "DGN" + ChatColor.RESET + "]";
	private DungeonTracker plugin;
	
	public Messenger(DungeonTracker plugin) {
		this.plugin = plugin;
	}
	
	public void commandNotAllowed(String dungeon, String command, Player player) {
		sendMessage(player, "You are not allowed to use the command \"" + ChatColor.YELLOW + command + ChatColor.RESET + "\" because you are in dungeon \"" + ChatColor.YELLOW + dungeon + ChatColor.RESET + "\"." );
		plugin.getLogger().info("Player \"" + player.getName() + "\" tried to use command \"" + command + "\" while in dungeon \"" + dungeon + "\" and was stopped.");
	}
	
	private void sendMessage(Player player, String message){
		player.sendMessage(prefix + " " + ChatColor.RESET + message);
	}
}
