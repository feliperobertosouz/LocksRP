package me.sieg.locksrp.utils;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MessageSender {
    private static String prefix = ChatColor.translateAlternateColorCodes('&', "&8[&6LocksRP&8] &7");


    private static String chatColor(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void  sendPlayerMessage(Player player, String message){
        player.sendMessage(prefix + chatColor(message));
    }

    public void sendPlayerMessage(Player player, String message, Sound sound){
        player.sendMessage(prefix + chatColor(message));
        player.playSound(player, sound, 1, 1);
    }

    public void sendPlayerMessage(Player player, String message, Sound sound, float volume, float pitch){
        player.sendMessage(prefix + chatColor(message));
        player.playSound(player, sound, volume, pitch);
    }
}
