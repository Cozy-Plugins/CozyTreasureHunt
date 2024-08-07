package com.github.cozyplugins.cozytreasurehunt.storage;

import com.github.cozyplugins.cozytreasurehunt.TreasureLocation;
import com.github.cozyplugins.cozytreasurehunt.storage.indicator.Savable;
import com.github.smuddgge.squishyconfiguration.indicator.ConfigurationConvertable;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import com.github.smuddgge.squishyconfiguration.memory.MemoryConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Represents the player's data located in this plugin's data file.
 */
public class PlayerData implements Savable, ConfigurationConvertable<PlayerData> {

    private final @NotNull UUID playerUuid;
    private @NotNull ConfigurationSection information;
    /*
    This uses the treasure's name instead of the identifier, so it is easier to see
    in the data file.
     */
    private @NotNull Map<String, Integer> treasureFound;

    /**
     * Used to create a new player data class.
     *
     * @param playerUuid The players uuid.
     */
    public PlayerData(@NotNull UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.information = new MemoryConfigurationSection(new HashMap<>());
        this.treasureFound = new HashMap<>();
    }

    /**
     * Used to get the identifier of this class.
     *
     * @return The players uuid.
     */
    public @NotNull UUID getIdentifier() {
        return this.playerUuid;
    }

    /**
     * Used to get the information configuration section.
     * Plugins can use this method to save extra data.
     *
     * @return The player information.
     */
    public @NotNull ConfigurationSection getInformation() {
        return this.information;
    }

    /**
     * Used to get the treasure the player has found.
     * The map may not contain any values.
     *
     * @return The map of treasure to amount found.
     */
    public @NotNull Map<String, Integer> getTreasureFound() {
        return this.treasureFound;
    }

    /**
     * Used to get the amount of treasure found.
     * If the treasure is not listed in the map, it will return 0.
     *
     * @param treasureName The name of the treasure.
     * @return The amount of treasure found by this player.
     */
    public int getTreasureFound(@NotNull String treasureName) {
        return this.treasureFound.getOrDefault(treasureName, 0);
    }

    /**
     * Used to get the amount of treasure found in total.
     *
     * @return The amount of treasure found.
     */
    public int getAmountFound() {
        int amount = 0;

        for (int value : this.treasureFound.values()) {
            amount += value;
        }

        return amount;
    }

    /**
     * Used to increase the amount of treasure found for a specific treasure.
     * This will also increase the amount redeemed.
     *
     * @param location The instance of the treasure location.
     * @return This instance.
     */
    public @NotNull PlayerData increaseTreasureFound(@NotNull TreasureLocation location) {
        String treasureName = location.getTreasure().getName();
        if (this.treasureFound.containsKey(treasureName)) {
            this.treasureFound.put(treasureName, this.treasureFound.get(treasureName) + 1);

            return this;
        }

        this.treasureFound.put(treasureName, 1);
        return this;
    }

    /**
     * Used to set the player's information.
     *
     * @param section The player's information.
     * @return This instance.
     */
    public @NotNull PlayerData setInformation(@NotNull ConfigurationSection section) {
        this.information = section;
        return this;
    }

    /**
     * Used to add the location to the player information.
     * This value changes when the treasure disappears.
     *
     * @param location The instance of the location.
     */
    public void addRedeemedLocation(@NotNull TreasureLocation location) {
        String treasureName = location.getTreasure().getName();

        // Store treasure location.
        ConfigurationSection locationSection = this.information.getSection("no_longer_redeemable");
        List<String> locationList = locationSection.getListString(treasureName, new ArrayList<>());
        locationList.add(location.getIdentifier());
        locationSection.set(treasureName, locationList);
    }

    /**
     * Used to remove a redeemed location.
     * This is called normally when the treasure disappears.
     *
     * @param location The instance of the location.
     * @return This instance.
     */
    public @NotNull PlayerData removeRedeemedLocation(@NotNull TreasureLocation location) {
        String treasureName = location.getTreasure().getName();

        // Remove redeemed location.
        ConfigurationSection locationSection = this.information.getSection("no_longer_redeemable");
        List<String> locationList = locationSection.getListString(treasureName, new ArrayList<>());
        locationList.remove(location.getIdentifier());
        locationSection.set(treasureName, locationList);

        return this;
    }

    /**
     * Used to check if a player has redeemed a treasure location.
     *
     * @param location The instance of the location.
     * @return True if the player has redeemed this location.
     */
    public boolean hasRedeemed(@NotNull TreasureLocation location) {
        for (String locationIdentifier : this.information.getSection("no_longer_redeemable")
                .getListString(location.getTreasure().getName())) {

            if (locationIdentifier.equals(location.getIdentifier())) return true;
        }

        return false;
    }

    @Override
    public void save() {
        DataStorage.insert(this);
    }

    @Override
    public @NotNull ConfigurationSection convert() {
        ConfigurationSection section = new MemoryConfigurationSection(new HashMap<>());

        section.set("treasure_found", this.treasureFound);
        section.set("info", this.information.getMap());

        return section;
    }

    @Override
    public PlayerData convert(ConfigurationSection section) {
        this.treasureFound = new HashMap<>();

        // Put the treasure found into the map.
        for (String treasure : section.getSection("treasure_found").getKeys()) {
            this.treasureFound.put(treasure, section.getInteger("treasure_found." + treasure));
        }

        this.information = new MemoryConfigurationSection(section.getSection("info").getMap());
        return this;
    }

    /**
     * Used to create a player data class.
     *
     * @param playerUuid The instance of the players uuid.
     * @param section    The configuration section holding the player's data.
     * @return The requested player data instance.
     */
    public static @NotNull PlayerData create(@NotNull UUID playerUuid, @NotNull ConfigurationSection section) {
        PlayerData playerData = new PlayerData(playerUuid);
        playerData.convert(section);
        return playerData;
    }
}
