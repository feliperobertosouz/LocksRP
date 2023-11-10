package me.sieg.locksrp.item;

import me.sieg.locksrp.utils.NameSpacedKeys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TrapFactory {


    public static ItemStack getAlarmTrap(){
        ItemStack alarmTrap = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta meta = alarmTrap.getItemMeta();
        meta = NameSpacedKeys.setNameSpacedKey(meta,"isTrap", "true");
        meta = NameSpacedKeys.setNameSpacedKey(meta,"isAlarmTrap", "true");
        meta.setDisplayName(ChatColor.GRAY + "ALARM TRAP");
        List<String> newlore = new ArrayList<>();
        newlore.add(ChatColor.GOLD + "RIGHT CLICK TO SET THE ALARM TRAP");
        meta.setLore(newlore);
        alarmTrap.setItemMeta(meta);

        return alarmTrap;
    }

}
