package me.sieg.locksrp.traps;

import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.utils.InventoryChecker;
import me.sieg.locksrp.utils.MessageSender;
import me.sieg.locksrp.utils.SaveDoor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpectralTrap implements Trap {
    @Override
    public void install(PlayerInteractEvent event, Player player, Location loc, ItemStack trapItem) {
        MessageSender messageSender = new MessageSender();
        SaveDoor saveDoor = new SaveDoor();
        String trapType = ItemManager.getTrapType(trapItem.getItemMeta());
        saveDoor.addTrapToDoor(loc, trapType);
        InventoryChecker.useItem(player, trapItem);
        messageSender.sendPlayerMessage(player, "&c Você acaba instalando uma armadilha espectral na tranca");
    }

    @Override
    public void smithingTableHandler(Player player, ItemStack item) {

    }

    @Override
    public void activate(Player player, Location loc) {
        MessageSender messageSender = new MessageSender();
        messageSender.sendPlayerMessage(player,"&c Você acaba se espetando em algo");
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5*10,0));
    }

    @Override
    public void lastActivate(Player player, Location loc) {
        MessageSender messageSender = new MessageSender();
        messageSender.sendPlayerMessage(player,"&c Você acaba ativando o mecanismo espectral");
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5*80, 0));
    }
}
