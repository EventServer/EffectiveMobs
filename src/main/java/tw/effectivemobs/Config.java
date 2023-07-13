package tw.effectivemobs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {
    private Config() {
        throw new IllegalStateException("Utility class");
    }

    public static void load(FileConfiguration file) {
        final ConfigurationSection catCashback = file.getConfigurationSection("cat-cashback");
        if (catCashback != null)
            CAT_CASHBACK_CONFIG = parseCatCashback(catCashback);
        SHOP_LORE_FOOTER = file.getStringList("shop-lore-footer");
        MESSAGES.CASHBACK = file.getString("messages.cashback");

        final ConfigurationSection cowSection = file.getConfigurationSection("cow-double");
        if (cowSection != null) {
            COW_DOUBLE = parseMob(cowSection);
        }

        final ConfigurationSection expWither = file.getConfigurationSection("exp-wither");
        if (expWither != null) {
            EXP_WITHER = parseMob(expWither);
        }
    }

    public static CatCashbackConfig parseCatCashback(ConfigurationSection section) {
        final String name = section.getString("name");
        final List<String> lore = section.getStringList("lore");
        final int returnInv = section.getInt("return-inv-cashback", 100);
        final int spawnEggs = section.getInt("mob-spawn-egg-cashback", 10);
        ShopConfig shopConfig;
        final ConfigurationSection shop = section.getConfigurationSection("shop");
        if (shop == null) {
            shopConfig = new ShopConfig(0, false);
        } else {
            shopConfig = parseShopConfig(shop);
        }

        return new CatCashbackConfig(name, lore, shopConfig, returnInv, spawnEggs);
    }

    public static Mob parseMob(ConfigurationSection section) {
        final String name = section.getString("name");
        final List<String> lore = section.getStringList("lore");
        ShopConfig shopConfig;
        final ConfigurationSection shop = section.getConfigurationSection("shop");
        if (shop == null) {
            shopConfig = new ShopConfig(0, false);
        } else {
            shopConfig = parseShopConfig(shop);
        }

        return new Mob(name, lore, shopConfig);
    }

    public static class MESSAGES {
        public static String CASHBACK;
    }

    public static ShopConfig parseShopConfig(ConfigurationSection section) {
        final double price = section.getDouble("price");
        final boolean enabled = section.getBoolean("enabled");
        return new ShopConfig(price, enabled);
    }

    public static Mob COW_DOUBLE;
    public static CatCashbackConfig CAT_CASHBACK_CONFIG;

    @Getter @ToString
    public static class CatCashbackConfig extends Mob {
        public int returnInvCashback;
        public int spawnEggsCashback;

        public CatCashbackConfig(String name, List<String> lore, ShopConfig shopConfig, int returnInvCashback, int spawnEggsCashback) {
            super(name, lore, shopConfig);
            this.returnInvCashback = returnInvCashback;
            this.spawnEggsCashback = spawnEggsCashback;
        }
    }

    public static List<String> SHOP_LORE_FOOTER;
    public static Mob EXP_WITHER;

    @AllArgsConstructor @Getter @ToString
    public static class ShopConfig {
        private final double price;
        private final boolean enabled;
    }

    @AllArgsConstructor @Getter @ToString
    public static class Mob {
        private final String name;
        private final List<String> lore;
        private final ShopConfig shopConfig;
    }
}
