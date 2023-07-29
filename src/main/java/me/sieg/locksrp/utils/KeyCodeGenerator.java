package me.sieg.locksrp.utils;

import java.util.Random;

public class KeyCodeGenerator {

    public static String generateUniqueCode() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 24; i++) {
            sb.append(generateRandomChar());
            if ((i + 1) % 4 == 0 && i < 24) {
                sb.append(".");
            }
        }

        return sb.toString();
    }

    private static char generateRandomChar() {
        String characters = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789%#@!*";
        Random random = new Random();
        int index = random.nextInt(characters.length());
        return characters.charAt(index);
    }

}
