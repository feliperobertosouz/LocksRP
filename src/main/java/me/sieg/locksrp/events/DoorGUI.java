package me.sieg.locksrp.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class DoorGUI {


    public void openCustomGUI(Player player){
        Inventory menu = Bukkit.createInventory(player, 9, "lockPick Minigame");
        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glassPane.getItemMeta();
        glassMeta.setDisplayName(" ");
        glassPane.setItemMeta(glassMeta);

        for (int i = 3; i < 9; i++) {
            menu.setItem(i, glassPane);
        }

        player.openInventory(menu);
    }
}
