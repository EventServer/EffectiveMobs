package tw.effectivemobs.mobs;

import com.sk89q.worldedit.math.BlockVector3;
import eventserver.teamwars.Config;
import eventserver.teamwars.TeamWars;
import eventserver.teamwars.event.MemberBuyReturnInventoryEvent;
import eventserver.teamwars.game.Team;
import eventserver.teamwars.game.TeamMember;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.Nullable;
import tw.effectivemobs.EffectiveMobs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MobManager implements Listener {
    public MobManager() {
        EffectiveMobs.getInstance().getServer().getPluginManager().registerEvents(this, EffectiveMobs.getInstance());
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        final ItemStack stack = event.getItem();
        if (stack == null) return;

        EffectiveMobsType type = EffectiveMobsType.getMobType(stack);
        if (type == null) return;
        event.setCancelled(true);
        stack.setAmount(stack.getAmount()-1);

        final Block block = event.getClickedBlock();
        assert block != null;
        type.getMob().spawn(block.getLocation().add(0,1,0));
        launchFirework(block.getLocation());
    }

    @EventHandler
    private void onMemberBuyReturnInventory(MemberBuyReturnInventoryEvent event) {
        final Team team = event.getTeam();
        if (EffectiveMobsType.CAT_CASHBACK.contains(team.getRegion(), Config.world)) {
            final TeamMember member = event.getMember();
            final Player player = member.getBukkitInstance();
            if (player != null) {
                event.setPrice(0);
                player.sendMessage(tw.effectivemobs.Config.MESSAGES.CASHBACK.replace("%price%", String.format("%.2f", event.getPrice())));
            }
        }
    }

    @EventHandler
    private void onEntityDeathExp(EntityDeathEvent event) {
        final Entity entity = event.getEntity();
        final Team team = getTeam(entity.getLocation());
        if (team == null) return;
        if (EffectiveMobsType.EXP_WITHER.contains(team.getRegion(), Config.world)) {
            event.setDroppedExp(event.getDroppedExp()*50);
        }
    }

    @EventHandler
    private void onEntityDeath(EntityDeathEvent event) {
        final Entity entity = event.getEntity();
        if (entity.getType() == EntityType.COW) {
            final Team team = getTeam(entity.getLocation());
            if (team == null) return;
            final int count = EffectiveMobsType.COW_DOUBLE.getContainsCount(team.getRegion(), Config.world);
            if (count < 1) return;
            final List<ItemStack> drops = event.getDrops();
            drops.removeIf(i -> i.getType() == Material.BEEF);
            final List<ItemStack> bonus = setCowBonus(count, new ItemStack(Material.BEEF, getInt(1, 4)));
            drops.addAll(bonus);
        }
    }

    private int getInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private List<ItemStack> setCowBonus(int count, ItemStack stack) {
        int amount = (stack.getAmount()*2) * count;
        if (amount <= 64) {
            stack.setAmount(amount);
            return List.of(stack);
        }
        else {
            final List<ItemStack> result = new ArrayList<>();
            while (amount > 0) {
                final ItemStack stack1 = new ItemStack(stack.getType());
                stack1.setAmount(Math.min(64, amount));
                amount = amount-stack1.getAmount();
                result.add(stack1);
            }
            return result;
        }
    }

    private void launchFirework(Location location) {
        Firework firework = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL)
                .withColor(Color.RED)
                .withFade(Color.ORANGE)
                .flicker(true)
                .trail(true)
                .build();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
    }


    @Nullable
    private Team getTeam(Location location) {
        final BlockVector3 v3 = BlockVector3.at(location.getX(), location.getY(), location.getZ());
        for (Team team: TeamWars.getInstance().getGame().getTeamManager().getTeams()) {
            if (team.getRegion().contains(v3) || team.getNetherRegion().contains(v3))
                return team;
        }
        return null;
    }
}
