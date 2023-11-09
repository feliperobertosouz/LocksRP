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


    public static ItemMeta setNameSpacedKeyInt(ItemMeta meta, String tag, int value){

        ItemMeta metaInside = meta;
        metaInside.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), tag), PersistentDataType.INTEGER, value);


        return metaInside;
    }




}
