package me.sieg.locksrp.events;
import me.sieg.locksrp.utils.*;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Bisected;
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

    MessageSender sender = new MessageSender();
    ItemManager itemManager = new ItemManager();
    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        SaveDoor saveDoor = new SaveDoor();
        if ( block != null && SaveDoor.isDoor(block)) {
            Location loc = event.getBlock().getLocation();
            Door doorDataDois = (Door) block.getBlockData();
            if(doorDataDois.getHalf() == Bisected.Half.TOP){
                loc = SaveDoor.getBlockBelow(block.getLocation());
            }
            if (saveDoor.isLocationRegistered(loc)) {
                //System.out.println("QUEBRADO PORTA");
                boolean  dorLocked = saveDoor.isDoorLocked(loc);
                if (dorLocked) {
                    //System.out.println("PORTA TRANCADA");
                    event.setCancelled(true);
                    sender.sendPlayerMessage(player, "&cA porta esta trancada, destranque antes de tirar", Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                } else {
                    //System.out.println("PORTA DESTRANCADA");
                    if(LandsChecker.PlayerCanBreakLand(player)) {
                        //System.out.println("PODE RETIRAR A TRANCA");

                        String keyCode = saveDoor.getLockCode(loc);
                        Integer level = saveDoor.getLockLevel(loc);
                        ItemStack itemDrop = itemManager.generateLock(level, keyCode);
                        saveDoor.dropItemOnGround(loc, itemDrop);
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                        sender.sendPlayerMessage(player, "Voce quebrou a porta que estava trancada", Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                        saveDoor.removeLocationFromFile(loc);
                    }else{
                        //System.out.println("NÃO PODE RETIRAR A TRANCA");
                        event.setCancelled(true);
                    }
                }
            }

        } else if (block.getType() == Material.CHEST  || block.getType() == Material.BARREL) {
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
                            sender.sendPlayerMessage(player, "&4Tire a tranca do bau para o quebrar primeiro", Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        }
                    }
                }
            }
        }
    }
}
