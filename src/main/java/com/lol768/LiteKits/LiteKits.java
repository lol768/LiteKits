package com.lol768.LiteKits;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.lol768.LiteKits.commands.Kit;

public class LiteKits extends JavaPlugin {
    public String prefix;
    public int forceSlot = -1;

    public void onEnable() {
        new Kit(this);
        prefix = getConfig().getString("settings.prefix", ChatColor.GRAY + "[" + ChatColor.RED + "LiteKits" + ChatColor.GRAY + "]") + " ";
        forceSlot = getConfig().getInt("settings.forceSlot", -1);
    }
    
    public Boolean kitExists(String kit) {
        return getConfig().contains("kits." + kit);
    }
    
    public String getBrand(Boolean space) {
        if (!space) {
            return this.prefix.trim();
        }
        return this.prefix;
    }
    
    public void supplyKitToPlayer(String kit, Player p) {
        p.getInventory().clear();
        Object armour = getConfig().get("kits." + kit + ".armour");
        Object main = getConfig().get("kits." + kit + ".main");
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
            getLogger().severe("!! Config appears to have been corrupted !!");
            p.sendMessage("An error ocurred.");
            
        }
        
        if (!mDone || !aDone) {
            getLogger().severe("!! Config appears to have been corrupted !!");
            p.sendMessage("An error ocurred.");
        }
        
        try {
            if (forceSlot != -1) {
                p.getInventory().setHeldItemSlot(forceSlot);
            }
        } catch (Exception e) {
            getLogger().warning("Your force slot item index is invalid.");
        }
        
        if (getConfig().getStringList("kits." + kit + ".commands") != null) {
            for (String command : getConfig().getStringList("kits." + kit + ".commands")) {
                command = command.replace("{player}", p.getName());
                command = command.replace("{kit}", kit);
                if (command.contains("[FORCEPLAYER]")) {
                    command = command.replace("[FORCEPLAYER]", "");
                    p.chat("/" + command);
                } else {
                    if (command.contains("[FORCECHAT]")) {
                        command = command.replace("[FORCECHAT]", "");
                        p.chat(command);
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
                
            }
        }
        
        p.updateInventory();
    }

}
