package yourselvs.dungeontracker.dungeons;

import java.util.Date;

import org.bukkit.entity.Player;

import yourselvs.dungeontracker.DungeonTracker;

public class DungeonRecord {
	private Player player;
	private String dungeon;
	private Date startTime;
	private Date finishTime;
	private Date completionTime;
	
	public DungeonRecord(String dungeon, Player player, Date startTime){
		this.player = player;
		this.dungeon = dungeon;
		this.startTime = startTime;
		this.finishTime = null;
	}
	
	public DungeonRecord(DungeonTracker plugin, String dungeon, Player player, Date startTime, Date finishTime) throws Exception{
		this.player = player;
		this.dungeon = dungeon;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.completionTime = plugin.getFormatter().parse(plugin.subtractTime(finishTime, startTime));
	}
	
	public Player getPlayer() {return player;}
	public String getDungeon() {return dungeon;}
	public Date getStartTime() {return startTime;}
	public Date getFinishTime() {return finishTime;}
	public Date getCompletionTime() {return completionTime;}
}
