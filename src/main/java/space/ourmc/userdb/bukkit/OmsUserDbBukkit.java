package space.ourmc.userdb.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import space.ourmc.userdb.bukkit.command.OmsUserDbCommand;
import space.ourmc.userdb.bukkit.listener.OmsUserDbListener;

public final class OmsUserDbBukkit extends JavaPlugin  {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("userdb").setExecutor(new OmsUserDbCommand(this));
        getServer().getPluginManager().registerEvents(new OmsUserDbListener(this), this);
        if (getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            ReportUrlAlerter.register(this);
        }
    }

}
