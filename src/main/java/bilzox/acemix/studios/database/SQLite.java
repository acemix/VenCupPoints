package bilzox.acemix.studios.database;

import bilzox.acemix.studios.utils.Colors;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acemix
 * Project: VenCupPoints
 * Date: 4/29/2025 @ 12:50 AM
 */
public class SQLite {

    private final JavaPlugin plugin;
    private Connection connection;

    public SQLite(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        try {
            File pluginFolder = plugin.getDataFolder();
            if (!pluginFolder.exists()) {
                pluginFolder.mkdirs();
            }

            File dbFile = new File(pluginFolder, "sqlite.db");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());

            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS points (name TEXT PRIMARY KEY, points INTEGER)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearPoints() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM points");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPoints(String name, int points) {
        try {
            PreparedStatement select = connection.prepareStatement(
                    "SELECT points FROM points WHERE name = ?"
            );
            select.setString(1, name);
            ResultSet rs = select.executeQuery();

            if (rs.next()) {
                int current = rs.getInt("points");
                PreparedStatement update = connection.prepareStatement(
                        "UPDATE points SET points = ? WHERE name = ?"
                );
                update.setInt(1, current + points);
                update.setString(2, name);
                update.executeUpdate();
            } else {
                PreparedStatement insert = connection.prepareStatement(
                        "INSERT INTO points (name, points) VALUES (?, ?)"
                );
                insert.setString(1, name);
                insert.setInt(2, points);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPoints(String name) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT points FROM points WHERE name = ?"
            );
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("points");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String[]> getTopPlayers() {
        List<String[]> top = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT name, points FROM points ORDER BY points DESC LIMIT 10");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                top.add(new String[]{rs.getString("name"), String.valueOf(rs.getInt("points"))});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return top;
    }

    public void sendTopPoints(Player player) {
        player.sendMessage(Colors.set(""));
        player.sendMessage(Colors.set("&6        &lVenCup &7| &fTablero"));
        player.sendMessage(Colors.set(""));

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, points FROM points ORDER BY points DESC");

            int rank = 1;
            while (rs.next() && rank <= 8) {
                String playerName = rs.getString("name");
                int points = rs.getInt("points");

                String lineColor;
                String rankColor;

                if (rank == 1) {
                    lineColor = "&e";
                    rankColor = "&f";
                } else if (rank == 2) {
                    lineColor = "&7";
                    rankColor = "&e";
                } else if (rank == 3) {
                    lineColor = "&6";
                    rankColor = "&e";
                } else {
                    lineColor = "&8";
                    rankColor = "&f";
                }

                String line = "          " + lineColor + "#" + rank + " " + rankColor + playerName + " &7- &f" + points;
                player.sendMessage(Colors.set(line));
                rank++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        player.sendMessage(Colors.set(""));
        player.sendMessage(Colors.set("&6   Powered by Bilzox Studios"));
        player.sendMessage(Colors.set(""));
    }
}