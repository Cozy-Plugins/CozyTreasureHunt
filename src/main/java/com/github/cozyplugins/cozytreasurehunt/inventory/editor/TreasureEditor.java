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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An inventory used to edit a type of treasure.
 */
public class TreasureEditor extends InventoryInterface {

    private final @NotNull Treasure treasure;
    private int page;
    private final int LAST_PAGE = 0;
    private @NotNull String modifierDescription;

    /**
     * Used to create a treasure editor.
     *
     * @param treasure The treasure to edit.
     */
    public TreasureEditor(@NotNull Treasure treasure) {
        super(54, "&8&l" + treasure.getIdentifier());

        this.treasure = treasure;
        this.page = 0;
        this.generatePageDescription();
    }

    /**
     * Used to create a treasure editor.
     * <li>
     * The page can be changed to reveal new customisable options.
     * </li>
     *
     * @param treasure The treasure to edit.
     * @param page     The page number.
     */
    public TreasureEditor(@NotNull Treasure treasure, int page) {
        this(treasure);

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
                    TreasureListEditor editor = new TreasureListEditor();
                    editor.open(user.getPlayer());
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
                .addAction(new AnvilValueAction() {
                    @Override
                    public @NotNull String getAnvilTitle() {
                        return "&8&lChange name to";
                    }

                    @Override
                    public void onValue(@Nullable String text, @NotNull PlayerUser playerUser) {
                        if (text != null && !text.equals("")) {
                            treasure.setName(text);
                            treasure.save();
                        }

                        TreasureEditor treasureEditor = new TreasureEditor(treasure, page);
                        treasureEditor.open(player.getPlayer());
                    }
                })
        );

        // Change description.
        this.setItem(new InventoryItem()
                .setMaterial(Material.NAME_TAG)
                .setName("&6&lChange Description")
                .addLore("&7Click to change the treasures description.")
                .addLore("&aCurrently &e" + this.treasure.getDescription())
                .addSlot(11)
                .addAction(new AnvilValueAction() {
                    @Override
                    public @NotNull String getAnvilTitle() {
                        return "&8&lChange description to";
                    }

                    @Override
                    public void onValue(@Nullable String text, @NotNull PlayerUser playerUser) {
                        if (text != null && !text.equals("")) {
                            treasure.setDescription(text);
                            treasure.save();
                        }

                        TreasureEditor treasureEditor = new TreasureEditor(treasure, page);
                        treasureEditor.open(player.getPlayer());
                    }
                })
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
                    Treasure clone = this.treasure.clone();
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
                .addAction(new ConfirmAction() {
                    @Override
                    public @NotNull String getTitle() {
                        return "&8&lConfirm to delete treasure";
                    }

                    @Override
                    public void onConfirm(@NotNull PlayerUser playerUser) {
                        TreasureStorage.delete(treasure.getIdentifier());
                        playerUser.sendMessage("&7Treasure with name &f" + treasure.getName() + "&7 has been deleted.");

                        TreasureListEditor listEditor = new TreasureListEditor();
                        listEditor.open(playerUser.getPlayer());
                    }

                    @Override
                    public void onAbort(@NotNull PlayerUser playerUser) {
                        TreasureEditor editor = new TreasureEditor(treasure, page);
                        editor.open(playerUser.getPlayer());
                    }
                })
        );
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
        this.removeActionRange(27, 45);

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
                        "&7when a player finds this treasure.")
                .addSlot(28)
                .addAction(new AnvilValueAction() {
                    @Override
                    public @NotNull String getAnvilTitle() {
                        return "&8&lChange public broadcast message to";
                    }

                    @Override
                    public void onValue(@Nullable String value, @NotNull PlayerUser user) {
                        // Value can equal "".
                        if (value != null) {
                            treasure.setPublicBroadcastMessage(value);
                            treasure.save();
                        }

                        TreasureEditor editor = new TreasureEditor(treasure);
                        editor.open(user.getPlayer());
                    }
                })
        );


        // Private broadcast message.
        this.setItem(new InventoryItem()
                .setMaterial(Material.NAME_TAG)
                .setName("&6&lChange Private Broadcast Message")
                .setLore("&7Click to change the private broadcast message.",
                        "&7This message will be sent to the player ",
                        "&7that finds this treasure.")
                .addSlot(28)
                .addAction(new AnvilValueAction() {
                    @Override
                    public @NotNull String getAnvilTitle() {
                        return "&8&lChange private broadcast message to";
                    }

                    @Override
                    public void onValue(@Nullable String value, @NotNull PlayerUser user) {
                        // Value can equal "".
                        if (value != null) {
                            treasure.setPrivateBroadcastMessage(value);
                            treasure.save();
                        }

                        TreasureEditor editor = new TreasureEditor(treasure);
                        editor.open(user.getPlayer());
                    }
                })
        );
    }
}
