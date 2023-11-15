package me.sieg.locksrp.traps;

import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.utils.InventoryChecker;
import me.sieg.locksrp.utils.MessageSender;
import me.sieg.locksrp.utils.SaveDoor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ReinforcementTrap implements Trap{


    @Override
    public void install(PlayerInteractEvent event,Player player, Location loc, ItemStack trapItem) {
        MessageSender messageSender = new MessageSender();
        SaveDoor saveDoor = new SaveDoor();
        String trapType = ItemManager.getTrapType(trapItem.getItemMeta());
        saveDoor.addTrapToDoor(loc, trapType);
        InventoryChecker.useItem(player, trapItem);
        messageSender.sendPlayerMessage(player, "&c Você acaba instalando uma armadilha de reforço na tranca");
    }

    @Override
    public void smithingTableHandler(Player player, ItemStack item) {

    }

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
    }
}
