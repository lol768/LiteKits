package com.lol768.LiteKits.conversation;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

import com.lol768.LiteKits.LiteKits;

public class KitMadePrompt extends MessagePrompt {
    private LiteKits lk;

    public KitMadePrompt(LiteKits plugin) {
        this.lk = plugin;
    }

    @Override
    public String getPromptText(ConversationContext arg0) {
        
        return ChatColor.GREEN + "Your kit has been created.";
    }

    @Override
    protected Prompt getNextPrompt(ConversationContext arg0) {
        // TODO Auto-generated method stub
        return Prompt.END_OF_CONVERSATION;
    }

}
