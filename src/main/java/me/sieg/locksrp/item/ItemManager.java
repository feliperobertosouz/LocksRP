package me.sieg.locksrp.item;

import me.sieg.locksrp.Main;
import me.sieg.locksrp.traps.TrapType;
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

public class ItemManager {

    protected int customModelDataLockPick = 9940;

    protected int customModelDataLockRemover = 9940;

    public static Boolean hasKeyCode(ItemMeta meta){
        if(meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "keyCode"), PersistentDataType.STRING)){
            return true;
        }
        return false;
    }

    public static String getKeyCode(ItemMeta meta){
        if(ItemManager.isLock(meta)){
            String lockCode = NameSpacedKeys.getNameSpacedKey(meta, "keyCode");
            return lockCode;
        }

        return null;
    }

    public static String getKeyCode(ItemStack item){
        ItemMeta meta = item.getItemMeta();

        if(meta != null && ItemManager.isLock(meta)){
            String lockCode = NameSpacedKeys.getNameSpacedKey(meta, "keyCode");
            return lockCode;
        }

        return null;
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


    public ItemStack getKeyItem(int amount){
        ItemStack chave = KeyFactory.createKey(amount);
        return chave;
    }

    public ItemStack getKeyItem(int amount, int customModel){
        ItemStack chave = KeyFactory.createKey(amount, customModel);
        return chave;
    }

    public ItemStack getUniversalKey(){
        ItemStack chave = KeyFactory.createUniversalKey();
        return chave;
    }

    public ItemStack getLock(int level,int customModel, int amount){
        ItemStack tranca = LockFactory.createLock(level, customModel, amount);

        return tranca;
    }

    public ItemStack generateKey(String code){
        ItemStack chave = KeyFactory.generateKey(code);
        return chave;

    }

    public ItemStack generateKey(ItemStack item, String code){
        ItemStack chave = KeyFactory.generateKey(item, code);
        return chave;

    }


    public ItemStack generateLock(int level, String code){
        ItemStack tranca = LockFactory.generateLock(level, code);
        return tranca;
    }

    public ItemStack generateLock(int level, String code, int amount){
        ItemStack tranca = LockFactory.generateLock(level, code, amount);
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

    public ItemStack getAlarmTrap(){
        ItemStack alarmTrap = TrapFactory.getAlarmTrap();
        return alarmTrap;
    }

    public ItemStack getTrap(TrapType trapType){
        ItemStack trap = TrapFactory.createTrap(trapType);
        return trap;
    }

    public static Boolean isTrap(ItemMeta meta){
        if(meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "isTrap"),
                PersistentDataType.STRING) ||
                meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "isTrap"),
                        PersistentDataType.BYTE)){
            return true;
        }
        return false;
    }

    public static ItemStack setOwner(ItemStack item, String owner){
        ItemMeta meta = item.getItemMeta();
        meta = NameSpacedKeys.setNameSpacedKey(meta, "owner", owner);
        item.setItemMeta(meta);
        return item;

    }

    public static String getOwner(ItemMeta meta){
        String owner = NameSpacedKeys.getNameSpacedKey(meta, "owner");
        return owner;
    }

    public static String getTrapType(ItemMeta meta){
        if(ItemManager.isTrap(meta)){
            String trapType = NameSpacedKeys.getNameSpacedKey(meta, "trapType");
            return trapType;
        }
        return null;
    }

}
