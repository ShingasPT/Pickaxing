package pickaxing.server.core.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pickaxing.server.core.Main;

import java.util.ArrayList;
import java.util.List;

public class Pickaxe extends ItemStack {

    private static final MiniMessage mm = MiniMessage.miniMessage();

    public Pickaxe() {
        this.setType(Material.DIAMOND_PICKAXE);
        this.setAmount(1);
        ItemMeta meta = this.getItemMeta();

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(new NamespacedKey(Main.getInstance(), "pickaxe"), PersistentDataType.INTEGER, 1);

        meta.addItemFlags(ItemFlag.values());

        meta.displayName(mm.deserialize("<italic:false><#b175ff>Pickaxe <gray>[0]"));

        // darker main color: <#9254e3>
        // lighter main color: <#b175ff>
        List<Component> pickaxeLore = new ArrayList<>();
        pickaxeLore.add(mm.deserialize(""));
        pickaxeLore.add(mm.deserialize("<italic:false>  <#9254e3><bold>DESCRIPTION"));
        pickaxeLore.add(mm.deserialize("<italic:false>    <dark_gray>» <gray>This is your pickaxe"));
        pickaxeLore.add(mm.deserialize("<italic:false>    <dark_gray>» <#b175ff>RIGHT-CLICK <gray>for more info"));
        pickaxeLore.add(mm.deserialize(""));

        meta.lore(pickaxeLore);

        this.setItemMeta(meta);

    }
}
