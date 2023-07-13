package tw.effectivemobs.mobs.mob;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import tw.effectivemobs.Config;

import java.util.List;

public class ExpWither extends EffectiveAbstractMob {
    @Override
    public String getName() {
        return Config.EXP_WITHER.getName();
    }

    @Override
    public List<String> getLore() {
        return Config.EXP_WITHER.getLore();
    }

    @Override
    public Material getItemMaterial() {
        return Material.WITHER_SKELETON_SPAWN_EGG;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.WITHER;
    }

    @Override
    public Config.ShopConfig getShopConfig() {
        return Config.EXP_WITHER.getShopConfig();
    }
}
