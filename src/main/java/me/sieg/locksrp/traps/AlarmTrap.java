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

public class AlarmTrap implements Trap{

    @Override
    public void install(PlayerInteractEvent event,Player player, Location loc, ItemStack trapItem) {
        MessageSender messageSender = new MessageSender();
        SaveDoor saveDoor = new SaveDoor();
        String trapType = ItemManager.getTrapType(trapItem.getItemMeta());
        saveDoor.addTrapToDoor(loc, trapType);
        InventoryChecker.useItem(player, trapItem);
        messageSender.sendPlayerMessage(player, "&c Você acaba instalando uma armadilha de alarme na tranca");
    }

    @Override
    public void smithingTableHandler(Player player, ItemStack item) {

    }

    @Override
    public void activate(Player player, Location loc) {
        MessageSender messageSender = new MessageSender();
        loc.getWorld().playSound(loc, org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
        messageSender.sendPlayerMessage(player,"&c Você acaba tocando os pequenos sinos que haviam presos na tranca");
    }

    @Override
    public void lastActivate(Player player, Location loc) {
        MessageSender messageSender = new MessageSender();
        loc.getWorld().playSound(loc, Sound.BLOCK_BELL_USE, 10.0f, 2.0f);
        messageSender.sendPlayerMessage(player,"&c Você acaba ativando o mecanismo de alarme");
    }
}
