package yourselvs.events;

import java.time.format.DateTimeFormatter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerStartDungeonEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private DateTimeFormatter time;
	private String dungeon;
	private boolean cancelled;

	public PlayerStartDungeonEvent(Player player, DateTimeFormatter time, String dungeon) {
		this.player = player;
		this.time = time;
		this.dungeon = dungeon;		
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

	public String getDungeon() {
		return dungeon;
	}

	public void setDungeon(String dungeon) {
		this.dungeon = dungeon;
	}

	public DateTimeFormatter getTime() {
		return time;
	}

	public void setTime(DateTimeFormatter time) {
		this.time = time;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCanceled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
