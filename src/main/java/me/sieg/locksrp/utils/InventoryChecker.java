package me.sieg.locksrp.utils;

import me.sieg.locksrp.Main;
import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.item.KeyFactory;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class InventoryChecker {
    //Inventory checker is a class responsible to check if player has correct items and remove itens
    public static boolean hasCorrectKey(Player player, String keyCode) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if(ItemManager.isKey(meta)){
                    NamespacedKey key = new NamespacedKey(Main.getPlugin(), "keyCode");

                    if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                        String itemKeyCode = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

                        if (itemKeyCode != null && itemKeyCode.equals(keyCode)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasLockPick(Player player){
        for(ItemStack item : player.getInventory().getContents()){
            if(item != null && item.hasItemMeta()){
                ItemMeta meta = item.getItemMeta();
                if(ItemManager.isLockPick(meta)){
                    return true;
                }
            }
        }

        return false;
    }
    public static boolean hasUniversalKey(Player player){
        for(ItemStack item : player.getInventory().getContents()){
            if(item != null && item.hasItemMeta()){
                ItemMeta meta = item.getItemMeta();
                if(ItemManager.isUniversalKey(meta)){
                    return true;
                }
            }
        }

        return false;
    }


    public static void removeLockPick(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.getType() == Material.STICK) {
                if (ItemManager.isLockPick(item.getItemMeta())) {
                    int amount = item.getAmount();
                    if (amount > 1) {
                        item.setAmount(amount - 1);
                        inventory.setItem(i, item);
                    } else {
                        inventory.setItem(i, null);
                    }
                    break;
                }
            }
        }
    }
}
