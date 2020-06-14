package space.ourmc.userdb.bukkit.event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import space.ourmc.userdb.api.OmsUserDb;
import space.ourmc.userdb.api.OmsUserDbException;

import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class OmsUserDbListener implements Listener {

    public final Plugin plugin;

    public OmsUserDbListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        List<Player> receivers = player.getServer().getOnlinePlayers().stream().filter(ServerOperator::isOp).collect(Collectors.toList());
        try {
            OmsUserDb.getReportCountOfUser(player.getUniqueId()).thenAccept(result -> plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (result.count == 0) {
                    receivers.forEach(receiver -> {
                        receiver.sendMessage(ChatColor.GREEN + "No reports found for " + ChatColor.YELLOW + player.getName());
                    });
                    return;
                }
                receivers.forEach(receiver -> {
                    receiver.sendMessage(result.count + " report(s) found for " + ChatColor.YELLOW + player.getName());
                    receiver.sendMessage("Use " + ChatColor.AQUA + "/userdb get " + player.getName() + " for more details");
                });
            })).exceptionally(throwable -> {
                if (throwable instanceof CompletionException && throwable.getCause() instanceof OmsUserDbException) {
                    OmsUserDbException e = (OmsUserDbException) throwable.getCause();
                    if (e.code == 404) {
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            receivers.forEach(receiver -> {
                                receiver.sendMessage(ChatColor.GREEN + "No reports found for " + ChatColor.YELLOW + player.getName());
                            });
                        });
                    }
                }
                return null;
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
