package com.lol768.LiteKits.conversation;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import com.lol768.LiteKits.LiteKits;

public class KitCommandsPrompt extends StringPrompt {
    private LiteKits lk;
    private String kit = null;

    public KitCommandsPrompt(LiteKits plugin) {
        this.lk = plugin;
    }
    
    public KitCommandsPrompt(LiteKits plugin, String kit) {
        this.lk = plugin;
        this.kit = kit;
    }

    @Override
    public Prompt acceptInput(ConversationContext arg0, String arg1) {
        String name;
        if (kit == null) {
            name = (String) arg0.getSessionData("kitName");
        } else {
            name = kit;
        }
        
        List<String> commands = lk.getConfig().getStringList("kits." + name + ".commands");
        if (commands == null) {
            commands = Arrays.asList(arg1);
        } else {
            commands.add(arg1);
        }
        lk.getConfig().set("kits." + name + ".commands", commands);
        arg0.getForWhom().sendRawMessage(lk.prefix + ChatColor.GREEN + "Command added.");
        return new KitAddCommandPrompt(lk);
    }

    @Override
    public String getPromptText(ConversationContext arg0) {
        // TODO Auto-generated method stub
        arg0.setSessionData("first", false);
        return "Enter the command (without the slash) that you'd like to attach:";
    }

}
