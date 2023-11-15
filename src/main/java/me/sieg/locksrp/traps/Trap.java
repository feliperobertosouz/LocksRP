package me.sieg.locksrp.traps;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public interface Trap {
    void install(PlayerInteractEvent event, Player player, Location loc, ItemStack trapItem);

    void smithingTableHandler(Player player, ItemStack item);

    void activate(Player player, Location loc);

    void lastActivate(Player player, Location loc);


}
