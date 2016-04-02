package yourselvs;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BukkitHandler {
	private DungeonTracker plugin;
	
	public BukkitHandler(DungeonTracker plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	public Location getPlayerLocation(String name){
		Player player = plugin.getServer().getPlayer(name);
		return player.getLocation();
	}
	
	public Player playerExistsOnline(String name){
		Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();
		for(Player player : onlinePlayers){
			if(player.getName().equalsIgnoreCase(name))
				return player;
		}
		return null;
	}
	
	public boolean playerExistsOffline(String name){
		OfflinePlayer[] offlinePlayers = plugin.getServer().getOfflinePlayers();
		for(OfflinePlayer player : offlinePlayers){
			if(player.getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	
	public void setPlayerLocation(Player player, String worldName, int x, int y, int z){
		World world = plugin.getServer().getWorld(worldName);
		Location location = new Location(world, x, y, z);
		player.teleport(location);
	}
}
