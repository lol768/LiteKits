package com.lol768.LiteKits.API;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KitCheckEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Boolean cancel = false;
    private Player p;
    private String kitName;
    
    public KitCheckEvent(Player p, String kitName) {
        this.p = p;
        this.kitName = kitName;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public void setCancelled(Boolean cancelled) {
        this.cancel = cancelled;
    }
    
    public boolean isCancelled() {
        return this.cancel;
    }
    
    public Player getPlayer() {
        return this.p;
    }
    
    public String getKitName() {
        return this.kitName;
    }
}
