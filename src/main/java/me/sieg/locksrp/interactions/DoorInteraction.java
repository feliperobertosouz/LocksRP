package me.sieg.locksrp.interactions;

import me.sieg.locksrp.events.LockPickMinigame;
import me.sieg.locksrp.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DoorInteraction {

    private ChestKeeper chestKeeper;
    private SaveDoor saveDoor;
    private MessageSender messageSender;

    public DoorInteraction(ChestKeeper chestKeeper) {
        this.chestKeeper = chestKeeper;
        this.messageSender = new MessageSender();

    }

    public void handleDoorInteraction(PlayerInteractEvent event) {
        this.saveDoor = new SaveDoor();
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        SaveDoor saveDoor = new SaveDoor();
        assert clickedBlock != null;
        if (isValidDoorBlock(clickedBlock)) {
            Location loc = checkAndHandleTopHalfDoor(clickedBlock);
            boolean isLocked = saveDoor.isDoorLocked(loc);
            if (isLocked) {
                handleLockedDoorInteraction(event, loc);
            }
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.hasItemMeta()) {
                if (NameSpacedKeys.isLock(item.getItemMeta())) {
                    handleLockInteraction(event, loc);
                }else if(saveDoor.isLocationRegistered(loc)){
                    if (NameSpacedKeys.isKey(item.getItemMeta()) && NameSpacedKeys.hasKeyCode(item.getItemMeta()) || (item.hasItemMeta() && NameSpacedKeys.isUniversalKey(item.getItemMeta()) )){
                        handleKeyInteraction(event, loc, item);
                    }else if(NameSpacedKeys.isLockRemover(item.getItemMeta())){
                        handleLockRemoverInteraction(event, loc);
                    }
                }
            }
        }
    }

    private boolean isValidDoorBlock(Block block) {
        return block.getBlockData() instanceof Door || block.getBlockData() instanceof TrapDoor
                || block.getBlockData() instanceof Gate;
    }


    private Location checkAndHandleTopHalfDoor(Block clickedBlock) {
        Location loc = clickedBlock.getLocation();
        if (clickedBlock.getBlockData() instanceof Door) {

            Door doorData = (Door) clickedBlock.getBlockData();

            if (doorData != null && doorData.getHalf() == Door.Half.TOP) {
                loc = SaveDoor.getBlockBelow(clickedBlock.getLocation());
                return loc;
            }
        }
        return loc;
    }

    private void handleLockInteraction(PlayerInteractEvent event, Location loc){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!player.hasPermission("locksrp.admin") && !LandsChecker.PlayerCanPlaceLand(player)) {
            messageSender.sendPlayerMessage(player, "&c Você não tem permissão para colocar uma tranca aqui", Sound.ENTITY_VILLAGER_NO, 1.0f, 2.0f);
            event.setCancelled(true);
        } else {
            if (saveDoor.isLocationRegistered(loc)) {
                messageSender.sendPlayerMessage(player, "&4 A porta parece já tem uma tranca, você deve tirar ela primeiro", Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            } else {
                ItemMeta meta = item.getItemMeta();
                String keyCode = NameSpacedKeys.getNameSpacedKey(meta, "keyCode");
                if (keyCode != null) {
                   installLock(player, loc, item, meta, keyCode);
                }else {
                    messageSender.sendPlayerMessage(player, "&cVocê precisa vincular uma chave a sua tranca antes de tentar colocar ela em uma porta", Sound.ENTITY_VILLAGER_TRADE, 1.0f, 2.0f);
                }
            }
        }

    }

    private void handleLockedDoorInteraction(PlayerInteractEvent event, Location loc){
        Player player = event.getPlayer();

        String lockCode = saveDoor.getLockCode(loc);
        if (InventoryChecker.hasLockPick(player) && !InventoryChecker.hasCorrectKey(player, lockCode)) {
            handleLockPick(player, loc);
            event.setCancelled(true);
        } else if (InventoryChecker.hasCorrectKey(player, lockCode) || InventoryChecker.hasUniversalKey(player)) {
            messageSender.sendPlayerMessage(player, "&fVocê tem a chave.", Sound.ENTITY_VILLAGER_TRADE, 1.0f, 2.0f);
        }else{
            messageSender.sendPlayerMessage(player, "&cA porta está trancada, você não tem a chave dela", Sound.ENTITY_VILLAGER_NO, 1.0f, 2.0f);
            event.setCancelled(true);
        }

    }
    private void handleKeyInteraction(PlayerInteractEvent event, Location loc, ItemStack item){
        Player player = event.getPlayer();
        String lockCode = saveDoor.getLockCode(loc);
        boolean isLocked = saveDoor.isDoorLocked(loc);
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
    }

    private void handleLockRemoverInteraction(PlayerInteractEvent event, Location loc){
        Player player = event.getPlayer();
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

    private void installLock(Player player, Location loc, ItemStack item, ItemMeta meta, String keyCode){
        messageSender.sendPlayerMessage(player, "&fColocado a tranca na porta", Sound.BLOCK_ANVIL_USE, 1.0f, 2.0f);
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


    private void handleLockPick(Player player,Location loc){
        Block block = loc.getBlock();
        chestKeeper.setLastClickedChest(player.getUniqueId(), block);
        LockPickMinigame minigame = new LockPickMinigame(chestKeeper);
        int level = saveDoor.getLockLevel(loc);
        if (level != 6) {
            minigame.openCustomGUI(player, level);
        } else {
            messageSender.sendPlayerMessage(player, "&5&l A Tranca parece muito poderosa para se fazer isso", Sound.ENTITY_VILLAGER_NO, 1.0f, 2.0f);
        }
    }

    //caso o de cima não de certo
    private boolean isValidDoorBlockSaveDoor(Block block){
       return saveDoor.isDoor(block);
    }


}

