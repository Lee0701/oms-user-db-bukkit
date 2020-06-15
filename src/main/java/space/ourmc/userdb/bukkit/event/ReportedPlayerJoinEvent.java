package space.ourmc.userdb.bukkit.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ReportedPlayerJoinEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private final int reportCount;

    public ReportedPlayerJoinEvent(Player playerJoined, int reportCount) {
        super(playerJoined);
        this.reportCount = reportCount;
    }

    public int getReportCount() {
        return reportCount;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
