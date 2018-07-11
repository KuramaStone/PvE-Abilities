package me.Stone.Magic.abilities.pheonix;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Stone.Magic.Magic;
import me.Stone.Magic.abilities.Ability;

public class MagicFireAttack extends Ability {

	private static Random random = new Random();

	public MagicFireAttack(Magic main) {
		super(main, Material.BLAZE_ROD, 20, true, Magic.FIREATTACK_USE, Magic.FIREATTACK_BYPASS);
	}

	@Override
	public void activate(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		SmallFireball fireball = player.launchProjectile(SmallFireball.class);
		fireball.setCustomName(Pheonix.FIREBALL_NAME);
		fireball.setIsIncendiary(false);
		playSound(player.getWorld(), player.getLocation(), Sound.ITEM_FIRECHARGE_USE);
		particles(player);

		player.sendMessage(message("&cYour powers manifested into a fireball!"));
	}

	private void particles(Player player) {
		double locX = player.getLocation().getX();
		double locY = player.getLocation().getY();
		double locZ = player.getLocation().getZ();
		for(int i = 0; i < 3; i++) {
			player.getWorld().spawnParticle(Particle.SMOKE_LARGE, locX + (random.nextDouble() - 0.5D),
					locY + random.nextDouble() * 2, locZ + (random.nextDouble() - 0.5D), 1);
		}
	}

}
