package tw.effectivemobs.gui;

import eventserver.teamwars.TeamWars;
import eventserver.teamwars.game.Team;
import eventserver.teamwars.game.TeamMember;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tw.effectivemobs.Config;
import tw.effectivemobs.mobs.EffectiveMobsType;
import tw.effectivemobs.mobs.mob.EffectiveMob;

import java.util.ArrayList;
import java.util.List;

public class ShopGui implements Listener {
    public static final Component title = Component.text("Селекционер").color(TextColor.color(0));
    private static final ShopHolder holder = new ShopHolder();
    private final Inventory inventory;
    public ShopGui(JavaPlugin plugin) {
        inventory = ShopGui.holder.getInventory();
        build(inventory);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void build(Inventory inventory) {
        for (EffectiveMobsType type: EffectiveMobsType.values()) {
            final EffectiveMob mob = type.getMob();

            final Config.ShopConfig shopConfig = mob.getShopConfig();
            if (!shopConfig.isEnabled())
                continue;

            final ItemStack stack = new ItemStack(mob.getItemMaterial());
            final ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(mob.getName());
            final List<String> lore = new ArrayList<>(mob.getLore());
            for (String str: Config.SHOP_LORE_FOOTER) {
                lore.add(str.replace("%price%", String.format("%.2f", shopConfig.getPrice())));
            }
            meta.setLore(lore);
            stack.setItemMeta(meta);
            inventory.addItem(stack);
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    private static class ShopHolder implements InventoryHolder {
        @Override
        public @NotNull Inventory getInventory() {
            return Bukkit.createInventory(this, 54, ShopGui.title);
        }
    }

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != ShopGui.holder)
            return;

        event.setCancelled(true);

        final ItemStack stack = event.getCurrentItem();
        if (stack == null) return;

        final EffectiveMob mob = getMobByItemMaterial(stack.getType());
        if (mob == null) return;

        final Player player = (Player) event.getWhoClicked();
        final Team team = TeamWars.getInstance().getGame().getTeamManager().getPlayerTeam(player);
        if (team == null) {
            player.sendMessage(eventserver.teamwars.Config.MESSAGES.YOU_NO_TEAM);
            return;
        }

        final TeamMember member = team.getMember(player.getName());
        assert member != null;
        if (member.getBalance() < mob.getShopConfig().getPrice()) {
            player.sendMessage(eventserver.teamwars.Config.MESSAGES.NO_BALANCE);
            return;
        }

        member.setBalance(member.getBalance() - mob.getShopConfig().getPrice());
        player.getInventory().addItem(mob.getItem()).forEach((i, st) -> {
            player.getWorld().dropItem(player.getLocation(), st);
        });
    }

    @Nullable
    private EffectiveMob getMobByItemMaterial(Material material) {
        for (EffectiveMobsType type: EffectiveMobsType.values()) {
            if (type.getMob().getItemMaterial() == material)
                return type.getMob();
        }
        return null;
    }
}
