package me.sieg.locksrp.commands;

import me.sieg.locksrp.utils.Itemmanager;
import me.sieg.locksrp.utils.NameSpacedKeys;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LocksRPCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(!player.hasPermission("locksrp.admin")){

                sender.sendMessage(ChatColor.DARK_RED + "Você não tem permissão pra fazer isso");
                return false;
            }
            if(args.length == 0){
                commandAjuda(args, player);
        }else{
                Itemmanager items = new Itemmanager();
                if(args[0].equalsIgnoreCase("getkey")){
                    sender.sendMessage(ChatColor.DARK_GREEN + "[LOCKSRP] " + ChatColor.DARK_PURPLE + "Você recebeu uma chave");
                    ItemStack chave = items.getChaveItem(1);
                    player.getInventory().addItem(chave);
                }else if(args[0].equalsIgnoreCase("getLock")){
                    try{
                        int locklevel = Integer.parseInt(args[1]);
                        int amount = 1;
                        ItemStack tranca = null;
                        if(locklevel == 1)
                            tranca = items.getLock(1, 9999, amount);
                        else if(locklevel == 2)
                            tranca = items.getLock(2, 9998, amount);
                        else if(locklevel == 3)
                            tranca = items.getLock(3,9997, amount);
                        else if(locklevel == 4)
                            tranca = items.getLock(4,9996, amount);
                        else if(locklevel == 5)
                            tranca = items.getLock(5,9995, amount);
                        else if(locklevel == 6)
                            tranca = items.getLock(6,99964, amount);
                        else{
                            sender.sendMessage(ChatColor.RED + "Informe um nivel de 1 a 6");
                            return false;
                        }
                        player.getInventory().addItem(tranca);

                    }catch(Exception e){
                        sender.sendMessage(ChatColor.RED + "Informe um nivel correto de tranca 1 a 5");
                    }
                }else if(args[0].equalsIgnoreCase("getLockPick")){
                    sender.sendMessage(ChatColor.DARK_GREEN + "[LOCKSRP]:" + ChatColor.WHITE + "você recebeu um lockpick");
                    ItemStack lockpick = items.getLockPick();
                    player.getInventory().addItem(lockpick);
                }else if(args[0].equalsIgnoreCase("getData")){
                    ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
                    ItemMeta meta = item.getItemMeta();
                    if(meta != null){

                    String code = NameSpacedKeys.getNameSpacedKey(meta, "keyCode");

                    if(code != null){
                        sender.sendMessage(ChatColor.GOLD + "Codigo da chave:" + ChatColor.WHITE + code);
                        if(NameSpacedKeys.getNameSpacedKey(meta,"isLock") != null){
                            sender.sendMessage(ChatColor.GOLD + "TRANCA STATUS: " + ChatColor.WHITE + NameSpacedKeys.getNameSpacedKey(meta,"bindable"));
                            sender.sendMessage(ChatColor.DARK_PURPLE + "NIVEL DA TRANCA: " + ChatColor.WHITE + NameSpacedKeys.getNameSpacedKey(meta, "level"));
                        }
                    }

                    }
                }else if(args[0].equalsIgnoreCase("getLockRemover")){
                    sender.sendMessage(ChatColor.DARK_GREEN + "[LOCKSRP]:" + ChatColor.WHITE + "você recebeu um lock remover");
                    ItemStack lockRemover = items.getLockRemover();
                    player.getInventory().addItem(lockRemover);
                }else if(args[0].equalsIgnoreCase("getKeyOfLock")){
                    ItemStack itemHand = player.getInventory().getItemInMainHand();
                    if(itemHand != null && itemHand.hasItemMeta()){
                        ItemMeta meta = itemHand.getItemMeta();
                        if(NameSpacedKeys.isLock(meta) && NameSpacedKeys.hasKeyCode(meta)){
                            String lockCode = NameSpacedKeys.getNameSpacedKey(meta,"keyCode");
                            ItemStack chave = items.generateKey(lockCode);
                            player.getInventory().addItem(chave);

                            player.sendMessage(ChatColor.DARK_GREEN + "[LocksRP]:" + ChatColor.WHITE +
                                    "você gera uma chave da tranca que você pegou");
                        }else{
                            player.sendMessage(ChatColor.RED + "Parece que a tranca não tem codigo");
                        }
                    }else{
                        player.sendMessage(ChatColor.RED + "Segure uma tranca na mão para gerar uma chave");
                    }

                }else if(args[0].equalsIgnoreCase("getCustomKey")){
                    if(args[1] != null){
                        String code = args[1];

                        ItemStack chave = items.generateKey(code);

                        player.getInventory().addItem(chave);
                        player.sendMessage(ChatColor.DARK_GREEN + "[LocksRP]:" + ChatColor.WHITE +
                                " Você cria uma nova chave, unica");
                    }
                }else{
                    commandAjuda(args, player);
                }
            }
        }
        return false;
    }


    public void commandAjuda(String[] args, Player sender) {
        sender.sendMessage(ChatColor.DARK_GREEN + "[LOCKSRP]" + ChatColor.WHITE + "HELP");
        sender.sendMessage(ChatColor.GOLD + "/locksRP getkey " + ChatColor.DARK_PURPLE + "Lhe da uma chave branca");
        sender.sendMessage(ChatColor.GOLD + "/locksRP getLock nivel(1-6) " + ChatColor.DARK_PURPLE +
                "Lhe da uma tranca do nivel informado");
        sender.sendMessage(ChatColor.GOLD + "/locksRP getLockPick " + ChatColor.DARK_PURPLE + " Lhe da um lockpick");
        sender.sendMessage(ChatColor.GOLD + "/locksRP getLockRemover "
                + ChatColor.DARK_PURPLE + " Lhe da um lockRemover");
        sender.sendMessage(ChatColor.GOLD + "/locksRP getKeyOfLock " + ChatColor.DARK_PURPLE + " Lhe da uma chave da " +
                "tranca que você esta segurando na mão");
        sender.sendMessage(ChatColor.GOLD + "/locksRP getCustomKey  + codigo" + ChatColor.DARK_PURPLE + " Lhe da uma chave com " +
                "codigo especial que você informar");
    }
}
