package com.github.cozyplugins.cozytreasurehunt;

import com.github.cozyplugins.cozylibrary.CozyPlugin;
import com.github.cozyplugins.cozytreasurehunt.database.table.TreasureTable;
import com.github.smuddgge.squishydatabase.DatabaseCredentials;
import com.github.smuddgge.squishydatabase.DatabaseFactory;
import com.github.smuddgge.squishydatabase.interfaces.Database;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>Represents the main plugin class</h1>
 */
public final class CozyTreasureHunt extends CozyPlugin {

    @SuppressWarnings("all")
    private static @NotNull Database database;

    @Override
    public boolean enableCommandDirectory() {
        return true;
    }

    @Override
    public void onCozyEnable() {

        // Create the database.
        DatabaseFactory databaseFactory = DatabaseFactory.SQLITE;
        CozyTreasureHunt.database = databaseFactory.create(new DatabaseCredentials(this.getDataFolder() + "/database.sqlite3"));

        // Create the database tables.
        CozyTreasureHunt.database.createTable(new TreasureTable());
    }

    /**
     * <h1>Used to get the instance of the database</h1>
     *
     * @return The instance of the database.
     */
    public static @NotNull Database getDatabase() {
        return CozyTreasureHunt.database;
    }
}
