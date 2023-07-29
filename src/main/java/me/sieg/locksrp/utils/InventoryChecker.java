package me.sieg.locksrp.utils;

import me.sieg.locksrp.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class InventoryChecker {

    public static boolean hasCorrectKey(Player player, String keyCode) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if(NameSpacedKeys.isKey(meta)){
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
                if(NameSpacedKeys.isLockPick(meta)){
                    return true;
                }
            }
        }

        return false;
    }


    private static boolean hasLockPickTag(ItemStack item) {
        NamespacedKey lockPickKey = new NamespacedKey(Main.getPlugin(), "isLockPick");
        if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(lockPickKey, PersistentDataType.STRING)) {
            String isLockPick = item.getItemMeta().getPersistentDataContainer().get(lockPickKey, PersistentDataType.STRING);
            return isLockPick != null && isLockPick.equals("true");
        }
        return false;
    }
    public static void removeLockPick(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.getType() == Material.STICK) {
                if (hasLockPickTag(item)) {
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
