package com.github.cozyplugins.cozytreasurehunt.inventory.editor;

import com.github.cozyplugins.cozylibrary.inventory.InventoryInterface;
import com.github.cozyplugins.cozylibrary.inventory.InventoryItem;
import com.github.cozyplugins.cozylibrary.inventory.action.action.ClickAction;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * An inventory used to edit a type of treasure.
 */
public class TreasureEditor extends InventoryInterface {

    private final @NotNull Treasure treasure;
    private int page;

    /**
     * Used to create a treasure editor.
     *
     * @param treasure The treasure to edit.
     */
    public TreasureEditor(@NotNull Treasure treasure) {
        super(54, "&8&l" + treasure.getIdentifier());

        this.treasure = treasure;
        this.page = 0;
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
    }

    @Override
    protected void onGenerate(PlayerUser player) {

        // Create background.
        this.setItem(new InventoryItem().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setName("&7").addSlotRange(0, 26).addSlot(27, 36, 25, 45).addSlotRange(45, 53));

        // Back button.
        this.setItem(new InventoryItem().setMaterial(Material.LIME_STAINED_GLASS_PANE).setName("&a&lBack").addLore("&7Click to go back to the list of treasure types.").addAction((ClickAction) (user, type) -> {
            user.getPlayer().closeInventory();
            TreasureListEditor editor = new TreasureListEditor();
            editor.open(user.getPlayer());
        }));

        // Previous button.
        this.setItem(new InventoryItem().setMaterial(Material.YELLOW_STAINED_GLASS_PANE).setName("&e&lPrevious").addLore("&7Click to go back a page.").addAction((ClickAction) (user, type) -> {
            if (this.page <= 0) return;
            this.page -= 1;
            this.generateModifiers();
        }));

        // Next button.
        this.setItem(new InventoryItem().setMaterial(Material.YELLOW_STAINED_GLASS_PANE).setName("&e&lNext").addLore("&7Click to go forward a page.").addAction((ClickAction) (user, type) -> {
            this.page += 1;
            this.generateModifiers();
        }));

        // Change name.
        this.setItem(new InventoryItem()
                .setMaterial(Material.NAME_TAG)
                .setName("&6&lChange name")
                .addLore("&7Click to change the treasures name.")
                .addSlot(10)
        );
    }

    private void generateModifiers() {

    }
}
