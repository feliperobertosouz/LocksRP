package me.sieg.locksrp.utils;

import me.sieg.locksrp.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class SaveDoor {

//    File file = new File(Main.getPlugin().getDataFolder(), "doors.yml");
//    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    private FileConfiguration doorsConfig;
    private File doorsFile;

    public SaveDoor() {
        doorsFile = new File(Main.getPlugin().getDataFolder(), "doors.yml");
        doorsConfig = YamlConfiguration.loadConfiguration(doorsFile);
    }

    public void setLockCode(Block block, String lockCode) {
        Location location = block.getLocation();
        String key = locationToString(location);

        doorsConfig.set(key + ";TYPE=" + block.getType().name() + ".lock.code", lockCode);

        saveDoorsConfig();
    }

    public void setDoorLocked(Location location, boolean locked) {
        String key = locationToString(location);

        doorsConfig.set(key + ".locked", locked);

        saveDoorsConfig();
    }

    public void saveLocationToFile(Location location) {
        String key = locationToString(location);

        doorsConfig.set(key + ".x", location.getX());
        doorsConfig.set(key + ".y", location.getY());
        doorsConfig.set(key + ".z", location.getZ());
        doorsConfig.set(key + ".world", location.getWorld().getName());
        doorsConfig.set(key + ".type", location.getBlock().getType().toString().toLowerCase());
        doorsConfig.set(key + ".locked", "false");

        saveDoorsConfig();
    }

    private String locationToString(Location location) {
        return "X=" + location.getBlockX() + ";Y=" + location.getBlockY() + ";Z=" + location.getBlockZ();
    }

    private void saveDoorsConfig() {
        try {
            doorsConfig.save(doorsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLocationToFile(Location location, boolean locked, String lockCode, int lockLevel) {
        String key = locationToString(location);

        doorsConfig.set(key + ".x", location.getX());
        doorsConfig.set(key + ".y", location.getY());
        doorsConfig.set(key + ".z", location.getZ());
        doorsConfig.set(key + ".world", location.getWorld().getName());
        doorsConfig.set(key + ".type", location.getBlock().getType().toString());
        doorsConfig.set(key + ".locked", locked);

            doorsConfig.set(key + ".lock.code", lockCode.toString());
            doorsConfig.set(key + ".lock.level", lockLevel);


        saveDoorsConfig();
    }

    public boolean isDoorLocked(Location location) {
        String key = locationToString(location);

        if (doorsConfig.contains(key + ".locked")) {
            return doorsConfig.getBoolean(key + ".locked");
        }

        return false;
    }

    public boolean isLocationRegistered(Location location) {
        File file = new File(Main.getPlugin().getDataFolder(), "doors.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String key = locationToString(location);
        return config.contains(key);
    }

    public String getLockCode(Location location) {
        File file = new File(Main.getPlugin().getDataFolder(), "doors.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String key = locationToString(location);

        if (config.contains(key + ".lock.code")) {
            return config.getString(key + ".lock.code");
        }

        return null; // Caso não exista lock code registrado para a localização
    }

    public int getLockLevel(Location location) {
        File file = new File(Main.getPlugin().getDataFolder(), "doors.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String key = locationToString(location);

        if (config.contains(key + ".lock.level")) {
            return config.getInt(key + ".lock.level");
        }

        return 0; // Caso não exista level registrado para a localização
    }

    public void removeLocationFromFile(Location location) {
        String key = locationToString(location);
        if (doorsConfig.contains(key)) {
            doorsConfig.set(key, null); // Remove o registro da localização do arquivo
            saveDoorsConfig();
        }
    }
    public static boolean isDoor(Block block) {
        Material blockType = block.getType();

        // Lista de todos os tipos de blocos de porta, trapdoor e fence conhecidos no Minecraft
        Material[] doorTypes = {
                // Portas
                Material.ACACIA_DOOR,
                Material.BIRCH_DOOR,
                Material.DARK_OAK_DOOR,
                Material.JUNGLE_DOOR,
                Material.OAK_DOOR,
                Material.SPRUCE_DOOR,
                Material.CRIMSON_DOOR,
                Material.WARPED_DOOR,
                Material.MANGROVE_DOOR,
                Material.CHERRY_DOOR,
                Material.BAMBOO_DOOR,
                // Trapdoors
                Material.ACACIA_TRAPDOOR,
                Material.BIRCH_TRAPDOOR,
                Material.DARK_OAK_TRAPDOOR,
                Material.IRON_TRAPDOOR,
                Material.JUNGLE_TRAPDOOR,
                Material.OAK_TRAPDOOR,
                Material.SPRUCE_TRAPDOOR,
                Material.CRIMSON_TRAPDOOR,
                Material.WARPED_TRAPDOOR,
                Material.MANGROVE_TRAPDOOR,
                Material.CHERRY_TRAPDOOR,
                Material.BAMBOO_TRAPDOOR,
                // Fences
                Material.ACACIA_FENCE_GATE,
                Material.BIRCH_FENCE_GATE,
                Material.DARK_OAK_FENCE_GATE,
                Material.JUNGLE_FENCE_GATE,
                Material.OAK_FENCE_GATE,
                Material.SPRUCE_FENCE_GATE,
                Material.CRIMSON_FENCE_GATE,
                Material.WARPED_FENCE_GATE,
                Material.MANGROVE_FENCE_GATE,
                Material.CHERRY_FENCE_GATE,
                Material.BAMBOO_FENCE_GATE,
        };

        for (Material doorType : doorTypes) {
            if (blockType == doorType) {
                return true;
            }
        }

        return false;
    }

    public static Location getBlockBelow(Location location) {
        double x = location.getX();
        double y = location.getY() - 1;
        double z = location.getZ();
        return new Location(location.getWorld(), x, y, z);
    }

    public static void dropItemOnGround(Location location, ItemStack itemStack) {
        World world = location.getWorld();

        // Verifica se a localização é de bloco sólido, para evitar drops em locais inválidos
        if (location.getBlock().getType().isSolid()) {
            // Cria uma entidade de item na localização
            Item itemEntity = world.dropItem(location, itemStack);

            // Define o pickup delay do item para 0, para que possa ser pego imediatamente
            itemEntity.setPickupDelay(0);
        }
    }

    public static Boolean isValidContainer(Block clickedBlock){
        if(clickedBlock.getType() == Material.CHEST || clickedBlock.getType() == Material.BARREL || clickedBlock.getState() instanceof ShulkerBox){
            return true;
        }
        return false;
    }


    public static boolean isValidDoorBlock(Block block) {
        return block.getBlockData() instanceof Door || block.getBlockData() instanceof TrapDoor
                || block.getBlockData() instanceof Gate;
    }

    public static Inventory getInventoryFromClickedBlock(Block clickedBlock) {
        if (clickedBlock.getType() == Material.CHEST) {
            Chest chest = (Chest) clickedBlock.getState();
            return chest.getInventory();
        } else if (clickedBlock.getType() == Material.BARREL) {
            Barrel barrel = (Barrel) clickedBlock.getState();
            return barrel.getInventory();
        } else if (clickedBlock.getState() instanceof ShulkerBox) {
            ShulkerBox shulkerBox = (ShulkerBox) clickedBlock.getState();
            return shulkerBox.getInventory();
        }
        return null; // Retorna null se o bloco não for do tipo suportado
    }

    //ADDTRAP
    public void addTrapToDoor(Location location, String trapName) {
        String key = locationToString(location);

        // Verifica se a localização já está registrada
        if (doorsConfig.contains(key)) {
            ConfigurationSection doorSection = doorsConfig.getConfigurationSection(key);

            // Adiciona o item "trap" ao registro da porta
            doorSection.set("trap", trapName);

            // Salva as alterações no arquivo
            saveDoorsConfig();
        }
    }

    public void removeTrapFromDoor(Location location) {
        String key = locationToString(location);

        // Verifica se a localização está registrada
        if (doorsConfig.contains(key)) {
            ConfigurationSection doorSection = doorsConfig.getConfigurationSection(key);

            // Remove o item "trap" do registro da porta
            doorSection.set("trap", null);

            // Salva as alterações no arquivo
            saveDoorsConfig();
        }
    }
    public boolean hasTrap(Location location) {
        String key = locationToString(location);

        // Verifica se a localização está registrada e se há uma armadilha associada
        return doorsConfig.contains(key + ".trap");
    }

    public String getTrap(Location location) {
        String key = locationToString(location);

        // Verifica se a localização está registrada e se há uma armadilha associada
        if (doorsConfig.contains(key + ".trap")) {
            return doorsConfig.getString(key + ".trap");
        }

        // Retorna null se não houver armadilha associada
        return null;
    }

    public void removeDoor(Location location){
        removeLocationFromFile(location);
        if(hasTrap(location)){
            removeTrapFromDoor(location);
        }
    }

    public void setDoorOwner(Location location, String owner) {

        String key = locationToString(location);

        doorsConfig.set(key + ".owner", owner);

        saveDoorsConfig();
    }

    public String getDoorOwner(Location location) {
        String key = locationToString(location);

        if (doorsConfig.contains(key + ".owner")) {
            return doorsConfig.getString(key + ".owner");
        }

        return null; // Retorna null se não houver proprietário registrado para a localização
    }

    public void setUses(Location location, int uses) {
        String key = locationToString(location);

        doorsConfig.set(key + ".uses", uses);

        saveDoorsConfig();
    }

    public int getUses(Location location) {
        String key = locationToString(location);

        if (doorsConfig.contains(key + ".uses")) {
            return doorsConfig.getInt(key + ".uses");
        }

        System.out.println("Não tinha uses");
        setUses(location, 10);
        return 5;
    }

    public void decrementUses(Location location) {
        String key = locationToString(location);
        int uses = getUses(location);
        if (doorsConfig.contains(key + ".uses")) {
            uses = doorsConfig.getInt(key + ".uses");

            if (uses > 0 || uses == -1) {
                // Se uses for -1, a armadilha é inquebrável, então não decrementamos
                if (uses != -1) {
                    uses--;
                    if(uses <= 0){
                        removeTrapFromDoor(location);
                        doorsConfig.set(key + ".uses", null);
                    }
                    doorsConfig.set(key + ".uses", uses);
                    saveDoorsConfig();
                }
            }
        }else{
            System.out.println("Não tinha uses");
            setUses(location, 10);
        }
    }
}
