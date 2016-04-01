package yourselvs.events;

import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerFinishDungeonEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private Date time;
	private String dungeon;
	private boolean cancelled;

	public PlayerFinishDungeonEvent(Player player, Date time, String dungeon) {
		this.player = player;
		this.time = time;
		this.setDungeon(dungeon);
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getDungeon() {
		return dungeon;
	}

	public void setDungeon(String dungeon) {
		this.dungeon = dungeon;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCanceled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
