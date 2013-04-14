package com.lol768.LiteKits.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.lol768.LiteKits.LiteKits;
import com.lol768.LiteKits.conversation.CreationConversationPrefix;
import com.lol768.LiteKits.conversation.KitNamePrompt;
import com.lol768.LiteKits.conversation.KitRemovalPrompt;
import com.lol768.LiteKits.utility.CommandUtility;

public class Kit extends CommandUtility implements CommandExecutor {

    public Kit(LiteKits lk) {
        super(lk);
        super.setCommandExecutor(this);
        super.registerCommand("kit");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args.length == 0) {
            return false;
        }
        
        if (args[0].equalsIgnoreCase("list")) {
            ConfigurationSection main = super.getPlugin().getConfig().getConfigurationSection("kits");
            StringBuilder sb = new StringBuilder();
            for (String kit: main.getKeys(false)) {
                if (sender.hasPermission("LiteKits.use." + kit)) {
                    sb.append(ChatColor.GREEN + kit + ChatColor.RESET + ", ");
                } else {
                    sb.append(ChatColor.GRAY + kit + ChatColor.RESET + ", "); 
                }
                
            }
            String list = sb.toString();
            
            sender.sendMessage(list.substring(0, list.length() - 2));
            return true;
            
        }
        
        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                if (!super.getPlugin().getConfig().contains("kits." + args[1])) {
                    sender.sendMessage(super.getPlugin().prefix + ChatColor.RED + "Kit doesn't exist.");
                } else {
                    super.getPlugin().getConfig().set("kits." + args[1], null);
                    sender.sendMessage(super.getPlugin().prefix + ChatColor.GREEN + "Kit has been removed.");
                }
            } else {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Incorrect usage: /kit remove <name>");
                    return true;
                } else {
                    Player p = (Player) sender;
                    p.sendMessage(super.getPlugin().prefix + ChatColor.AQUA + "Welcome to the LiteKits kit removal wizard!");
                    p.sendMessage(super.getPlugin().prefix + ChatColor.AQUA + "Answer the questions in chat or use /quit to abort");
                    ConversationFactory factory = new ConversationFactory(super.getPlugin()).withModality(true);
                    factory.withEscapeSequence("/quit");
                    factory.withLocalEcho(false);
                    factory.withPrefix(new CreationConversationPrefix(super.getPlugin()));
                    factory.withFirstPrompt(new KitRemovalPrompt(super.getPlugin()));
                    factory.buildConversation(p).begin();
                }
            }
        }
        
        if (args[0].equalsIgnoreCase("create")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to do this.");
                return true;
            }
            
            /*if (args.length != 2) {
                sender.sendMessage("You must supply a name for the kit.");
                return false;
            } 
            
            if (super.getPlugin().getConfig().contains("kits." + args[1])) {
                sender.sendMessage("That kit already exists.");
                return true;
            }*/
            Player p = (Player) sender;
            p.sendMessage(super.getPlugin().prefix + ChatColor.AQUA + "Welcome to the LiteKits kit creation wizard!");
            p.sendMessage(super.getPlugin().prefix + ChatColor.AQUA + "Answer the questions in chat or use /quit to abort");
            ConversationFactory factory = new ConversationFactory(super.getPlugin()).withModality(true);
            factory.withEscapeSequence("/quit");
            factory.withLocalEcho(false);
            factory.withPrefix(new CreationConversationPrefix(super.getPlugin()));
            factory.withFirstPrompt(new KitNamePrompt(super.getPlugin()));
            factory.buildConversation(p).begin();
            
                
        }
        
        if (args[0].equalsIgnoreCase("select")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to do this.");
                return true;
            }
            
            if (args.length != 2) {
                sender.sendMessage("You must supply a name for the kit to select.");
                return false;
            } 
            
            if (!super.getPlugin().getConfig().contains("kits." + args[1])) {
                sender.sendMessage("That kit doesn't exist.");
                return true;
            }
            Player p = (Player) sender;
            
            ConfigurationSection main = super.getPlugin().getConfig().getConfigurationSection("kits." + args[1] + ".main");
            for (String key: main.getKeys(false)) {
                super.getPlugin().getLogger().info("kits." + args[1] + ".main." + key);
                p.getInventory().setItem(Integer.parseInt(key), super.getPlugin().getConfig().getItemStack("kits." + args[1] + ".main." + key));
            }
            
            
        }
        
        
        return false;
        
    }

}
