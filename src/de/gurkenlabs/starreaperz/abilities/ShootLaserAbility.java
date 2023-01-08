package de.gurkenlabs.starreaperz.abilities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityExecution;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.starreaperz.entities.LaserProjectile;

import java.awt.geom.Point2D;

// TODO: Fast shooting upgrade (reduce cooldown to <=100)
@AbilityInfo(cooldown = 250)
public class ShootLaserAbility extends Ability {
  private static final String[] shootSounds = new String[]{
          "laserShoot.wav",
          "laserShoot2.wav",
          "laserShoot3.wav",
          "laserShoot4.wav",
          "laserShoot5.wav",
          "laserShoot6.wav"
  };

  public ShootLaserAbility(Creature executor) {
    super(executor);
    this.addEffect(new ShootLaserProjectileEffect(this));
    this.addEffect(new ScreenShakeEffect(this, 0.5, 30));
  }

  @Override
  public AbilityExecution cast() {
    return super.cast();
  }

  private static class ShootLaserProjectileEffect extends Effect {
    protected ShootLaserProjectileEffect(Ability ability) {
      super(ability, EffectTarget.EXECUTINGENTITY);
    }

    @Override
    protected void apply(ICombatEntity entity) {
      final var center = entity.getCenter();
      var leftProjectileOrigin = new Point2D.Double(center.getX() - 18, center.getY());
      var rightProjectileOrigin = new Point2D.Double(center.getX() + 14, center.getY());
      // spawn projectiles
      var leftProjectile = new LaserProjectile(leftProjectileOrigin, false);
      var rightProjectile = new LaserProjectile(rightProjectileOrigin, true);
      Game.world().environment().add(leftProjectile);
      Game.world().environment().add(rightProjectile);
      Game.audio().playSound(Game.random().choose(shootSounds));
      super.apply(entity);
    }
  }
}
