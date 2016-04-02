package yourselvs;

import org.bukkit.entity.Player;

import yourselvs.commands.DungeonCommand;
import yourselvs.events.PlayerFinishDungeonEvent;
import yourselvs.events.PlayerStartDungeonEvent;

public class PlayerManager {
	private DungeonTracker plugin;
	
	public PlayerManager(DungeonTracker plugin){
		this.plugin = plugin;
	}
	
	public void joinDungeon(PlayerStartDungeonEvent event){
		// TODO Process having a player join a dungeon
	}
	
	public void leaveDungeon(Player player){
		// TODO Process having a player leave a dungeon
	}
	
	public void finishDungeon(PlayerFinishDungeonEvent event){
		// TODO Process having a player finish a dungeon
	}

	public void viewLeaderboard(DungeonCommand command, int page) {
		// TODO Auto-generated method stub
		
	}
}
