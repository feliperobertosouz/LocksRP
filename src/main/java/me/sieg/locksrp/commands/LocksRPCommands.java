package me.sieg.locksrp.commands;
import me.sieg.locksrp.item.ItemManager;
import me.sieg.locksrp.item.LockFactory;
import me.sieg.locksrp.item.TrapFactory;
import me.sieg.locksrp.traps.Trap;
import me.sieg.locksrp.traps.TrapType;
import me.sieg.locksrp.utils.MessageSender;
import me.sieg.locksrp.utils.NameSpacedKeys;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LocksRPCommands implements CommandExecutor {
    ItemManager items = new ItemManager();
    MessageSender sender = new MessageSender();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true; // Se não for um jogador, saia
        }

        Player player = (Player) sender;

        if (!player.hasPermission("locksrp.admin")) {
            sender.sendMessage(ChatColor.DARK_RED + "Você não tem permissão para fazer isso");
            return false;
        }

        ItemManager items = new ItemManager();

        if (args.length == 0) {
            commandAjuda(args, player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "getkey":
                giveItem(player, items.getKeyItem(1), "Você recebeu uma chave");
                break;
            case "getlock":
                giveLock(player, args);
                break;
            case "getlockpick":
                giveItem(player, items.getLockPick(), " Você recebeu um lockpick");
                break;
            case "getdata":
                getData(player);
                break;
            case "getlockremover":
                giveItem(player, items.getLockRemover(), " Você recebeu um lock remover");
                break;
            case "getkeyoflock":
                getKeyOfLock(player);
                break;
            case "getcustomkey":
                getCustomKey(player, args);
                break;
            case "getuniversalkey":
                giveItem(player, items.getUniversalKey(), ": Você recebeu uma chave universal");
                break;
            case "getalarmtrap":
                giveItem(player, items.getAlarmTrap(), " Você recebeu uma armadilha de alarme");
                break;
            case "gettrap":
                getTrap(player, args);
                break;
            case "getcustomtrap":
                getCustomTrap(player, args);
                break;
            case "keychain":
                giveItem(player, items.getKeyChain(), "Você recebeu um chaveiro");
                break;
            default:
                commandAjuda(args,player);
        }

        return true;
    }

    private void giveItem(Player player, ItemStack item, String message) {
        player.getInventory().addItem(item);
        sender.sendPlayerMessage(player, message);
    }

    private void giveLock(Player player, String[] args) {
        if (args.length == 2) {
            try {
                int lockLevel = Integer.parseInt(args[1]);
                ItemStack lock = LockFactory.createLock(lockLevel, 1);
                player.getInventory().addItem(lock);
            } catch (NumberFormatException e) {
                sender.sendPlayerMessage(player, "Nivel disponivel 1 a 6");
            }
        } else {
            sender.sendPlayerMessage(player, "Uso correto /locksrp getlock <nivel>");
        }
    }

    private void getData(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String code = NameSpacedKeys.getNameSpacedKey(meta, "keyCode");

            if (code != null) {
                player.sendMessage(ChatColor.GOLD + "Código da chave:" + ChatColor.WHITE + code);
                if (NameSpacedKeys.getNameSpacedKey(meta, "isLock") != null) {
                    player.sendMessage(ChatColor.GOLD + "TRANCA STATUS: " + ChatColor.WHITE +
                            NameSpacedKeys.getNameSpacedKey(meta, "bindable"));
                    player.sendMessage(ChatColor.DARK_PURPLE + "NÍVEL DA TRANCA: " + ChatColor.WHITE +
                            NameSpacedKeys.getNameSpacedKey(meta, "level"));
                }
            }
        }
    }

    private void getKeyOfLock(Player player) {
        ItemStack itemHand = player.getInventory().getItemInMainHand();
        if (itemHand != null && itemHand.hasItemMeta()) {
            ItemMeta meta = itemHand.getItemMeta();
            if (ItemManager.isLock(meta) && ItemManager.hasKeyCode(meta)) {
                String lockCode = NameSpacedKeys.getNameSpacedKey(meta, "keyCode");
                ItemStack chave = items.generateKey(lockCode);
                player.getInventory().addItem(chave);
                sender.sendPlayerMessage(player,"Gerado a chave da tranca");
            } else {
                sender.sendPlayerMessage(player,"Segure uma tranca na mão para gerar uma chave");
            }
        } else {
            sender.sendPlayerMessage(player,"Segure uma tranca na mão para gerar uma chave");
        }
    }

    private void getCustomKey(Player player, String[] args) {
        if (args.length > 1) {
            String code = args[1];
            ItemStack chave = items.generateKey(code);
            player.getInventory().addItem(chave);
            sender.sendPlayerMessage(player, "Você cria uma nova chave unica");
        } else {
            sender.sendPlayerMessage(player,ChatColor.RED + "Uso correto: /locksRP getCustomKey <codigo>");
        }
    }

    private void getTrap(Player player, String[] args) {
        if (args.length > 1) {
            try {
                TrapType trap = TrapType.valueOf(args[1]);
                ItemStack trapItem = items.getTrap(trap);
                player.getInventory().addItem(trapItem);
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + "Armadilha não encontrada: " + args[1]);
            }
        } else {
            player.sendMessage(ChatColor.RED + "Uso correto: /locksRP getTrap <tipo>");
        }
    }

    private void getCustomTrap(Player player, String[] args) {
        if (args.length > 2) {
            try {
                TrapType trap = TrapType.valueOf(args[1]);
                int maxUses = Integer.parseInt(args[2]);
                ItemStack trapItem = TrapFactory.createTrap(trap, maxUses, maxUses);
                player.getInventory().addItem(trapItem);
            } catch (IllegalArgumentException e){
                player.sendMessage(ChatColor.RED + "Uso correto: /locksRP getCustomTrap <tipo> <usos>");
                sender.sendPlayerMessage(player, "Uso correto /locksrp getCustomTrap <tipo> <numero>");
            }
        } else {
            sender.sendPlayerMessage(player,ChatColor.RED + "Uso correto: /locksRP getCustomTrap <tipo> <usos>");
        }
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
