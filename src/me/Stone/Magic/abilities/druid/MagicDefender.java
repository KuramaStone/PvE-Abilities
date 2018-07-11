package me.Stone.Magic.abilities.druid;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Stone.Magic.Magic;
import me.Stone.Magic.Magic.MagicRunnable;
import me.Stone.Magic.abilities.Ability;

public class MagicDefender extends Ability {

	private static int[][] coords = { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

	public MagicDefender(Magic main) {
		super(main, Material.BONE, 300, true, Magic.DEFENDER_USE, Magic.DEFENDER_BYPASS);
	}

	@Override
	public void activate(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		getWolfTimer(summonWolves(player)).runTaskLater(main, 300l);
		playSound(player.getWorld(), player.getLocation(), Sound.AMBIENT_CAVE);
		player.sendMessage(message("&aNature has answered your call!"));
	}

	private List<Wolf> summonWolves(Player player) {
		List<Wolf> wolves = new ArrayList<>();
		for(int i = 0; i < coords.length; i++) {
			Location location = player.getLocation().add(coords[i][0], 0, coords[i][1]);
			if(checkAndAlterLocation(player, location, 0)) {
				wolves.add(spawnWolf(player, location));
			}
		}

		return wolves;
	}

	private MagicRunnable getWolfTimer(List<Wolf> wolves) {
		MagicRunnable mr = new MagicRunnable() {

			@Override
			public void run() {
				wolves.forEach(wolf -> wolf.remove());
			}

			public void stop() {
				wolves.forEach(wolf -> wolf.remove());
			}
		};
		main.addRunnable(mr);
		
		return mr;
	}

	/*
	 * Checks if the Location is safe three times before returning false
	 */
	private boolean checkAndAlterLocation(Player player, Location location, int attempt) {
		Material mat = location.getBlock().getType();
		if(mat == Material.AIR || mat == Material.WATER || mat == Material.LONG_GRASS || mat == Material.GRASS
				|| mat == Material.RED_ROSE || mat == Material.CHORUS_FLOWER || mat == Material.YELLOW_FLOWER) {
			return true;
		}
		else if(attempt < 4) {
			return checkAndAlterLocation(player, location.add(0, 1, 0), attempt + 1);
		}

		return false;
	}

	@SuppressWarnings("deprecation")
	private Wolf spawnWolf(Player player, Location location) {
		Wolf wolf = (Wolf) location.getWorld().spawnEntity(location, EntityType.WOLF);
		wolf.setTamed(true);
		wolf.setOwner(player);
		wolf.setCollarColor(DyeColor.GREEN);
		wolf.setMaxHealth(20);
		wolf.setHealth(20);
		wolf.setCustomName(ChatColor.GREEN + "Nature's Defender");
		wolf.setCustomNameVisible(true);

		LivingEntity target = getTarget(player, 16);
		if(target != null) {
			wolf.setTarget(target);
		}
		else {
			LivingEntity attacker = main.getLastAttacker(player);
			if(attacker != null)
				wolf.setTarget(attacker);
		}

		return wolf;
	}

}
