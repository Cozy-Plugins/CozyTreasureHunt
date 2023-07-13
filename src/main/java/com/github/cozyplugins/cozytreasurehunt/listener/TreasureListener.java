package com.github.cozyplugins.cozytreasurehunt.listener;

import com.github.cozyplugins.cozylibrary.MessageManager;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import com.github.cozyplugins.cozytreasurehunt.TreasureLocation;
import com.github.cozyplugins.cozytreasurehunt.event.TreasurePostClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Represents the base treasure listener.
 */
public class TreasureListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTreasurePostClick(TreasurePostClickEvent event) {
        Treasure treasure = event.getTreasure();
        TreasureLocation treasureLocation = event.getTreasureLocation();

        // Remove the treasure.
        treasureLocation.remove();

        // Broadcasts.
        if (treasure.getPublicBroadcastMessage() != null) {
            MessageManager.broadcast(MessageManager.parse(treasure.getPublicBroadcastMessage(), event.getPlayer().getPlayer()));
        }
        if (treasure.getPrivateBroadcastMessage() != null) {
            event.getPlayer().sendMessage(treasure.getPrivateBroadcastMessage());
        }

        // Particles.
        treasure.spawnParticles(event.getLocation());
    }
}
