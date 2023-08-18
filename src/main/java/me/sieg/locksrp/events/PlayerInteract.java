package me.sieg.locksrp.events;
import me.sieg.locksrp.utils.*;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Fence;
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
    public PlayerInteract(ChestKeeper chestlist){
        this.chestlist = chestlist;

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Block clickedBlock = event.getClickedBlock();
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if (clickedBlock != null && clickedBlock.getType() == Material.SMITHING_TABLE) {
                if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().hasItemMeta()) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (NameSpacedKeys.isKey(item.getItemMeta()) && NameSpacedKeys.getNameSpacedKey(item.getItemMeta(), "keyCode") == null) {
                        event.setCancelled(true);
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                        player.sendMessage(ChatColor.GOLD + "Você forja uma nova chave");
                        String code = KeyCodeGenerator.generateUniqueCode();
                        ItemMeta meta = item.getItemMeta();
                        meta = NameSpacedKeys.setNameSpacedKey(meta, "keyCode", code);
                        meta.setLore(Collections.singletonList(ChatColor.WHITE + "Chave:" + ChatColor.DARK_PURPLE + code));
                        item.setItemMeta(meta);
                        player.getInventory().setItemInMainHand(item);
                    }
                }
            } else if (clickedBlock != null && clickedBlock.getType() == Material.CHEST
                    || clickedBlock.getType() == Material.BARREL) {
                Inventory chestInventory = player.getInventory();
                if(clickedBlock.getType() == Material.CHEST){
                    Chest chest = (Chest) clickedBlock.getState();
                    chestInventory = chest.getInventory();
                }else if(clickedBlock.getType() == Material.BARREL){
                    Barrel barrel = (Barrel) clickedBlock.getState();
                    chestInventory = barrel.getInventory();
                }


                if (!player.hasPermission("locksrp.admin")) {
                    ItemStack firstItem = chestInventory.getItem(0);

                    if (firstItem != null) {
                        // Envia o item para o jogador
                        if (NameSpacedKeys.isLock(firstItem.getItemMeta())) {
                            if (NameSpacedKeys.hasKeyCode(firstItem.getItemMeta())) {
                                Integer level = Integer.valueOf(NameSpacedKeys.getNameSpacedKey(firstItem.getItemMeta(), "level"));
                                String lockCode = NameSpacedKeys.getNameSpacedKey(firstItem.getItemMeta(), "keyCode");
                                if (InventoryChecker.hasCorrectKey(player, lockCode)) {
                                    player.sendMessage(ChatColor.GOLD + "Você abre o bau");
                                } else {
                                    event.setCancelled(true);

                                    if (InventoryChecker.hasLockPick(player)) {
                                        chestlist.setLastClickedChest(player.getUniqueId(), clickedBlock);
                                        LockPickMinigame minigame = new LockPickMinigame(chestlist);
                                        minigame.openCustomGUI(player, level);


                                    } else {
                                        player.sendMessage(ChatColor.DARK_RED + "O bau esta trancado");
                                    }
                                }
                            }
                        }

                    }
                }
            } else if (clickedBlock != null && SaveDoor.isDoor(clickedBlock)) {
                SaveDoor saveDoor = new SaveDoor();
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (clickedBlock.getBlockData() instanceof Door || clickedBlock.getBlockData() instanceof TrapDoor
                            || clickedBlock.getBlockData() instanceof Gate) {
                        Location loc = event.getClickedBlock().getLocation();
                        boolean isLocked = saveDoor.isDoorLocked(loc);
                        if(clickedBlock.getBlockData() instanceof  Door){
                            Door doorData = (Door) clickedBlock.getBlockData();

                            if ( doorData!= null && doorData.getHalf() == Door.Half.TOP) {
                                loc = SaveDoor.getBlockBelow(event.getClickedBlock().getLocation());
                                isLocked = saveDoor.isDoorLocked(loc);
                            }
                        }

                        if(isLocked && !player.hasPermission("locksrp.admin")){
                            player.sendMessage(ChatColor.RED + "A PORTA ESTA TRANCADA");
                            event.setCancelled(true);
                            String lockCode = saveDoor.getLockCode(loc);
                            if(InventoryChecker.hasLockPick(player) && !InventoryChecker.hasCorrectKey(player,lockCode)){
                                Block block = loc.getBlock();
                                chestlist.setLastClickedChest(player.getUniqueId(),block);
                                LockPickMinigame minigame = new LockPickMinigame(chestlist);
                                int level = saveDoor.getLockLevel(loc);
                                minigame.openCustomGUI(player, level);
                            }
                        }
                        // Faça o que for necessário com a informação se a porta está trancada ou não

                        ItemStack item = player.getInventory().getItemInMainHand();
                        if (item != null && item.getType() != Material.AIR && NameSpacedKeys.isLock(item.getItemMeta())) {
                            if (saveDoor.isLocationRegistered(loc)) {
                                player.sendMessage("A PORTA PARECE JA TEM UMA TRANCA");
                            } else {
                                player.sendMessage("A PORTA NÃO TINHA TRANCA, COLOCANDO...");
                                ItemMeta meta = item.getItemMeta();
                                String keyCode = NameSpacedKeys.getNameSpacedKey(meta, "keyCode");
                                if(keyCode != null){
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
                        } else if ( item.hasItemMeta() && NameSpacedKeys.isKey(item.getItemMeta()) && NameSpacedKeys.hasKeyCode(item.getItemMeta())
                                && saveDoor.isLocationRegistered(loc)) {
                            String lockCode = saveDoor.getLockCode(loc);
                            String keyCode = NameSpacedKeys.getNameSpacedKey(item.getItemMeta(),"keyCode");

                            if(lockCode.equals(keyCode)){
                                if (isLocked) {
                                    player.sendMessage("DESTRANCANDO PORTA");
                                    saveDoor.setDoorLocked(loc, false);
                                    event.setCancelled(true);
                                } else {
                                    player.sendMessage("TRANCANDO PORTA");
                                    saveDoor.setDoorLocked(loc, true);
                                    event.setCancelled(true);
                                }
                                player.playSound(player.getLocation(), Sound.ITEM_SPYGLASS_USE, 1.0f, 0.1f);
                                player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.5f);
                            }
                        }else if (item.hasItemMeta() && NameSpacedKeys.isLockRemover(item.getItemMeta())){
                            if(saveDoor.isLocationRegistered(loc)){
                                if(!saveDoor.isDoorLocked(loc) || player.isOp() || player.hasPermission("locksrp.admin")){
                                    Itemmanager itemManager = new Itemmanager();
                                    String keyCode = saveDoor.getLockCode(loc);
                                    Integer level = saveDoor.getLockLevel(loc);
                                    ItemStack itemDrop = itemManager.generateLock(level,keyCode);
                                    saveDoor.dropItemOnGround(loc,itemDrop);
                                    player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 0.1f, 1.5f);
                                    saveDoor.removeLocationFromFile(loc);
                                    player.sendMessage(ChatColor.RED + "Você remove a tranca da porta");
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }else if(clickedBlock != null && clickedBlock.getType() == Material.GRINDSTONE){
                ItemStack item = player.getInventory().getItemInMainHand();
                    if(item != null && item.hasItemMeta()){
                        if(NameSpacedKeys.isKey(item.getItemMeta())){
                            event.setCancelled(true);
                            Integer amount = item.getAmount();
                            Itemmanager items = new Itemmanager();
                            player.getInventory().setItemInMainHand(items.getKeyItem(amount));
                            player.playSound(player.getLocation(), Sound.BLOCK_GRINDSTONE_USE, 1f, 1f);
                        }
                    }
            }
        }

    }


    }