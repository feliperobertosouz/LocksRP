package me.sieg.locksrp.utils;

import me.sieg.locksrp.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class NameSpacedKeys {


    public static String getNameSpacedKey(ItemMeta meta, String tag){
        String response = null;

        if(meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), tag), PersistentDataType.STRING)){
            response = meta.getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(), tag), PersistentDataType.STRING);
        }
        return response;
    }

    public static Integer getNameSpacedKeyInt(ItemMeta meta, String tag){
        Integer response = null;

        if(meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), tag), PersistentDataType.INTEGER)){
            response = meta.getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(), tag), PersistentDataType.INTEGER);
        }
        return response;
    }

    public static ItemMeta setNameSpacedKey(ItemMeta meta, String tag, String value){

        ItemMeta metaInside = meta;
        metaInside.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), tag), PersistentDataType.STRING, value);


        return metaInside;
    }


    public static Boolean isKey(ItemMeta meta){

        if(
                meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "isKey"),
                PersistentDataType.STRING) ||
                        meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "isKey"),
                                PersistentDataType.BYTE)){
            return true;
        }
        return false;
    }

    public static  Boolean isLock(ItemMeta meta){

        if(meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "isLock"),
                PersistentDataType.STRING) ||
        meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "isLock"),
                PersistentDataType.BYTE)){
            return true;
        }
        return false;
    }

    public static  Boolean isUniversalKey(ItemMeta meta){

        if(meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "isUniversalKey"),
                PersistentDataType.STRING) ||
                meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "isUniversalKey"),
                        PersistentDataType.BYTE)){
            return true;
        }
        return false;
    }

    public static  Boolean isLockPick(ItemMeta meta){

        if(meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "isLockPick"),
                PersistentDataType.STRING) ||
                meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "isLockPick"),
                        PersistentDataType.BYTE)){
            return true;
        }
        return false;
    }

    public static Boolean isLockRemover(ItemMeta meta){
        if(meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "isLockRemover"),
                PersistentDataType.STRING) ||
                meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "isLockRemover"),
                        PersistentDataType.BYTE)){
            return true;
        }
        return false;
    }

    public static boolean isKeyChain(ItemMeta meta) {
        if (meta != null) {
            NamespacedKey key = new NamespacedKey(Main.getPlugin(), "isKeyChain");
            if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)
            ||
            meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "iskeyChain"),
                    PersistentDataType.BYTE)) {

                return true;
            }
        }
        return false;
    }

    public static Boolean hasKeyCode(ItemMeta meta){
        if(meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "keyCode"), PersistentDataType.STRING)){
            return true;
        }
        return false;
    }
    public static ItemMeta setNameSpacedKeyInt(ItemMeta meta, String tag, int value){

        ItemMeta metaInside = meta;
        metaInside.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), tag), PersistentDataType.INTEGER, value);


        return metaInside;
    }

}
