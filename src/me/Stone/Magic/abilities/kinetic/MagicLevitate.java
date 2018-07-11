package me.Stone.Magic.abilities.kinetic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Stone.Magic.Magic;
import me.Stone.Magic.abilities.Ability;

public class MagicLevitate extends Ability {
	
//	private static final int range = 16;

	public MagicLevitate(Magic main) {
		super(main, Material.REDSTONE_TORCH_ON, 20, true, Magic.LEVITATE_USE, Magic.LEVITATE_BYPASS);
	}

	@Override
	public void activate(PlayerInteractEvent event) {
		Player player = event.getPlayer();
//		LivingEntity target = getTarget(player, range);
		Location loc = player.getEyeLocation().add(player.getEyeLocation().getDirection());
		ShulkerBullet bullet = (ShulkerBullet) player.getWorld().spawnEntity(loc, EntityType.SHULKER_BULLET);
		bullet.setVelocity(player.getLocation().getDirection());
		bullet.setShooter(player);
		bullet.setGravity(false);
//		if(target != null)
//			bullet.setTarget(target);

		playSound(player.getWorld(), player.getEyeLocation(), Sound.BLOCK_SNOW_BREAK);
		bullet.getWorld().spawnParticle(Particle.CLOUD, bullet.getLocation(), 3);
		
		player.sendMessage(message("&5You sent forth a magical projectile!"));
	}

}
