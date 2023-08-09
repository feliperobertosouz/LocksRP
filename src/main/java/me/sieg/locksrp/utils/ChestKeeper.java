package me.sieg.locksrp.utils;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class ChestKeeper {
        private static Map<UUID, Block> lastClickedChests = new HashMap<>();


        public static void setLastClickedChest(UUID playerId, Block chestBlock) {
            lastClickedChests.put(playerId, chestBlock);
        }

        public static Block getLastClickedChest(UUID playerId) {
            return lastClickedChests.get(playerId);
        }

        public static void removeLastClickedChest(UUID playerId) {
            lastClickedChests.remove(playerId);
        }

    public static void setLastClickedDoor(UUID playerId, Block doorBlock) {
        lastClickedChests.put(playerId, doorBlock);
    }


        public static List<Block> getAllChests() {
            return new ArrayList<>(lastClickedChests.values());
        }
    }

