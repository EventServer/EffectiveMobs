package tw.effectivemobs.mobs.mob;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import tw.effectivemobs.Config;

import java.util.List;

public class CatCashback extends EffectiveAbstractMob {
    @Override
    public String getName() {
        return Config.CAT_CASHBACK_CONFIG.getName();
    }

    @Override
    public List<String> getLore() {
        return Config.CAT_CASHBACK_CONFIG.getLore();
    }

    @Override
    public Material getItemMaterial() {
        return Material.FOX_SPAWN_EGG;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.FOX;
    }

    @Override
    public Config.ShopConfig getShopConfig() {
        return Config.CAT_CASHBACK_CONFIG.getShopConfig();
    }
}
