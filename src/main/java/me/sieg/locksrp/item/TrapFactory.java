package me.sieg.locksrp.item;

import me.sieg.locksrp.traps.TrapType;
import me.sieg.locksrp.utils.NameSpacedKeys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TrapFactory {

    public static ItemStack getTrap(){
        ItemStack trap = new ItemStack(Material.POPPED_CHORUS_FRUIT);
        ItemMeta meta = trap.getItemMeta();
        meta = NameSpacedKeys.setNameSpacedKey(meta,"isTrap", "true");
        trap.setItemMeta(meta);
        return trap;
    }

    public static ItemStack getAlarmTrap(){
       ItemStack alarmTrap = getTrap();
       ItemMeta meta = alarmTrap.getItemMeta();
        meta = NameSpacedKeys.setNameSpacedKey(meta, "trapType", TrapType.ALARM.getValue());
        meta.setDisplayName(ChatColor.GRAY + "ALARM TRAP");
        List<String> newlore = new ArrayList<>();
        newlore.add(ChatColor.GOLD + "RIGHT CLICK TO SET THE ALARM TRAP");
        meta.setLore(newlore);
        alarmTrap.setItemMeta(meta);
        return alarmTrap;
    }

    public static ItemStack createTrap(TrapType trapType){
        ItemStack trap = getTrap();
        ItemMeta meta = trap.getItemMeta();
        meta = NameSpacedKeys.setNameSpacedKey(meta, "trapType", trapType.getValue());
        meta.setDisplayName("Armadilha de " + trapType.getDisplayName());
        List<String> newlore = new ArrayList<>();
        newlore.add(ChatColor.GOLD + "RIGHT CLICK EM UMA PORTA OU 2º SLOT DO BAU PARA USAR A" + trapType.getValue() + " TRAP");
        meta = NameSpacedKeys.setNameSpacedKeyInt(meta,"maxUses", trapType.getMaxUses());
        meta = NameSpacedKeys.setNameSpacedKeyInt(meta, "uses", trapType.getMaxUses());
        if(trapType.getMaxUses() == -1) {
            newlore.add(ChatColor.GOLD + "USOS: " + ChatColor.RED + "INQUEBRAVEl");
        }else{
            newlore.add(ChatColor.GRAY + "USOS: " + ChatColor.WHITE + ItemManager.getUses(meta) + "/" + ItemManager.getMaxUses(meta));
        }
        meta.setLore(newlore);
        meta.setCustomModelData(trapType.customModelData);
        trap.setItemMeta(meta);
        return trap;
    }

    public static ItemStack createTrap(TrapType trapType, int uses){
        System.out.println("RODANDO O CREATE TRAP JA COM USOS");
        System.out.println("USOS: " + uses);
        ItemStack trap = getTrap();
        ItemMeta meta = trap.getItemMeta();
        meta = NameSpacedKeys.setNameSpacedKey(meta, "trapType", trapType.getValue());
        meta.setDisplayName("Armadilha de " + trapType.getDisplayName());
        List<String> newlore = new ArrayList<>();
        newlore.add(ChatColor.GOLD + "RIGHT CLICK EM UMA PORTA OU 2º SLOT DO BAU PARA USAR A" + trapType.getValue() + " TRAP");
        meta = NameSpacedKeys.setNameSpacedKeyInt(meta,"maxUses", trapType.getMaxUses());
        meta = NameSpacedKeys.setNameSpacedKeyInt(meta, "uses", uses);
        if(trapType.getMaxUses() == -1) {
            newlore.add(ChatColor.GOLD + "USOS: " + ChatColor.RED + "INQUEBRAVEl");
        }else{
            newlore.add(ChatColor.GRAY + "USOS: " + ChatColor.WHITE + uses + "/" + ItemManager.getMaxUses(meta));
        }
        meta.setLore(newlore);
        meta.setCustomModelData(trapType.customModelData);
        trap.setItemMeta(meta);

        System.out.println("USOS UPDATE: " +ItemManager.getUses(trap.getItemMeta()));
        return trap;
    }

}
