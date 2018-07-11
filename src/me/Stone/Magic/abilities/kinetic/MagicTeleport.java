package me.Stone.Magic.abilities.kinetic;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

import me.Stone.Magic.Magic;
import me.Stone.Magic.abilities.Ability;

public class MagicTeleport extends Ability {

	private static Random random = new Random();

	public MagicTeleport(Magic main) {
		super(main, Material.REDSTONE_TORCH_ON, 200, true, Magic.TELEPORT_USE, Magic.TELEPORT_BYPASS);
	}

	@Override
	public void activate(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = player.getTargetBlock(null, 32);
		if(block != null) {
			Location location = block.getLocation().add(0.5d, player.isGliding() ? 2 : 1.1d, 0.5d); // Center of block above target block

			Block bottom = location.getBlock();
			if(isBlockSafe(bottom) && isBlockSafe(bottom.getRelative(BlockFace.UP))) {
				location.setPitch(player.getLocation().getPitch());
				location.setYaw(player.getLocation().getYaw());
				Vector velocity = player.getVelocity();
				velocity.setY(player.isGliding() ? 0.25D : 0);
				player.teleport(location, TeleportCause.CHORUS_FRUIT);
				player.setVelocity(velocity);
				player.setFallDistance(0);
				player.getWorld().playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
				addParticleEffects(location);

				player.sendMessage(message("&5You teleported ahead!"));
			}
			else {
				player.sendMessage(message("&5That area is not safe"));
			}

		}
	}

	private void addParticleEffects(Location location) {
		for(int i = 0; i < 10; i++) {
			Location loc1 = location.clone();
			loc1.add(random.nextDouble() - .5f, random.nextDouble() - .5f, random.nextDouble() - .5f);
			location.getWorld().spawnParticle(Particle.PORTAL, loc1, 2);
		}
	}

	private boolean isBlockSafe(Block block) {
		Material mat = block.getType();
		return (mat == Material.AIR || mat == Material.GRASS || mat == Material.LONG_GRASS
				|| mat == Material.CHORUS_FLOWER || mat == Material.YELLOW_FLOWER || mat == Material.RED_ROSE
				|| mat == Material.WATER || mat == Material.WATER_LILY || mat == Material.TORCH
				|| mat == Material.REDSTONE || mat == Material.REDSTONE_TORCH_OFF || mat == Material.REDSTONE_TORCH_ON)
				&& (block.getWorld().getWorldBorder().isInside(block.getLocation()));
	}

}
