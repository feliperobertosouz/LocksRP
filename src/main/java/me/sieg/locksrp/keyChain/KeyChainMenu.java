package me.sieg.locksrp.keyChain;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KeyChainMenu implements InventoryHolder {
    private Inventory inventory;

    public void initMenu(Player player, int size, List<ItemStack> keys) {
        inventory = Bukkit.createInventory(this, size, "KeyChain");

        // Adicione os itens da lista keys ao inventário
        ItemStack[] keyArray = new ItemStack[size];
        for (int i = 0; i < keys.size() && i < size; i++) {
            keyArray[i] = keys.get(i);
        }
        inventory.setContents(keyArray);

        player.openInventory(inventory);
    }
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
