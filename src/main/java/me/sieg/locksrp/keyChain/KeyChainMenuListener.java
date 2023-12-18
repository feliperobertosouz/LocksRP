package me.sieg.locksrp.keyChain;

import me.sieg.locksrp.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KeyChainMenuListener implements Listener {


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof KeyChainMenu) {
            // Cancela o evento para evitar que o jogador mova itens no KeyChainMenu
            event.setCancelled(true);

            // Agendar uma tarefa para obter a lista de ItemStacks após 1 tick
            Player player = (Player) event.getWhoClicked();
            Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
                List<ItemStack> itemsInMenu = getItemsInMenu(player);
                // Faça o que quiser com a lista de ItemStacks
                player.sendMessage("Itens no KeyChainMenu: " + itemsInMenu);
            }, 1);
        }
    }

    // Método para obter a lista de ItemStacks no KeyChainMenu
    private List<ItemStack> getItemsInMenu(Player player) {
        List<ItemStack> items = new ArrayList<>();
        Inventory keyChainMenu = player.getOpenInventory().getTopInventory();

        // Itera sobre os itens no inventário e adiciona à lista
        for (ItemStack item : keyChainMenu.getContents()) {
            if (item != null && !item.getType().isAir()) {
                items.add(item.clone());
            }
        }

        return items;
    }
}
