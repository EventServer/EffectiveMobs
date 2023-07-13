package tw.effectivemobs.mobs.mob;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import tw.effectivemobs.Config;

import java.util.List;

public class CowDouble extends EffectiveAbstractMob {
    @Override
    public String getName() {
        return Config.COW_DOUBLE.getName();
    }

    @Override
    public List<String> getLore() {
        return Config.COW_DOUBLE.getLore();
    }

    @Override
    public Material getItemMaterial() {
        return Material.COW_SPAWN_EGG;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.COW;
    }

    @Override
    public Config.ShopConfig getShopConfig() {
        return Config.COW_DOUBLE.getShopConfig();
    }
}
