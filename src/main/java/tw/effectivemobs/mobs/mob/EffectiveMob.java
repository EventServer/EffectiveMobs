package tw.effectivemobs.mobs.mob;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import tw.effectivemobs.Config;
import tw.effectivemobs.mobs.EffectiveMobsType;

import java.util.List;

public interface EffectiveMob {
    String getName();
    List<String> getLore();
    ItemStack getItem();
    Material getItemMaterial();
    boolean isIn(ItemStack stack);
    boolean isIn(Entity entity);
    EntityType getEntityType();
    Entity spawn(Location location);
    Config.ShopConfig getShopConfig();
    @Nullable
    static EffectiveMobsType getType(String name) {
        for (EffectiveMobsType type: EffectiveMobsType.values()) {
            if (type.getMob().getClass().getName().equalsIgnoreCase(name))
                return type;
        }
        return null;
    }
}
