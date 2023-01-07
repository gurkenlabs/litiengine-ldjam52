package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.AnimationInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.physics.StickyForce;
import de.gurkenlabs.starreaperz.GameManager;

import java.awt.geom.Point2D;

@AnimationInfo(spritePrefix = {"energy-red", "energy-blue", "energy-green", "energy-yellow"})
public class Energy extends Creature implements SpaceshipListener {

  private static final float VELOCITY_DIFF_PERCENT = 0.1f;

  public enum EnergyColor {
    RED,
    BLUE,
    GREEN,
    YELLOW
  }

  private final StickyForce movementForce;

  private final float velocityDiff;

  public Energy(EnergyColor color, Point2D origin) {
    super("energy-" + color.name().toLowerCase());
    this.velocityDiff = Game.random().nextFloat(VELOCITY_DIFF_PERCENT, VELOCITY_DIFF_PERCENT * 3f);
    this.setLocation(origin);

    var size = Game.random().nextInt(16, 48);
    this.setSize(size, size);
    this.animations().setAutoScaling(true);
    this.movementForce = new StickyForce(this, getEnergyVelocity(SpaceshipController.VERTICAL_VELOCITY), 10);
    this.movement().apply(this.movementForce);
  }

  private float getEnergyVelocity(float spaceshipVelocity){
    return spaceshipVelocity - (spaceshipVelocity * this.velocityDiff);
  }
  @Override
  public void loaded(Environment environment) {
    super.loaded(environment);
    GameManager.instance().getSpaceship().addSpaceshipListener(this);
  }

  @Override
  public void removed(Environment environment) {
    super.removed(environment);
    GameManager.instance().getSpaceship().removeSpaceshipListener(this);
  }

  @Override
  public void velocityChanged(float speed) {
    this.movementForce.setStrength(getEnergyVelocity(speed));
  }
}
