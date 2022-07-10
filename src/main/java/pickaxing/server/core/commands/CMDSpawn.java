package pickaxing.server.core.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pickaxing.server.core.Main;

import java.util.ArrayList;
import java.util.List;

public class CMDSpawn implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        MiniMessage mm = MiniMessage.miniMessage();

        if (args.length == 0) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage("Sorry! You can't do that command unless you are in-game!");
                return true;
            }
            p.teleport(getSpawn());
            sender.sendMessage(mm.deserialize("<#9254e3>Spawn <dark_gray>| <#b175ff>Successfully teleported to spawn"));
        } else {
            switch (args[0]) {
                case "getLocation" -> {
                    if(sender.hasPermission("admin.spawn")) {
                        TagResolver placeholder = TagResolver.resolver(Placeholder.parsed("spawn",String.valueOf(getSpawn())));
                        sender.sendMessage(mm.deserialize("<#9254e3>Spawn <dark_gray>| <#b175ff><spawn>",placeholder));
                    }
                }
                case "setLocation" -> {
                    if(sender.hasPermission("admin.spawn")) {
                        if (!(sender instanceof Player p)) {
                            sender.sendMessage("Sorry! You can't do that command unless you are in-game!");
                            return true;
                        }
                        sender.sendMessage(mm.deserialize("<#9254e3>Spawn <dark_gray>| <#b175ff>Successfully set spawn to your location"));
                    }
                }
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("admin.spawn")) {
                return StringUtil.copyPartialMatches(args[0], List.of("getLocation", "setLocation"), new ArrayList<>());
            }
        }
        return null;
    }

    private Location getSpawn() {

        Main instance = Main.getInstance();
        World spawnWorld = Bukkit.getWorld(instance.getConfig().getString("General.Spawn.World"));

        return new Location(spawnWorld,
                instance.getConfig().getDouble("General.Spawn.X"),
                instance.getConfig().getDouble("General.Spawn.Y"),
                instance.getConfig().getDouble("General.Spawn.Z"),
                (float) instance.getConfig().getDouble("General.Spawn.Yaw"),
                (float) instance.getConfig().getDouble("General.Spawn.Pitch"));
    }

    private void setSpawn(Location newSpawn) {

        Main instance = Main.getInstance();

        instance.getConfig().set("General.Spawn.World", newSpawn.getWorld());
        instance.getConfig().set("General.Spawn.X", newSpawn.getX());
        instance.getConfig().set("General.Spawn.Y", newSpawn.getY());
        instance.getConfig().set("General.Spawn.Z", newSpawn.getZ());
        instance.getConfig().set("General.Spawn.Yaw", newSpawn.getYaw());
        instance.getConfig().set("General.Spawn.Pitch", newSpawn.getPitch());

        instance.saveDefaultConfig();
    }
}
