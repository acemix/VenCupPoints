package bilzox.acemix.studios.expansion;

import bilzox.acemix.studios.VenCupPoints;
import bilzox.acemix.studios.database.SQLite;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Acemix
 * Project: VenCupPoints
 * Date: 4/29/2025 @ 12:50 AM
 */
public class PointsHolder extends PlaceholderExpansion {

    private final VenCupPoints plugin;
    private final SQLite sqlite;

    public PointsHolder(VenCupPoints plugin, SQLite sqlite) {
        this.plugin = plugin;
        this.sqlite = sqlite;
    }

    @Override
    public String getIdentifier() {
        return "vencup";
    }

    @Override
    public String getAuthor() {
        return "Acemix";
    }

    @Override
    public String getVersion() {
        return "1.2";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equalsIgnoreCase("points") && player != null) {
            return String.valueOf(sqlite.getPoints(player.getName()));
        }

        if (identifier.startsWith("points_name_")) {
            try {
                int index = Integer.parseInt(identifier.replace("points_name_", "")) - 1;
                List<String[]> top = sqlite.getTopPlayers();
                if (index >= 0 && index < top.size()) {
                    return top.get(index)[0];
                } else {
                    return "&c[X]";
                }
            } catch (NumberFormatException ignored) {
            }
        }

        if (identifier.startsWith("points_total_")) {
            try {
                int index = Integer.parseInt(identifier.replace("points_total_", "")) - 1;
                List<String[]> top = sqlite.getTopPlayers();
                if (index >= 0 && index < top.size()) {
                    return top.get(index)[1];
                } else {
                    return "&c[?]";
                }
            } catch (NumberFormatException ignored) {
            }
        }

        return null;
    }
}
