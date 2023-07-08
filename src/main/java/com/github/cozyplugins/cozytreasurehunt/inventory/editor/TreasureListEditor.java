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
import com.github.cozyplugins.cozytreasurehunt.storage.TreasureStorage;
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
                .addSlotRange(45, 53)
        );

        // Previous.
        this.setItem(new InventoryItem()
                .setMaterial(Material.YELLOW_STAINED_GLASS_PANE)
                .setName("&e&lPrevious")
                .setLore("&7Click to go back a page.")
                .addSlot(48)
                .addAction((ClickAction) (playerUser, clickType, inventory) -> {
                    this.page -= 1;
                    if (this.page < 0) this.page = 0;
                    this.onGenerate(playerUser);
                })
        );

        // Next.
        this.setItem(new InventoryItem()
                .setMaterial(Material.YELLOW_STAINED_GLASS_PANE)
                .setName("&e&lNext")
                .setLore("&7Click to go to the next page.")
                .addSlot(50)
                .addAction((ClickAction) (playerUser, clickType, inventory) -> {
                    this.page += 1;
                    if (this.page > this.getLastPageNumber()) this.page = this.getLastPageNumber();
                    this.onGenerate(playerUser);
                })
        );

        // Create new treasure.
        this.setItem(new InventoryItem()
                .setMaterial(Material.LIME_STAINED_GLASS_PANE)
                .setName("&a&lCreate New Treasure")
                .setLore("&7Click to create a new treasure type.")
                .addSlot(52)
                .addAction((ClickAction) (playerUser, clickType, inventory) -> {
                    Treasure treasure = new Treasure(UUID.randomUUID());
                    treasure.save();
                    this.onGenerate(playerUser);
                })
        );

        int from = (this.page) * 45;
        int to = ((this.page + 1) * 45) -1;
        int treasureIndex = -1;
        int slot = -1;

        System.out.println(from);
        System.out.println(to);

        for (Treasure treasure : TreasureStorage.getAll()) {
            System.out.println(treasure.getIdentifier());
            treasureIndex += 1;
            if (treasureIndex < from) continue;
            if (treasureIndex > to) continue;
            slot += 1;

            this.setItem(new InventoryItem()
                    .setMaterial(treasure.getMaterial())
                    .setName("&a&l" + treasure.getName())
                    .setLore("&7Click to edit this treasure.",
                            "&f" + treasure.getDescription(),
                            "&8&l--------------",
                            "&aMaterial &e" + treasure.getMaterial(),
                            "&aHead Database Value &e" + treasure.getHdb(),
                            "&7Public Broadcast Message &f" + treasure.getPublicBroadcastMessage(),
                            "&7Private Broadcast Message &f" + treasure.getPrivateBroadcastMessage(),
                            "&7Particle Type &f" + treasure.getParticleType(),
                            "&7Particle Amount &f" + treasure.getParticleAmount(),
                            "&7Particle Color"
                                    + " &c" + treasure.getParticleColor(0)
                                    + " &a" + treasure.getParticleColor(1)
                                    + " &b" + treasure.getParticleColor(2),
                            "&7Particle Size &f" + treasure.getParticleSize())
                    .addSlot(slot)
                    .addAction((ClickAction) (playerUser, clickType, inventory) -> {
                        TreasureEditor editor = new TreasureEditor(treasure);
                        editor.open(playerUser.getPlayer());
                    })
            );
        }
    }

    /**
     * Used to get the last page number.
     *
     * @return The last page number.
     */
    private int getLastPageNumber() {
        return TreasureStorage.getAmount() / 45;
    }
}
