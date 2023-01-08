package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.physics.IMovementController;
import de.gurkenlabs.starreaperz.abilities.ShootLaserAbility;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@MovementInfo(acceleration = 800, deceleration = 1500, velocity = 200)
@CombatInfo(hitpoints = 3)
public class Spaceship extends Creature {

  private final List<SpaceshipListener> listeners = new CopyOnWriteArrayList<>();

  private final ShootLaserAbility shootLaserAbility;

  public Spaceship() {
    super("spaceship");
    this.shootLaserAbility = new ShootLaserAbility(this);
  }

  @Override
  protected IMovementController createMovementController() {
    return new SpaceshipController(this);
  }

  @Override
  protected IEntityAnimationController<?> createAnimationController() {
    return new SpaceshipAnimationController(this);
  }

  public void addSpaceshipListener(SpaceshipListener listener) {
    this.listeners.add(listener);
  }

  public void removeSpaceshipListener(SpaceshipListener listener) {
    this.listeners.remove(listener);
  }

  public void notifyVelocityChanged(float velocity) {
    for (var listener : this.listeners) {
      listener.velocityChanged(velocity);
    }
  }

  public ShootLaserAbility getShootLaserAbility() {
    return shootLaserAbility;
  }

  public boolean isHavesting() {
    return ((SpaceshipController)this.movement()).isHarvesting();
  }
}
