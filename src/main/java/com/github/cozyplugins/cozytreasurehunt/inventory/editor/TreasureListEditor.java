package com.github.cozyplugins.cozytreasurehunt.inventory.editor;

import com.github.cozyplugins.cozylibrary.inventory.InventoryInterface;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;

/**
 * An inventory that displays the list of types of
 * treasure that the player can edit.
 */
public class TreasureListEditor extends InventoryInterface {

    public TreasureListEditor() {
        super(54, "&8&lTreasure Editor");
    }

    @Override
    protected void onGenerate(PlayerUser player) {

    }
}
