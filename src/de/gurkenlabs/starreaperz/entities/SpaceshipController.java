package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.input.KeyboardEntityController;
import de.gurkenlabs.litiengine.physics.StickyForce;

import java.awt.event.KeyEvent;

public class SpaceshipController extends KeyboardEntityController<Spaceship> {
  public static final int VERTICAL_VELOCITY = 200;

  private final StickyForce movementForce;

  private long breaking;
  private long boosting;
  private boolean keyPressed;
  private boolean keyLeftDown;
  private boolean keyRightDown;

  public SpaceshipController(Spaceship entity) {
    super(entity);
    this.movementForce = new StickyForce(this.getEntity(), VERTICAL_VELOCITY, 10);
    this.apply(this.movementForce);
  }

  @Override
  public void update() {
    super.update();

    if((!this.isBreaking() && !this.isBoosting())){
      Game.world().camera().setZoom(1.0f, 100);
    }

    if (!keyPressed || (!this.isBreaking() && !this.isBoosting())) {
      this.changeVelocity(VERTICAL_VELOCITY);
      this.movementForce.setStrength(VERTICAL_VELOCITY);
    }

    this.keyPressed = false;
    this.keyLeftDown = false;
    this.keyRightDown = false;
  }

  public boolean isKeyLeftDown(){
    return keyLeftDown;
  }

  public boolean isKeyRightDown(){
    return keyRightDown;
  }

  public boolean isBoosting() {
    return boosting == Game.time().now();
  }

  public boolean isBreaking() {
    return breaking == Game.time().now();
  }

  @Override
  public void handlePressedKey(KeyEvent keyCode) {
    keyPressed = true;
    if (this.getUpKeys().contains(keyCode.getKeyCode())) {
      this.changeVelocity(VERTICAL_VELOCITY * 1.75f);
      this.boosting = Game.time().now();
      Game.world().camera().setZoom(0.95f, 100);
      return;
    } else if (this.getDownKeys().contains(keyCode.getKeyCode())) {
      this.changeVelocity(VERTICAL_VELOCITY * 0.25f);
      this.breaking = Game.time().now();
      Game.world().camera().setZoom(1.1f, 100);
      return;
    } else if (this.getLeftKeys().contains(keyCode.getKeyCode())) {
      this.keyLeftDown = true;
    } else if (this.getRightKeys().contains(keyCode.getKeyCode())) {
      this.keyRightDown = true;
    }

    super.handlePressedKey(keyCode);
  }

  private void changeVelocity(float velocity){
    this.movementForce.setStrength(velocity);
    this.getEntity().notifyVelocityChanged(velocity);
  }
}

