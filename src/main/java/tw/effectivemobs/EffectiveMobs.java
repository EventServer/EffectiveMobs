package tw.effectivemobs;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import tw.effectivemobs.command.BreederCommand;
import tw.effectivemobs.gui.ShopGui;
import tw.effectivemobs.mobs.MobManager;

public final class EffectiveMobs extends JavaPlugin {
    @Getter
    private ShopGui shopGui;
    @Getter
    private static EffectiveMobs instance;
    private MobManager mobManager;
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Config.load(getConfig());
        shopGui = new ShopGui(this);

        mobManager = new MobManager();

        getServer().getPluginCommand("breeder").setExecutor(new BreederCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
