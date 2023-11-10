package me.sieg.locksrp.events;
import me.sieg.locksrp.interactions.ContainerInteraction;
import me.sieg.locksrp.interactions.DoorInteraction;
import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.item.KeyFactory;
import me.sieg.locksrp.item.LockFactory;
import me.sieg.locksrp.utils.*;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInteract implements Listener {

    private ChestKeeper chestlist;

    public PlayerInteract(ChestKeeper chestlist) {
        this.chestlist = chestlist;

    }

    ItemManager itemManager = new ItemManager();
    MessageSender messageSender = new MessageSender();


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Block clickedBlock = event.getClickedBlock();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock != null) {
            if (clickedBlock.getType() == Material.SMITHING_TABLE) {
                handleSmithingTableInteraction(event);
            } else if (SaveDoor.isValidContainer(clickedBlock)) {
                ContainerInteraction containerInteraction = new ContainerInteraction(chestlist);
                containerInteraction.handleContainerInteraction(event);
            } else if (SaveDoor.isDoor(clickedBlock)) {
                DoorInteraction doorInteraction = new DoorInteraction(chestlist);
                doorInteraction.handleDoorInteraction(event);
            } else if (clickedBlock != null && clickedBlock.getType() == Material.GRINDSTONE) {
                handleGrindStoneInteraction(event);
            }
        }

    }

    private void handleGrindStoneInteraction(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.hasItemMeta()) {
            if (ItemManager.isKey(item.getItemMeta())) {
                event.setCancelled(true);
                Integer amount = item.getAmount();
                ItemManager items = new ItemManager();
                int customModel = item.getItemMeta().getCustomModelData();
                player.getInventory().setItemInMainHand(items.getKeyItem(amount, customModel));
                messageSender.sendPlayerMessage(player, "&6Você limpa sua chave", Sound.BLOCK_GRINDSTONE_USE);
            }else if(ItemManager.isLock(item.getItemMeta())){
                event.setCancelled(true);
                Integer amount = item.getAmount();
                ItemManager items = new ItemManager();
                int customModel = item.getItemMeta().getCustomModelData();
                Integer level = Integer.valueOf(NameSpacedKeys.getNameSpacedKey(item.getItemMeta(), "level"));
                player.getInventory().setItemInMainHand(items.getLock(level, customModel, amount));
                messageSender.sendPlayerMessage(player, "&6Você limpa sua tranca", Sound.BLOCK_GRINDSTONE_USE);
            }
        }
    }

    private void handleSmithingTableInteraction(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().hasItemMeta()) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (ItemManager.isKey(item.getItemMeta()) && NameSpacedKeys.getNameSpacedKey(item.getItemMeta(), "keyCode") == null) {
                event.setCancelled(true);
                messageSender.sendPlayerMessage(player, "&6 Você forja uma nova chave", Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                String code = KeyCodeGenerator.generateUniqueCode();
                ItemMeta meta = item.getItemMeta();
                item = itemManager.generateKey(item, code);
                player.getInventory().setItemInMainHand(item);
            }
        }
    }

    private void oldhandleContainerInteraction(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Inventory chestInventory = player.getInventory();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock.getType() == Material.CHEST) {
            Chest chest = (Chest) clickedBlock.getState();
            chestInventory = chest.getInventory();
        } else if (clickedBlock.getType() == Material.BARREL) {
            Barrel barrel = (Barrel) clickedBlock.getState();
            chestInventory = barrel.getInventory();
        } else if (clickedBlock.getState() instanceof ShulkerBox) {
            ShulkerBox shulkerBox = (ShulkerBox) clickedBlock.getState();
            chestInventory = shulkerBox.getInventory();
        }


        if (!InventoryChecker.hasUniversalKey(player)) {
            ItemStack firstItem = chestInventory.getItem(0);

            if (firstItem != null) {
                if (ItemManager.isLock(firstItem.getItemMeta())) {
                    if (ItemManager.hasKeyCode(firstItem.getItemMeta())) {
                        Integer level = Integer.valueOf(NameSpacedKeys.getNameSpacedKey(firstItem.getItemMeta(), "level"));
                        String lockCode = NameSpacedKeys.getNameSpacedKey(firstItem.getItemMeta(), "keyCode");
                        if (InventoryChecker.hasCorrectKey(player, lockCode)) {
                            messageSender.sendPlayerMessage(player, "&8 Você abre o baú");
                        } else {
                            event.setCancelled(true);

                            if (InventoryChecker.hasLockPick(player)) {
                                chestlist.setLastClickedChest(player.getUniqueId(), clickedBlock);
                                LockPickMinigame minigame = new LockPickMinigame(chestlist);
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