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

package com.github.cozyplugins.cozytreasurehunt.inventory.editor;

import com.github.cozyplugins.cozylibrary.datatype.ratio.Ratio;
import com.github.cozyplugins.cozylibrary.inventory.CozyInventory;
import com.github.cozyplugins.cozylibrary.inventory.InventoryItem;
import com.github.cozyplugins.cozylibrary.inventory.action.action.AnvilValueAction;
import com.github.cozyplugins.cozylibrary.inventory.action.action.ClickAction;
import com.github.cozyplugins.cozylibrary.inventory.action.action.ConfirmAction;
import com.github.cozyplugins.cozylibrary.inventory.action.action.PlaceAction;
import com.github.cozyplugins.cozylibrary.item.CozyItem;
import com.github.cozyplugins.cozylibrary.reward.RewardBundle;
import com.github.cozyplugins.cozylibrary.reward.RewardBundleEditorInventory;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import com.github.cozyplugins.cozytreasurehunt.dependency.HeadDatabaseDependency;
import com.github.cozyplugins.cozytreasurehunt.storage.TreasureStorage;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An inventory used to edit a type of treasure.
 */
public class TreasureEditor extends CozyInventory {

    private final @NotNull Treasure treasure;
    private final TreasureListEditor listEditor;

    private int page;
    private final int LAST_PAGE = 1;
    private @NotNull String modifierDescription;

    /**
     * Used to create a treasure editor.
     *
     * @param treasure   The treasure to edit.
     * @param listEditor The instance of the list editor. This will be used to go backwards.
     */
    public TreasureEditor(@NotNull Treasure treasure, @NotNull TreasureListEditor listEditor) {
        super(54, "&8&l" + treasure.getName());

        this.treasure = treasure;
        this.listEditor = listEditor;
        this.modifierDescription = "null";

        this.page = 0;
        this.generatePageDescription();
    }

    /**
     * Used to create a treasure editor.
     * <li>
     * The page can be changed to reveal new customisable options.
     * </li>
     *
     * @param treasure   The treasure to edit.
     * @param listEditor The instance of the list editor. This will be used to go backwards.
     * @param page       The page number.
     */
    public TreasureEditor(@NotNull Treasure treasure, @NotNull TreasureListEditor listEditor, int page) {
        this(treasure, listEditor);

        this.page = page;
        this.generatePageDescription();
    }

    @Override
    protected void onGenerate(PlayerUser player) {

        // Create background.
        this.setItem(new InventoryItem()
                .setMaterial(Material.GRAY_STAINED_GLASS_PANE)
                .setName("&7")
                .addSlotRange(0, 26)
                .addSlot(27, 36, 35, 44)
                .addSlotRange(45, 53)
        );

        // Back button.
        this.setItem(new InventoryItem()
                .setMaterial(Material.LIME_STAINED_GLASS_PANE)
                .setName("&a&lBack")
                .addLore("&7Click to go back to the list of treasure types.")
                .addSlot(45)
                .addAction((ClickAction) (user, type, inventory) -> {
                    // Save treasure.
                    this.treasure.save();

                    // Exit to the main list of treasure.
                    user.getPlayer().closeInventory();
                    this.listEditor.open(user.getPlayer());
                }));

        // Previous button.
        this.setItem(new InventoryItem().setMaterial(Material.YELLOW_STAINED_GLASS_PANE)
                .setName("&e&lPrevious")
                .setLore("&7Click to go back a page.",
                        "&aPage &e" + this.page,
                        "&aModifiers &e" + this.modifierDescription)
                .addSlot(48)
                .addAction((ClickAction) (user, type, inventory) -> {
                    if (this.page <= 0) return;
                    this.page -= 1;
                    this.onPageArrowClick();
                }));

        // Next button.
        this.setItem(new InventoryItem().setMaterial(Material.YELLOW_STAINED_GLASS_PANE)
                .setName("&e&lNext")
                .setLore("&7Click to go to the next page.",
                        "&aPage &e" + this.page,
                        "&aModifiers &e" + this.modifierDescription)
                .addSlot(50)
                .addAction((ClickAction) (user, type, inventory) -> {
                    if (this.page == this.LAST_PAGE) return;
                    this.page += 1;
                    this.onPageArrowClick();
                }));

        // Change name.
        this.setItem(new InventoryItem()
                .setMaterial(Material.NAME_TAG)
                .setName("&6&lChange Name")
                .addLore("&7Click to change the treasures name.")
                .addLore("&7This value is used in player data. It is ")
                .addLore("&7recommended that the value is unique out of ")
                .addLore("&7the types of treasure that can be found.")
                .addLore("&aCurrently &e" + this.treasure.getName())
                .addSlot(10)
                .addAction(new AnvilValueAction()
                        .setAnvilTitle("&8&lChange name to")
                        .setAction((value, user) -> {
                            if (value != null && !value.isEmpty()) {
                                treasure.setName(value);
                                treasure.save();

                                user.sendMessage("&7Changed treasures name to &f" + value);
                            }

                            TreasureEditor treasureEditor = new TreasureEditor(treasure, this.listEditor, page);
                            treasureEditor.open(player.getPlayer());
                        })
                )
        );

        // Change description.
        this.setItem(new InventoryItem()
                .setMaterial(Material.NAME_TAG)
                .setName("&6&lChange Description")
                .addLore("&7Click to change the treasures description.")
                .addLore("&aCurrently &e" + this.treasure.getDescription())
                .addSlot(11)
                .addAction(new AnvilValueAction()
                        .setAnvilTitle("&8&lChange description to")
                        .setAction((value, user) -> {
                            if (value != null && !value.isEmpty()) {
                                treasure.setDescription(value);
                                treasure.save();
                            }

                            TreasureEditor treasureEditor = new TreasureEditor(treasure, this.listEditor, page);
                            treasureEditor.open(player.getPlayer());
                        })
                )
        );

        // The treasure block.
        this.setItem(new InventoryItem(this.treasure.getItem().create())
                .setMaterial(this.treasure.getMaterial())
                .setName("&6&lMaterial")
                .addLore("&7Replace this item with another")
                .addLore("&7block or head to set the treasure's material.")
                .addSlot(13)
                .addAction((PlaceAction) (user, item) -> {
                    if (item.getMaterial() == Material.AIR) return;

                    // Set the material.
                    this.treasure.setMaterial(item.getMaterial());

                    // Check if the material may be in the head database.
                    if (this.treasure.getMaterial() == Material.PLAYER_HEAD && HeadDatabaseDependency.isEnabled()) {
                        HeadDatabaseAPI api = HeadDatabaseDependency.get();
                        String hdb = api.getItemID(item.create());

                        // Check if there is a hdb value.
                        if (hdb != null) this.treasure.setHdb(hdb);
                    }

                    if (this.treasure.getMaterial() == Material.PLAYER_HEAD) {

                        if (HeadDatabaseDependency.isEnabled()) {
                            HeadDatabaseAPI api = HeadDatabaseDependency.get();
                            String hdb = api.getItemID(item.create());

                            if (hdb != null) {
                                this.treasure.setHdb(hdb);
                            }
                        }

                        if (this.treasure.getHdb() == null) {
                            this.treasure.setSkullOwner(item.getSkullOwner());
                        }
                    }

                    this.treasure.save();

                    // Replace the item type.
                    this.setItem(new CozyItem(this.treasure.getItem().create())
                            .setName("&6&lMaterial")
                            .addLore("&7Replace this item with another")
                            .addLore("&7block or head to set the treasure's material."), 13);
                })
        );

        // Duplicate.
        this.setItem(new InventoryItem()
                .setMaterial(Material.ITEM_FRAME)
                .setName("&6&lDuplicate")
                .addLore("&7Click to duplicate this treasure.")
                .addLore("&fClicking this option will not exit the inventory.")
                .addLore("&fThe new treasure will appear in the list of treasure.")
                .addSlot(15)
                .addAction((ClickAction) (user, type, inventory) -> {
                    Treasure clone = this.treasure.duplicate();
                    clone.save();

                    user.sendMessage("&7Created new clone with identifier &f" + clone.getIdentifier());
                })
        );

        // Delete.
        this.setItem(new InventoryItem()
                .setMaterial(Material.CAULDRON)
                .setName("&c&lDelete")
                .setLore("&7Click to delete this treasure permanently.",
                        "&7You will be asked to confirm this action.")
                .addSlot(16)
                .addAction(new ConfirmAction()
                        .setAnvilTitle("&8&lDelete treasure")
                        .setConfirm(user -> {
                            TreasureStorage.delete(treasure.getIdentifier());
                            user.sendMessage("&7Treasure with name &f" + treasure.getName() + "&7 has been deleted.");

                            TreasureListEditor listEditor = new TreasureListEditor();
                            listEditor.open(user.getPlayer());
                        })
                        .setAbort(user -> {
                            TreasureEditor editor = new TreasureEditor(treasure, this.listEditor, page);
                            editor.open(user.getPlayer());
                        })
                )
        );

        this.generateModifiers();
    }

    /**
     * Used to generate the page description.
     * This should be called when the page has changed.
     */
    private void generatePageDescription() {
        if (this.page == 0) this.modifierDescription = "Cosmetic";
        if (this.page == 1) this.modifierDescription = "Mechanic";
    }

    /**
     * Called when the back or previous button is pressed.
     */
    private void onPageArrowClick() {
        this.generateModifiers();
        this.generatePageDescription();

        // Reset the items.
        this.setItem(new CozyItem().setMaterial(Material.YELLOW_STAINED_GLASS_PANE).setName("&e&lPrevious")
                .setLore("&7Click to go back a page.",
                        "&aPage &e" + this.page,
                        "&aModifiers &e" + this.modifierDescription), 48);
        this.setItem(new CozyItem().setMaterial(Material.YELLOW_STAINED_GLASS_PANE).setName("&e&lNext")
                .setLore("&7Click to go to the next page.",
                        "&aPage &e" + this.page,
                        "&aModifiers &e" + this.modifierDescription), 50);
    }

    /**
     * Generates the modifiers for this page.
     * Called when the page is changed.
     */
    private void generateModifiers() {
        // Remove actions.
        this.removeActionRange(27, 44);

        // Remove items.
        this.setItem(new CozyItem().setMaterial(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName("&7"), 28, 34);
        this.setItem(new CozyItem().setMaterial(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName("&7"), 37, 43);

        // Set the items on this page.
        if (this.page == 0) this.setPage0();
        if (this.page == 1) this.setPage1();
    }

    /**
     * Generates the modifiers on page 0.
     */
    private void setPage0() {
        // Public broadcast message.
        this.setItem(new InventoryItem()
                .setMaterial(Material.NAME_TAG)
                .setName("&6&lChange Public Broadcast Message")
                .setLore("&7Click to change the public broadcast message.",
                        "&7This message will appear in chat to all players",
                        "&7when a player finds this treasure.",
                        "&f{player} &7returns the players name.",
                        "&f{treasure} &7returns this treasures name.",
                        "&aCurrently &e" + this.treasure.getPublicBroadcastMessage())
                .addSlot(28)
                .addAction(new AnvilValueAction()
                        .setAnvilTitle("&8&lPublic broadcast message")
                        .setAction((value, user) -> {
                            // Value can equal "".
                            if (value != null) {
                                treasure.setPublicBroadcastMessage(value);
                                treasure.save();
                            }

                            TreasureEditor editor = new TreasureEditor(treasure, this.listEditor, page);
                            editor.open(user.getPlayer());
                        })
                )
        );


        // Private broadcast message.
        this.setItem(new InventoryItem()
                .setMaterial(Material.NAME_TAG)
                .setName("&6&lChange Private Broadcast Message")
                .setLore("&7Click to change the private broadcast message.",
                        "&7This message will be sent to the player ",
                        "&7that finds this treasure.",
                        "&f{player} &7returns the players name.",
                        "&f{treasure} &7returns this treasures name.",
                        "&aCurrently &e" + this.treasure.getPrivateBroadcastMessage())
                .addSlot(29)
                .addAction(new AnvilValueAction()
                        .setAnvilTitle("&8&lPrivate broadcast message")
                        .setAction((value, user) -> {
                            // Value can equal "".
                            if (value != null) {
                                treasure.setPrivateBroadcastMessage(value);
                                treasure.save();
                            }

                            TreasureEditor editor = new TreasureEditor(treasure, this.listEditor, page);
                            editor.open(user.getPlayer());
                        })
                )
        );

        // Public action bar message.
        this.setItem(new InventoryItem()
                .setMaterial(Material.NAME_TAG)
                .setName("&7&lChange Public Action Bar Message")
                .setLore("&7Click to change the public action bar message.",
                        "&7This message will be sent to the server ",
                        "&7when a player finds this treasure.",
                        "&eOnly available with CozyTreasureHuntPlus")
                .addSlot(30)
        );

        // Private action bar message.
        this.setItem(new InventoryItem()
                .setMaterial(Material.NAME_TAG)
                .setName("&7&lChange Private Action Bar Message")
                .setLore("&7Click to change the private action bar message.",
                        "&7This message will be sent to the player",
                        "&7when they find this treasure.",
                        "&eOnly available with CozyTreasureHuntPlus")
                .addSlot(31)
        );

        // To the side.
        // Particle type.
        this.setItem(new InventoryItem()
                .setMaterial(Material.WHITE_CANDLE)
                .setName("&6&lParticle Type")
                .setLore("&7Click to change the particle type.",
                        "&7This particle will be spawned when",
                        "&7the treasure is found.",
                        "&aCurrent &e" + this.treasure.getParticleType())
                .addSlot(32)
                .addAction(new AnvilValueAction()
                        .setAnvilTitle("&8&lParticle type")
                        .setAction((value, user) -> {
                            if (value != null) {
                                try {
                                    Particle particle = Particle.valueOf(value.toUpperCase());
                                    treasure.setParticleType(particle);
                                    treasure.save();
                                    treasure.spawnParticlesPlayerRight(user);
                                } catch (Exception exception) {
                                    user.sendMessage("&7Invalid particle type.");
                                }
                            }

                            TreasureEditor editor = new TreasureEditor(treasure, this.listEditor, page);
                            editor.open(user.getPlayer());
                        })
                )
        );

        // Particle amount.
        this.setItem(new InventoryItem()
                .setMaterial(Material.WHITE_CANDLE)
                .setName("&6&lParticle Amount")
                .setLore("&7Click to change the amount of particles",
                        "&7that will be spawned.",
                        "&aCurrent &e" + treasure.getParticleAmount(),
                        "&f+10 &7Shift Left Click",
                        "&f+1 &7Left Click",
                        "&f-1 &7Right Click",
                        "&f-10 &7Shift Right Click")
                .addSlot(33)
                .addAction((ClickAction) (user, type, inventory) -> {
                    if (type == ClickType.SHIFT_LEFT) treasure.setParticleAmount(treasure.getParticleAmount() + 10);
                    if (type == ClickType.LEFT) treasure.setParticleAmount(treasure.getParticleAmount() + 1);
                    if (type == ClickType.RIGHT) treasure.setParticleAmount(treasure.getParticleAmount() - 1);
                    if (type == ClickType.SHIFT_RIGHT) treasure.setParticleAmount(treasure.getParticleAmount() - 10);

                    treasure.save();
                    treasure.spawnParticlesPlayerRight(user);

                    // Reset the item.
                    this.setItem(new CozyItem()
                                    .setMaterial(Material.WHITE_CANDLE)
                                    .setName("&6&lParticle Amount")
                                    .setLore("&7Click to change the amount of particles",
                                            "&7that will be spawned.",
                                            "&aCurrent &e" + treasure.getParticleAmount(),
                                            "&f+10 &7Shift Left Click",
                                            "&f+1 &7Left Click",
                                            "&f-1 &7Right Click",
                                            "&f-10 &7Shift Right Click")
                            , 33);
                })
        );

        // Particle size.
        this.setItem(new InventoryItem()
                .setMaterial(Material.WHITE_CANDLE)
                .setName("&6&lParticle Size")
                .setLore("&7Click to change the particle size.",
                        "&7This will only work for particles that",
                        "&7have a dust option.",
                        "&aCurrent &e" + treasure.getParticleSize(),
                        "&f+0.1 &7Shift Left Click",
                        "&f+0.02 &7Left Click",
                        "&f-0.02 &7Right Click",
                        "&f-0.1 &7Shift Right Click")
                .addSlot(34)
                .addAction((ClickAction) (user, type, inventory) -> {
                    if (type == ClickType.SHIFT_LEFT) treasure.setParticleSize(treasure.getParticleSize() + 0.1f);
                    if (type == ClickType.LEFT) treasure.setParticleSize(treasure.getParticleSize() + 0.02f);
                    if (type == ClickType.RIGHT) treasure.setParticleSize(treasure.getParticleSize() - 0.02f);
                    if (type == ClickType.SHIFT_RIGHT) treasure.setParticleSize(treasure.getParticleSize() - 0.1f);

                    // Boundary's.
                    if (treasure.getParticleSize() > 1f) treasure.setParticleSize(1f);
                    if (treasure.getParticleSize() < 0f) treasure.setParticleSize(0f);

                    treasure.save();
                    treasure.spawnParticlesPlayerRight(user);

                    // Reset the item.
                    this.setItem(new CozyItem()
                                    .setMaterial(Material.WHITE_CANDLE)
                                    .setName("&a&lParticle Size")
                                    .setLore("&7Click to change the particle size.",
                                            "&7This will only work for particles that",
                                            "&7have a dust option.",
                                            "&aCurrent &e" + treasure.getParticleSize(),
                                            "&f+0.1 &7Shift Left Click",
                                            "&f+0.02 &7Left Click",
                                            "&f-0.02 &7Right Click",
                                            "&f-0.1 &7Shift Right Click")
                            , 34);
                })
        );

        // Particle color red.
        this.setItem(new InventoryItem()
                .setMaterial(Material.RED_CANDLE)
                .setName("&c&lParticle Color Red")
                .setLore("&7Click to change the amount of ",
                        "&7red in the particle.",
                        "&7This will only work with particles ",
                        "&7where the colour can change.",
                        "&aCurrent &e" + treasure.getParticleColor(0),
                        "&f+20 &7Shift Left Click",
                        "&f+1 &7Left Click",
                        "&f-1 &7Right Click",
                        "&f-20 &7Shift Right Click")
                .addSlot(41)
                .addAction((ClickAction) (user, type, inventory) -> {
                    if (type == ClickType.SHIFT_LEFT) treasure.increaseRedParticle(20);
                    if (type == ClickType.LEFT) treasure.increaseRedParticle(1);
                    if (type == ClickType.RIGHT) treasure.increaseRedParticle(-1);
                    if (type == ClickType.SHIFT_RIGHT) treasure.increaseRedParticle(-20);

                    // Boundary's.
                    if (treasure.getParticleColor(0) < 0) treasure.increaseRedParticle(255);
                    if (treasure.getParticleColor(0) > 255) treasure.increaseRedParticle(-255);

                    treasure.save();
                    treasure.spawnParticlesPlayerRight(user);

                    // Reset the item.
                    this.setItem(new CozyItem()
                                    .setMaterial(Material.RED_CANDLE)
                                    .setName("&c&lParticle Color Red")
                                    .setLore("&7Click to change the amount of ",
                                            "&7red in the particle.",
                                            "&7This will only work with particles ",
                                            "&7where the colour can change.",
                                            "&aCurrent &e" + treasure.getParticleColor(0),
                                            "&f+20 &7Shift Left Click",
                                            "&f+1 &7Left Click",
                                            "&f-1 &7Right Click",
                                            "&f-20 &7Shift Right Click")
                            , 41);
                })
        );

        // Particle color green.
        this.setItem(new InventoryItem()
                .setMaterial(Material.GREEN_CANDLE)
                .setName("&a&lParticle Color Green")
                .setLore("&7Click to change the amount of ",
                        "&7green in the particle.",
                        "&7This will only work with particles ",
                        "&7where the colour can change.",
                        "&aCurrent &e" + treasure.getParticleColor(1),
                        "&f+20 &7Shift Left Click",
                        "&f+1 &7Left Click",
                        "&f-1 &7Right Click",
                        "&f-20 &7Shift Right Click")
                .addSlot(42)
                .addAction((ClickAction) (user, type, inventory) -> {
                    if (type == ClickType.SHIFT_LEFT) treasure.increaseGreenParticle(20);
                    if (type == ClickType.LEFT) treasure.increaseGreenParticle(1);
                    if (type == ClickType.RIGHT) treasure.increaseGreenParticle(-1);
                    if (type == ClickType.SHIFT_RIGHT) treasure.increaseGreenParticle(-20);

                    // Boundary's.
                    if (treasure.getParticleColor(1) < 0) treasure.increaseGreenParticle(255);
                    if (treasure.getParticleColor(1) > 255) treasure.increaseGreenParticle(-255);

                    treasure.save();
                    treasure.spawnParticlesPlayerRight(user);

                    // Reset the item.
                    this.setItem(new CozyItem()
                                    .setMaterial(Material.GREEN_CANDLE)
                                    .setName("&a&lParticle Color Green")
                                    .setLore("&7Click to change the amount of ",
                                            "&7green in the particle.",
                                            "&7This will only work with particles ",
                                            "&7where the colour can change.",
                                            "&aCurrent &e" + treasure.getParticleColor(1),
                                            "&f+20 &7Shift Left Click",
                                            "&f+1 &7Left Click",
                                            "&f-1 &7Right Click",
                                            "&f-20 &7Shift Right Click")
                            , 42);
                })
        );

        // Particle color blue.
        this.setItem(new InventoryItem()
                .setMaterial(Material.BLUE_CANDLE)
                .setName("&b&lParticle Color Blue")
                .setLore("&7Click to change the amount of ",
                        "&7blue in the particle.",
                        "&7This will only work with particles ",
                        "&7where the colour can change.",
                        "&aCurrent &e" + treasure.getParticleColor(2),
                        "&f+20 &7Shift Left Click",
                        "&f+1 &7Left Click",
                        "&f-1 &7Right Click",
                        "&f-20 &7Shift Right Click")
                .addSlot(43)
                .addAction((ClickAction) (user, type, inventory) -> {
                    if (type == ClickType.SHIFT_LEFT) treasure.increaseBlueParticle(20);
                    if (type == ClickType.LEFT) treasure.increaseBlueParticle(1);
                    if (type == ClickType.RIGHT) treasure.increaseBlueParticle(-1);
                    if (type == ClickType.SHIFT_RIGHT) treasure.increaseBlueParticle(-20);

                    // Boundary's.
                    if (treasure.getParticleColor(2) < 0) treasure.increaseBlueParticle(255);
                    if (treasure.getParticleColor(2) > 255) treasure.increaseBlueParticle(-255);

                    treasure.save();
                    treasure.spawnParticlesPlayerRight(user);

                    // Reset the item.
                    this.setItem(new CozyItem()
                                    .setMaterial(Material.BLUE_CANDLE)
                                    .setName("&b&lParticle Color Blue")
                                    .setLore("&7Click to change the amount of ",
                                            "&7blue in the particle.",
                                            "&7This will only work with particles ",
                                            "&7where the colour can change.",
                                            "&aCurrent &e" + treasure.getParticleColor(2),
                                            "&f+20 &7Shift Left Click",
                                            "&f+1 &7Left Click",
                                            "&f-1 &7Right Click",
                                            "&f-20 &7Shift Right Click")
                            , 43);
                })
        );

        // Fireworks.
        this.setItem(new InventoryItem()
                .setMaterial(Material.FIREWORK_ROCKET)
                .setName("&7&lEdit Fireworks")
                .setLore("&7Click to change the type of fireworks spawned when found.",
                        "&eOnly available with CozyTreasureHuntPlus")
                .addSlot(37)
        );

        // Sounds.
        this.setItem(new InventoryItem()
                .setMaterial(Material.MUSIC_DISC_CAT)
                .setName("&7&lChange Global Sound.")
                .setLore("&7Click to change the sound played for each",
                        "&7player when a treasure is found.",
                        "&eOnly available with CozyTreasureHuntPlus")
                .addSlot(38)
        );

        this.setItem(new InventoryItem()
                .setMaterial(Material.MUSIC_DISC_CAT)
                .setName("&7&lChange Local Sound.")
                .setLore("&7Click to change the sound played by the ",
                        "&7treasure when it is found.",
                        "&eOnly available with CozyTreasureHuntPlus")
                .addSlot(39)
        );
    }

    private void setPage1() {
        // Treasure limit.
        this.setItem(new InventoryItem()
                .setMaterial(Material.CLOCK)
                .setName("&6&lSet Limit")
                .setLore("&7Click to set the amount",
                        "&7of this treasure a player can find.",
                        "&7Setting this to -1 will let the player",
                        "&7find unlimited amounts of this treasure.",
                        "&aCurrent &e" + treasure.getLimit(),
                        "&f+20 &7Shift Left Click",
                        "&f+1 &7Left Click",
                        "&f-1 &7Right Click",
                        "&f-20 &7Shift Right Click")
                .addSlot(28)
                .addAction((ClickAction) (user, type, inventory) -> {
                    if (type == ClickType.SHIFT_LEFT) treasure.setLimit(treasure.getLimit() + 20);
                    if (type == ClickType.LEFT) treasure.setLimit(treasure.getLimit() + 1);
                    if (type == ClickType.RIGHT) treasure.setLimit(treasure.getLimit() - 1);
                    if (type == ClickType.SHIFT_RIGHT) treasure.setLimit(treasure.getLimit() - 20);

                    // Boundary's.
                    if (treasure.getLimit() < -1) treasure.setLimit(-1);

                    treasure.save();

                    // Reset the item.
                    this.setItem(new CozyItem()
                                    .setMaterial(Material.CLOCK)
                                    .setName("&6&lSet Limit")
                                    .setLore("&7Click to set the amount",
                                            "&7of this treasure a player can find.",
                                            "&7Setting this to -1 will let the player",
                                            "&7find unlimited amounts of this treasure.",
                                            "&aCurrent &e" + treasure.getLimit(),
                                            "&f+20 &7Shift Left Click",
                                            "&f+1 &7Left Click",
                                            "&f-1 &7Right Click",
                                            "&f-20 &7Shift Right Click")
                            , 28);
                })
        );

        // Limit message.
        this.setItem(new InventoryItem()
                .setMaterial(Material.CLOCK)
                .setName("&6&lLimit Message")
                .setLore("&7Click to set the limit message.",
                        "&7This message is sent to the player when",
                        "&7the player attempts to click another treasure",
                        "&7while over the limit.",
                        "&aCurrently &e" + treasure.getLimitMessage())
                .addSlot(29)
                .addAction(new AnvilValueAction()
                        .setAnvilTitle("&8&lLimit message")
                        .setAction((value, user) -> {
                            if (value != null) {
                                if (value.isEmpty()) {
                                    treasure.setLimitMessage(null);
                                } else {
                                    treasure.setLimitMessage(value);
                                }
                            }

                            treasure.save();

                            this.setItem(new CozyItem()
                                            .setMaterial(Material.CLOCK)
                                            .setName("&6&lLimit Message")
                                            .setLore("&7Click to set the limit message.",
                                                    "&7This message is sent to the player when",
                                                    "&7the player attempts to click anouther treasure",
                                                    "&7while over the limit.",
                                                    "&aCurrently &e" + treasure.getLimitMessage())
                                    , 29);
                        })
                )
        );

        // Reward bundle.
        this.setItem(new InventoryItem()
                .setMaterial(Material.CHEST)
                .setName("&6&lRewards")
                .setLore("&7Click to change the rewards that",
                        "&7will be given when this treasure is found.")
                .addSlot(31)
                .addAction((ClickAction) (user, type, inventory) -> {
                    CozyInventory back = this;

                    // Create editor inventory.
                    RewardBundleEditorInventory editorInventory = new RewardBundleEditorInventory(this.treasure.getRewardBundle()) {
                        @Override
                        protected void onBundleUpdate(@NotNull RewardBundle bundle) {
                            treasure.setRewardBundle(bundle);
                            treasure.save();
                        }

                        @Override
                        protected @Nullable CozyInventory onBackButton(@NotNull PlayerUser user) {
                            return back;
                        }
                    };

                    // Open the inventory.
                    editorInventory.open(user.getPlayer());
                })
        );

        // Multiple redeeming.
        this.setItem(new InventoryItem()
                .setMaterial(Material.COBWEB)
                .setName("&6&lSet Redeemable Amount")
                .setLore("&7Click to set the redeemable amount.",
                        "&7This defines the amount of times it can be",
                        "&7clicked by unique players before it disappears.",
                        "&7Setting this to -1 will ensure that the treasure",
                        "&7never disappears when a player clicks it.",
                        "&aCurrent &e" + treasure.getRedeemable(),
                        "&f+20 &7Shift Left Click",
                        "&f+1 &7Left Click",
                        "&f-1 &7Right Click",
                        "&f-20 &7Shift Right Click")
                .addSlot(33)
                .addAction((ClickAction) (user, type, inventory) -> {
                    if (type == ClickType.SHIFT_LEFT) treasure.setRedeemable(treasure.getRedeemable() + 20);
                    if (type == ClickType.LEFT) treasure.setRedeemable(treasure.getRedeemable() + 1);
                    if (type == ClickType.RIGHT) treasure.setRedeemable(treasure.getRedeemable() - 1);
                    if (type == ClickType.SHIFT_RIGHT) treasure.setRedeemable(treasure.getRedeemable() - 20);

                    // Boundary's.
                    if (treasure.getRedeemable() < -1) treasure.setRedeemable(-1);

                    treasure.save();

                    // Reset the item.
                    this.setItem(new CozyItem()
                                    .setMaterial(Material.COBWEB)
                                    .setName("&6&lSet Redeemable Amount")
                                    .setLore("&7Click to set the redeemable amount.",
                                            "&7This defines the amount of times it can be",
                                            "&7clicked by unique players before it disappears.",
                                            "&7Setting this to -1 will ensure that the treasure",
                                            "&7never disappears when a player clicks it.",
                                            "&aCurrent &e" + treasure.getRedeemable(),
                                            "&f+20 &7Shift Left Click",
                                            "&f+1 &7Left Click",
                                            "&f-1 &7Right Click",
                                            "&f-20 &7Shift Right Click")
                            , 33);
                })
        );

        // Multiple redeeming message.
        this.setItem(new InventoryItem()
                .setMaterial(Material.COBWEB)
                .setName("&6&lNot Redeemable Message")
                .setLore("&7Click to set the not redeemable message.",
                        "&7This message is sent to the player when",
                        "&7the player attempts to click another treasure",
                        "&7when they have already clicked it.",
                        "&aCurrently &e" + treasure.getRedeemableMessage())
                .addSlot(34)
                .addAction(new AnvilValueAction()
                        .setAnvilTitle("&8&lNot Redeemable Message")
                        .setAction((value, user) -> {
                            if (value != null) {
                                if (value.isEmpty()) {
                                    treasure.setRedeemableMessage(null);
                                } else {
                                    treasure.setRedeemableMessage(value);
                                }
                            }

                            treasure.save();

                            this.setItem(new CozyItem()
                                            .setMaterial(Material.COBWEB)
                                            .setName("&6&lNot Redeemable Message")
                                            .setLore("&7Click to set the not redeemable message.",
                                                    "&7This message is sent to the player when",
                                                    "&7the player attempts to click another treasure",
                                                    "&7when they have already clicked it.",
                                                    "&aCurrently &e" + treasure.getRedeemableMessage())
                                    , 34);
                        })
                )
        );

        // Respawnable toggle.
        this.setItem(new InventoryItem()
                .setMaterial(Material.ENDER_EYE)
                .setName("&6&lRespawnable")
                .setLore("&7Click to toggle if this treasure",
                        "&7will respawn when clicked.",
                        "&aCurrently &e" + treasure.isRespawnable())
                .addSlot(37)
                .addAction((ClickAction) (playerUser, clickType, inventory) -> {
                    treasure.setRespawnable(!treasure.isRespawnable());
                    treasure.save();

                    this.setItem(new CozyItem()
                                    .setMaterial(Material.ENDER_EYE)
                                    .setName("&6&lRespawnable")
                                    .setLore("&7Click to toggle if this treasure",
                                            "&7will respawn when clicked.",
                                            "&aCurrently &e" + treasure.isRespawnable())
                            , 37);
                })
        );

        // Static toggle.
        this.setItem(new InventoryItem()
                .setMaterial(Material.ENDER_EYE)
                .setName("&6&lStatic")
                .setLore("&7Click to toggle if this treasure",
                        "&7will will respawn in the same location.",
                        "&aCurrently &e" + treasure.isStatic())
                .addSlot(38)
                .addAction((ClickAction) (playerUser, clickType, inventory) -> {
                    treasure.setStatic(!treasure.isStatic());
                    treasure.save();

                    this.setItem(new CozyItem()
                                    .setMaterial(Material.ENDER_EYE)
                                    .setName("&6&lStatic")
                                    .setLore("&7Click to toggle if this treasure",
                                            "&7will will respawn in the same location.",
                                            "&aCurrently &e" + treasure.isStatic())
                            , 38);
                })
        );

        // Respawn time.
        this.setItem(new InventoryItem()
                .setMaterial(Material.ENDER_EYE)
                .setName("&6&lRespawn Time")
                .setLore("&7Click to set the respawn time.",
                        "&7When the treasure is clicked, if the treasure",
                        "&7is respawnable it will wait this amount of seconds",
                        "&7and respawn.",
                        "&aCurrent &e" + treasure.getRespawnTime(),
                        "&f+20 &7Shift Left Click",
                        "&f+1 &7Left Click",
                        "&f-1 &7Right Click",
                        "&f-20 &7Shift Right Click")
                .addSlot(39)
                .addAction((ClickAction) (user, type, inventory) -> {
                    if (type == ClickType.SHIFT_LEFT) treasure.setRespawnTime(treasure.getRespawnTime() + 20);
                    if (type == ClickType.LEFT) treasure.setRespawnTime(treasure.getRespawnTime() + 1);
                    if (type == ClickType.RIGHT) treasure.setRespawnTime(treasure.getRespawnTime() - 1);
                    if (type == ClickType.SHIFT_RIGHT) treasure.setRespawnTime(treasure.getRespawnTime() - 20);

                    // Boundary's.
                    if (treasure.getRespawnTime() < 0) treasure.setRespawnTime(0);

                    treasure.save();

                    // Reset the item.
                    this.setItem(new CozyItem()
                                    .setMaterial(Material.ENDER_EYE)
                                    .setName("&6&lRespawn Time")
                                    .setLore("&7Click to set the respawn time.",
                                            "&7When the treasure is clicked, if the treasure",
                                            "&7is respawnable it will wait this amount of seconds",
                                            "&7and respawn.",
                                            "&aCurrent &e" + treasure.getRespawnTime(),
                                            "&f+20 &7Shift Left Click",
                                            "&f+1 &7Left Click",
                                            "&f-1 &7Right Click",
                                            "&f-20 &7Shift Right Click")
                            , 39);
                })
        );

        // Respawn ratio.
        this.setItem(new InventoryItem()
                .setMaterial(Material.PUMPKIN_PIE)
                .setName("&6&lSpawn Ratio")
                .setLore("&7Click to set the spawn ratio.",
                        "&7The amount of treasure to spawn locations.",
                        "&7Example:",
                        "&7 &7 &7 &7 1:2",
                        "&7 &7 &7 &7 Every 2 locations, 1 treasure will spawn.",
                        "&aCurrently &e" + treasure.getSpawnRatio())
                .addSlot(40)
                .addAction(new AnvilValueAction()
                        .setAnvilTitle("&8&lNot Redeemable Message")
                        .setAction((value, user) -> {
                            if (value != null) {
                                if (value.isEmpty()) {
                                    treasure.setSpawnRatio(new Ratio());
                                } else {
                                    try {
                                        Ratio ratio = new Ratio(value);

                                        // Check if the ratio is valid.
                                        if (!ratio.isLeftSmallerOrEqual()) {
                                            user.sendMessage("&7The left number must be smaller or" +
                                                    " equal to the right number.");
                                            return;
                                        }

                                        // Set the spawn ratio.
                                        treasure.setSpawnRatio(ratio);

                                    } catch (Exception exception) {
                                        user.sendMessage("&7Unable to convert to a ratio. Make sure it" +
                                                " follows the format &fnumber:number");
                                    }
                                }
                            }

                            treasure.save();

                            this.setItem(new CozyItem()
                                            .setMaterial(Material.ENDER_EYE)
                                            .setName("&6&lSpawn Ratio")
                                            .setLore("&7Click to set the spawn ratio.",
                                                    "&7The amount of treasure to spawn locations.",
                                                    "&7Example:",
                                                    "&7 &7 &7 &7 1:2",
                                                    "&7 &7 &7 &7 Every 2 locations, 1 treasure will spawn.",
                                                    "&aCurrently &e" + treasure.getSpawnRatio())
                                    , 40);
                        })
                )
        );
    }
}
