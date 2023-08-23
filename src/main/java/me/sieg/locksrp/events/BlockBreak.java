package me.sieg.locksrp.events;

import me.sieg.locksrp.utils.InventoryChecker;
import me.sieg.locksrp.utils.ItemManager;
import me.sieg.locksrp.utils.NameSpacedKeys;
import me.sieg.locksrp.utils.SaveDoor;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BlockBreak implements Listener {

    public BlockBreak() {
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        // check if the clicked block is a door
        if (SaveDoor.isDoor(block)) {
            SaveDoor saveDoor = new SaveDoor();
            Location loc = event.getBlock().getLocation();
            //check if the door is registered
            if (saveDoor.isLocationRegistered(loc)) {
                //Checa se a porta esta trancada e o player não tem permissão
                //Check if the door is locked or the player has permission
                if (saveDoor.isDoorLocked(loc) && !player.hasPermission("locksrp.admin")) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "A porta esta trancada, destranque antes de tirar");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                } else if (!saveDoor.isDoorLocked(loc) || player.hasPermission("locksrp.admin")) {
                    if (block instanceof Door) {
                        Door doorData = (Door) block.getBlockData();
                        // Verifica se é a parte de cima da porta
                        //check if clicked block is the top of door
                        if (doorData.getHalf() == Door.Half.TOP) {
                            //get bottom door
                            loc = SaveDoor.getBlockBelow(block.getLocation());

                        }
                        // Remove the locatio of doors.yml

                    }
                    ItemManager itemManager = new ItemManager();
                    String keyCode = saveDoor.getLockCode(loc);
                    Integer level = saveDoor.getLockLevel(loc);
                    ItemStack itemDrop = itemManager.generateLock(level, keyCode);
                    saveDoor.dropItemOnGround(loc, itemDrop);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                    saveDoor.removeLocationFromFile(loc);
                }
            }

        } else if (block != null && block.getType() == Material.CHEST
                || block.getType() == Material.BARREL) {
            Inventory chestInventory = player.getInventory();
            if (block.getType() == Material.CHEST) {
                Chest chest = (Chest) block.getState();
                chestInventory = chest.getInventory();
            } else if (block.getType() == Material.BARREL) {
                Barrel barrel = (Barrel) block.getState();
                chestInventory = barrel.getInventory();
            }


            if (!InventoryChecker.hasUniversalKey(player)) {
                ItemStack firstItem = chestInventory.getItem(0);

                if (firstItem != null) {
                    // Envia o item para o jogador
                    if (firstItem.hasItemMeta() && NameSpacedKeys.isLock(firstItem.getItemMeta())) {
                        if (NameSpacedKeys.hasKeyCode(firstItem.getItemMeta())) {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.DARK_RED + "Tire a tranca do bau para o quebrar primeiro");
                        }


                    }
                }
            }
        }
    }
}
