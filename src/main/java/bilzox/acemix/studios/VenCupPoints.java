package bilzox.acemix.studios;

import bilzox.acemix.studios.commands.Handler;
import bilzox.acemix.studios.database.SQLite;
import bilzox.acemix.studios.expansion.PointsHolder;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Acemix
 * Project: VenCupPoints
 * Date: 4/29/2025 @ 12:50 AM
 */
public final class VenCupPoints extends JavaPlugin {

    private static VenCupPoints instance;
    private SQLite sqLite;

    @Override
    public void onEnable() {
        instance = this;
        sqLite = new SQLite(this);
        sqLite.connect();

        getCommand("vencuppoints").setExecutor(new Handler(sqLite));
        new PointsHolder(this, sqLite).register();
    }

    @Override
    public void onDisable() {
        sqLite.disconnect();
    }

    public static VenCupPoints getInstance() {
        return instance;
    }

}