package yourselvs.dungeontracker.dungeons;

public class Dungeon {
	private String name;
	private String creator;
	private DungeonRecord record;
	private int timesCompleted;
	private String difficulty;
	
	public Dungeon(String name, String creator, DungeonRecord record, int timesCompleted, String difficulty){
		this.name = name;
		this.creator = creator;
		this.record = record;
		this.timesCompleted = timesCompleted;
		this.difficulty = difficulty;
	}
	
	public String getName() {return name;}
	public String getCreator() {return creator;}
	public DungeonRecord getRecord() {return record;}
	public int getTimesCompleted() {return timesCompleted;}
	public String getDifficulty() {return difficulty;}
}
