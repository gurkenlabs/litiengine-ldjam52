package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.graphics.OverlayPixelsImageEffect;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.EntityAnimationController;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;
import de.gurkenlabs.starreaperz.GameManager;

import java.awt.*;
import java.awt.geom.Point2D;

@MovementInfo(velocity = 200, turnOnMove = false)
@EntityInfo(width = 5, height = 5)
public class EnergyProjectile extends Creature implements IUpdateable {

  private final EnergyColor color;

  private final double angle;

  public EnergyProjectile(EnergyColor color, Point2D origin) {
    this.color = color;
    this.setLocation(origin);
    this.angle = GeometricUtilities.calcRotationAngleInDegrees(this.getCenter(), GameManager.instance().getSpaceship().getCenter());
  }

  @Override
  protected IEntityAnimationController<?> createAnimationController() {
    var controller = new EntityAnimationController<>(this,
        new Animation("enemy-shoot_yellow", true, false),
        new Animation("enemy-shoot_green", true, false),
        new Animation("enemy-shoot_blue", true, false));

    controller.addRule(p -> p.color == EnergyColor.YELLOW, p -> "enemy-shoot_yellow");
    controller.addRule(p -> p.color == EnergyColor.GREEN, p -> "enemy-shoot_green");
    controller.addRule(p -> p.color == EnergyColor.BLUE, p -> "enemy-shoot_blue");
    return controller;
  }

  @Override
  public void update() {
    // if we hit the spaceship
    var hitEnemy = Game.world().environment().findCombatEntities(this.getHitBox(), e -> e instanceof Spaceship && !e.isDead()).stream().findFirst();
    if (hitEnemy.isPresent()) {
      hitEnemy.get().hit(1);
      hitEnemy.get().animations().add(new OverlayPixelsImageEffect(50, Color.WHITE));

      Game.world().environment().remove(this);
    }

    if (!Game.world().camera().getViewport().intersects(this.getBoundingBox())) {
      Game.world().environment().remove(this);
    } else {
      Game.physics().move(this, this.angle, this.getTickVelocity());
    }
  }
}
