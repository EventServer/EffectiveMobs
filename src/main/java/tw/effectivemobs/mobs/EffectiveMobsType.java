package tw.effectivemobs.mobs;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import tw.effectivemobs.mobs.mob.CatCashback;
import tw.effectivemobs.mobs.mob.CowDouble;
import tw.effectivemobs.mobs.mob.EffectiveMob;
import tw.effectivemobs.mobs.mob.ExpWither;
import tw.effectivemobs.util.NBTEditor;

public enum EffectiveMobsType {
    COW_DOUBLE(new CowDouble()),
    EXP_WITHER(new ExpWither()),
    CAT_CASHBACK(new CatCashback());

    @Getter
    private final EffectiveMob mob;
    EffectiveMobsType(EffectiveMob mob) {
        this.mob = mob;
    }

    @Nullable
    public static EffectiveMobsType getMobType(ItemStack stack) {
        if (stack == null) return null;
        final String name = NBTEditor.getString(stack, "tw", "effective-mob", "type");
        if (name == null)
            return null;
        return EffectiveMob.getType(name);
    }

    public static boolean isEffective(Entity entity) {
        final String name = entity.getCustomName();
        for (EffectiveMobsType type: EffectiveMobsType.values()) {
            if (type.getMob().getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public boolean contains(ProtectedCuboidRegion region, World world) {
        for (Entity entity: world.getEntitiesByClass(mob.getEntityType().getEntityClass())) {
            final Location location = entity.getLocation();
            if (isEffective(entity) && region.contains(BlockVector3.at(location.getX(), location.getY(), location.getZ())))
                return true;
        }
        return false;
    }

    public int getContainsCount(ProtectedCuboidRegion region, World world) {
        int count = 0;
        for (Entity entity: world.getEntitiesByClass(mob.getEntityType().getEntityClass())) {
            final Location location = entity.getLocation();
            if (isEffective(entity) && region.contains(BlockVector3.at(location.getX(), location.getY(), location.getZ())))
                count++;
        }
        return count;
    }
}
