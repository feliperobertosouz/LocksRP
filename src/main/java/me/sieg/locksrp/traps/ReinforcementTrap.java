package me.sieg.locksrp.traps;

import me.sieg.locksrp.utils.InventoryChecker;
import me.sieg.locksrp.utils.MessageSender;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ReinforcementTrap implements Trap{
    @Override
    public void install(Player player, Location loc) {

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
