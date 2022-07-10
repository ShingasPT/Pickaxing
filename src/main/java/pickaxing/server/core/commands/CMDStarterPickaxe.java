package pickaxing.server.core.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pickaxing.server.core.items.Pickaxe;

public class CMDStarterPickaxe implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        MiniMessage mm = MiniMessage.miniMessage();

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Sorry! You can't do that command unless you are in-game!");
            return true;
        }

        ItemStack pickaxe = new Pickaxe();

        int temp = player.getInventory().firstEmpty();

        // darker main color: <#9254e3>
        // lighter main color: <#b175ff>
        if (temp != -1) {
            player.getInventory().addItem(pickaxe);
        } else {
            player.sendMessage(mm.deserialize("<#b175ff><bold>PICKAXING</bold><newline> <dark_gray>» <gray>You do not have enough space for this pickaxe!"));
        }

        return true;
    }

}
