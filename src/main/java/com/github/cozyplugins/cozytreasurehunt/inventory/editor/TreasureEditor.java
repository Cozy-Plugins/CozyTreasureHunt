package com.github.cozyplugins.cozytreasurehunt.inventory.editor;

import com.github.cozyplugins.cozylibrary.inventory.InventoryInterface;
import com.github.cozyplugins.cozylibrary.inventory.InventoryItem;
import com.github.cozyplugins.cozylibrary.inventory.action.action.ClickAction;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>An inventory used to edit a type of treasure</h1>
 */
public class TreasureEditor extends InventoryInterface {

    private @NotNull Treasure treasure;
    private int page;

    /**
     * <h1>Used to create a treasure editor</h1>
     *
     * @param treasure The treasure to edit.
     */
    public TreasureEditor(@NotNull Treasure treasure) {
        super(54, "&8&l" + treasure.getIdentifier());

        this.treasure = treasure;
        this.page = 0;
    }

    /**
     * <h1>Used to create a treasure editor</h1>
     * <li>The page can be changed to reveal new customisable options.</li>
     *
     * @param treasure The treasure to edit.
     * @param page The page number.
     */
    public TreasureEditor(@NotNull Treasure treasure, int page) {
        this(treasure);

        this.page = page;
    }

    @Override
    protected void onGenerate(PlayerUser player) {

        // Create background.
        this.setItem(new InventoryItem()
                .setMaterial(Material.GRAY_STAINED_GLASS_PANE)
                .setName("&7")
                .addSlotRange(0, 26)
                .addSlot(27, 36, 25, 45)
                .addSlotRange(45, 53));

        // Back button.
        this.setItem(new InventoryItem()
                .setMaterial(Material.LIME_STAINED_GLASS_PANE)
                .setName("&a&lBack")
                .addLore("&7Click to go back to the list of treasure types.")
                .addAction((ClickAction) (user, type) -> {
                    user.getPlayer().closeInventory();
                    TreasureListEditor editor = new TreasureListEditor();
                    editor.open(user.getPlayer());
                }));

        // Previous button.
        this.setItem(new InventoryItem()
                .setMaterial(Material.YELLOW_STAINED_GLASS_PANE)
                .setName("&e&lPrevious")
                .addLore("&7Click to go back a page.")
                .addAction((ClickAction) (user, type) -> {
                    if (this.page <= 0) return;

                    user.getPlayer().closeInventory();
                    TreasureEditor editor = new TreasureEditor(this.treasure, this.page - 1);
                    editor.open(user.getPlayer());
                }));

        // Next button.
        this.setItem(new InventoryItem()
                .setMaterial(Material.YELLOW_STAINED_GLASS_PANE)
                .setName("&e&lNext")
                .addLore("&7Click to go forward a page.")
                .addAction((ClickAction) (user, type) -> {
                    user.getPlayer().closeInventory();
                    TreasureEditor editor = new TreasureEditor(this.treasure, this.page + 1);
                    editor.open(user.getPlayer());
                }));

    }
}
