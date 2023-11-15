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

public class ExplosiveTrap implements Trap {
    @Override
    public void install(PlayerInteractEvent event, Player player, Location loc, ItemStack trapItem) {
        MessageSender messageSender = new MessageSender();
        SaveDoor saveDoor = new SaveDoor();
        String trapType = ItemManager.getTrapType(trapItem.getItemMeta());
        saveDoor.addTrapToDoor(loc, trapType);
        InventoryChecker.useItem(player, trapItem);
        messageSender.sendPlayerMessage(player, "&c Você acaba instalando uma armadilha explosiva na tranca");
    }

    @Override
    public void smithingTableHandler(Player player, ItemStack item) {

    }

    @Override
    public void activate(Player player, Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_CREEPER_PRIMED, 0.1f, 2.0f);
    }

    @Override
    public void lastActivate(Player player, Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 2.0f);
        player.damage(15.0);
        SaveDoor saveDoor = new SaveDoor();
        saveDoor.removeTrapFromDoor(loc);
    }
}
