package com.lol768.LiteKits.conversation;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

import com.lol768.LiteKits.LiteKits;

public class KitRemovalPrompt extends ValidatingPrompt {
    public LiteKits lk;

    public KitRemovalPrompt(LiteKits plugin) {
        this.lk = plugin;
    }

    @Override
    public String getPromptText(ConversationContext arg0) {
        
        return "Which kit would you like to remove?";
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1) {
        lk.getConfig().set("kits." + arg1, null);
        arg0.getForWhom().sendRawMessage(lk.prefix + ChatColor.GREEN + "Kit has been removed.");
        return Prompt.END_OF_CONVERSATION;
    }

    @Override
    protected boolean isInputValid(ConversationContext arg0, String arg1) {
        if (!lk.getConfig().contains("kits." + arg1)) {
            arg0.getForWhom().sendRawMessage(lk.prefix + ChatColor.RED + "Kit doesn't exist.");
        }
        return lk.getConfig().contains("kits." + arg1);
    }

}
