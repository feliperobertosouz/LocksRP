package me.sieg.locksrp.events;

import me.sieg.locksrp.Main;
import me.sieg.locksrp.utils.ChestKeeper;
import me.sieg.locksrp.utils.InventoryChecker;
import me.sieg.locksrp.utils.NameSpacedKeys;
import me.sieg.locksrp.utils.SaveDoor;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class LockPickMinigame implements Listener {

    public ChestKeeper chests;

    public LockPickMinigame(){

    }

    public LockPickMinigame(ChestKeeper chests){
        this.chests = chests;
    }

    public void openCustomGUI(Player player, Integer level){
        Inventory menu = Bukkit.createInventory(player, 27, "lockPick Minigame");
        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glassPane.getItemMeta();
        glassMeta.setDisplayName(" ");
        glassPane.setItemMeta(glassMeta);
        ItemStack ironIngot = new ItemStack(Material.IRON_INGOT);
        ItemMeta ironMeta = ironIngot.getItemMeta();
        ironMeta.setDisplayName("PINO");
        ironIngot.setItemMeta(ironMeta);
        int minimo = 3;
        int quantidade = 9 + minimo + level;
        List<Integer> numeros = numerosAleatorios(level + minimo);

        for (int i = 0; i < 9; i++) {
            menu.setItem(i, glassPane);
        }


        int contador = 1;
        for(int i = 9; i < quantidade; i++){

            ironMeta = NameSpacedKeys.setNameSpacedKeyInt(ironMeta, "order", numeros.get(contador - 1));
            contador++;
            ironIngot.setItemMeta(ironMeta);
            menu.setItem(i,ironIngot);

        }
        ItemStack chances = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta chancesMeta = chances.getItemMeta();
        if(level == 1){
            level = 5;
        }
        chancesMeta.setDisplayName("" + (8 - level));
        chances.setItemMeta(chancesMeta);
        menu.setItem(0,chances);
        player.openInventory(menu);
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // Verifique se o inventário clicado é o menu específico
        if (event.getView().getTitle().equals("lockPick Minigame")) {
            ItemStack chancesItem = event.getInventory().getItem(0);
            ItemMeta chanceMeta = chancesItem.getItemMeta();
            Integer chances = Integer.parseInt(chanceMeta.getDisplayName());
            event.setCancelled(true); // Cancela o evento para evitar que o jogador interaja com o menu

            // Verifica se o jogador tem LockPicks no inventário
            if (!InventoryChecker.hasLockPick(player)) {
                player.closeInventory();
                return;
            }

            Integer level = countItemAmount(event.getInventory(), Material.IRON_INGOT);
            List<Integer> acertados = numerosSelecionados(level, event.getInventory());

            // Verifica se o jogador clicou em um slot válido
            if (event.getSlot() >= 9 && event.getSlot() <= 17 && event.getInventory().getItem(event.getSlot()) != null) {
                Integer order = NameSpacedKeys.getNameSpacedKeyInt(event.getCurrentItem().getItemMeta(), "order");
                if (order == 1 && acertados.isEmpty()) {
                    Integer slot = event.getSlot();
                    event.getInventory().setItem(slot + 9, event.getCurrentItem());
                    event.getInventory().setItem(slot, new ItemStack(Material.AIR));
                } else if (!acertados.isEmpty() && order == Collections.max(acertados) + 1) {
                    Integer slot = event.getSlot();
                    event.getInventory().setItem(slot + 9, event.getCurrentItem());
                    event.getInventory().setItem(slot, new ItemStack(Material.AIR));
                } else {
                    chances--;
                }

                if (chances < 1) {
                    player.closeInventory();
                }

                chanceMeta.setDisplayName("" + chances);
                chancesItem.setItemMeta(chanceMeta);
                event.getInventory().setItem(0, chancesItem);
                InventoryChecker.removeLockPick(player);
                if (!InventoryChecker.hasLockPick(player)) {
                    player.closeInventory();
                }

                if (areSlotsEmpty(event.getInventory())) {

                    Block block = ChestKeeper.getLastClickedChest(player.getUniqueId());
                    if (block != null && block.getState() instanceof Chest) {
                        Chest chest = (Chest) block.getState();
                        Inventory chestInventory = chest.getInventory();

                        // Abre o inventário do baú para o jogador
                        player.openInventory(chestInventory);
                    }else if(block!= null && SaveDoor.isDoor(block)){
                        SaveDoor saveDoor = new SaveDoor();
                        player.sendMessage("Você destranca a porta");
                        saveDoor.setDoorLocked(block.getLocation(),false);
                        player.closeInventory();
                    }else if(block != null && block.getState() instanceof Barrel){
                        Barrel barrel = (Barrel) block.getState();
                        Inventory barrelInventory = barrel.getInventory();
                        player.openInventory(barrelInventory);
                    }
                }
            }
        }
    }

    public static int mapearNumero(int numero) {
        if (numero >= 9 && numero <= 17) {
            return numero - 8;
        } else {
            throw new IllegalArgumentException("Número fora do intervalo de 9 a 17.");
        }
    }

    public List<Integer> numerosAleatorios(int level){
        List<Integer> numeros = new ArrayList<>();

        // Adiciona números de 1 a 9 na lista
        for (int i = 1; i <= level; i++) {
            numeros.add(i);
        }

        // Embaralha a lista aleatoriamente
         Collections.shuffle(numeros);

        return numeros;
    }


    public List<Integer> numerosSelecionados(int level, Inventory inventory){
        List<Integer> numeros = new ArrayList<>();

        for (int slot = 18; slot <= 26; slot++) {
            ItemStack item = inventory.getItem(slot);

            // Verifica se o slot tem um item com meta
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();

                // Verifica se o item tem um PersistentDataContainer chamado "order"
                if (meta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(),"order"), PersistentDataType.INTEGER)) {
                    if(meta.getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(),"order"), PersistentDataType.INTEGER) != null){
                        int orderValue = meta.getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(),"order"), PersistentDataType.INTEGER);
                        numeros.add(orderValue);
                    }
                }
            }
        }

        return numeros;
    }


    public static int countItemAmount(Inventory inventory, Material itemType) {
        int totalAmount = 0;

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == itemType) {
                totalAmount += item.getAmount();
            }
        }

        return totalAmount;
    }

    public boolean areSlotsEmpty(Inventory inventory) {
        for (int i = 9; i <= 17; i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && !item.getType().isAir()) {
                return false;
            }
        }
        return true;
    }
}
