package tw.effectivemobs.event;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import tw.effectivemobs.mobs.mob.EffectiveMob;

public class EffectiveMobSpawn extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    @Getter
    private final EffectiveMob mob;
    @Getter
    private final Location location;
    @Getter
    private final Entity entity;

    public EffectiveMobSpawn(EffectiveMob mob, Location location, Entity entity) {
        this.mob = mob;
        this.location = location;
        this.entity = entity;
    }
}
