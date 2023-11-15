package me.sieg.locksrp.traps;

import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.utils.InventoryChecker;
import me.sieg.locksrp.utils.MessageSender;
import me.sieg.locksrp.utils.SaveDoor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SpikeTrap implements Trap{


    @Override
    public void install(PlayerInteractEvent event, Player player, Location loc, ItemStack trapItem) {
        MessageSender messageSender = new MessageSender();
        SaveDoor saveDoor = new SaveDoor();
        String trapType = ItemManager.getTrapType(trapItem.getItemMeta());
        saveDoor.addTrapToDoor(loc, trapType);
        InventoryChecker.useItem(player, trapItem);
        messageSender.sendPlayerMessage(player, "&c Você acaba instalando uma armadilha de espinhos na tranca");
    }

    @Override
    public void smithingTableHandler(Player player, ItemStack item) {

    }

    @Override
    public void activate(Player player, Location loc) {
        MessageSender messageSender = new MessageSender();
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1.0f, 1.0f);
        player.damage(6.0);
        messageSender.sendPlayerMessage(player,"&c Você se machuca com os espinhos no meio da tranca");
    }

    @Override
    public void lastActivate(Player player, Location loc) {
        MessageSender messageSender = new MessageSender();
        player.damage(10.0);
        messageSender.sendPlayerMessage(player,"&c Você acaba ativando o mecanismo de espinhos");
    }
}
