package me.sieg.locksrp.events;
import me.sieg.locksrp.interactions.ContainerInteraction;
import me.sieg.locksrp.interactions.DoorInteraction;
import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.item.KeyChainFactory;
import me.sieg.locksrp.item.MaterialKey;
import me.sieg.locksrp.keyChain.KeyChainMenu;
import me.sieg.locksrp.traps.Trap;
import me.sieg.locksrp.traps.TrapType;
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

import java.util.List;

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
        ItemStack mainItem = event.getPlayer().getInventory().getItemInMainHand();

        if(event.getAction() == Action.RIGHT_CLICK_AIR && ItemManager.isKeyChain(mainItem.getItemMeta())){
            handleKeyChainInteraction(event);
            event.setCancelled(true);
        }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock != null) {
                Player player = event.getPlayer();
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
                } else if (clickedBlock != null && (clickedBlock.getType() == Material.ANVIL || clickedBlock.getType() == Material.CHIPPED_ANVIL || clickedBlock.getType() == Material.DAMAGED_ANVIL)) {
                    handleAnvilInteraction(event);
                }
            }

    }

    private void handleKeyChainInteraction(PlayerInteractEvent event){
        Player player = event.getPlayer();
        messageSender.sendPlayerMessage(player,"Você clicou com o chaveiro");
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        List<ItemStack> keys = KeyChainFactory.generateKeysFromKeyChain(mainHandItem);
        KeyChainMenu keyChainMenu = new KeyChainMenu();
        keyChainMenu.initMenu(player, 9, keys);
    }

    private void handleAnvilInteraction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();


        if (mainHandItem.hasItemMeta() && ItemManager.isKey(mainHandItem.getItemMeta()) && offHandItem.getType() != Material.AIR) {
            event.setCancelled(true);
            MaterialKey matchingMaterialKey = getMatchingMaterialKey(offHandItem.getType());
            if (matchingMaterialKey != null) {
                offHandItem.setAmount(offHandItem.getAmount() - 1);


                int customModelData = matchingMaterialKey.getCustomModelData();
                mainHandItem = setCustomModelData(mainHandItem, customModelData);
                messageSender.sendPlayerMessage(player, "&6Você da uma bela embelezada na sua chave", Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);

                player.getInventory().setItemInMainHand(mainHandItem);
            }
        }
    }


    private ItemStack setCustomModelData(ItemStack item, int customModelData) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(customModelData);
        item.setItemMeta(itemMeta);
        return item;
    }
    private MaterialKey getMatchingMaterialKey(Material material) {
        for (MaterialKey materialKey : MaterialKey.values()) {
            if (materialKey.getMaterial() == material) {
                return materialKey;
            }
        }
        return null;
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
            }else if(ItemManager.isTrap(item.getItemMeta())){
                event.setCancelled(true);
                String trapTypeString = ItemManager.getTrapType(item.getItemMeta());
                TrapType trapType = TrapType.valueOf(trapTypeString);
                Trap trap = trapType.getTrap();
                if(trap != null){
                    trap.smithingTableHandler(player, item);
                }
            }
        }
    }



}