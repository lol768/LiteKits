package com.lol768.LiteKits;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.lol768.LiteKits.commands.Kit;

public class LiteKits extends JavaPlugin {
    public String prefix;

    public void onEnable() {
        new Kit(this);
        prefix = getConfig().getString("settings.prefix", ChatColor.GRAY + "[" + ChatColor.RED + "LiteKits" + ChatColor.GRAY + "] ");
    }

}
