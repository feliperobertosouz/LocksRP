package me.sieg.locksrp.commands;
import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.item.LockFactory;
import me.sieg.locksrp.item.TrapFactory;
import me.sieg.locksrp.traps.Trap;
import me.sieg.locksrp.traps.TrapType;
import me.sieg.locksrp.utils.NameSpacedKeys;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
                ItemManager items = new ItemManager();
                if(args[0].equalsIgnoreCase("getkey")){
                    sender.sendMessage(ChatColor.DARK_GREEN + "[LOCKSRP] " + ChatColor.DARK_PURPLE + "Você recebeu uma chave");
                    ItemStack chave = items.getKeyItem(1);
                    player.getInventory().addItem(chave);
                }else if(args[0].equalsIgnoreCase("getLock")){
                    try{
                        int locklevel = Integer.parseInt(args[1]);
                        int amount = 1;
                        ItemStack tranca = null;
                        if(locklevel == 1)
                            tranca = LockFactory.createLock(1, amount);
                        else if(locklevel == 2)
                            tranca = LockFactory.createLock(2, amount);
                        else if(locklevel == 3)
                            tranca = LockFactory.createLock(3, amount);
                        else if(locklevel == 4)
                            tranca = LockFactory.createLock(4, amount);
                        else if(locklevel == 5)
                            tranca = LockFactory.createLock(5, amount);
                        else if(locklevel == 6)
                            tranca = LockFactory.createLock(6, amount);
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
                        if(ItemManager.isLock(meta) && ItemManager.hasKeyCode(meta)){
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
                }else if(args[0].equalsIgnoreCase("getUniversalKey")){
                    sender.sendMessage(ChatColor.DARK_GREEN + "[LOCKSRP]:" + ChatColor.WHITE + "você recebeu uma chave universal");
                    ItemStack universalKey = items.getUniversalKey();
                    player.getInventory().addItem(universalKey);
                }else if(args[0].equalsIgnoreCase("getAlarmTrap")){
                    sender.sendMessage(ChatColor.DARK_GREEN + "[LOCKSRP]:" + ChatColor.WHITE + "você recebeu uma armadilha de alarme");
                    ItemStack alarmTrap = items.getAlarmTrap();
                    player.getInventory().addItem(alarmTrap);
                }else if(args[0].equalsIgnoreCase("getTrap")){
                    sender.sendMessage(ChatColor.DARK_GREEN + "[LOCKSRP]:" + ChatColor.WHITE + "você recebeu uma armadilha de " + args[1] );
                    TrapType trap = TrapType.valueOf(args[1]);
                    ItemStack trapItem = items.getTrap(trap);
                    player.getInventory().addItem(trapItem);
                }else if(args[0].equalsIgnoreCase("getCustomTrap")){
                    sender.sendMessage( " Você recebeu uma armadilha de " + args[1] + "com durabilidade especial");
                    TrapType trap = TrapType.valueOf(args[1]);
                    int maxUses = Integer.parseInt(args[2]);
                    ItemStack trapItem = TrapFactory.createTrap(trap, maxUses, maxUses);
                    player.getInventory().addItem(trapItem);
                }else if(args[0].equalsIgnoreCase("getKeyChain")){
                    sender.sendMessage(ChatColor.DARK_GREEN + "[LOCKSRP]:" + ChatColor.WHITE + "você recebeu um chaveiro teste");
                    ItemStack keyChain = items.getKeyChain();
                    player.getInventory().addItem(keyChain);
                }
                else{
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
