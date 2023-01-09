package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.entities.Tag;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.graphics.emitters.EntityEmitter;
import de.gurkenlabs.litiengine.graphics.emitters.xml.EmitterLoader;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.physics.IMovementController;
import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.abilities.ShootLaserAbility;

import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@MovementInfo(acceleration = ReaperConstantZ.REAPER_ACCELERATION, deceleration = ReaperConstantZ.REAPER_DECELERATION, velocity = ReaperConstantZ.REAPER_VELOCITY)
@CombatInfo(hitpoints = ReaperConstantZ.SPACESHIP_HITPOINTS)
@Tag("minimap")
public class Spaceship extends Creature {

  private final List<SpaceshipListener> listeners = new CopyOnWriteArrayList<>();
  private boolean harvesting;
  private EntityEmitter harvestingEmitter;

  private final ShootLaserAbility shootLaserAbility;

  public Spaceship() {
    super("spaceship");
    this.shootLaserAbility = new ShootLaserAbility(this);
    Input.keyboard().onKeyReleased(l -> {
      if (l.getKeyCode() == KeyEvent.VK_SHIFT && isHarvesting()) {
        stopHarvesting();
      }
    });
    onDeath(d -> {
      GameManager.instance().loseLevel();
      setVisible(false);
      Game.world().environment().remove(this);
      EntityEmitter explosion = new EntityEmitter(this, EmitterLoader.get("explosion-red"));
      explosion.setRenderType(RenderType.OVERLAY);
      Game.world().environment().add(explosion);
    });
  }

  @Override
  public Shape getHitBox() {
    return new Ellipse2D.Double(this.getX() + 10, this.getY() + 10, this.getWidth() - 20, this.getHeight() - 20);
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

  public boolean isHarvesting() {
    return harvesting;
  }

  protected void startHarvesting() {
    this.harvesting = true;
    if (harvestingEmitter == null) {
      this.harvestingEmitter = new EntityEmitter(this, EmitterLoader.get("harvest"), true);
      harvestingEmitter.setRenderType(RenderType.GROUND);
    }
    Game.world().environment().add(harvestingEmitter);
  }

  protected void stopHarvesting() {
    this.harvesting = false;
    Game.world().environment().remove(harvestingEmitter);
  }
}
