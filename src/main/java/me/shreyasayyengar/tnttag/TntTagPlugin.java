package me.shreyasayyengar.tnttag;

import me.shreyasayyengar.tnttag.commands.JoinCommand;
import me.shreyasayyengar.tnttag.commands.TntTagCommand;
import me.shreyasayyengar.tnttag.events.GameEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class TntTagPlugin extends JavaPlugin {

    public static Plugin getInstance() {
        return JavaPlugin.getPlugin(TntTagPlugin.class);
    }

    @Override
    public void onEnable() {
        this.getCommand("tnttag").setExecutor(new TntTagCommand());
        this.getCommand("join").setExecutor(new JoinCommand());
        registerEvents();
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new GameEvents(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
