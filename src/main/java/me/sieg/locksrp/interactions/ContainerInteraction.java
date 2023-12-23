package me.sieg.locksrp.interactions;

import me.sieg.locksrp.events.LockPickMinigame;
import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.item.LockFactory;
import me.sieg.locksrp.utils.*;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ContainerInteraction {

    private ChestKeeper chestKeeper;
    private SaveDoor saveDoor;
    private MessageSender messageSender;

    public ContainerInteraction(ChestKeeper chestKeeper) {
        this.chestKeeper = chestKeeper;
        this.messageSender = new MessageSender();
    }

    public void handleContainerInteraction(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Inventory chestInventory = player.getInventory();
        Block clickedBlock = event.getClickedBlock();
        Inventory containerInventory = saveDoor.getInventoryFromClickedBlock(clickedBlock);
        if(containerInventory == null){
            return;
        }

        if (!InventoryChecker.hasUniversalKey(player)) {
            ItemStack firstItem = containerInventory.getItem(0);


            if (firstItem != null) {
                if (ItemManager.isLock(firstItem.getItemMeta())) {
                    if (ItemManager.hasKeyCode(firstItem.getItemMeta())) {
                        Integer level = LockFactory.getLockLevel(firstItem.getItemMeta());
                        String lockCode = ItemManager.getKeyCode(firstItem.getItemMeta());
                        if (InventoryChecker.hasCorrectKey(player, lockCode) || InventoryChecker.hasCorrectCodeInKeyChain(player, lockCode)) {
                            messageSender.sendPlayerMessage(player, "&8 Você abre o baú");
                        } else {
                            event.setCancelled(true);
                            if (InventoryChecker.hasLockPick(player)) {
                                chestKeeper.setLastClickedChest(player.getUniqueId(), clickedBlock);
                                LockPickMinigame minigame = new LockPickMinigame(chestKeeper);
                                if (level != 6) {
                                    minigame.openCustomGUI(player, level);
                                } else {
                                    messageSender.sendPlayerMessage(player, "&5&l A Tranca parece muito poderosa para se fazer isso", Sound.ENTITY_VILLAGER_NO, 1.0f, 2.0f);
                                }
                            } else {
                                messageSender.sendPlayerMessage(player, "&4 O baú esta trancado, você precisa de uma chave para isso");
                            }
                        }
                    }
                }

            }
        }
    }


}
