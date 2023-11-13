package me.sieg.locksrp.traps;

import me.sieg.locksrp.utils.MessageSender;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonTrap implements Trap{
    @Override
    public void activate(Player player, Location loc) {

    }

    @Override
    public void lastActivate(Player player, Location loc) {
        MessageSender messageSender = new MessageSender();
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*5, 1));
        messageSender.sendPlayerMessage(player,"&c Você acaba ativando o mecanismo de veneno");
    }
}
