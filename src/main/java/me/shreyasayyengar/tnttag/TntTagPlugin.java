package me.shreyasayyengar.tnttag;

import me.shreyasayyengar.tnttag.commands.TntTagCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class TntTagPlugin extends JavaPlugin {

    public static Plugin getInstance() {
        return JavaPlugin.getPlugin(TntTagPlugin.class);
    }

    @Override
    public void onEnable() {
        this.getCommand("tnttag").setExecutor(new TntTagCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
