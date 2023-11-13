package me.sieg.locksrp.traps;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public interface Trap {

    void activate(Player player, Location loc);

    void lastActivate(Player player, Location loc);
}
