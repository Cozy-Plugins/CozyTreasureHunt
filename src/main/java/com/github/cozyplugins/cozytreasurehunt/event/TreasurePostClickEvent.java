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

package com.github.cozyplugins.cozytreasurehunt.event;

import com.github.cozyplugins.cozylibrary.item.CozyItem;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import com.github.cozyplugins.cozytreasurehunt.TreasureLocation;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when the treasure click event has passed.
 * <li>
 * Plugins can listen to this event to add effects when someone clicks the treasure.
 * </li>
 */
public class TreasurePostClickEvent extends CozyEvent {

    private final @NotNull TreasureLocation treasureLocation;
    private final @NotNull PlayerInteractEvent event;

    /**
     * Used to create a treasure click event.
     *
     * @param treasureLocation The instance of the treasure location.
     * @param event            The instance of the {@link PlayerInteractEvent} event.
     */
    public TreasurePostClickEvent(
            @NotNull TreasureLocation treasureLocation,
            @NotNull PlayerInteractEvent event
    ) {

        this.treasureLocation = treasureLocation;
        this.event = event;
    }

    /**
     * Used to get the player that clicked the treasure.
     *
     * @return The player user.
     */
    public @NotNull PlayerUser getPlayer() {
        return new PlayerUser(this.event.getPlayer());
    }

    /**
     * Used to get the instance of the treasure that was clicked.
     */
    public @NotNull Treasure getTreasure() {
        return this.treasureLocation.getTreasure();
    }

    /**
     * Used to get the location of the treasure that was clicked.
     *
     * @return The instance of the location.
     */
    public @NotNull Location getLocation() {
        return this.treasureLocation.getLocation();
    }

    /**
     * Used to get the instance of the treasure location.
     * This class will also include the treasure and other
     * data fields about the treasure that won't be found in the treasure class.
     *
     * @return The treasure location instance.
     */
    public @NotNull TreasureLocation getTreasureLocation() {
        return this.treasureLocation;
    }

    /**
     * Used to get the item in the player's hand that was used
     * to click the treasure.
     *
     * @return The instance of the item.
     */
    public @Nullable CozyItem getItemInHand() {
        return this.event.getItem() == null ? null : new CozyItem(this.event.getItem());
    }

    /**
     * Used to get the type of action used on the treasure block.
     *
     * @return The click action.
     */
    public @NotNull ClickAction getActionType() {
        return this.event.getAction() == Action.LEFT_CLICK_BLOCK ? ClickAction.LEFT : ClickAction.RIGHT;
    }

    /**
     * Used to get the instance of the block that was clicked.
     *
     * @return The instance of the block.
     */
    public @Nullable Block getBlock() {
        return this.event.getClickedBlock();
    }

    /**
     * Used to get the equipment slot that the treasure was clicked with.
     *
     * @return The instance of the equipment slot.
     */
    public @Nullable EquipmentSlot getHand() {
        return this.event.getHand();
    }

    /**
     * Used to get the exact position on the block that
     * the treasure was clicked.
     *
     * @return The instance of the vector.
     */
    public @Nullable Vector getClickedPosition() {
        return this.event.getClickedPosition();
    }
}
