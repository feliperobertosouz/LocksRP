package me.sieg.locksrp.traps;

import me.sieg.locksrp.interactions.ContainerInteraction;
import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.item.TrapFactory;
import me.sieg.locksrp.utils.InventoryChecker;
import me.sieg.locksrp.utils.MessageSender;
import me.sieg.locksrp.utils.SaveDoor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SuperTrap implements Trap{


    public SuperTrap() {

    }

    @Override
    public ItemStack getTrapItem(ItemStack item) {
        return item;
    }

    @Override
    public void install(PlayerInteractEvent event, Player player, Location loc, ItemStack trapItem) {
        SaveDoor saveDoor = new SaveDoor();
        String trapType = ItemManager.getTrapType(trapItem.getItemMeta());
        saveDoor.addTrapToDoor(loc, trapType);
        int uses = ItemManager.getUses(trapItem.getItemMeta());
        saveDoor.setUses(loc,uses);
        InventoryChecker.useItem(player, trapItem);
    }

    @Override
    public void smithingTableHandler(Player player, ItemStack item) {

    }

    @Override
    public void activate(Player player, Location loc) {

    }

    @Override
    public void lastActivate(Player player, Location loc) {

    }

    @Override
    public void removeTrap(Player player, Location loc) {
        SaveDoor saveDoor = new SaveDoor();
        int lastUses = saveDoor.getUses(loc);
        String trapTypeString = saveDoor.getTrap(loc);
        TrapType type = TrapType.valueOf(trapTypeString);
        ItemStack drop = TrapFactory.createTrap(type,lastUses);
        saveDoor.dropItemOnGround(loc, drop);
    }

    public void decrementUses(Player player, Location loc){
        SaveDoor saveDoor = new SaveDoor();
        if(SaveDoor.isValidDoorBlock(loc.getBlock())){
            saveDoor.decrementUses(loc);
        }else if(SaveDoor.isValidContainer(loc.getBlock())){
            Inventory containerInventory = SaveDoor.getInventoryFromClickedBlock(loc.getBlock());
            ItemStack trapItem = containerInventory.getItem(1);
            if(ItemManager.isTrap(trapItem.getItemMeta())){
                int uses = ItemManager.getUses(trapItem.getItemMeta());
                if( uses != -1){
                    trapItem = ItemManager.decrementUses(trapItem);
                    if(trapItem.hasItemMeta() || ItemManager.isTrap(trapItem.getItemMeta())){
                        String trapTypeString = ItemManager.getTrapType(trapItem.getItemMeta());
                        TrapType type = TrapType.valueOf(trapTypeString);
                        uses = ItemManager.getUses(trapItem.getItemMeta());
                        String newLine = ChatColor.GRAY + "USES: " + uses + "/" + type.getMaxUses();
                        trapItem = ItemManager.updateLastLoreLine(trapItem,newLine);
                        containerInventory.setItem(1,trapItem);
                    }else{
                        containerInventory.setItem(1,trapItem);
                        MessageSender.sendPlayerMessage(player,"&c Você acaba quebrando a armadilha");
                    }
                }
            }
        }
        }
}
