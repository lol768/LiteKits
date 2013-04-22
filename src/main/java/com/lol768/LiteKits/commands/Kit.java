package com.lol768.LiteKits.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.lol768.LiteKits.LiteKits;
import com.lol768.LiteKits.API.KitCheckEvent;
import com.lol768.LiteKits.API.KitReceivedEvent;
import com.lol768.LiteKits.conversation.CreationConversationPrefix;
import com.lol768.LiteKits.conversation.KitNamePrompt;
import com.lol768.LiteKits.conversation.KitRemovalPrompt;
import com.lol768.LiteKits.utility.CommandUtility;
import com.lol768.LiteKits.utility.Messaging;

public class Kit extends CommandUtility implements CommandExecutor {

    public Kit(LiteKits lk) {
        super(lk);
        super.setCommandExecutor(this);
        super.registerCommand("kit");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args.length == 0) {
            return false;
        }
        
        if (args[0].equalsIgnoreCase("list")) {
            if (!sender.hasPermission("LiteKits.list")) {
                Messaging.sendPermissionsError(sender, super.getPlugin().prefix);
                return true;
            }
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
            if (list == "") {
                sender.sendMessage("There are currently no available kits.");
            } else {
                sender.sendMessage(list.substring(0, list.length() - 2));
            }
            return true;
            
        }
        
        if (args[0].equalsIgnoreCase("remove")) {
            if (!sender.hasPermission("LiteKits.remove")) {
                Messaging.sendPermissionsError(sender, super.getPlugin().prefix);
                return true;
            }
            
            if (args.length == 2) {
                if (!super.getPlugin().getConfig().contains("kits." + args[1])) {
                    sender.sendMessage(super.getPlugin().prefix + ChatColor.RED + "Kit doesn't exist.");
                    return true;
                } else {
                    super.getPlugin().getConfig().set("kits." + args[1], null);
                    super.getPlugin().saveConfig();
                    sender.sendMessage(super.getPlugin().prefix + ChatColor.GREEN + "Kit has been removed.");
                    return true;
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
                    return true;
                }
            }
        }
        
        if (args[0].equalsIgnoreCase("create")) {
            if (!sender.hasPermission("LiteKits.create")) {
                Messaging.sendPermissionsError(sender, super.getPlugin().prefix);
                return true;
            }
            
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to do this.");
                return true;
            }
            
            
            
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
            if (!sender.hasPermission("LiteKits.kit")) {
                Messaging.sendPermissionsError(sender, super.getPlugin().prefix);
                return true;
            }
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
            
            if (!sender.hasPermission("LiteKits.use." + args[1])) {
                Messaging.sendPermissionsError(sender, super.getPlugin().prefix);
                return true;
            }
            
            Player p = (Player) sender;
            KitCheckEvent kce = new KitCheckEvent(p, args[1]);
            Bukkit.getServer().getPluginManager().callEvent(kce);
            if (kce.isCancelled()) {
                return true;
            }
           
            Object armour = super.getPlugin().getConfig().get("kits." + args[1] + ".armour");
            Object main = super.getPlugin().getConfig().get("kits." + args[1] + ".main");
            Boolean aDone = false;
            Boolean mDone = false;
            if (armour instanceof ItemStack[]) {
                p.getInventory().setArmorContents((ItemStack[]) armour);
                aDone = true;
            }
            
            if (main instanceof ItemStack[]) {
                p.getInventory().setContents((ItemStack[]) main);
                mDone = true;
            }
            try {
                if (armour instanceof List) {
                    p.getInventory().setArmorContents((ItemStack[]) ((List) armour).toArray(new ItemStack[0]));
                    aDone = true;
                }
                
                if (main instanceof List) {
                    p.getInventory().setContents((ItemStack[]) ((List) main).toArray(new ItemStack[0]));
                    mDone = true;
                }
            } catch (Exception e) {
                super.getPlugin().getLogger().severe("Looks like you've broken the config buddy.");
                super.getPlugin().getLogger().severe("Don't expect any support from the authors - you've edited the config.");
                p.sendMessage("An error ocurred.");
                
            }
            
            if (!mDone || !aDone) {
                super.getPlugin().getLogger().severe("Looks like you've broken the config buddy.");
                super.getPlugin().getLogger().severe("Don't expect any support from the authors - you've edited the config.");
                p.sendMessage("An error ocurred.");
            }
            
            FixedMetadataValue lastKit = new FixedMetadataValue(super.getPlugin(), args[1]);
            p.setMetadata("lastKit", lastKit);
            KitReceivedEvent kre = new KitReceivedEvent(p, args[1]);
            Bukkit.getServer().getPluginManager().callEvent(kre);
            return true;
            
            
        }
        
        
        return false;
        
    }

}
