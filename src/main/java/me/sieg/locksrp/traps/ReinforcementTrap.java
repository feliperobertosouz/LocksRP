package me.sieg.locksrp.traps;

import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.utils.InventoryChecker;
import me.sieg.locksrp.utils.MessageSender;
import me.sieg.locksrp.utils.SaveDoor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ReinforcementTrap extends SuperTrap{


    @Override
    public void activate(Player player, Location loc) {
        MessageSender sender = new MessageSender();
        InventoryChecker.removeLockPick(player);
        sender.sendPlayerMessage(player,"&c Você acaba tendo que gastar mais lockpicks para abrir a tranca");
    }


    @Override
    public void lastActivate(Player player, Location loc) {
        MessageSender sender = new MessageSender();
        InventoryChecker.removeLockPick(player);
        sender.sendPlayerMessage(player,"&c Você acaba ativando o mecanismo da porta e gasta mais lockpicks para tirar as mãos");
        InventoryChecker.removeLockPick(player,10);
        super.decrementUses(player,loc);
    }
}
