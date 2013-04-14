package com.lol768.LiteKits.conversation;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

import com.lol768.LiteKits.LiteKits;

public class KitNamePrompt extends ValidatingPrompt {
    public LiteKits lk;

    public KitNamePrompt(LiteKits plugin) {
        this.lk = plugin;
    }

    @Override
    public String getPromptText(ConversationContext arg0) {
        
        return "Which kit would you like to remove?";
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1) {
        arg0.setSessionData("kitName", arg1);
        return new KitAddCommandPrompt(lk);
    }

    @Override
    protected boolean isInputValid(ConversationContext arg0, String arg1) {
        if (lk.getConfig().contains("kits." + arg1)) {
            arg0.getForWhom().sendRawMessage(lk.prefix + ChatColor.RED + "Kit already exists.");
        }
        return !lk.getConfig().contains("kits." + arg1);
    }

}
