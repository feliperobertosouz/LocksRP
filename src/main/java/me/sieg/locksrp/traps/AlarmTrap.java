package me.sieg.locksrp.traps;

import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.utils.InventoryChecker;
import me.sieg.locksrp.utils.MessageSender;
import me.sieg.locksrp.utils.SaveDoor;
import me.sieg.locksrp.utils.SoundPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AlarmTrap extends SuperTrap{

    @Override
    public void install(PlayerInteractEvent event,Player player, Location loc, ItemStack trapItem) {
        MessageSender messageSender = new MessageSender();
        super.install(event, player, loc, trapItem);
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
        super.decrementUses(player,loc);
        SoundPlayer soundPlayer = new SoundPlayer();
        soundPlayer.playSoundRepeatedly(loc, Sound.BLOCK_BELL_USE, 7,30, 11.0f, 2.0f);
    }


}
