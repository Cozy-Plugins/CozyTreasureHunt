package com.github.cozyplugins.cozytreasurehunt.storage;

import com.github.cozyplugins.cozylibrary.indicator.ConfigurationConvertable;
import com.github.cozyplugins.cozytreasurehunt.storage.indicator.Savable;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import com.github.smuddgge.squishyconfiguration.memory.MemoryConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents the player's data located in this plugin's data file.
 */
public class PlayerData implements Savable, ConfigurationConvertable {

    private @NotNull UUID playerUuid;
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
        this.treasureFound = new HashMap<>();
    }

    /**
     * Used to get the identifier of this class.
     *
     * @return The players uuid.
     */
    public UUID getIdentifier() {
        return this.playerUuid;
    }

    /**
     * Used to get the treasure the player has found.
     *
     * @return The map of treasure to amount found.
     */
    public @NotNull Map<String, Integer> getTreasureFound() {
        return this.treasureFound;
    }

    /**
     * Used to set a treasure to a certain amount in the map.
     *
     * @param treasureName The treasure identifier.
     * @param amount       The amount to set found.
     * @return This instance.
     */
    public @NotNull PlayerData putTreasureFound(@NotNull String treasureName, int amount) {
        this.treasureFound.put(treasureName, amount);
        return this;
    }

    /**
     * Used to increase the amount of treasure found for a specific treasure.
     *
     * @param treasureName The treasure's identifier.
     * @param amount       The amount to increase.
     * @return This instance.
     */
    public @NotNull PlayerData increaseTreasureFound(@NotNull String treasureName, int amount) {
        if (this.treasureFound.containsKey(treasureName)) {
            this.treasureFound.put(treasureName, this.treasureFound.get(treasureName) + amount);
            return this;
        }

        this.treasureFound.put(treasureName, amount);
        return this;
    }

    @Override
    public void save() {
        DataStorage.insert(this);
    }

    @Override
    public @NotNull ConfigurationSection convert() {
        ConfigurationSection section = new MemoryConfigurationSection(new HashMap<>());

        section.set("treasure_found", this.treasureFound);

        return section;
    }

    @Override
    public void convert(ConfigurationSection section) {
        this.treasureFound = new HashMap<>();

        // Put the treasure found into the map.
        for (String treasure : section.getSection("treasure_found").getKeys()) {
            this.treasureFound.put(treasure, section.getInteger("treasure_found." + treasure));
        }
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
