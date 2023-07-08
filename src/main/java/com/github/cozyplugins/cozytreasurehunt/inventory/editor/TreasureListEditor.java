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
import com.github.cozyplugins.cozylibrary.inventory.action.action.ClickAction;
import com.github.cozyplugins.cozylibrary.item.CozyItem;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * An inventory that displays the list of types of
 * treasure that the player can edit.
 */
public class TreasureListEditor extends InventoryInterface {

    private int page;

    /**
     * Used to create a treasure editor.
     * Displays the list of treasure.
     */
    public TreasureListEditor() {
        super(54, "&8&lTreasure Editor");

        this.page = 0;
    }

    /**
     * Used to create a treasure editor.
     * Displays the list of treasure.
     *
     * @param page The page.
     */
    public TreasureListEditor(int page) {
        this();
        this.page = page;
    }

    @Override
    protected void onGenerate(PlayerUser player) {
        // Reset all slots.
        this.removeActionRange(0, 53);
        this.setItem(new CozyItem().setMaterial(Material.AIR), 0 ,53);

        // Background.
        this.setItem(new InventoryItem()
                .setMaterial(Material.GRAY_STAINED_GLASS_PANE)
                .setName("&7")
                .addSlot(45, 53)
        );

        // Create new treasure.
        this.setItem(new InventoryItem()
                .setMaterial(Material.LIME_STAINED_GLASS_PANE)
                .setName("&a&lCreate New Treasure")
                .setLore("&7Click to create a new treasure type.")
                .addSlot(47)
                .addAction((ClickAction) (playerUser, clickType, inventory) -> {
                    Treasure treasure = new Treasure(UUID.randomUUID());
                    treasure.save();
                    this.onGenerate(playerUser);
                })
        );
    }
}
