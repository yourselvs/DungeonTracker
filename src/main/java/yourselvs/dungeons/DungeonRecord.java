package yourselvs.dungeons;

import java.time.format.DateTimeFormatter;

import org.bukkit.entity.Player;

public class DungeonRecord {
	public Player player;
	public String dungeon;
	public DateTimeFormatter completionTime;
	public DateTimeFormatter finishTime;
	
	public DungeonRecord(String dungeon, Player player, DateTimeFormatter completionTime, DateTimeFormatter finishTime){
		this.player = player;
		this.dungeon = dungeon;
		this.completionTime = completionTime;
		this.finishTime = finishTime;
	}
}
