package me.sieg.locksrp;

import me.sieg.locksrp.commands.LocksRPCommands;
import me.sieg.locksrp.commands.LocksRPTabCompleter;
import me.sieg.locksrp.events.*;
import me.sieg.locksrp.utils.ChestKeeper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static boolean landsPluginPresent = false;
    ChestKeeper chest = new ChestKeeper();

    private static Main plugin;

    public static Main getPlugin() {
        return plugin;
    }

    public static boolean getLandsPluginPresent() { return landsPluginPresent; }
    @Override
    public void onEnable(){
        plugin = this;
       getServer().getPluginManager().registerEvents(new PlayerInteract(chest), this);
       getServer().getPluginManager().registerEvents(new InventoryClick(), this);
       getServer().getPluginManager().registerEvents(new LockPickMinigame(), this);
       getServer().getPluginManager().registerEvents(new BlockBreak(),this);
       getServer().getPluginManager().registerEvents(new PlayerInteractEntity(), this);
       getCommand("LocksRP").setExecutor(new LocksRPCommands());
       getCommand("LocksRP").setTabCompleter(new LocksRPTabCompleter());

        Logger logger = getLogger();
        if (isLandsPluginPresent()) {
            // O Lands está presente, você pode interagir com suas funcionalidades com segurança.
            getLogger().info("O plugin Lands foi detectado e está presente!");
        } else {
            getLogger().warning("O plugin Lands não foi encontrado. Algumas funcionalidades podem estar desativadas.");
        }
        logger.info("LOCSKRP INITIALIZED");
    }

    private boolean isLandsPluginPresent() {
        Plugin landsPlugin = getServer().getPluginManager().getPlugin("Lands");

        // Verifique se o plugin Lands está carregado e ativo
        if (landsPlugin != null && landsPlugin.isEnabled()) {
            landsPluginPresent = true;
        }

        return landsPluginPresent;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
