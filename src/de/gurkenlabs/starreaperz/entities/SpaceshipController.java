package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.input.KeyboardEntityController;
import de.gurkenlabs.litiengine.physics.StickyForce;

import java.awt.event.KeyEvent;

public class SpaceshipController extends KeyboardEntityController<Spaceship> {
  private static final int VERTICAL_VELOCITY = 200;

  private final StickyForce movementForce;

  private boolean breaking;
  private boolean boosting;
  private boolean keyPressed;

  public SpaceshipController(Spaceship entity) {
    super(entity);
    this.movementForce = new StickyForce(this.getEntity(), VERTICAL_VELOCITY, 10);
    this.apply(this.movementForce);
  }

  @Override
  public void update() {
    super.update();

    if(!keyPressed || (!this.breaking && !this.boosting)){
      this.movementForce.setStrength(VERTICAL_VELOCITY);
    }

    this.keyPressed = false;
    this.breaking = false;
    this.boosting = false;
  }

  @Override
  public void handlePressedKey(KeyEvent keyCode) {
    if (this.getUpKeys().contains(keyCode.getKeyCode())) {
      this.movementForce.setStrength(VERTICAL_VELOCITY * 1.75f);
      this.boosting = true;
    } else if (this.getDownKeys().contains(keyCode.getKeyCode())) {
      this.movementForce.setStrength(VERTICAL_VELOCITY * 0.25f);
      this.breaking = true;
    } else {
      super.handlePressedKey(keyCode);
    }

    keyPressed = true;
  }
}

