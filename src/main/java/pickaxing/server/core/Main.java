package pickaxing.server.core;

import org.bukkit.plugin.java.JavaPlugin;
import pickaxing.server.core.commands.CMDSpawn;
import pickaxing.server.core.commands.CMDStarterPickaxe;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        saveDefaultConfig();

        getCommand("spawn").setExecutor(new CMDSpawn());
        getCommand("starterpick").setExecutor(new CMDStarterPickaxe());

        getServer().getPluginManager().registerEvents(new Events(), this);
    }

    public static Main getInstance() { return instance; }

}
