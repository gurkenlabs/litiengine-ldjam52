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

@AbilityInfo(cooldown = 250)
public class ShootLaserAbility extends Ability {
  public ShootLaserAbility(Creature executor) {
    super(executor);
    this.addEffect(new ShootLaserProjectileEffect(this));
    // TODO: SOUNDS
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
      super.apply(entity);
    }
  }
}
