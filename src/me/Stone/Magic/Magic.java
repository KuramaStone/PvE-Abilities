package me.Stone.Magic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Stone.Magic.abilities.Ability;
import me.Stone.Magic.abilities.druid.Druid;
import me.Stone.Magic.abilities.druid.MagicDefender;
import me.Stone.Magic.abilities.kinetic.Kinetic;
import me.Stone.Magic.abilities.kinetic.MagicLeap;
import me.Stone.Magic.abilities.kinetic.MagicLevitate;
import me.Stone.Magic.abilities.pheonix.MagicFireAttack;
import me.Stone.Magic.abilities.pheonix.Pheonix;

public class Magic extends JavaPlugin implements Listener {

	public static final Permission
	// Pheonix
	FIREATTACK_USE = new Permission("Magic.pheonix.active.fire_use"),
			FIREATTACK_BYPASS = new Permission("Magic.admin.pheonix.fire_bypass"),
			PHEONIX_HEAL = new Permission("Magic.pheonix.passive.fire_heal"),
			PHEONIX_RAIN_DAMAGE = new Permission("Magic.pheonix.passive.rain_damage"),
			// Kinetic
			LEVITATE_USE = new Permission("Magic.kinetic.active.levitate_use"),
			LEVITATE_BYPASS = new Permission("Magic.admin.kinetic.levitate_bypass"),
			TELEPORT_USE = new Permission("Magic.kinetic.active.teleport_use"),
			TELEPORT_BYPASS = new Permission("Magic.admin.kinetic.teleport_bypass"),
			LEAP_USE = new Permission("Magic.kinetic.active.leap_use"),
			LEAP_BYPASS = new Permission("Magic.admin.kinetic.leap_bypass"),
			KINETIC_FALL = new Permission("Magic.kinetic.passive.prevent_fall"),
			KINETIC_ELYTRA = new Permission("Magic.kinetic.passive.elytra_boost"),
			// Druid
			DEFENDER_USE = new Permission("Magic.druid.active.defender_use"),
			DEFENDER_BYPASS = new Permission("Magic.admin.druid.defender_bypass"),
			DRUID_BLESSING = new Permission("Magic.druid.active.blessing"),
			DRUID_GROW = new Permission("Magic.druid.passive.grow_boost"),
			// Siren
			SIREN_RAIN_HEAL = new Permission("Magic.siren.passive.rain_heal");

	private static List<Ability> abilities;

	private static List<MagicRunnable> runnables;

	private static Map<OfflinePlayer, LivingEntity> lastDamageCause;

	@Override
	public void onEnable() {
		runnables = new ArrayList<>();
		lastDamageCause = new HashMap<>();
		abilities = new ArrayList<>();
		abilities.addAll(Arrays.asList( // Abilities
				new MagicFireAttack(this), // Pheonix
				new MagicLevitate(this), new MagicLeap(this), // Kinetic
				new MagicDefender(this) // Druid
		));

		getServer().getPluginManager().registerEvents(this, this);

		getServer().getPluginManager().registerEvents(new Pheonix(), this);
		getServer().getPluginManager().registerEvents(new Kinetic(), this);
		getServer().getPluginManager().registerEvents(new Druid(), this);
		
		rainRunnable.runTaskTimer(this, 0l, 40l);
	}

	@Override
	public void onDisable() {
		for(MagicRunnable br : runnables) {
			br.stop();
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getHand() == EquipmentSlot.HAND) {
			for(Ability ability : abilities) {
				if(ability.event(event))
					break;
			}
		}
	}

	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.getEntityType() == EntityType.PLAYER && event.getDamager() instanceof LivingEntity) {
			Player player = (Player) event.getEntity();
			lastDamageCause.put(player, (LivingEntity) event.getDamager());
		}
	}

	public LivingEntity getLastAttacker(OfflinePlayer player) {
		if(lastDamageCause.containsKey(player)) {
			return lastDamageCause.get(player);
		}

		return null;
	}

	public void addRunnable(MagicRunnable run) {
		runnables.add(run);
	}

	public void removeRunnable(MagicRunnable run) {
		runnables.remove(run);
	}

	public static abstract class MagicRunnable extends BukkitRunnable {

		@Override
		public void run() {

		}

		public abstract void stop();

	}

	private static final int RAIN_HEAL = 1, RAIN_DAMAGE = 1;
	private static final BukkitRunnable rainRunnable = new BukkitRunnable() {

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			for(World world : Bukkit.getWorlds()) {
				if(world.hasStorm()) {
					for(Player player : world.getPlayers()) {
						if(!inDesert(world, player) && !hasCover(world, player) &&
								!(player.hasPermission(Magic.PHEONIX_RAIN_DAMAGE) && player.hasPermission(Magic.SIREN_RAIN_HEAL))) {
							if(player.hasPermission(Magic.PHEONIX_RAIN_DAMAGE)
									&& player.getGameMode() != GameMode.CREATIVE) {
								player.damage(RAIN_DAMAGE);
								player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f);
							}
							else if(player.hasPermission(Magic.SIREN_RAIN_HEAL)) {
								player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + RAIN_HEAL));
							}
						}
					} 	
				}
			}
		}

		private boolean hasCover(World world, Player player) {
			Block b = world.getHighestBlockAt(player.getLocation());
			return b.getY() > player.getEyeLocation().getY();
		}

		private boolean inDesert(World world, Player player) {
			Biome b = world.getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
			
			return b == Biome.DESERT || b == Biome.DESERT_HILLS || b == Biome.MUTATED_DESERT;
		}
	};

}
