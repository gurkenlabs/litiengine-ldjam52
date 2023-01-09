package de.gurkenlabs.starreaperz.abilities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.starreaperz.entities.Enemy;
import de.gurkenlabs.starreaperz.entities.EnergyProjectile;

import java.awt.geom.Point2D;

@AbilityInfo(cooldown = 2000)
public class ShootEnergyProjectile extends Ability {
  private static final String[] shootSounds = new String[] {
      "energyShoot1.ogg",
      "energyShoot2.ogg",
      "energyShoot3.ogg",
      "energyShoot4.ogg",
      "energyShoot5.ogg"};

  public ShootEnergyProjectile(Creature executor) {
    super(executor);
    this.addEffect(new ShootShootEnergyProjectileEffect(this));
  }

  private static class ShootShootEnergyProjectileEffect extends Effect {
    protected ShootShootEnergyProjectileEffect(Ability ability) {
      super(ability, EffectTarget.EXECUTINGENTITY);
    }

    @Override
    protected void apply(ICombatEntity entity) {
      var enemy = (Enemy) entity;
      final var center = enemy.getCenter();
      var projectileOrigin = new Point2D.Double(center.getX(), center.getY() + 18);
      // spawn projectiles
      var projectile = new EnergyProjectile(enemy.getColor(), projectileOrigin);
      Game.world().environment().add(projectile);
      Game.audio().playSound(Game.random().choose(shootSounds));
      super.apply(enemy);
    }
  }
}
