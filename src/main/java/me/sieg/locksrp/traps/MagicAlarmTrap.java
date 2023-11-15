package me.sieg.locksrp.traps;

import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.utils.InventoryChecker;
import me.sieg.locksrp.utils.MessageSender;
import me.sieg.locksrp.utils.SaveDoor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MagicAlarmTrap implements Trap{


    @Override
    public void install(PlayerInteractEvent event, Player player, Location loc, ItemStack trapItem) {
        if(ItemManager.getOwner(trapItem.getItemMeta()) != null){
            SaveDoor saveDoor = new SaveDoor();
            String trapType = ItemManager.getTrapType(trapItem.getItemMeta());
            saveDoor.addTrapToDoor(loc,trapType);
            String owner = ItemManager.getOwner(trapItem.getItemMeta());
            saveDoor.setDoorOwner(loc,owner);
            InventoryChecker.useItem(player,trapItem);
        }else {
            MessageSender.sendPlayerMessage(player,"&c Você precisa dar um dono a essa armadilha!");
            event.setCancelled(true);
        }

    }

    @Override
    public void smithingTableHandler(Player player, ItemStack item) {
        MessageSender messageSender = new MessageSender();
        messageSender.sendPlayerMessage(player, "&6 Você se torna dono desta armadilha", Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
        ItemMeta meta = item.getItemMeta();
        List<String> newlore = meta.getLore();
        newlore.add(ChatColor.GRAY + "OWNER: " + player.getName());
        meta.setLore(newlore);
        item.setItemMeta(meta);
        item = ItemManager.setOwner(item, player.getName());
        player.getInventory().setItemInMainHand(item);
        messageSender.sendPlayerMessage(player, "&5 Parece que agora você é meu dono!");

    }

    @Override
    public void activate(Player player, Location loc) {
        MessageSender messageSender = new MessageSender();
        SaveDoor saveDoor = new SaveDoor();
        String owner = null;
        if(SaveDoor.isValidDoorBlock(loc.getBlock())){
            owner = saveDoor.getDoorOwner(loc);
        }else if(SaveDoor.isValidContainer(loc.getBlock())){
            Inventory inventory = SaveDoor.getInventoryFromClickedBlock(loc.getBlock());
            ItemStack trap = inventory.getItem(1);
            if(trap != null){
                if(ItemManager.isTrap(trap.getItemMeta())){
                    owner = ItemManager.getOwner(trap.getItemMeta());
                }
            }
        }
        if(owner != null){
            messageSender.sendPlayerMessage(player, "Eu vou falar para o " + owner + " que você tentou roubar!");
        }else{
            player.sendMessage("Eu não tenho dono para avisar :(");
        }

    }

    @Override
    public void lastActivate(Player player, Location loc) {
        MessageSender messageSender = new MessageSender();
        SaveDoor saveDoor = new SaveDoor();
        String owner = null;
        if(SaveDoor.isValidDoorBlock(loc.getBlock())){
            owner = saveDoor.getDoorOwner(loc);
        }else if(SaveDoor.isValidContainer(loc.getBlock())){
            Inventory inventory = SaveDoor.getInventoryFromClickedBlock(loc.getBlock());
            ItemStack trap = inventory.getItem(1);
            if(trap != null){
                if(ItemManager.isTrap(trap.getItemMeta())){
                    owner = ItemManager.getOwner(trap.getItemMeta());
                }
            }
        }
        if(owner != null){
            messageSender.sendPlayerMessage(player, " VOu falar para meu dono que você tentou roubar ele");
            sendMessageToOnlinePlayer(owner, " Alguem tentou roubar você! SOCORRO!");
        }else{
            player.sendMessage("Eu não tenho dono para avisar :(");
        }
    }

    public static void sendMessageToOnlinePlayer(String playerName, String message) {
        MessageSender messageSender = new MessageSender();
        Player player = Bukkit.getPlayerExact(playerName);
        String name = player.getDisplayName();
        if (player != null && player.isOnline()) {
            messageSender.sendPlayerMessage(player, message, Sound.ENTITY_ALLAY_HURT, 2.0f, 2.0f);
        } else {
            Bukkit.getLogger().info( name + " não está acordado.");
        }
    }
}
