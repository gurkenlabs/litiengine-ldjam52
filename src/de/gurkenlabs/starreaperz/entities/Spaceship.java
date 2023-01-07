package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.attributes.RangeAttribute;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.physics.IMovementController;

@MovementInfo(acceleration = 1500, deceleration = 3000, velocity = 200)
@CombatInfo(hitpoints = 3)
public class Spaceship extends Creature {
  public Spaceship() {
    super("spaceship");
  }

  @Override
  protected IMovementController createMovementController() {
    return new SpaceshipController(this);
  }

  @Override
  protected IEntityAnimationController<?> createAnimationController() {
    return new SpaceshipAnimationController(this);
  }
}
