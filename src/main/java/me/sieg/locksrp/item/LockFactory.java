package me.sieg.locksrp.item;

import me.sieg.locksrp.Main;
import me.sieg.locksrp.utils.NameSpacedKeys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LockFactory {

        public static ItemStack generateLock(int level, String code) {
            int customModel = getCustomModelByLevel(level);

            ItemStack lock = new ItemStack(Material.FLINT);
            ItemMeta meta = lock.getItemMeta();

            meta.setDisplayName(ChatColor.GOLD + "lock");
            meta = NameSpacedKeys.setNameSpacedKey(meta, "isLock", "true");
            meta = NameSpacedKeys.setNameSpacedKey(meta, "bindable", "true");
            meta = NameSpacedKeys.setNameSpacedKey(meta, "level", String.valueOf(level));
            meta = NameSpacedKeys.setNameSpacedKey(meta, "keyCode", code);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.WHITE + "Level: " + ChatColor.GOLD + level);
            lore.add(ChatColor.WHITE + "code: " + ChatColor.DARK_PURPLE + code);
            meta.setLore(lore);
            meta.setCustomModelData(customModel);

            lock.setItemMeta(meta);
            return lock;
        }

        public static ItemStack generateLock(int level, String code, int amount) {
            int customModel = getCustomModelByLevel(level);

            ItemStack lock = new ItemStack(Material.FLINT, amount);
            ItemMeta meta = lock.getItemMeta();

            meta.setDisplayName(ChatColor.GOLD + "lock");
            meta = NameSpacedKeys.setNameSpacedKey(meta, "isLock", "true");
            meta = NameSpacedKeys.setNameSpacedKey(meta, "bindable", "true");
            meta = NameSpacedKeys.setNameSpacedKey(meta, "level", String.valueOf(level));
            meta = NameSpacedKeys.setNameSpacedKey(meta, "keyCode", code);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.WHITE + "Level: " + ChatColor.GOLD + level);
            lore.add(ChatColor.WHITE + "code: " + ChatColor.DARK_PURPLE + code);
            meta.setLore(lore);
            meta.setCustomModelData(customModel);

            lock.setItemMeta(meta);
            return lock;
        }

    public static ItemStack createLock(int level,int amount){

        ItemStack tranca = new ItemStack((Material.FLINT), amount);
        ItemMeta meta = tranca.getItemMeta();
        int customModelData = getCustomModelByLevel(level);
        meta.setCustomModelData(customModelData);
        meta.setDisplayName(ChatColor.GOLD + "Lock");

        meta = NameSpacedKeys.setNameSpacedKey(meta, "isLock", "true");
        meta = NameSpacedKeys.setNameSpacedKey(meta, "bindable", "true");
        meta = NameSpacedKeys.setNameSpacedKey(meta, "level", String.valueOf(level));

        meta.setLore(Collections.singletonList(ChatColor.WHITE + "Level: " + ChatColor.GOLD + level));


        tranca.setItemMeta(meta);

        return tranca;
    }
    public static ItemStack createLock(int level,int customModel, int amount){

        ItemStack tranca = new ItemStack((Material.FLINT), amount);
        ItemMeta meta = tranca.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + "Lock");

        meta = NameSpacedKeys.setNameSpacedKey(meta, "isLock", "true");
        meta = NameSpacedKeys.setNameSpacedKey(meta, "bindable", "true");
        meta = NameSpacedKeys.setNameSpacedKey(meta, "level", String.valueOf(level));

        meta.setLore(Collections.singletonList(ChatColor.WHITE + "Level: " + ChatColor.GOLD + level));

        meta.setCustomModelData(customModel);
        tranca.setItemMeta(meta);

        return tranca;
    }


        private static int getCustomModelByLevel(int level) {
            switch (level) {
                case 1:
                    return 9999;
                case 2:
                    return 9998;
                case 3:
                    return 9997;
                case 4:
                    return 9996;
                case 5:
                    return 9995;
                case 6:
                    return 9994;
                default:
                    return 9999;
            }
        }

    public static int getLockLevel(ItemMeta meta){
        if(ItemManager.isLock(meta)){
            Integer level = Integer.valueOf(NameSpacedKeys.getNameSpacedKey(meta, "level"));
            return level;
        }
        return 0;
    }

}
