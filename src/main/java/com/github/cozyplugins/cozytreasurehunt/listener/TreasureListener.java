package com.github.cozyplugins.cozytreasurehunt.listener;

import com.github.cozyplugins.cozylibrary.MessageManager;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import com.github.cozyplugins.cozytreasurehunt.TreasureLocation;
import com.github.cozyplugins.cozytreasurehunt.event.TreasurePostClickEvent;
import com.github.cozyplugins.cozytreasurehunt.event.TreasurePreClickEvent;
import com.github.cozyplugins.cozytreasurehunt.storage.ConfigFile;
import com.github.cozyplugins.cozytreasurehunt.storage.PlayerData;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
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

        // Check if they have reached the global limit.
        int treasureFound = playerData.getAmountFound();
        int globalLimit = ConfigFile.getGlobalLimit();

        if (treasureFound >= globalLimit && globalLimit != -1) {
            event.setCancelled(true);

            if (!ConfigFile.getGlobalLimitMessage().equals("")) {
                event.getPlayer().sendMessage(ConfigFile.getGlobalLimitMessage()
                        .replace("{name}", treasure.getName())
                );
            }

            return;
        }

        // Check if they have gone over the treasure type limit.
        int treasureTypeFound = playerData.getTreasureFound(treasure.getName());
        int limit = treasure.getLimit();

        // Check if the player has gone over the limit.
        if (treasureTypeFound >= limit && limit != -1) {
            event.setCancelled(true);

            if (treasure.getLimitMessage() != null || !treasure.getLimitMessage().equals("")) {
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
        PlayerData playerData = event.getPlayerData();
        playerData.increaseTreasureFound(event.getTreasure().getName(), 1);

        // Player information.
        ConfigurationSection section = playerData.getInformation();
        section.set("name", event.getPlayer().getName());

        // Save player data.
        playerData.save();

        // Broadcasts.
        if (treasure.getPublicBroadcastMessage() != null) {
            MessageManager.broadcast(MessageManager.parse(treasure.getPublicBroadcastMessage(), event.getPlayer().getPlayer()));
        }
        if (treasure.getPrivateBroadcastMessage() != null) {
            event.getPlayer().sendMessage(treasure.getPrivateBroadcastMessage());
        }

        // Particles.
        treasure.spawnParticles(event.getLocation());

        // Rewards.
        treasure.getRewardBundle().giveRewardBundle(event.getPlayer());
    }
}
