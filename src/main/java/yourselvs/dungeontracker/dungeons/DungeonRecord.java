package yourselvs.dungeontracker.dungeons;

import java.util.Date;

import yourselvs.dungeontracker.DungeonTracker;

public class DungeonRecord {
	private String player;
	private String dungeon;
	private Date startTime;
	private Date finishTime;
	private Date completionTime;
	
	public DungeonRecord(String dungeon, String player, Date startTime){
		this.player = player;
		this.dungeon = dungeon;
		this.startTime = startTime;
		this.finishTime = null;
		this.completionTime = null;
	}
	
	public DungeonRecord(DungeonTracker plugin, String dungeon, String player, Date startTime, Date finishTime) throws Exception{
		this.player = player;
		this.dungeon = dungeon;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.completionTime = plugin.getLongFormatter().parse(plugin.subtractTime(finishTime, startTime));
	}
	
	public String getPlayer() {return player;}
	public String getDungeon() {return dungeon;}
	public Date getStartTime() {return startTime;}
	public Date getFinishTime() {return finishTime;}
	public Date getCompletionTime() {return completionTime;}
	
	public void setFinishTime(Date time) {this.finishTime = time;}
	public void setCompletionTime(Date time) {this.completionTime = time;}
}
