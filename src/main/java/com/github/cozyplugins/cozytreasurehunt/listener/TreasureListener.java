package com.github.cozyplugins.cozytreasurehunt.listener;

import com.github.cozyplugins.cozylibrary.MessageManager;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import com.github.cozyplugins.cozytreasurehunt.TreasureLocation;
import com.github.cozyplugins.cozytreasurehunt.event.TreasurePostClickEvent;
import com.github.cozyplugins.cozytreasurehunt.event.TreasurePreClickEvent;
import com.github.cozyplugins.cozytreasurehunt.storage.ConfigFile;
import com.github.cozyplugins.cozytreasurehunt.storage.DataStorage;
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

        // Check if they have already redeemed this treasure.
        if (playerData.hasRedeemed(event.getTreasureLocation())) {

            event.getPlayer().sendMessage("&7You have already redeemed this treasure.");
            event.setCancelled(true);
            return;
        }

        // Check if they have reached the global limit.
        int treasureFound = playerData.getAmountFound();
        int globalLimit = ConfigFile.getGlobalLimit();

        if (treasureFound >= globalLimit && globalLimit != -1) {
            event.setCancelled(true);

            if (!ConfigFile.getGlobalLimitMessage().isEmpty()) {
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

            if (treasure.getLimitMessage() != null || !treasure.getLimitMessage().isEmpty()) {
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
        PlayerData playerData = event.getPlayerData();

        // Check if the treasure can be redeemed again by a different pearson.
        int maxRedeemableAmount = treasure.getRedeemable();
        int redeemedAmount = DataStorage.getAmountRedeemed(treasureLocation);

        // If the treasure is fully redeemed.
        // If the max is -1 that means the treasure will always be spawned.
        if ((redeemedAmount + 1) >= maxRedeemableAmount && maxRedeemableAmount != -1) {
            // Remove the treasure.
            // This will also respawn the treasure if the treasure is respawnable.
            treasureLocation.removeSilently();

            // Reset the location player data.
            DataStorage.resetLocationData(treasureLocation);
        } else {

            // Add redeemed location for the player.
            playerData.addRedeemedLocation(treasureLocation);
        }

        // Add treasure to player data.
        playerData.increaseTreasureFound(treasureLocation);

        // Player information.
        ConfigurationSection section = playerData.getInformation();
        section.set("name", event.getPlayer().getName());

        // Save player data.
        playerData.save();

        // Broadcasts.
        if (treasure.getPublicBroadcastMessage() != null) {
            MessageManager.broadcast(MessageManager.parse(
                    treasure.getPublicBroadcastMessage()
                            .replace("{player}", event.getPlayer().getName())
                            .replace("{treasure}", treasure.getName()),
                    event.getPlayer().getPlayer())
            );
        }
        if (treasure.getPrivateBroadcastMessage() != null) {
            event.getPlayer().sendMessage(
                    treasure.getPrivateBroadcastMessage()
                            .replace("{player}", event.getPlayer().getName())
                            .replace("{treasure}", treasure.getName())
            );
        }

        // Particles.
        treasure.spawnParticles(event.getLocation());

        // Rewards.
        treasure.getRewardBundle().giveRewardBundle(event.getPlayer());
    }
}
