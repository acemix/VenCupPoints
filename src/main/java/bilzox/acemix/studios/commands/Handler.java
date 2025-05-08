package bilzox.acemix.studios.commands;

import bilzox.acemix.studios.database.SQLite;
import bilzox.acemix.studios.utils.Colors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Acemix
 * Project: VenCupPoints
 * Date: 4/30/2025 @ 08:50 AM
 */
public class Handler implements CommandExecutor {

    private final SQLite sqLite;
    private final String PREFIX = Colors.set("&6&lVENCUP &7| ");

    public Handler(SQLite sqLite) {
        this.sqLite = sqLite;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(PREFIX + Colors.set("&cUso: /vencuppoints <add/list/removeall>"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add":
                if (!sender.hasPermission("vencuppoints.add")) {
                    sender.sendMessage(PREFIX + Colors.set("&cNo tienes permiso para usar este comando."));
                    return true;
                }

                if (args.length < 4) {
                    sender.sendMessage(PREFIX + Colors.set("&cUso correcto: /vencuppoints add <jugador1> <jugador2> <jugador3>"));
                    return true;
                }

                sqLite.addPoints(args[1], 3);
                sqLite.addPoints(args[2], 2);
                sqLite.addPoints(args[3], 1);

                sender.sendMessage(PREFIX + Colors.set("&a¡Puntos añadidos correctamente!"));
                return true;

            case "list":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    sqLite.sendTopPoints(player);
                } else {
                    sender.sendMessage(PREFIX + "Este comando solo puede ser usado por jugadores.");
                }
                return true;

            case "removeall":
                if (!sender.hasPermission("vencuppoints.removeall")) {
                    sender.sendMessage(PREFIX + Colors.set("&cNo tienes permiso para usar este comando."));
                    return true;
                }

                sqLite.clearPoints();
                sender.sendMessage(PREFIX + Colors.set("&a¡Todos los puntos han sido eliminados!"));
                return true;

            default:
                sender.sendMessage(PREFIX + Colors.set("&cComando no reconocido. Usa /vencuppoints <add/list/removeall>"));
                return true;
        }
    }
}
