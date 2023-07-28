/*
    This file is part of the project CozyTreasureHunt.
    Copyright (C) 2023  Smudge (Smuddgge), Cozy Plugins and contributors.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.cozyplugins.cozytreasurehunt;

import com.github.cozyplugins.cozylibrary.ConsoleManager;
import com.github.cozyplugins.cozylibrary.indicator.ConfigurationConvertable;
import com.github.cozyplugins.cozylibrary.indicator.Replicable;
import com.github.cozyplugins.cozylibrary.item.CozyItem;
import com.github.cozyplugins.cozylibrary.reward.RewardBundle;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;
import com.github.cozyplugins.cozytreasurehunt.dependency.HeadDatabaseDependency;
import com.github.cozyplugins.cozytreasurehunt.storage.TreasureStorage;
import com.github.cozyplugins.cozytreasurehunt.storage.indicator.Savable;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import com.github.smuddgge.squishyconfiguration.memory.MemoryConfigurationSection;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Represents a type of treasure.
 * <li>
 * Defaults to {@link Material#CHEST}
 * </li>
 */
public class Treasure implements ConfigurationConvertable<Treasure>, Savable, Replicable<Treasure> {

    private final @NotNull UUID identifier;
    private @NotNull String name;
    private @NotNull String description;

    private @NotNull Material material;
    private @Nullable String hdb;

    private @Nullable String publicBroadcastMessage;
    private @Nullable String privateBroadcastMessage;

    private @Nullable Particle particleType;
    private List<Integer> particleColor; // [Red,Green,Blue]
    private float particleSize;
    private int particleAmount;

    private int limit;
    private @Nullable String limitMessage;

    private @NotNull RewardBundle rewardBundle;

    /**
     * Used to create a treasure type.
     *
     * @param identifier The treasure's identifier.
     */
    public Treasure(@NotNull UUID identifier) {
        this.identifier = identifier;
        this.name = "New Treasure";
        this.description = "None";
        this.material = Material.CHEST;
        this.privateBroadcastMessage = "&aYou found &f{treasure}";
        this.particleSize = 1f;
        this.limit = -1;
        this.rewardBundle = new RewardBundle();
    }

    /**
     * Used to get the treasure identifier.
     * <li>
     * A unique set of characters that defines this treasure type.
     * </li>
     *
     * @return The identifier.
     */
    public @NotNull UUID getIdentifier() {
        return this.identifier;
    }

    /**
     * Used to get the treasure's name.
     *
     * @return The treasure's name.
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Used to get the treasure's description.
     * Defaults to "None".
     *
     * @return The treasures description.
     */
    public @NotNull String getDescription() {
        return this.description;
    }

    /**
     * Used to get the treasure's material.
     *
     * @return The treasure's material.
     */
    public @NotNull Material getMaterial() {
        return this.material;
    }

    /**
     * Used to get the hdb value.
     *
     * @return The hdb value.
     */
    public @Nullable String getHdb() {
        return this.hdb;
    }

    /**
     * Used to get the treasure as an item.
     *
     * @return The treasure as an item.
     */
    public @NotNull CozyItem getItem() {
        // Check if the head database is enabled and there is a hdb value.
        if (HeadDatabaseDependency.isEnabled() && this.hdb != null && !this.hdb.equals("")) {
            ItemStack item = HeadDatabaseDependency.get().getItemHead(this.hdb);
            return new CozyItem(item);
        }

        return new CozyItem(this.material);
    }

    /**
     * Used to get the public broadcast message.
     * If null, no public broadcast message should be displayed.
     *
     * @return The public broadcast message.
     */
    public @Nullable String getPublicBroadcastMessage() {
        return this.publicBroadcastMessage;
    }

    /**
     * Used to get the private broadcast message.
     * If null, no private broadcast message should be
     * sent to the player that clicked the treasure.
     *
     * @return The private broadcast message.
     */
    public @Nullable String getPrivateBroadcastMessage() {
        return this.privateBroadcastMessage;
    }

    /**
     * Used to get the particle type.
     *
     * @return The particle type.
     */
    public @Nullable Particle getParticleType() {
        return this.particleType;
    }

    /**
     * Used to get the particle color.
     * The index represents the type of color.
     * <li>0 = Red</li>
     * <li>1 = Green</li>
     * <li>2 = Blue</li>
     *
     * @param index The color index.
     * @return The color value.
     */
    public int getParticleColor(int index) {
        if (this.particleColor == null) return 255;
        if (this.particleColor.isEmpty()) return 255;
        return this.particleColor.get(index);
    }

    /**
     * Used to get the particle size.
     * If the particle doesn't allow {@link Particle.DustOptions}
     * the size will not be changed.
     *
     * @return The size of the particle.
     */
    public float getParticleSize() {
        return this.particleSize;
    }

    /**
     * Used to get the number of particles.
     *
     * @return The number of particles.
     */
    public int getParticleAmount() {
        return this.particleAmount;
    }

    /**
     * Used to get the treasure's limit.
     * This is the amount of treasure a player can find
     * for this type of treasure.
     *
     * @return The treasure's limit.
     */
    public int getLimit() {
        return this.limit;
    }

    /**
     * Used to get the limit message.
     * The message displayed to the user when
     * they try to click a treasure, but they have
     * already gone to the limit.
     *
     * @return The limit message.
     */
    public @Nullable String getLimitMessage() {
        return this.limitMessage;
    }

    /**
     * Used to get the reward bundle for this treasure.
     *
     * @return The reward bundle.
     */
    public @NotNull RewardBundle getRewardBundle() {
        return rewardBundle;
    }

    /**
     * Used to set the name of the treasure.
     *
     * @param name The treasures new name.
     * @return This instance.
     */
    public @NotNull Treasure setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    /**
     * Used to set the treasure's description.
     *
     * @param description The description to set the treasure to.
     * @return This instance.
     */
    public @NotNull Treasure setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Used to set the treasure's material.
     *
     * @param material The material to set the treasure.
     * @return This instance.
     */
    public @NotNull Treasure setMaterial(@NotNull Material material) {
        this.material = material;
        return this;
    }

    /**
     * Used to set the hdb value.
     *
     * @param hdb The hdb value.
     * @return This instance.
     */
    public @NotNull Treasure setHdb(@Nullable String hdb) {
        this.hdb = hdb;
        return this;
    }

    /**
     * Used to set the public broadcast message.
     * Setting this value to null will not show a
     * public broadcast message in chat.
     *
     * @param message The message.
     * @return This instance.
     */
    public @NotNull Treasure setPublicBroadcastMessage(@Nullable String message) {
        this.publicBroadcastMessage = message;
        return this;
    }

    /**
     * Used to set the private broadcast message.
     * <p>
     * This message will be sent to the player that clicked the treasure.
     * If null, the private broadcast message will not be sent.
     * </p>
     *
     * @param privateBroadcastMessage The private broadcast message.
     * @return This instance.
     */
    public @NotNull Treasure setPrivateBroadcastMessage(String privateBroadcastMessage) {
        this.privateBroadcastMessage = privateBroadcastMessage;
        return this;
    }

    /**
     * Used to set the particle type to spawn when a
     * player finds this treasure.
     * If null, no particles will be spawned.
     *
     * @param particleType The particle type.
     */
    public @NotNull Treasure setParticleType(@Nullable Particle particleType) {
        this.particleType = particleType;
        return this;
    }


    /**
     * Used to set the particle's color.
     * If null, there is no color.
     * <li>0 = Red</li>
     * <li>1 = Green</li>
     * <li>2 = Blue</li>
     *
     * @param particleColor The particle color.
     */
    public @NotNull Treasure setParticleColor(@Nullable List<Integer> particleColor) {
        this.particleColor = particleColor;
        return this;
    }

    /**
     * Used to increase the red color in the particle.
     *
     * @param amount The amount to increase.
     * @return This instance.
     */
    public @NotNull Treasure increaseRedParticle(int amount) {
        if (this.particleColor == null) this.particleColor = new ArrayList<>();

        if (this.particleColor.isEmpty()) {
            this.particleColor.add(0);
            this.particleColor.add(0);
            this.particleColor.add(0);
        }

        this.particleColor.set(0, this.particleColor.get(0) + amount);
        return this;
    }

    /**
     * Used to increase the green color in the particle.
     *
     * @param amount The amount to increase.
     * @return This instance.
     */
    public @NotNull Treasure increaseGreenParticle(int amount) {
        if (this.particleColor == null) this.particleColor = new ArrayList<>();

        if (this.particleColor.isEmpty()) {
            this.particleColor.add(0);
            this.particleColor.add(0);
            this.particleColor.add(0);
        }

        this.particleColor.set(1, this.particleColor.get(1) + amount);
        return this;
    }

    /**
     * Used to increase the blue color in the particle.
     *
     * @param amount The amount to increase.
     * @return This instance.
     */
    public @NotNull Treasure increaseBlueParticle(int amount) {
        if (this.particleColor == null) this.particleColor = new ArrayList<>();

        if (this.particleColor.isEmpty()) {
            this.particleColor.add(0);
            this.particleColor.add(0);
            this.particleColor.add(0);
        }

        this.particleColor.set(2, this.particleColor.get(2) + amount);
        return this;
    }

    /**
     * Used to set the particle size.
     * This will only affect the particle if the particle
     * has the data {@link Particle.DustOptions}
     *
     * @param size The size of the particle.
     * @return This instance.
     */
    public @NotNull Treasure setParticleSize(float size) {
        this.particleSize = size;
        return this;
    }

    /**
     * Used to set the number of particles to spawn.
     *
     * @param particleAmount The particle amount.
     * @return This instance.
     */
    public @NotNull Treasure setParticleAmount(int particleAmount) {
        this.particleAmount = particleAmount;
        return this;
    }

    /**
     * Used to set the amount of treasure each player
     * may find.
     * <li>Setting this to -1 will remove the limit.</li>
     *
     * @param limit The treasure's limit.
     * @return This instance.
     */
    public @NotNull Treasure setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Used to set the limit message.
     * The message displayed to the user when
     * they try to click a treasure, but they have
     * already gone to the limit.
     *
     * @param message The limit message.
     * @return This instance.
     */
    public @NotNull Treasure setLimitMessage(@Nullable String message) {
        this.limitMessage = message;
        return this;
    }

    /**
     * Used to set the reward bundle.
     * This will be given when found.
     *
     * @param rewardBundle The instance of a reward bundle.
     * @return This instance.
     */
    public @NotNull Treasure setRewardBundle(@NotNull RewardBundle rewardBundle) {
        this.rewardBundle = rewardBundle;
        return this;
    }


    /**
     * Used to spawn the treasure block.
     *
     * @param location The location to spawn the treasure block.
     * @return This instance.
     */
    public @NotNull Treasure spawn(Location location) {
        location.getBlock().setType(this.material);

        // If there is a head database value.
        if (this.hdb != null && !this.hdb.equals("")) {

            // Check if the head database is enabled.
            if (!HeadDatabaseDependency.isEnabled()) {
                ConsoleManager.warn("Attempted to spawn {treasure}, but the Head Database plugin is not enabled."
                        .replace("{treasure}", this.getName()));
                return this;
            }

            HeadDatabaseDependency.get().setBlockSkin(location.getBlock(), this.hdb);
        }

        return this;
    }

    /**
     * Used to attempt to spawn this treasures
     * particles at a location.
     *
     * @return This instance.
     */
    public @NotNull Treasure spawnParticles(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) {
            ConsoleManager.warn("Attempted to spawn particles for treasure " + this.getName() + " but the world is null. " + location);
            return this;
        }

        // Check if the particle type is null.
        if (this.particleType == null) return this;

        // Adjust the location to the center of the block.
        location.setX(location.getBlockX() + 0.5d);
        location.setY(location.getBlockY() + 0.5d);
        location.setZ(location.getBlockZ() + 0.5d);

        // Check if a color is specified and the particle is able to display colors.
        if (this.particleType.getDataType() == Particle.DustOptions.class) {
            // Check if there is no particle color set.
            if (this.particleColor.isEmpty()) {
                this.particleColor.add(255);
                this.particleColor.add(255);
                this.particleColor.add(255);
            }

            // Create the dust color.
            Particle.DustOptions options = new Particle.DustOptions(
                    Color.fromRGB(this.particleColor.get(0), this.particleColor.get(1), this.particleColor.get(2)),
                    this.particleSize
            );

            // Spawn the particle.
            world.spawnParticle(this.particleType, location, this.particleAmount, 0d, 0d, 0d, 0.2d, options);
            return this;
        }

        world.spawnParticle(this.particleType, location, this.particleAmount, 0d, 0d, 0d, 0.2d);
        return this;
    }

    /**
     * Used to spawn particles in front of a player.
     *
     * @param playerUser The instance of the player.
     */
    public void spawnParticlesPlayerRight(@NotNull PlayerUser playerUser) {
        Location clone = playerUser.getPlayer().getLocation();
        Vector direction = clone.getDirection();

        direction.rotateAroundY(Math.toRadians(40));
        direction.multiply(2);

        clone.add(direction);
        clone.add(0, 1, 0);

        this.spawnParticles(clone);
    }

    @Override
    public @NotNull ConfigurationSection convert() {
        ConfigurationSection section = new MemoryConfigurationSection(new HashMap<>());

        section.set("name", this.name);
        section.set("description", this.description);

        section.set("material", this.material.toString());
        section.set("hdb", this.hdb);

        section.set("public_broadcast_message", this.publicBroadcastMessage);
        section.set("private_broadcast_message", this.privateBroadcastMessage);
        section.set("particle.type", this.particleType == null ? null : this.particleType.toString());
        section.set("particle.color", this.particleColor);
        section.set("particle.size", Double.valueOf(String.valueOf(this.particleSize)));
        section.set("particle.amount", this.particleAmount);

        section.set("limit", this.limit);
        section.set("limit_message", this.limitMessage);
        section.set("reward", this.rewardBundle.convert().getMap());

        return section;
    }

    @Override
    @SuppressWarnings("all")
    public Treasure convert(ConfigurationSection section) {
        String materialName = section.getString("material", "CHEST").toUpperCase();
        if (Material.getMaterial(materialName) == null) {
            ConsoleManager.error("The treasure with identifier &f" + identifier + " &chas an invalid material. The treasure will default to a &fCHEST&c.");
            materialName = "CHEST";
        }

        if (Material.getMaterial(materialName) == null) {
            ConsoleManager.error("The material CHEST is invalid on this server version, further errors may occur.");
            return this;
        }

        this.name = section.getString("name", "New Treasure");
        this.description = section.getString("description", "None");

        this.material = Material.getMaterial(materialName);
        this.hdb = section.getString("hdb");

        this.publicBroadcastMessage = section.getString("public_broadcast_message");
        this.privateBroadcastMessage = section.getString("private_broadcast_message");
        this.particleType = section.getString("particle.type") == null ?
                null : Particle.valueOf(section.getString("particle.type"));

        List<Integer> defaultList = new ArrayList<>();
        defaultList.add(255);
        defaultList.add(255);
        defaultList.add(255);
        this.particleColor = section.getListInteger("particle.color", defaultList);
        this.particleSize = Float.valueOf(((Double) section.getSection("particle").get("size", 1.0d)).toString());
        this.particleAmount = section.getInteger("particle.amount", 10);

        this.limit = section.getInteger("limit", -1);
        this.limitMessage = section.getString("limit_message", "&7You have reached the maximum amount of &f{name}&7.");
        this.rewardBundle = new RewardBundle().convert(section.getSection("reward"));

        return this;
    }

    @Override
    public void save() {
        TreasureStorage.insert(this);
    }

    @Override
    public Treasure duplicate() {
        ConfigurationSection data = this.convert();
        return Treasure.create(UUID.randomUUID(), data);
    }

    public static @NotNull Treasure create(@NotNull UUID identifier, @NotNull ConfigurationSection section) {
        Treasure treasure = new Treasure(identifier);
        treasure.convert(section);
        return treasure;
    }
}
