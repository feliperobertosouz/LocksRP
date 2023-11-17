package me.sieg.locksrp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LocksRPTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        List<String> results = new ArrayList<String>();
        Player player = (Player) commandSender;
        if(!player.hasPermission("locksrp.admin")){
            return null;
        }
        if(args.length == 1){
            results.add("getKey");
            results.add("getLock");
            results.add("getLockPick");
            results.add("getData");
            results.add("getLockRemover");
            results.add("getKeyofLock");
            results.add("getCustomKey");
            results.add("getUniversalKey");
            results.add("getTrap");
            results.add("getCustomTrap");
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("getLock")){
                results.add("1");
                results.add("2");
                results.add("3");
                results.add("4");
                results.add("5");
                results.add("6");
            }else if(args[0].equalsIgnoreCase("getTrap")){
                results.add("ALARM");
                results.add("MAGIC_ALARM");
                results.add("SPIKE");
                results.add("POISON");
                results.add("REINFORCEMENT");
                results.add("SPECTRAL");
                results.add("EXPLOSIVE");
            }else if(args[0].equalsIgnoreCase("getCustomTrap")){
                results.add("ALARM");
                results.add("MAGIC_ALARM");
                results.add("SPIKE");
                results.add("POISON");
                results.add("REINFORCEMENT");
                results.add("SPECTRAL");
                results.add("EXPLOSIVE");
            }
        }
        return results;
    }
}
