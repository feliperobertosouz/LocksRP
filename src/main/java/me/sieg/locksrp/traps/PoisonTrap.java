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

public class PoisonTrap extends SuperTrap{

    @Override
    public void activate(Player player, Location loc) {

    }

    @Override
    public void lastActivate(Player player, Location loc) {
        MessageSender messageSender = new MessageSender();
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*40, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*10 , 1));
        messageSender.sendPlayerMessage(player,"&c Você acaba ativando o mecanismo de veneno");
        super.decrementUses(player,loc);
    }


}
