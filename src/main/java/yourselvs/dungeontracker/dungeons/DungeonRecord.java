package yourselvs.dungeontracker.dungeons;

import java.util.Date;

import org.bukkit.entity.Player;

public class DungeonRecord {
	public Player player;
	public String dungeon;
	public Date startTime;
	public Date finishTime;
	
	public DungeonRecord(String dungeon, Player player, Date startTime){
		this.player = player;
		this.dungeon = dungeon;
		this.startTime = startTime;
		this.finishTime = null;
	}
	
	public DungeonRecord(String dungeon, Player player, Date startTime, Date finishTime){
		this.player = player;
		this.dungeon = dungeon;
		this.startTime = startTime;
		this.finishTime = finishTime;
	}
}
