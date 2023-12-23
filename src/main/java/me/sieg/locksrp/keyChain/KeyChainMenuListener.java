package me.sieg.locksrp.keyChain;

import me.sieg.locksrp.Main;
import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.item.KeyChainFactory;
import me.sieg.locksrp.utils.MessageSender;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static me.sieg.locksrp.item.KeyChainFactory.updateKeyChainMetadata;

public class KeyChainMenuListener implements Listener {


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof KeyChainMenu) {
            // Cancela o evento para evitar que o jogador mova itens no KeyChainMenu
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            ItemStack keyChain = event.getWhoClicked().getInventory().getItemInMainHand();

            if (event.getClickedInventory().getType() != InventoryType.PLAYER) {
                Player player = (Player) event.getWhoClicked();
                KeyChainMenu keyChainMenu = (KeyChainMenu) event.getInventory().getHolder();
                int clickedSlot = event.getSlot();

                // Remove a chave do KeyChainMenu e adiciona ao inventário do jogador
                moveKeyToInventory(player, keyChainMenu, clickedSlot);

                // Atualiza o KeyChain com a lista atualizada de chaves
                Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
                    List<ItemStack> updatedKeys = getItemsInMenu(player);
                    ItemStack updatedKeyChain = updateKeyChainMetadata(keyChain, updatedKeys);

                    // Limpa e recria o menu com as chaves atualizadas
                    keyChainMenu.initMenu(player, 9, updatedKeys);

                    // Atualiza o item na mão do jogador
                    player.getInventory().setItemInMainHand(updatedKeyChain);
                }, 2);
            }else if(event.getClickedInventory().getType() == InventoryType.PLAYER){
                if(clickedItem != null){
                    if (clickedItem.hasItemMeta() &&  ItemManager.isKey(clickedItem.getItemMeta())) {
                        Player player = (Player) event.getWhoClicked();
                        player.playSound(player, Sound.ITEM_AXE_SCRAPE, 1.0f,2.0f);
                        KeyChainMenu keyChainMenu = (KeyChainMenu) event.getInventory().getHolder();
                        int clickedSlot = event.getSlot();
                        moveKeyToKeyChain(player, keyChainMenu, clickedSlot);

                            // Agende uma tarefa para atualizar o KeyChain após 1 tick
                            Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
                                // Gere a lista de itens no KeyChainMenu
                                List<ItemStack> itemsInMenu = keyChainMenu.getItems();

                                // Atualize o KeyChain com a nova lista de itens
                                ItemStack updateKeyChain = KeyChainFactory.updateKeyChainMetadata(keyChain, itemsInMenu);

                                keyChainMenu.initMenu(player, 9, itemsInMenu);
                                player.getInventory().setItemInMainHand(updateKeyChain);

                            }, 1);

                    }
                }
            }
        }
    }

    // Método para obter a lista de ItemStacks no KeyChainMenu
    private List<ItemStack> getItemsInMenu(Player player) {

        List<ItemStack> items = new ArrayList<>();
        Inventory keyChainMenu = player.getOpenInventory().getTopInventory();

        // Itera sobre os itens no inventário e adiciona à lista
        for (ItemStack item : keyChainMenu.getContents()) {
            if (item != null && !item.getType().isAir()) {
                items.add(item.clone());
            }
        }

        return items;
    }

    public int getFirstEmptySlot(Player player) {
        ItemStack[] contents = player.getOpenInventory().getTopInventory().getContents();

        for (int i = 0; i < contents.length; i++) {
            if (contents[i] == null || contents[i].getType() == Material.AIR) {
                return i;
            }
        }

        return -1; // Nenhum slot vazio encontrado
    }


    public static void moveKeyToInventory(Player player, KeyChainMenu keyChainMenu, int clickedSlot) {
        ItemStack clickedItem = keyChainMenu.getInventory().getItem(clickedSlot);
        if (clickedItem != null && clickedItem.getType() != Material.AIR) {
            if (hasInventorySpace(player)) {
                player.getInventory().addItem(clickedItem);
                keyChainMenu.remove(clickedSlot);
                player.playSound(player, Sound.ITEM_AXE_SCRAPE, 1.0f,2.0f);
            } else {
                MessageSender.sendPlayerMessage(player,"&4&lInventário cheio, não foi possível mover a chave");
                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            }
        }
    }

    // Método para verificar se há espaço no inventário do jogador
    private static boolean hasInventorySpace(Player player) {
        int emptySlots = 0;
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item == null || item.getType() == Material.AIR) {
                emptySlots++;
            }
        }
        return emptySlots > 0;
    }

    public void moveKeyToKeyChain(Player player, KeyChainMenu keyChainMenu, int clickedSlot) {
        ItemStack clickedItem = player.getInventory().getItem(clickedSlot);

        if (clickedItem != null && clickedItem.getType() != Material.AIR) {
            Inventory menu = keyChainMenu.getInventory();
            int firstEmptySlot = getFirstEmptySlot(player);

            if (firstEmptySlot != -1) {
                // Adicione ao KeyChainMenu e remova do inventário do jogador
                ItemStack itemToKeyChain = clickedItem.clone();
                itemToKeyChain.setAmount(1);
                menu.setItem(firstEmptySlot, itemToKeyChain);
                int amount = clickedItem.getAmount();
                if (amount > 1) {
                    clickedItem.setAmount(amount - 1);
                    player.getInventory().setItem(clickedSlot, clickedItem);
                } else{
                    player.getInventory().setItem(clickedSlot, null);
                }



                player.playSound(player, Sound.ITEM_AXE_SCRAPE, 1.0f, 2.0f);
            }
        }
    }

}
