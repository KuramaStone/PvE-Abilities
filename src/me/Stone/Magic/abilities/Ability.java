package me.Stone.Magic.abilities;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.util.Vector;

import me.Stone.Magic.Magic;

public abstract class Ability {
	
	protected Magic main;

	protected Map<OfflinePlayer, Long> delayList;
	protected final int delayPerUse;

	protected final Material activationMat;
	protected final Permission forUse, forBypass;

	protected final boolean LEFT_OR_RIGHT;

	public Ability(Magic main, Material mat, int ticksDelay, boolean left, Permission usage, Permission bypass) {
		this.main = main;
		delayPerUse = ticksDelay * 50;
		activationMat = mat;
		LEFT_OR_RIGHT = left;
		forUse = usage;
		forBypass = bypass;

		delayList = new HashMap<>();
	}

	public void playSound(Player player, Sound sound) {
		playSound(player, sound, 1, 1);
	}

	public void playSound(Player player, Sound sound, float f1, float f2) {
		player.playSound(player.getLocation(), sound, f1, f2);
	}

	public void playSound(World world, Location location, Sound sound) {
		playSound(world, location, sound, 1f, 1f);
	}

	public void playSound(World world, Location location, Sound sound, float f1, float f2) {
		world.playSound(location, sound, f1, f2);
	}

	protected String message(String text) {
		return ChatColor.translateAlternateColorCodes('&', "&b<&6&k!&bMagic&6&k! &b> &k" + text);
	}

	public boolean event(PlayerInteractEvent event) {
		Action action = event.getAction();
		if(LEFT_OR_RIGHT ? (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)
				: action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			if(player.hasPermission(forUse) && player.getInventory().getItemInMainHand().getType() == activationMat
					&& !player.isSneaking()) {
				boolean delayOver = player.hasPermission(forBypass) ? true : updateDelay(player);
				if(delayOver)
					activate(event);
				else {
					String seconds = getSecondsUntilDelay(player);
					player.sendMessage(message("&6You may use this again in &l" + seconds + "&6 seconds."));
				}
				event.setCancelled(true);
				return true;
			}
		}
		return false;
	}

	public abstract void activate(PlayerInteractEvent event);

	/*
	 * Checks if the player's delay is over. If so then it adds the player to the
	 * delay, otherwise it returns false
	 */
	protected boolean updateDelay(OfflinePlayer player) {
		long current = System.currentTimeMillis();
		if(delayList.containsKey(player)) {

			long delayed = delayList.get(player);
			if(current < delayed) {
				return false;
			}
		}
		delayList.put(player, current + delayPerUse);
		return true;
	}

	private static DecimalFormat df = new DecimalFormat("0.00");

	public String getSecondsUntilDelay(OfflinePlayer player) {
		double seconds = (delayList.get(player) - System.currentTimeMillis()) / 1000D;
		return df.format(seconds);
	}

	public LivingEntity getTarget(Player player, double range) {
		Location location = player.getEyeLocation();
		Vector direction = location.getDirection();
		for(double i = 0; i < range; i++) {
			location.add(direction);
			if(location.getBlock().getType() != Material.AIR)
				break;

			for(Entity entity : location.getWorld().getNearbyEntities(location, 0.25, 0.25, 0.25)) {
				if(entity instanceof LivingEntity && entity != player) {
					return (LivingEntity) entity;
				}
			}

		}

		return null;
	}

}
