package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.input.KeyboardEntityController;
import de.gurkenlabs.litiengine.physics.StickyForce;

import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.GameState;
import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;
import java.awt.event.KeyEvent;

public class SpaceshipController extends KeyboardEntityController<Spaceship> {

  private final StickyForce movementForce;

  private long breaking;
  private long boosting;
  private boolean keyPressed;
  private long keyLeftDown;
  private long keyRightDown;

  private long harvestingDown;

  public SpaceshipController(Spaceship entity) {
    super(entity);
    this.movementForce = new StickyForce(this.getEntity(), ReaperConstantZ.REAPER_VERTICAL_VELOCITY, 10);
    this.movementForce.setCancelOnCollision(false);
    this.apply(this.movementForce);
  }

  @Override
  public void update() {
    if (GameManager.instance().getState() != GameState.INGAME) {
      return;
    }
    super.update();

    if ((!this.isBraking() && !this.isBoosting())) {
      Game.world().camera().setZoom(ReaperConstantZ.CAMERA_STANDARD_ZOOM_FACTOR, ReaperConstantZ.CAMERA_ZOOM_DELAY);
    }

    if (!keyPressed || (!this.isBraking() && !this.isBoosting())) {
      this.changeVelocity(ReaperConstantZ.REAPER_VERTICAL_VELOCITY);
      this.movementForce.setStrength(ReaperConstantZ.REAPER_VERTICAL_VELOCITY);
    }

    this.keyPressed = false;
  }

  public boolean isKeyLeftDown() {
    return keyLeftDown == Game.time().now();
  }

  public boolean isKeyRightDown() {
    return keyRightDown == Game.time().now();
  }

  public boolean isBoosting() {
    return boosting == Game.time().now();
  }

  public boolean isBraking() {
    return breaking == Game.time().now();
  }

  @Override
  public void handlePressedKey(KeyEvent keyCode) {
    if (GameManager.instance().getState() != GameState.INGAME) {
      return;
    }
    keyPressed = true;
    if (this.getUpKeys().contains(keyCode.getKeyCode())) {
      this.changeVelocity(ReaperConstantZ.REAPER_VERTICAL_VELOCITY * ReaperConstantZ.REAPER_VELOCITY_BOOST_FACTOR);
      this.boosting = Game.time().now();
      Game.world().camera().setZoom(ReaperConstantZ.CAMERA_BOOST_ZOOM_FACTOR, ReaperConstantZ.CAMERA_ZOOM_DELAY);
      return;
    } else if (this.getDownKeys().contains(keyCode.getKeyCode())) {
      this.changeVelocity(ReaperConstantZ.REAPER_VERTICAL_VELOCITY * ReaperConstantZ.REAPER_VELOCITY_BRAKE_FACTOR);
      this.breaking = Game.time().now();
      Game.world().camera().setZoom(ReaperConstantZ.CAMERA_BRAKE_ZOOM_FACTOR, ReaperConstantZ.CAMERA_ZOOM_DELAY);
      return;
    } else if (this.getLeftKeys().contains(keyCode.getKeyCode())) {
      this.keyLeftDown = Game.time().now();
    } else if (this.getRightKeys().contains(keyCode.getKeyCode())) {
      this.keyRightDown = Game.time().now();
    }

    super.handlePressedKey(keyCode);


    if (keyCode.getKeyCode() == KeyEvent.VK_SPACE) {
      this.getEntity().getShootLaserAbility().cast();
    }

    if (keyCode.getKeyCode() == KeyEvent.VK_SHIFT && !getEntity().isHarvesting() && !getEntity().isDead()) {
      getEntity().startHarvesting();
    }
  }

  private void changeVelocity(float velocity) {
    this.movementForce.setStrength(velocity);
    this.getEntity().notifyVelocityChanged(velocity);
  }
}

