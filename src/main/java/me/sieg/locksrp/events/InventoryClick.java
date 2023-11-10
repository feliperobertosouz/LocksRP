package me.sieg.locksrp.events;

import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.item.KeyFactory;
import me.sieg.locksrp.item.LockFactory;
import me.sieg.locksrp.utils.MessageSender;
import me.sieg.locksrp.utils.NameSpacedKeys;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryClick implements Listener {

    ItemManager itemManager = new ItemManager();
    MessageSender messageSender = new MessageSender();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack cursorItem = event.getCursor();
        ItemStack clickedItem = event.getCurrentItem();

        if (cursorItem.hasItemMeta()) {
            ItemMeta metaCursor = cursorItem.getItemMeta();
            if (ItemManager.isKey(metaCursor)) {
                String keyCode = NameSpacedKeys.getNameSpacedKey(metaCursor, "keyCode");

                if (keyCode != null) {
                    if (clickedItem != null && clickedItem.hasItemMeta()) {
                        ItemMeta clickedMeta = clickedItem.getItemMeta();

                        if (clickedItem.getType().equals(Material.NAME_TAG) && ItemManager.isKey(clickedMeta)
                                && event.getClickedInventory().getType() == InventoryType.PLAYER) {

                            if (NameSpacedKeys.getNameSpacedKey(clickedItem.getItemMeta(), "keyCode") == null) {
                               // ItemMeta clickMeta = clickedItem.getItemMeta();
                               // clickMeta = NameSpacedKeys.setNameSpacedKey(clickMeta, "keyCode", keyCode);
                                //clickMeta.setLore(Collections.singletonList(ChatColor.WHITE + "Key:" + ChatColor.DARK_PURPLE + keyCode));
                                //clickedItem.setItemMeta(clickMeta);
                                clickedItem = itemManager.generateKey(clickedItem, keyCode);
                                if (player.getGameMode() != GameMode.CREATIVE) {
                                    event.setCancelled(true);
                                    player.getInventory().setItem(event.getSlot(), clickedItem);
                                } else {
                                    // Remove o clickedItem do inventário criativo
                                    player.getInventory().remove(clickedItem);
                                    player.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
                                    // Adiciona o clickedItem ao inventário do jogador
                                    player.getInventory().addItem(clickedItem);
                                }
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                                player.sendMessage(ChatColor.GOLD + "Você forja uma nova chave");
                            }
                        } else {
                            if (clickedItem.getType().equals(Material.FLINT) && ItemManager.isLock(clickedItem.getItemMeta())
                                    && event.getClickedInventory().getType() == InventoryType.PLAYER) {

                                    ItemMeta clickMeta = clickedItem.getItemMeta();
                                    clickMeta = NameSpacedKeys.setNameSpacedKey(clickMeta, "keyCode", keyCode);

                                    int level = Integer.parseInt(NameSpacedKeys.getNameSpacedKey(clickedItem.getItemMeta(), "level"));
                                    messageSender.sendPlayerMessage(player, "&6Você forja uma nova tranca", Sound.BLOCK_ANVIL_USE);
                                    int amount = clickedItem.getAmount();
                                    clickedItem = itemManager.generateLock(level,keyCode, amount);
                                    if (player.getGameMode() != GameMode.CREATIVE) {
                                        event.setCancelled(true);
                                        player.getInventory().setItem(event.getSlot(), clickedItem);
                                    } else {
                                        // Remove o clickedItem do inventário criativo
                                        player.getInventory().remove(clickedItem);
                                        player.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
                                        // Adiciona o clickedItem ao inventário do jogador
                                        player.getInventory().addItem(clickedItem);

                                    }

                            }
                        }
                    }
                } else {
                    // Aqui você pode lidar com o caso em que o keyCode é nulo, se necessário.
                }
            } else {
                // Aqui você pode lidar com o caso em que isKeyTag não é "true", se necessário.
            }
        }
    }
    }

