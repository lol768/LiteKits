package com.lol768.LiteKits;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import com.lol768.LiteKits.API.KitCheckEvent;
import com.lol768.LiteKits.API.KitReceivedEvent;
import com.lol768.LiteKits.commands.Kit;
import com.lol768.LiteKits.utility.Messaging;

public class LiteKits extends JavaPlugin {
    public String prefix;
    public int forceSlot = -1;

    public void onEnable() {
        new Kit(this);
        prefix = getConfig().getString("settings.prefix", ChatColor.GRAY + "[" + ChatColor.RED + "LiteKits" + ChatColor.GRAY + "]") + " ";
        forceSlot = getConfig().getInt("settings.forceSlot", -1);
    }
    
    public Boolean kitExists(String kit) {
        kit = kit.toLowerCase();
        return getConfig().contains("kits." + kit);
    }
    
    public String getBrand(Boolean space) {
        if (!space) {
            return this.prefix.trim();
        }
        return this.prefix;
    }
    
    public Boolean supplyKitToPlayer(String kit, Player p) {
        if (!kitExists(kit)) {
            throw new IllegalArgumentException("Kit doesn't exist.");
        }
        KitCheckEvent kce = new KitCheckEvent(p, kit);
        Bukkit.getServer().getPluginManager().callEvent(kce);
        if (kce.isCancelled()) {
            return false;
        }
        kit = kit.toLowerCase();
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
                    } else {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                }
                
            }
        }
        FixedMetadataValue lastKit = new FixedMetadataValue(this, kit);
        p.setMetadata("lastKit", lastKit);

        
        KitReceivedEvent kre = new KitReceivedEvent(p, kit);
        Bukkit.getServer().getPluginManager().callEvent(kre);
        p.updateInventory();
        return true;
    }
    
    public Boolean supplyKitToPlayer(String kit, Player p, Boolean usePerms) { 
        if (usePerms && !p.hasPermission("LiteKits.kit")) {
            Messaging.sendPermissionsError(p, prefix);
            return false;
        }
        
        if (usePerms && !p.hasPermission("LiteKits.use." + kit)) {
            Messaging.sendPermissionsError(p, prefix);
            return false;
        }
        return supplyKitToPlayer(kit, p);
        
    }
    

}
