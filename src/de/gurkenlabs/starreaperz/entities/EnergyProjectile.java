package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.graphics.OverlayPixelsImageEffect;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.EntityAnimationController;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;
import de.gurkenlabs.starreaperz.GameManager;

import de.gurkenlabs.starreaperz.GameState;
import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;

import java.awt.*;
import java.awt.geom.Point2D;

@MovementInfo(velocity = ReaperConstantZ.ENEMY_SHOT_VELOCITY, turnOnMove = false)
@EntityInfo(width = 5, height = 5, renderType = RenderType.OVERLAY)
public class EnergyProjectile extends Creature implements IUpdateable {

  private final EnergyColor color;

  private final double angle;
  private long lastflash;

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
    if (GameManager.instance().getState() != GameState.INGAME) {
      return;
    }
    // if we hit the spaceship
    var hitEnemy = Game.world().environment().findCombatEntities(this.getHitBox(), e -> e instanceof Spaceship && !e.isDead()).stream().findFirst();
    if (hitEnemy.isPresent()) {
      hitEnemy.get().hit(1);
      hitEnemy.get().animations().add(new OverlayPixelsImageEffect(ReaperConstantZ.REAPER_HIT_PIXEL_FLASH_DURATION, Color.WHITE));

      Game.world().environment().remove(this);
    }

    if (!Game.world().camera().getViewport().intersects(this.getBoundingBox())) {
      Game.world().environment().remove(this);
    } else {
      Game.physics().move(this, this.angle, this.getTickVelocity());
    }

    if (Game.time().since(this.lastflash) > 400) {
      var color = this.color.toAwtColor();
      this.animations().add(new OverlayPixelsImageEffect(200, new Color(color.getRed(), color.getGreen(), color.getBlue(), 200)));
      Game.loop().perform(200, () -> {
        this.animations().add(new OverlayPixelsImageEffect(200, new Color(255, 255, 255, 200)));
      });
      this.lastflash = Game.time().now();
    }
  }
}
