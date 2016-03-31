package yourselvs.events;

import java.time.format.DateTimeFormatter;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerFinishDungeonEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private String playerName;
	private DateTimeFormatter time;
	private boolean cancelled;

	public PlayerFinishDungeonEvent(String playerName, DateTimeFormatter time) {
		this.playerName = playerName;
		this.setTime(time);
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
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
