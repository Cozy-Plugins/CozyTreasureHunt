package com.github.cozyplugins.cozytreasurehunt.listener;

import com.github.cozyplugins.cozylibrary.MessageManager;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import com.github.cozyplugins.cozytreasurehunt.event.TreasurePostClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Represents the base treasure listener.
 */
public class TreasureListener implements Listener {

    @EventHandler
    public void onTreasurePostClick(TreasurePostClickEvent event) {
        Treasure treasure = event.getTreasure();

        // Broadcasts.
        if (treasure.getPublicBroadcastMessage() != null) {

        }

        if (treasure.getPrivateBroadcastMessage() != null) {
            event.getPlayer().sendMessage(treasure.getPrivateBroadcastMessage());
        }
    }
}
