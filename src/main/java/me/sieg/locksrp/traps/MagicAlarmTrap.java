package me.sieg.locksrp.traps;

import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.utils.MessageSender;
import me.sieg.locksrp.utils.SaveDoor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MagicAlarmTrap implements Trap{


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
