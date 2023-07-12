package com.github.cozyplugins.cozytreasurehunt.inventory.editor;

import com.github.cozyplugins.cozylibrary.inventory.inventory.ConfigurationDirectoryEditor;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;
import com.github.cozyplugins.cozytreasurehunt.storage.TreasureStorage;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Represents the treasure directory editor.
 */
public class TreasureDirectoryEditor extends ConfigurationDirectoryEditor {

    /**
     * Used to create a treasure directory editor.
     */
    public TreasureDirectoryEditor() {
        super(TreasureStorage.getMedium());
    }

    @Override
    public void onOpenFile(@NotNull File file, @NotNull PlayerUser playerUser) {
        new TreasureListEditor(file).open(playerUser.getPlayer());
    }
}
