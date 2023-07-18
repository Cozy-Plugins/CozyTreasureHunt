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

import com.github.cozyplugins.cozylibrary.inventory.InventoryInterface;
import com.github.cozyplugins.cozylibrary.inventory.InventoryItem;
import com.github.cozyplugins.cozylibrary.inventory.action.action.AnvilValueAction;
import com.github.cozyplugins.cozylibrary.inventory.action.action.ClickAction;
import com.github.cozyplugins.cozylibrary.inventory.action.action.ConfirmAction;
import com.github.cozyplugins.cozylibrary.inventory.action.action.PlaceAction;
import com.github.cozyplugins.cozylibrary.item.CozyItem;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import com.github.cozyplugins.cozytreasurehunt.storage.TreasureStorage;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

/**
 * An inventory used to edit a type of treasure.
 */
public class TreasureEditor extends InventoryInterface {

    private final @NotNull Treasure treasure;
    private final TreasureListEditor listEditor;

    private int page;
    private final int LAST_PAGE = 0;
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
                .addSlot(27, 36, 25, 45)
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

                    this.generateModifiers();
                    this.generatePageDescription();

                    // Reset the item.
                    this.setItem(new CozyItem()
                            .setMaterial(Material.YELLOW_STAINED_GLASS_PANE)
                            .setName("&e&lPrevious")
                            .setLore("&7Click to go back a page.",
                                    "&aPage &e" + this.page,
                                    "&aModifiers &e" + this.modifierDescription), 48);
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

                    this.generateModifiers();
                    this.generatePageDescription();

                    // Reset the item.
                    this.setItem(new CozyItem()
                            .setMaterial(Material.YELLOW_STAINED_GLASS_PANE)
                            .setName("&e&lNext")
                            .setLore("&7Click to go to the next page.",
                                    "&aPage &e" + this.page,
                                    "&aModifiers &e" + this.modifierDescription), 48);
                }));

        // Change name.
        this.setItem(new InventoryItem()
                .setMaterial(Material.NAME_TAG)
                .setName("&6&lChange Name")
                .addLore("&7Click to change the treasures name.")
                .addLore("&aCurrently &e" + this.treasure.getName())
                .addSlot(10)
                .addAction(new AnvilValueAction()
                        .setAnvilTitle("&8&lChange name to")
                        .setAction((value, user) -> {
                            if (value != null && !value.equals("")) {
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
                            if (value != null && !value.equals("")) {
                                treasure.setDescription(value);
                                treasure.save();
                            }

                            TreasureEditor treasureEditor = new TreasureEditor(treasure, this.listEditor, page);
                            treasureEditor.open(player.getPlayer());
                        })
                )
        );

        // The treasure block.
        this.setItem(new InventoryItem()
                .setMaterial(this.treasure.getMaterial())
                .setName("&6&lMaterial")
                .addLore("&7Replace this item with another")
                .addLore("&7block or head to set the treasure's material.")
                .addSlot(13)
                .addAction((PlaceAction) (user, item) -> {
                    if (item.getMaterial() == Material.AIR) return;

                    this.treasure.setMaterial(item.getMaterial());
                    this.treasure.save();

                    // Replace the item type.
                    this.setItem(new CozyItem()
                            .setMaterial(this.treasure.getMaterial())
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
        this.modifierDescription = "Cosmetic";
    }

    /**
     * Generates the modifiers for this page.
     * Called when the page is changed.
     */
    private void generateModifiers() {
        // Remove actions.
        this.removeActionRange(27, 44);

        // Set the items on this page.
        if (this.page == 0) this.setPage0();
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
                                    Particle particle = Particle.valueOf(value);
                                    treasure.setParticleType(particle);
                                    treasure.save();
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
                    if (treasure.getParticleColor(0) < 0) treasure.increaseGreenParticle(255);
                    if (treasure.getParticleColor(0) > 255) treasure.increaseGreenParticle(-255);

                    treasure.save();

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

                    // Reset the item.
                    this.setItem(new CozyItem()
                                    .setMaterial(Material.RED_CANDLE)
                                    .setName("&c&lParticle Color Green")
                                    .setLore("&7Click to change the amount of ",
                                            "&7red in the particle.",
                                            "&7This will only work with particles ",
                                            "&7where the colour can change.",
                                            "&aCurrent &e" + treasure.getParticleColor(0),
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
                .setName("&a&lParticle Color Blue")
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

                    // Reset the item.
                    this.setItem(new CozyItem()
                                    .setMaterial(Material.BLUE_CANDLE)
                                    .setName("&a&lParticle Color Blue")
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
    }
}
