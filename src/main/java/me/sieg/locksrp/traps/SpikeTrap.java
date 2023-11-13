package me.sieg.locksrp.traps;

import me.sieg.locksrp.utils.MessageSender;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SpikeTrap implements Trap{


    @Override
    public void activate(Player player, Location loc) {
        MessageSender messageSender = new MessageSender();
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1.0f, 1.0f);
        player.damage(8.0);
        messageSender.sendPlayerMessage(player,"&c Você se machuca com os espinhos no meio da tranca");
    }

    @Override
    public void lastActivate(Player player, Location loc) {
        MessageSender messageSender = new MessageSender();
        player.damage(10.0);
        messageSender.sendPlayerMessage(player,"&c Você acaba ativando o mecanismo de espinhos");
    }
}
