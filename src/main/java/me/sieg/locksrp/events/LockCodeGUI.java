package me.sieg.locksrp.events;

import me.sieg.locksrp.utils.ChestKeeper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class LockCodeGUI implements Listener {

    public ChestKeeper chests;

    public LockCodeGUI(){

    }

    public LockCodeGUI(ChestKeeper chests){
        this.chests = chests;
    }

    public void openCodeLockGUI(Player player){
        Inventory menu = Bukkit.createInventory(player, 9, "code");
        player.openInventory(menu);


    }

    @EventHandler
    public void onInventoryClickLockGUI(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // Verifique se o inventário clicado é o menu específico
        if (event.getView().getTitle().contains("code")) {
        }
        }
}
