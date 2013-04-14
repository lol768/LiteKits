package com.lol768.LiteKits.conversation;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

import com.lol768.LiteKits.LiteKits;

public class CreationConversationPrefix implements ConversationPrefix {
    private String prefix;
    
    public CreationConversationPrefix(LiteKits lk) {
        prefix = lk.prefix;
    }

    public String getPrefix(ConversationContext context) {
        return prefix;
    }

}
