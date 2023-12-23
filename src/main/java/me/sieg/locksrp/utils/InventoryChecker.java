package me.sieg.locksrp.utils;

import me.sieg.locksrp.Main;
import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.item.KeyChainFactory;
import me.sieg.locksrp.item.KeyFactory;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static me.sieg.locksrp.item.ItemManager.isKeyChain;

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

    public static boolean hasCorrectCodeInKeyChain(Player player, String desiredCode) {


        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta() && isKeyChain(item.getItemMeta())) {
                List<String> keyCodes = KeyChainFactory.getKeyCodesFromKeyChain(item);

                if (keyCodes.contains(desiredCode)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasCorrectKeyOrKeyChain(Player player, String keyCode) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();

                // Verifica se é uma chave
                if (ItemManager.isKey(meta)) {
                    NamespacedKey key = new NamespacedKey(Main.getPlugin(), "keyCode");
                    if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                        String itemKeyCode = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

                        if (itemKeyCode != null && itemKeyCode.equals(keyCode)) {
                            return true;
                        }
                    }
                }

                // Verifica se é um chaveiro
                if (ItemManager.isKeyChain(meta)) {
                    List<String> keyCodes = KeyChainFactory.getKeyCodesFromKeyChain(item);
                    if (keyCodes.contains(keyCode)) {
                        return true;
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

    public static void removeLockPick(Player player, int amount) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();

        int remainingAmount = amount;

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.getType() == Material.STICK) {
                if (ItemManager.isLockPick(item.getItemMeta())) {
                    int itemAmount = item.getAmount();
                    if (itemAmount >= remainingAmount) {
                        item.setAmount(itemAmount - remainingAmount);
                        inventory.setItem(i, item);
                        break;
                    } else {
                        remainingAmount -= itemAmount;
                        inventory.setItem(i, null);
                    }
                }
            }
        }
    }

    public static void useItem(Player player, ItemStack item){
        item.setAmount(item.getAmount() - 1);

        if (item.getAmount() <= 0) {
            // Se a quantidade for menor ou igual a 0, remove o item da mão do jogador
            player.getInventory().setItemInMainHand(null);
        } else {
            // Atualiza o item na mão do jogador
            player.getInventory().setItemInMainHand(item);
        }
    }
}
