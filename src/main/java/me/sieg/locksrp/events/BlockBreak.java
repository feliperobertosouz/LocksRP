package me.sieg.locksrp.events;
import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.item.LockFactory;
import me.sieg.locksrp.traps.Trap;
import me.sieg.locksrp.traps.TrapType;
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
            if(event.getBlock().getBlockData() instanceof Door){
                Door doorDataDois = (Door) block.getBlockData();
                if(doorDataDois.getHalf() == Bisected.Half.TOP){
                    loc = SaveDoor.getBlockBelow(block.getLocation());
                }
            }
            if (saveDoor.isLocationRegistered(loc)) {
                boolean  dorLocked = saveDoor.isDoorLocked(loc);
                if (dorLocked) {
                    event.setCancelled(true);
                    sender.sendPlayerMessage(player, "&cA porta esta trancada, destranque antes de tentar tirar a tranca", Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                } else {
                    //System.out.println("PORTA DESTRANCADA");
                    if(LandsChecker.PlayerCanBreakLand(player)) {
                        //System.out.println("PODE RETIRAR A TRANCA");

                        String keyCode = saveDoor.getLockCode(loc);
                        Integer level = saveDoor.getLockLevel(loc);
                        ItemStack itemDrop = itemManager.generateLock(level, keyCode);
                        saveDoor.dropItemOnGround(loc, itemDrop);
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                        sender.sendPlayerMessage(player, "Voce quebrou a porta que estava com uma tranca", Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                        if(saveDoor.hasTrap(loc)){
                            String trapTypeString = saveDoor.getTrap(loc);
                            TrapType trapType = TrapType.valueOf(trapTypeString);
                            sender.sendPlayerMessage(player, "&4A porta tinha uma armadilha, ela também foi removida", Sound.BLOCK_DISPENSER_DISPENSE, 0.1f, 1.5f);
                            Trap trap = TrapType.getTrapByType(trapType);
                            trap.removeTrap(player, loc);
                        }
                        saveDoor.removeLocationFromFile(loc);
                    }else{
                        //System.out.println("NÃO PODE RETIRAR A TRANCA");
                        sender.sendPlayerMessage(player,"&cVocê não pode quebrar a porta com tranca, use um lockremover", Sound.ENTITY_VILLAGER_NO, 1.0f, 2.0f);
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
                    if (firstItem.hasItemMeta() && ItemManager.isLock(firstItem.getItemMeta())) {
                        if (ItemManager.hasKeyCode(firstItem.getItemMeta())) {
                            event.setCancelled(true);
                            sender.sendPlayerMessage(player, "&4Tire a tranca do bau para o quebrar primeiro", Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        }
                    }
                }
            }
        }
    }

}
