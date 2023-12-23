package me.sieg.locksrp.keyChain;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KeyChainMenu implements InventoryHolder {
    private Inventory inventory;

    public void initMenu(Player player, int size, List<ItemStack> keys) {
        inventory = Bukkit.createInventory(this, size, "KeyChain");

        // Adicione os itens da lista keys ao inventário somente se a lista não estiver vazia
        if (! (keys == null)){
            ItemStack[] keyArray = new ItemStack[size];
            for (int i = 0; i < keys.size() && i < size; i++) {
                keyArray[i] = keys.get(i);
            }
            inventory.setContents(keyArray);
        }

        player.openInventory(inventory);
    }

    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                items.add(item);
            }
        }
        return items;
    }

    // Método para remover um item do KeyChainMenu com base no índice
    public void remove(int index) {
        if (index >= 0 && index < inventory.getSize()) {
            inventory.setItem(index, null);
        }
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
