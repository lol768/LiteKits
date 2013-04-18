package com.lol768.LiteKits.utility;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Messaging {

    public static void sendPermissionsError(CommandSender p, String prefix) {
        p.sendMessage(prefix + ChatColor.RED + "You don't have permission to perform this command.");
    }

}
