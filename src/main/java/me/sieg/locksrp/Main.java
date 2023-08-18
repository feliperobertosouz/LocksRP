package me.sieg.locksrp;

import me.sieg.locksrp.commands.LocksRPCommands;
import me.sieg.locksrp.commands.LocksRPTabCompleter;
import me.sieg.locksrp.events.BlockBreak;
import me.sieg.locksrp.events.InventoryClick;
import me.sieg.locksrp.events.LockPickMinigame;
import me.sieg.locksrp.events.PlayerInteract;
import me.sieg.locksrp.utils.ChestKeeper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {


    ChestKeeper chest = new ChestKeeper();

    private static Main plugin;

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable(){
        plugin = this;
       getServer().getPluginManager().registerEvents(new PlayerInteract(chest), this);
       getServer().getPluginManager().registerEvents(new InventoryClick(), this);
       getServer().getPluginManager().registerEvents(new LockPickMinigame(), this);
       getServer().getPluginManager().registerEvents(new BlockBreak(),this);
       getCommand("LocksRP").setExecutor(new LocksRPCommands());
       getCommand("LocksRP").setTabCompleter(new LocksRPTabCompleter());

        Logger logger = getLogger();
        logger.info("LOCSKRP INITIALIZED");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
