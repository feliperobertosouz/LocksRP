package me.sieg.locksrp.events;

import me.sieg.locksrp.utils.ChestKeeper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class DoorGUI {

    public ChestKeeper chests;
    public DoorGUI(){

    }

    public DoorGUI(ChestKeeper chests){
        this.chests = chests;
    }

    public void openCustomGUI(Player player){
        Inventory menu = Bukkit.createInventory(player, 9, "Door menu ");
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
