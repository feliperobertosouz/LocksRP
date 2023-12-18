package me.sieg.locksrp.item;

import me.sieg.locksrp.Main;
import me.sieg.locksrp.utils.NameSpacedKeys;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.sieg.locksrp.item.KeyFactory.generateKey;

public class KeyChainFactory {


    public static ItemStack createKeychainFromInfo(List<String> keyCodes, List<String> keyNames, List<Integer> customModelDatas, int size) {
        ItemStack keychain = new ItemStack(Material.NAME_TAG);
        ItemMeta meta = keychain.getItemMeta();

        // Configura o persistentDataContainer para armazenar as informações das chaves
        NamespacedKey namesKey = new NamespacedKey(Main.getPlugin(), "keyNames");
        NamespacedKey codesKey = new NamespacedKey(Main.getPlugin(), "keyCodes");
        NamespacedKey modelsKey = new NamespacedKey(Main.getPlugin(), "keyModels");
        NamespacedKey sizeKeyChain = new NamespacedKey(Main.getPlugin(), "size");
        meta = NameSpacedKeys.setNameSpacedKey(meta, "isKeyChain", "true");

        // Convertendo as listas para strings
        String keyCodesString = String.join(",", keyCodes);
        String keyNamesString = String.join(",", keyNames);
        List<String> customModelDatasStrings = customModelDatas.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());

        String customModelDatasString = String.join(",", customModelDatasStrings);

        // Configurando o persistentDataContainer
        meta.getPersistentDataContainer().set(codesKey, PersistentDataType.STRING, keyCodesString);
        meta.getPersistentDataContainer().set(namesKey, PersistentDataType.STRING, keyNamesString);
        meta.getPersistentDataContainer().set(modelsKey, PersistentDataType.STRING, customModelDatasString);
        meta.getPersistentDataContainer().set(sizeKeyChain, PersistentDataType.INTEGER, size);

        // Adiciona a lore com os códigos
        List<String> lore = new ArrayList<>();
        lore.add("KeyCodes:");
        lore.addAll(keyCodes);
        meta.setLore(lore);

        keychain.setItemMeta(meta);

        return keychain;
    }

    public static List<String> getKeyCodesFromKeyChain(ItemStack item) {
        List<String> keyCodes = new ArrayList<>();

        if (ItemManager.isKeyChain(item.getItemMeta())) {
            ItemMeta meta = item.getItemMeta();
            NamespacedKey key = new NamespacedKey(Main.getPlugin(), "keyCodes");

            if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                String keyInfoString = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

                // Divida a string nos delimitadores e adicione à lista
                String[] keyInfoArray = keyInfoString.split(",");
                keyCodes.addAll(Arrays.asList(keyInfoArray));
            }
        }

        return keyCodes;
    }

    public static List<String> getKeyNamesFromKeyChain(ItemStack item) {
        List<String> keyNames = new ArrayList<>();

        if (ItemManager.isKeyChain(item.getItemMeta())) {
            ItemMeta meta = item.getItemMeta();
            NamespacedKey key = new NamespacedKey(Main.getPlugin(), "keyNames");

            if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                String keyInfoString = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

                // Divida a string nos delimitadores e adicione à lista
                String[] keyInfoArray = keyInfoString.split(",");
                keyNames.addAll(Arrays.asList(keyInfoArray));
            }
        }

        return keyNames;
    }
    public static List<String> getKeyModelsFromKeyChain(ItemStack item) {
        List<String> keyModels = new ArrayList<>();

        if (ItemManager.isKeyChain(item.getItemMeta())) {
            ItemMeta meta = item.getItemMeta();
            NamespacedKey key = new NamespacedKey(Main.getPlugin(), "keyModels");

            if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                String keyInfoString = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

                // Divida a string nos delimitadores e adicione à lista
                String[] keyInfoArray = keyInfoString.split(",");
                keyModels.addAll(Arrays.asList(keyInfoArray));
            }
        }

        return keyModels;
    }


    public static List<ItemStack> generateKeysFromKeyChain(ItemStack keyChain) {
        System.out.println("Usando generateKeysFromKeyChain");
        List<ItemStack> keys = new ArrayList<>();

        if (ItemManager.isKeyChain(keyChain.getItemMeta())) {
            List<String> keyCodes = getKeyCodesFromKeyChain(keyChain);
            List<String> keyNames = getKeyNamesFromKeyChain(keyChain);
            List<String> keyModels = getKeyModelsFromKeyChain(keyChain);

            // Certifique-se de que todas as listas têm o mesmo tamanho
            int size = Math.min(keyCodes.size(), Math.min(keyNames.size(), keyModels.size()));

            for (int i = 0; i < size; i++) {
                String keyCode = keyCodes.get(i);
                String keyName = keyNames.get(i);

                // Converta a String para int
                int keyModel = Integer.parseInt(keyModels.get(i));

                // Use o método generateKey para criar um ItemStack para cada chave
                ItemStack keyItem = KeyFactory.generateKey(keyCode, keyModel, keyName);
                System.out.println(keyItem.getItemMeta().toString());
                keys.add(keyItem);
            }
        }

        return keys;
    }


    public static int getKeyChainSize(ItemStack keyChain) {
        ItemMeta meta = keyChain.getItemMeta();

        if (meta != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey sizeKeyChain = new NamespacedKey(Main.getPlugin(), "size");

            if (container.has(sizeKeyChain, PersistentDataType.INTEGER)) {
                return container.get(sizeKeyChain, PersistentDataType.INTEGER);
            }
        }

        return 0; // Valor padrão ou indicação de erro, dependendo da sua lógica
    }


}
