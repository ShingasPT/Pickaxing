package pickaxing.server.core.utils;

import com.destroystokyo.paper.ClientOption;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pickaxing.server.core.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatUtils {

    private static final MiniMessage mm = MiniMessage.miniMessage();

    public static TextComponent.Builder getChatHead(Player p) {
        BufferedImage c;
        TextComponent.Builder textComponent = Component.text();
        char pixel = '#';

        URL url = p.getPlayerProfile().getTextures().getSkin();
        try {
            c = ImageIO.read(url);
        } catch (IOException ex) {
            return null;
        }

        for (int x = 0; x < 10; x++) {
            if (p.getClientOption(ClientOption.SKIN_PARTS).hasHatsEnabled()) {
                for (int y = 0; y < 8; y++) {
                    Color color = new Color(c.getRGB(40+y, 8+x-2), true);
                    if (color.getAlpha() <= 254) {
                        textComponent.append(Component.text(pixel + "!").font(Key.key("custom", "custom")).color(TextColor.color(c.getRGB(8+y, 8+x-2))));
                    } else {
                        textComponent.append(Component.text(pixel + "!").font(Key.key("custom", "custom")).color(TextColor.color(c.getRGB(40+y, 8+x-2))));
                    }
                }
            } else {
                for (int y = 0; y < 8; y++) {
                    textComponent.append(Component.text(pixel + "!").font(Key.key("custom", "custom")).color(TextColor.color(c.getRGB(8+y, 8+x-2))));
                }
            }
            switch (x) {
                case 1 -> pixel = '#';
                case 2 -> pixel = '$';
                case 3 -> pixel = '%';
                case 4 -> pixel = '&';
                case 5 -> pixel = '\'';
                case 6 -> pixel = '(';
                case 7 -> pixel = ')';
                case 8 -> pixel = '*';
            }
            textComponent.append(Component.text("!!!!!!!!").font(Key.key("custom", "custom")));
        }
        return textComponent;
    }

    public static Component getConsoleMessage(Player p, Component message) {

        Component prefix = getPlayerPrefix(p);
        Component suffix = getPlayerSuffix(p);

        String plain = PlainTextComponentSerializer.plainText().serialize(message);
        Component finalMessage;

        if (p.hasPermission("chat.color")) {
            finalMessage = mm.deserialize("<white>").append(message);
            if (p.hasPermission("chat.admin")) {
                finalMessage = getColoredMessage("&f" + plain);
            }
        } else {
            finalMessage = mm.deserialize("<gray>").append(message);
        }

        TagResolver placeholders = TagResolver.resolver(Placeholder.component("prefix", prefix),
                Placeholder.component("name", p.displayName()),
                Placeholder.component("suffix", suffix),
                Placeholder.component("message", finalMessage));

        return mm.deserialize("<prefix><gray><name><suffix><dark_gray> » <gray><message>",placeholders);
    }

    public static Component getChatMessage(Player p, Component message, TextComponent.Builder textComponent) {

        Component prefix = getPlayerPrefix(p);
        Component suffix = getPlayerSuffix(p);

        TagResolver placeholders = TagResolver.resolver(Placeholder.component("prefix", prefix),
                Placeholder.component("name", p.displayName()),
                Placeholder.component("suffix", suffix));

        Component displayName = mm.deserialize("<prefix><gray><name><suffix>",placeholders);

        TextComponent.Builder chatTextComponent;
        chatTextComponent = textComponent
                .append(Component.text(("\"\"\"\"\"\"\"\"\"\"\"")).font(Key.key("custom", "custom")))
                .append(displayName)
                .append(mm.deserialize("<dark_gray> » "));

        String plain = PlainTextComponentSerializer.plainText().serialize(message);
        Component finalMessage;

        if (p.hasPermission("chat.color")) {
            finalMessage = mm.deserialize("<white>").append(message);
            if (p.hasPermission("chat.admin")) {
                finalMessage = getColoredMessage("&f" + plain);
            }
        } else {
            finalMessage = mm.deserialize("<gray>").append(message);
        }

        chatTextComponent.append(finalMessage);

        return textComponent.asComponent();
    }

    public static Component getPlayerPrefix(Player p) {
        Chat chat = Main.getInstance().getServer().getServicesManager().getRegistration(Chat.class).getProvider();
        return mm.deserialize(chat.getPlayerPrefix(p));
    }

    public static Component getPlayerSuffix(Player p) {
        Chat chat = Main.getInstance().getServer().getServicesManager().getRegistration(Chat.class).getProvider();
        return mm.deserialize(chat.getPlayerSuffix(p));
    }

    public static Component getNewWelcomeMessage(Player p) {
        TagResolver placeholders = TagResolver.resolver(Placeholder.component("name", p.displayName()),
                Placeholder.parsed("size", String.valueOf(Bukkit.getOfflinePlayers().length)));
        return mm.deserialize("<#9254e3>» <dark_gray>| <#b175ff><name> <gray>joined <dark_gray>[<#b175ff>#<size><dark_gray>]",placeholders);
    }

    public static Component getWelcomeMessage(Player p) {
        TagResolver placeholder = TagResolver.resolver(Placeholder.component("name", p.displayName()));
        return mm.deserialize("<#9254e3>» <dark_gray>| <#b175ff><name> <gray>joined",placeholder);
    }

    private static Component getColoredMessage(String message) {
        ArrayList<String> colors = new ArrayList<>(Arrays.asList("&0", "&8", "&7", "&f", "&5", "&d", "&9", "&1", "&3", "&b", "&a", "&2", "&e", "&6", "&c", "&4", "&r", "&o", "&n", "&m", "&l", "&k"));
        ArrayList<String> format = new ArrayList<>(Arrays.asList("<black>", "<dark_gray>", "<gray>", "<white>", "<dark_purple>", "<light_purple>", "<blue>", "<dark_blue>", "<dark_aqua>", "<aqua>", "<green>", "<dark_green>", "<yellow>", "<gold>", "<red>", "<dark_red>", "<reset>", "<italic>", "<underlined>", "<strikethrough>", "<bold>", "<obfuscated>"));
        for (int i = 0; i < colors.size(); i++) {
            message = message.replaceAll(colors.get(i), format.get(i));
        }
        return mm.deserialize(message);
    }
}
