package me.sieg.locksrp.events;

import me.sieg.locksrp.utils.NameSpacedKeys;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
        Entity entity = event.getRightClicked();
        if(item != null && item.hasItemMeta() && item.getType() != Material.AIR){
            if(NameSpacedKeys.isKey(item.getItemMeta()) || NameSpacedKeys.isUniversalKey(item.getItemMeta()) ){
                if(entity.getType() != null && !entity.getType().equals(EntityType.ITEM_FRAME)){
                    event.setCancelled(true);
                }
            }
        }
    }
}
