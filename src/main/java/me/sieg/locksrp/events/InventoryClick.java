package me.sieg.locksrp.events;

import me.sieg.locksrp.Main;
import me.sieg.locksrp.utils.Itemmanager;
import me.sieg.locksrp.utils.NameSpacedKeys;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack cursorItem = event.getCursor();
        ItemStack clickedItem = event.getCurrentItem();

        if (cursorItem.hasItemMeta()) {
            ItemMeta metaCursor = cursorItem.getItemMeta();
            if (NameSpacedKeys.isKey(metaCursor)) {
                String keyCode = NameSpacedKeys.getNameSpacedKey(metaCursor, "keyCode");

                if (keyCode != null) {
                    if (clickedItem != null && clickedItem.hasItemMeta()) {
                        ItemMeta clickedMeta = clickedItem.getItemMeta();

                        if (clickedItem.getType().equals(Material.NAME_TAG) && NameSpacedKeys.isKey(clickedMeta)
                                && event.getClickedInventory().getType() == InventoryType.PLAYER) {

                            if (NameSpacedKeys.getNameSpacedKey(clickedItem.getItemMeta(), "keyCode") == null) {
                                ItemMeta clickMeta = clickedItem.getItemMeta();
                                clickMeta = NameSpacedKeys.setNameSpacedKey(clickMeta, "keyCode", keyCode);
                                clickMeta.setLore(Collections.singletonList(ChatColor.WHITE + "Chave:" + ChatColor.DARK_PURPLE + keyCode));
                                clickedItem.setItemMeta(clickMeta);

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
                            if (clickedItem.getType().equals(Material.FLINT) && NameSpacedKeys.isLock(clickedItem.getItemMeta())
                                    && event.getClickedInventory().getType() == InventoryType.PLAYER) {

                                    ItemMeta clickMeta = clickedItem.getItemMeta();
                                    clickMeta = NameSpacedKeys.setNameSpacedKey(clickMeta, "keyCode", keyCode);
                                    if (clickMeta.hasLore()) {
                                        List<String> lore;
                                        List<String> newlore = new ArrayList<>();
                                        lore = clickMeta.getLore();
                                        newlore.add(lore.get(0));
                                        newlore.add(ChatColor.WHITE + "code:" + ChatColor.DARK_PURPLE + keyCode);
                                        clickMeta.setLore(newlore);
                                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                                        player.sendMessage(ChatColor.GOLD + "Você forja uma nova tranca");
                                    }
                                    clickedItem.setItemMeta(clickMeta);

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

