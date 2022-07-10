package pickaxing.server.core;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import pickaxing.server.core.utils.ChatUtils;

public class Events implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPlayedBefore()) {
            e.joinMessage(ChatUtils.getWelcomeMessage(p));
        } else {
            e.joinMessage(ChatUtils.getNewWelcomeMessage(p));
        }

        new BukkitRunnable() {

            @Override
            public void run() {

                if (p.isOnline()) {
                    MiniMessage mm = MiniMessage.miniMessage();

                    String online = String.valueOf(Bukkit.getOnlinePlayers().size());
                    String max = String.valueOf(Main.getInstance().getServer().getMaxPlayers());

                    Component header = Component.text("                               ")
                            .append(Component.newline())
                            .append(mm.deserialize("<#b175ff><bold>PICKAXING"))
                            .append(Component.newline());

                    Component footer = Component.newline()
                            .append(Component.text(ChatColor.GRAY + online + ChatColor.DARK_GRAY + "/" + ChatColor.GRAY + max))
                            .append(Component.newline());

                    TagResolver displayNamePlaceholders = TagResolver.resolver(Placeholder.component("prefix", ChatUtils.getPlayerPrefix(p)),
                            Placeholder.component("name", p.displayName()),
                            Placeholder.component("suffix", ChatUtils.getPlayerSuffix(p)));

                    Component displayName = mm.deserialize("<prefix><gray><name><suffix>", displayNamePlaceholders);

                    p.sendPlayerListHeaderAndFooter(header,footer);
                    p.playerListName(displayName);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        e.renderer((source, sourceDisplayName, message, viewer) -> {
            TextComponent.Builder textComponent = ChatUtils.getChatHead(source);
            if (viewer instanceof ConsoleCommandSender) {
                return ChatUtils.getConsoleMessage(source, message);
            }
            return ChatUtils.getChatMessage(source, message, textComponent);
        });
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (container.has(new NamespacedKey(Main.getInstance(), "pickaxe"), PersistentDataType.INTEGER)) e.setCancelled(true);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Bukkit.broadcast(Component.text("click"));
        if (e.getAction().isRightClick()) {
            Bukkit.broadcast(Component.text("is right click"));
            ItemStack item = e.getItem();
            if (item != null) {
                Bukkit.broadcast(Component.text("item not null"));
                PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
                if (container.has(new NamespacedKey(Main.getInstance(), "pickaxe"), PersistentDataType.INTEGER)) {
                    Bukkit.broadcast(Component.text("is pick"));
                    MiniMessage mm = MiniMessage.miniMessage();
                    Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, mm.deserialize("<#b175ff>Pickaxe Info"));
                    inv.setItem(2, item);
                    e.getPlayer().openInventory(inv);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        MiniMessage mm = MiniMessage.miniMessage();
        if (e.getView().title().equals(mm.deserialize("<#b175ff>Pickaxe Info"))) {
            e.setCancelled(true);
        }
    }
}