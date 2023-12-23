package me.sieg.locksrp.commands;

import me.sieg.locksrp.traps.TrapType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocksRPTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> results = new ArrayList<>();
        Player player = (Player) commandSender;

        if (!player.hasPermission("locksrp.admin")) {
            return null;
        }

        if (args.length == 1) {
            // If the player has typed the first argument, filter commands based on input
            String input = args[0].toLowerCase();
            for (String commandLine: getAllCommands()) {
                if (commandLine.toLowerCase().startsWith(input)) {
                    results.add(commandLine);
                }
            }
        } else if (args.length == 2) {
            // If the player has typed the second argument, filter options based on the first argument
            String arg0 = args[0].toLowerCase();
            if (arg0.equals("getlock")) {
                String input = args[1].toLowerCase();
                for (String lock : getAllLocks()) {
                    if (lock.toLowerCase().startsWith(input)) {
                        results.add(lock);
                    }
                }
            } else if (arg0.equals("gettrap") || arg0.equals("getcustomtrap")) {
                String input = args[1].toLowerCase();
                for (String trap : getAllTraps()) {
                    if (trap.toLowerCase().startsWith(input)) {
                        results.add(trap);
                    }
                }
            }
        }

        return results;
    }

    private List<String> getAllCommands() {
        // Return a list of all possible commands
        return Arrays.asList("getkey", "getlock", "getlockpick", "getdata", "getlockremover", "getkeyoflock",
                "getcustomkey", "getuniversalkey", "gettrap", "getcustomtrap", "keychain");
    }

    private List<String> getAllLocks() {
        // Return a list of all possible locks
        return Arrays.asList("1", "2", "3", "4", "5", "6");
    }

    private List<String> getAllTraps() {
        List<String> trapTypeValues = new ArrayList<>();
        for (TrapType trapType : TrapType.values()) {
            trapTypeValues.add(trapType.getValue());
        }
        return trapTypeValues;
    }
}
