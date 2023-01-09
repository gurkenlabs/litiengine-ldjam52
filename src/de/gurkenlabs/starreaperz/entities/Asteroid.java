package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICollisionEntity;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.entities.Tag;
import de.gurkenlabs.litiengine.graphics.OverlayPixelsImageEffect;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.EntityAnimationController;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;
import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

@CombatInfo(isIndestructible = true)
@MovementInfo(velocity = 150)
@Tag("minimap")
public class Asteroid extends Creature implements IUpdateable {
  private final int type;

  private final double angle;

  public Asteroid() {
    this.setLocation(this.determineSpawnPoint());
    this.type = Game.random().choose(1, 2, 3, 4, 5);

    switch (type) {
      case 1 -> this.setSize(50, 46);
      case 2 -> this.setSize(26, 25);
      case 3 -> this.setSize(21, 23);
      case 4 -> this.setSize(13, 18);
      case 5 -> this.setSize(23, 26);
    }

    var target = this.determineTarget();
    this.angle = GeometricUtilities.calcRotationAngleInDegrees(this.getLocation(), target);
  }

  @Override
  public boolean canCollideWith(ICollisionEntity otherEntity) {
    return super.canCollideWith(otherEntity) && otherEntity instanceof Spaceship;
  }

  private Point2D determineSpawnPoint() {
    var viewport = Game.world().camera().getViewport();
    final int offsetX = 50;
    var spawnLine =
        new Line2D.Double(viewport.getX() + offsetX, viewport.getY() - 50, viewport.getX() + viewport.getWidth() - 2 * offsetX, viewport.getY() - 50);
    return Game.random().getLocation(spawnLine);
  }

  private Point2D determineTarget() {
    var viewport = Game.world().camera().getViewport();
    final int offsetX = 50;
    var spawnLine =
        new Line2D.Double(viewport.getX() + offsetX, viewport.getY() + viewport.getHeight(), viewport.getX() + viewport.getWidth() - 2 * offsetX,
            viewport.getY() + viewport.getHeight());
    return Game.random().getLocation(spawnLine);
  }

  @Override
  protected IEntityAnimationController<?> createAnimationController() {
    var controller = new EntityAnimationController<>(this,
        new Animation("asteroid1", true, false),
        new Animation("asteroid2", true, false),
        new Animation("asteroid3", true, false),
        new Animation("asteroid4", true, false),
        new Animation("asteroid5", true, false));

    controller.addRule(p -> p.type == 1, p -> "asteroid1");
    controller.addRule(p -> p.type == 2, p -> "asteroid2");
    controller.addRule(p -> p.type == 3, p -> "asteroid3");
    controller.addRule(p -> p.type == 4, p -> "asteroid4");
    controller.addRule(p -> p.type == 5, p -> "asteroid5");

    return controller;
  }

  @Override
  public void update() {
    // if we hit the spaceship
    var hitEnemy =
        Game.world().environment().findCombatEntities(this.getHitBox(), e -> e instanceof Spaceship && !e.isDead()).stream().findFirst().orElse(null);
    if (hitEnemy != null && !hitEnemy.wasHit(ReaperConstantZ.REAPER_HIT_COOLDOWN)) {
      hitEnemy.hit(1);
      hitEnemy.animations().add(new OverlayPixelsImageEffect(50, Color.WHITE));
      // TODO: SOUND, explosion upon enemy death -> enemy class
      Game.world().environment().remove(this);
      return;
    }

    Game.physics().move(this, this.angle, this.getTickVelocity());

  }
}
