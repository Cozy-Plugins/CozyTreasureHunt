package com.github.cozyplugins.cozytreasurehunt.listener;

import com.github.cozyplugins.cozylibrary.MessageManager;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import com.github.cozyplugins.cozytreasurehunt.TreasureLocation;
import com.github.cozyplugins.cozytreasurehunt.event.TreasurePostClickEvent;
import com.github.cozyplugins.cozytreasurehunt.event.TreasurePreClickEvent;
import com.github.cozyplugins.cozytreasurehunt.storage.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Represents the base treasure listener.
 */
public class TreasureListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onTreasurePreClick(TreasurePreClickEvent event) {
        PlayerData playerData = event.getPlayerData();
        Treasure treasure = event.getTreasure();

        int amountFound = playerData.getTreasureFound().get(treasure.getName());
        int limit = treasure.getLimit();

        // Check if the player has gone over the limit.
        if (amountFound >= limit) {
            event.setCancelled(true);

            if (treasure.getLimitMessage() != null) {
                event.getPlayer().sendMessage(treasure.getLimitMessage()
                        .replace("{name}", treasure.getName())
                );
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTreasurePostClick(TreasurePostClickEvent event) {
        Treasure treasure = event.getTreasure();
        TreasureLocation treasureLocation = event.getTreasureLocation();

        // Remove the treasure.
        treasureLocation.removeSilently();

        // Add treasure to player data.
        event.getPlayerData()
                .increaseTreasureFound(event.getTreasure().getName(), 1)
                .save();

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
