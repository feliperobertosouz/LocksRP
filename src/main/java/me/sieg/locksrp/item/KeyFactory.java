package me.sieg.locksrp.item;

import me.sieg.locksrp.utils.NameSpacedKeys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class KeyFactory {
    private static final int DEFAULT_CUSTOM_MODEL_DATA = 9999;; // Substitua pelo valor desejado
    private static final int UNIVERSALKEY_CUSTOM_MODEL_DATA = 9998; // Substitua pelo valor desejado
    public static ItemStack createKey(int amount) {
        return createKey(amount, DEFAULT_CUSTOM_MODEL_DATA);
    }

    public static ItemStack createKey(int amount, int customModelData) {
        ItemStack key = new ItemStack(Material.NAME_TAG, amount);
        ItemMeta meta = key.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + "Key");

            // Adicione outras configurações da chave conforme necessário
            meta = NameSpacedKeys.setNameSpacedKey(meta, "isKey", "true");
            meta.setCustomModelData(customModelData);

            key.setItemMeta(meta);
        }

        return key;
    }

    public static ItemStack generateKey(String code) {
        ItemStack key = createKey(1);
        ItemMeta meta = key.getItemMeta();

        NameSpacedKeys.setNameSpacedKey(meta, "keyCode", code);
        meta.setLore(Collections.singletonList(ChatColor.WHITE + "Key: " + ChatColor.DARK_PURPLE + code));
        key.setItemMeta(meta);
        return key;
    }

    public static ItemStack generateKey(ItemStack item, String code) {
        ItemStack key = item.clone(); // Clone para evitar modificar a instância original
        ItemMeta meta = key.getItemMeta();

        NameSpacedKeys.setNameSpacedKey(meta, "keyCode", code);
        meta.setLore(Collections.singletonList(ChatColor.WHITE + "Key: " + ChatColor.DARK_PURPLE + code));
        key.setItemMeta(meta);
        return key;
    }

    public static ItemStack createUniversalKey(){
        ItemStack chave = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta meta = chave.getItemMeta();

        meta.setDisplayName(ChatColor.WHITE + "Universal Key");

        meta = NameSpacedKeys.setNameSpacedKey(meta,"isUniversalKey","true");
        meta.setCustomModelData(UNIVERSALKEY_CUSTOM_MODEL_DATA);
        chave.setItemMeta(meta);
        return chave;
    }

}
