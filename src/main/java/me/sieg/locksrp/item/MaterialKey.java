package me.sieg.locksrp.item;

import org.bukkit.Material;

public enum MaterialKey {

    AMETIST(Material.AMETHYST_SHARD, 9996),
    COAL(Material.COAL, 9995),
    DIAMOND(Material.DIAMOND, 9994),
    NETHERITE(Material.NETHERITE_SCRAP, 9993),
    EMERALD(Material.EMERALD, 9992),
    GOLD(Material.GOLD_INGOT, 9991),
    IRON(Material.IRON_INGOT, 9990),
    LAPIS(Material.LAPIS_LAZULI, 9989);


    private final Material material;
    private final int customModelData;

    MaterialKey(Material material, int customModelData) {
        this.material = material;
        this.customModelData = customModelData;
    }

    public Material getMaterial() {
        return material;
    }

    public int getCustomModelData() {
        return customModelData;
    }
}
