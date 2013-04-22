package com.lol768.LiteKits.conversation;

import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.lol768.LiteKits.LiteKits;

public class KitAddCommandPrompt extends BooleanPrompt {
    private LiteKits lk;

    public KitAddCommandPrompt(LiteKits plugin) {
        this.lk = plugin;
    }

    @Override
    public String getPromptText(ConversationContext arg0) {
        if (arg0.getSessionData("first") == null) {
            return "Would you like to attach a command to this kit?";
        } else {
            return "Would you like to attach another command to this kit?";
        }
        
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext arg0, boolean arg1) {
        if (arg1) {
            return new KitCommandsPrompt(lk);
        }
        PlayerInventory i = ((Player) arg0.getForWhom()).getInventory();
        
        ItemStack[] main = i.getContents();
        lk.getConfig().set("kits." + arg0.getSessionData("kitName") + ".armour", i.getArmorContents());
        lk.getConfig().set("kits." + arg0.getSessionData("kitName") + ".main", main);
        
        
        lk.saveConfig();
        
        return new KitMadePrompt(lk);
    }

}
