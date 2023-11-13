package me.sieg.locksrp.traps;

import me.sieg.locksrp.utils.MessageSender;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class AlarmTrap implements Trap{
    @Override
    public void install(Player player, Location loc) {

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
