package me.sieg.locksrp.events;

import me.sieg.locksrp.utils.NameSpacedKeys;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractEntity implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(item != null && item.hasItemMeta() && item.getType() != Material.AIR && NameSpacedKeys.isKey(item.getItemMeta())){
            event.setCancelled(true);
        }else if(item != null && item.hasItemMeta() && item.getType() != Material.AIR && NameSpacedKeys.isUniversalKey(item.getItemMeta())){
            event.setCancelled(true);
        }
    }
}
