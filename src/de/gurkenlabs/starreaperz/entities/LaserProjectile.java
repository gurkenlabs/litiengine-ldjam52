package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.*;
import de.gurkenlabs.litiengine.graphics.OverlayPixelsImageEffect;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.EntityAnimationController;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.physics.IMovementController;
import de.gurkenlabs.litiengine.physics.MovementController;
import de.gurkenlabs.litiengine.physics.StickyForce;

import java.awt.*;
import java.awt.geom.Point2D;


@AnimationInfo(spritePrefix = {"laser_1", "laser_1-right"})
@MovementInfo(velocity = 350, turnOnMove = false)
@EntityInfo(width = 5, height = 10)
public class LaserProjectile extends Creature implements IUpdateable {

  private final boolean right;

  public LaserProjectile(Point2D origin, boolean right) {
    this.setLocation(origin);
    this.right = right;
  }

  @Override
  protected IMovementController createMovementController() {
    return new LaserProjectileMovementController(this);
  }

  @Override
  protected IEntityAnimationController<?> createAnimationController() {
    var controller = new EntityAnimationController<>(this,
            new Animation("laser_1", true, false),
            new Animation("laser_1-right", true, false));

    controller.addRule(p -> p.right, p -> "laser_1-right");
    controller.addRule(p -> !p.right, p -> "laser_1");
    return controller;
  }

  @Override
  public void update() {
    // if we hit the spaceship
    var hitEnemy = Game.world().environment().findCombatEntities(this.getHitBox(), e -> e instanceof Enemy && !e.isDead()).stream().findFirst();
    if(hitEnemy.isPresent()){
      hitEnemy.get().hit(1);
      hitEnemy.get().animations().add(new OverlayPixelsImageEffect(50, Color.WHITE));
      // TODO: SOUND, explosion upon enemy death -> enemy class
      Game.world().environment().remove(this);
    }

    if (!Game.world().camera().getViewport().intersects(this.getBoundingBox())) {
      Game.world().environment().remove(this);
    }
  }

  private static class LaserProjectileMovementController extends MovementController<LaserProjectile> {

    public LaserProjectileMovementController(LaserProjectile mobileEntity) {
      super(mobileEntity);
      this.apply(new StickyForce(this.getEntity(), this.getEntity().getVelocity().get(), 10));
    }
  }
}
