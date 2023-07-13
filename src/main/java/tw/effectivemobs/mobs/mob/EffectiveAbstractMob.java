package tw.effectivemobs.mobs.mob;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;
import tw.effectivemobs.event.EffectiveMobSpawn;
import tw.effectivemobs.mobs.EffectiveMobsType;
import tw.effectivemobs.util.EntityCreator;
import tw.effectivemobs.util.NBTEditor;

public abstract class EffectiveAbstractMob implements EffectiveMob {

    @Override
    public ItemStack getItem() {
        ItemStack stack = new ItemStack(getItemMaterial());
        final ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(getName());
        meta.setLore(getLore());
        stack.setItemMeta(meta);
        stack = NBTEditor.set(stack, getClass().getName(), "tw", "effective-mob", "type");
        return stack;
    }

    @Override
    public boolean isIn(ItemStack stack) {
        final String name = NBTEditor.getString(stack, "tw", "effective-mob", "type");
        return name != null && name.equalsIgnoreCase(getClass().getName());
    }

    @Nullable
    private EffectiveMob getItemMobType(ItemStack stack) {
        final String name = NBTEditor.getString(stack, "tw", "effective-mob", "type");
        if (name == null)
            return null;
        EffectiveMobsType type = EffectiveMob.getType(name);
        if (type != null) {
            return type.getMob();
        }
        return null;
    }

    @Nullable
    private EffectiveMob getEntityMobType(Entity stack) {
        final String name = NBTEditor.getString(stack, "tw", "effective-mob", "type");
        if (name == null)
            return null;
        EffectiveMobsType type = EffectiveMob.getType(name);
        if (type != null) {
            return type.getMob();
        }
        return null;
    }

    @Override
    public boolean isIn(Entity entity) {
        final String name = NBTEditor.getString(entity, "tw", "effective-mob", "type");
        return name != null && name.equalsIgnoreCase(getClass().getName());
    }

    @Override
    public Entity spawn(Location location) {
//        Entity entity = location.getWorld().spawnEntity(location, getEntityType(), CreatureSpawnEvent.SpawnReason.CUSTOM, e -> {
//            e.setCustomNameVisible(true);
//            e.setCustomName(getName());
//            e.setPersistent(true);
//            e.setTicksLived(1000);
//        });
        Entity e = EntityCreator.create(getEntityType(), location);
        e.setCustomNameVisible(true);
        e.setCustomName(getName());
        e.setPersistent(true);
        e.setTicksLived(1000);
        e.spawnAt(location);
        new EffectiveMobSpawn(this, location, e).callEvent();
        return e;
    }
}
