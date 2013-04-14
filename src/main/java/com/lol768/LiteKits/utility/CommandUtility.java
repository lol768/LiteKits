package com.lol768.LiteKits.utility;

import org.bukkit.command.CommandExecutor;

import com.lol768.LiteKits.LiteKits;

public abstract class CommandUtility {
    
    private LiteKits plugin;
    private CommandExecutor commandClass;

    public CommandUtility(LiteKits p) {
        this.plugin = p;
        
    }
    
    public void setCommandExecutor(CommandExecutor ce) {
        this.commandClass = ce;
    }
   
    
    public LiteKits getPlugin() {
        return this.plugin;
    }
    
    public void registerCommand(String label) {
        plugin.getCommand(label).setExecutor(commandClass);
    }

}
