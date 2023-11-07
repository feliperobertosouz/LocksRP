package me.sieg.locksrp.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemManager {

    //Itemmanager class is responsible to create the item stacks of plugin
    protected int customModelDataKey = 9999;

    protected int customModelDataUniversalKey = 9998;

    protected int customModelDataLockPick = 9940;

    protected int customModelDataLockRemover = 9940;


    public ItemStack getKeyItem(int amount){
        ItemStack chave = new ItemStack(Material.NAME_TAG, amount);
        ItemMeta meta = chave.getItemMeta();

        meta.setDisplayName(ChatColor.WHITE + "Key");

        meta = NameSpacedKeys.setNameSpacedKey(meta,"isKey","true");
        meta.setCustomModelData(this.customModelDataKey);
        chave.setItemMeta(meta);
        return chave;
    }

    public ItemStack getKeyItem(int amount, int customModel){
        ItemStack chave = new ItemStack(Material.NAME_TAG, amount);
        ItemMeta meta = chave.getItemMeta();

        meta.setDisplayName(ChatColor.WHITE + "Key");

        meta = NameSpacedKeys.setNameSpacedKey(meta,"isKey","true");
        meta.setCustomModelData(customModel);
        chave.setItemMeta(meta);
        return chave;
    }

    public ItemStack getUniversalKey(){
        ItemStack chave = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta meta = chave.getItemMeta();

        meta.setDisplayName(ChatColor.WHITE + "Universal Key");

        meta = NameSpacedKeys.setNameSpacedKey(meta,"isUniversalKey","true");
        meta.setCustomModelData(this.customModelDataUniversalKey);
        chave.setItemMeta(meta);
        return chave;
    }

    public ItemStack getLock(int level,int customModel, int amount){

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

    public ItemStack generateKey(String code){
        ItemStack chave = getKeyItem(1);

        ItemMeta meta = chave.getItemMeta();

        NameSpacedKeys.setNameSpacedKey(meta,"keyCode",code);
        meta.setLore(Collections.singletonList(ChatColor.WHITE + "Key: " + ChatColor.DARK_PURPLE + code));
        chave.setItemMeta(meta);
        return chave;

    }

    public ItemStack generateKey(ItemStack item, String code){
        ItemStack chave = item;

        ItemMeta meta = chave.getItemMeta();

        NameSpacedKeys.setNameSpacedKey(meta,"keyCode",code);
        meta.setLore(Collections.singletonList(ChatColor.WHITE + "Key: " + ChatColor.DARK_PURPLE + code));
        chave.setItemMeta(meta);
        return chave;

    }




    public ItemStack generateLock(int level, String code){
        int customModel = 9999;
        if(level == 1){
            customModel = 9999;
        }else if(level == 2){
            customModel = 9998;
        }else if(level == 3){
            customModel = 9997;
        }else if(level == 4){
            customModel = 9996;
        }else if(level == 5){
            customModel = 9995;
        }else if(level == 6){
            customModel = 9994;
        }

        ItemStack tranca = new ItemStack((Material.FLINT));
        ItemMeta meta = tranca.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + "lock");
        meta = NameSpacedKeys.setNameSpacedKey(meta, "isLock", "true");
        meta = NameSpacedKeys.setNameSpacedKey(meta, "bindable", "true");
        meta = NameSpacedKeys.setNameSpacedKey(meta, "level", String.valueOf(level));
        meta = NameSpacedKeys.setNameSpacedKey(meta,"keyCode", code);

        List<String> newlore = new ArrayList<>();
        newlore.add(ChatColor.WHITE + "Level: " + ChatColor.GOLD + level);
        newlore.add(ChatColor.WHITE + "code:" + ChatColor.DARK_PURPLE + code);
        meta.setLore(newlore);
        meta.setCustomModelData(customModel);

        tranca.setItemMeta(meta);
        return tranca;
    }

    public ItemStack getLockPick(){
        ItemStack lockpick = new ItemStack((Material.STICK));

        ItemMeta meta = lockpick.getItemMeta();
        meta = NameSpacedKeys.setNameSpacedKey(meta, "isLockPick", "true");
        meta.setLore(Collections.singletonList(ChatColor.WHITE + "LockPick"));
        meta.setDisplayName(ChatColor.GOLD + "LockPick");

        meta.setCustomModelData(customModelDataLockPick);

        lockpick.setItemMeta(meta);
        return lockpick;
    }

    public ItemStack getLockRemover(){

        ItemStack lockRemover = new ItemStack(Material.IRON_HORSE_ARMOR);
        ItemMeta meta = lockRemover.getItemMeta();

        meta = NameSpacedKeys.setNameSpacedKey(meta,"isLockRemover", "true");
        meta.setDisplayName(ChatColor.GRAY + "LOCK REMOVER");
        List<String> newlore = new ArrayList<>();
        newlore.add(ChatColor.GOLD + "RIGHT CLICK TO REMOVE THE LOCK OF BLOCK");
        meta.setCustomModelData(this.customModelDataLockRemover);
        meta.setLore(newlore);
        lockRemover.setItemMeta(meta);

        return lockRemover;
    }
}
