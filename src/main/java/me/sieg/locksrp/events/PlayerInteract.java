package me.sieg.locksrp.events;
import me.sieg.locksrp.utils.*;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class PlayerInteract implements Listener {

    private ChestKeeper chestlist;

    public PlayerInteract(ChestKeeper chestlist) {
        this.chestlist = chestlist;

    }

    ItemManager itemManager = new ItemManager();
    MessageSender messageSender = new MessageSender();


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Block clickedBlock = event.getClickedBlock();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock != null) {
            if (clickedBlock.getType() == Material.SMITHING_TABLE) {
                if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().hasItemMeta()) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (NameSpacedKeys.isKey(item.getItemMeta()) && NameSpacedKeys.getNameSpacedKey(item.getItemMeta(), "keyCode") == null) {
                        event.setCancelled(true);
                        messageSender.sendPlayerMessage(player, "&6 Você forja uma nova chave", Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                        String code = KeyCodeGenerator.generateUniqueCode();
                        ItemMeta meta = item.getItemMeta();
                        item = itemManager.generateKey(item, code);
                        player.getInventory().setItemInMainHand(item);
                    }
                }
            } else if (clickedBlock.getType() == Material.CHEST
                    || clickedBlock.getType() == Material.BARREL
                    || clickedBlock.getState() instanceof ShulkerBox) {
                Inventory chestInventory = player.getInventory();
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
                        // Envia o item para o jogador
                        if (NameSpacedKeys.isLock(firstItem.getItemMeta())) {
                            if (NameSpacedKeys.hasKeyCode(firstItem.getItemMeta())) {
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
            } else if (SaveDoor.isDoor(clickedBlock)) {
                SaveDoor saveDoor = new SaveDoor();
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (clickedBlock.getBlockData() instanceof Door || clickedBlock.getBlockData() instanceof TrapDoor
                            || clickedBlock.getBlockData() instanceof Gate) {
                        Location loc = event.getClickedBlock().getLocation();
                        boolean isLocked = saveDoor.isDoorLocked(loc);
                        if (clickedBlock.getBlockData() instanceof Door) {
                            Door doorData = (Door) clickedBlock.getBlockData();

                            if (doorData != null && doorData.getHalf() == Door.Half.TOP) {
                                loc = SaveDoor.getBlockBelow(event.getClickedBlock().getLocation());
                                isLocked = saveDoor.isDoorLocked(loc);
                            }
                        }

                        if (isLocked) {
                            event.setCancelled(true);
                            String lockCode = saveDoor.getLockCode(loc);
                            if (InventoryChecker.hasLockPick(player) && !InventoryChecker.hasCorrectKey(player, lockCode)) {
                                Block block = loc.getBlock();
                                chestlist.setLastClickedChest(player.getUniqueId(), block);
                                LockPickMinigame minigame = new LockPickMinigame(chestlist);
                                int level = saveDoor.getLockLevel(loc);
                                if (level != 6) {
                                    minigame.openCustomGUI(player, level);
                                } else {
                                    messageSender.sendPlayerMessage(player, "&5&l A Tranca parece muito poderosa para se fazer isso", Sound.ENTITY_VILLAGER_NO, 1.0f, 2.0f);
                                }

                            }
                        }
                        // Faça o que for necessário com a informação se a porta está trancada ou não

                        ItemStack item = player.getInventory().getItemInMainHand();
                        if (item != null && item.getType() != Material.AIR && NameSpacedKeys.isLock(item.getItemMeta())) {
                            if (!player.hasPermission("locksrp.admin") && !LandsChecker.PlayerCanPlaceLand(player)) {
                                messageSender.sendPlayerMessage(player, "&c Você não tem permissão para colocar uma tranca aqui", Sound.ENTITY_VILLAGER_NO, 1.0f, 2.0f);
                                return;
                            }
                            if (saveDoor.isLocationRegistered(loc)) {
                                messageSender.sendPlayerMessage(player, "&4 A porta parece já ter uma tranca", Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                            } else {

                                messageSender.sendPlayerMessage(player, "&fColocado a tranca na porta", Sound.BLOCK_ANVIL_USE, 1.0f, 2.0f);
                                ItemMeta meta = item.getItemMeta();
                                String keyCode = NameSpacedKeys.getNameSpacedKey(meta, "keyCode");
                                if (keyCode != null) {
                                    Integer level = Integer.valueOf(NameSpacedKeys.getNameSpacedKey(meta, "level"));
                                    saveDoor.saveLocationToFile(loc, false, keyCode, level);

                                    // Diminui a quantidade do item em 1
                                    item.setAmount(item.getAmount() - 1);

                                    if (item.getAmount() <= 0) {
                                        // Se a quantidade for menor ou igual a 0, remove o item da mão do jogador
                                        player.getInventory().setItemInMainHand(null);
                                    } else {
                                        // Atualiza o item na mão do jogador
                                        player.getInventory().setItemInMainHand(item);
                                    }
                                }
                            }
                        } else if (item != null && item.getType() != Material.AIR && item.hasItemMeta() && NameSpacedKeys.isKey(item.getItemMeta()) && NameSpacedKeys.hasKeyCode(item.getItemMeta())
                                && saveDoor.isLocationRegistered(loc) || (item.hasItemMeta() && NameSpacedKeys.isUniversalKey(item.getItemMeta()) && saveDoor.isLocationRegistered(loc))) {
                            String lockCode = saveDoor.getLockCode(loc);
                            String keyCode = NameSpacedKeys.getNameSpacedKey(item.getItemMeta(), "keyCode");

                            if (lockCode.equals(keyCode) || NameSpacedKeys.isUniversalKey(item.getItemMeta())) {
                                if (isLocked) {
                                    messageSender.sendPlayerMessage(player, "&fDestrancando Porta", Sound.BLOCK_BARREL_OPEN, 1.0f, 2.0f);
                                    saveDoor.setDoorLocked(loc, false);
                                    event.setCancelled(true);
                                } else {
                                    messageSender.sendPlayerMessage(player, "&8Trancando Porta", Sound.BLOCK_BARREL_CLOSE, 1.0f, 2.0f);
                                    saveDoor.setDoorLocked(loc, true);
                                    event.setCancelled(true);
                                }
                                player.playSound(player.getLocation(), Sound.ITEM_SPYGLASS_USE, 1.0f, 0.1f);
                                player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.5f);
                            }
                        } else if (item != null && item.getType() != Material.AIR && item.hasItemMeta() && NameSpacedKeys.isLockRemover(item.getItemMeta())) {
                            if (saveDoor.isLocationRegistered(loc)) {
                                if (!saveDoor.isDoorLocked(loc) || player.isOp() || player.hasPermission("locksrp.admin")) {
                                    if (!player.hasPermission("locksrp.admin") && !LandsChecker.PlayerCanBreakLand(player)) {
                                        messageSender.sendPlayerMessage(player, "&c Você não tem permissão para retirar trancas aqui", Sound.ENTITY_VILLAGER_NO, 1.0f, 2.0f);
                                        return;
                                    }
                                    ItemManager itemManager = new ItemManager();
                                    String keyCode = saveDoor.getLockCode(loc);
                                    Integer level = saveDoor.getLockLevel(loc);
                                    ItemStack itemDrop = itemManager.generateLock(level, keyCode);
                                    saveDoor.dropItemOnGround(loc, itemDrop);
                                    saveDoor.removeLocationFromFile(loc);
                                    messageSender.sendPlayerMessage(player, "&4Você removeu a tranca da porta", Sound.BLOCK_DISPENSER_DISPENSE, 0.1f, 1.5f);
                                    event.setCancelled(true);
                                }
                            }
                        } else {
                            if (isLocked) {
                                messageSender.sendPlayerMessage(player, "&4A porta esta trancada", Sound.ENTITY_VILLAGER_NO, 1.0f, 2.0f);
                            }
                        }
                    }
                }
            } else if (clickedBlock != null && clickedBlock.getType() == Material.GRINDSTONE) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item != null && item.hasItemMeta()) {
                    if (NameSpacedKeys.isKey(item.getItemMeta())) {
                        event.setCancelled(true);
                        Integer amount = item.getAmount();
                        ItemManager items = new ItemManager();
                        int customModel = item.getItemMeta().getCustomModelData();
                        player.getInventory().setItemInMainHand(items.getKeyItem(amount, customModel));
                        messageSender.sendPlayerMessage(player, "&6Você limpa sua chave", Sound.BLOCK_GRINDSTONE_USE);
                    }
                }
            }
        }

    }
}