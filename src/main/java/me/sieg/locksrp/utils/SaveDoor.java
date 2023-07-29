package me.sieg.locksrp.utils;

import me.sieg.locksrp.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
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
                // Fences
                Material.ACACIA_FENCE_GATE,
                Material.BIRCH_FENCE_GATE,
                Material.DARK_OAK_FENCE_GATE,
                Material.JUNGLE_FENCE_GATE,
                Material.OAK_FENCE_GATE,
                Material.SPRUCE_FENCE_GATE,
                Material.CRIMSON_FENCE_GATE,
                Material.WARPED_FENCE_GATE
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
}
