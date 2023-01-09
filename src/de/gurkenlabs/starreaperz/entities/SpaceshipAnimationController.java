package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.input.Input;

public class SpaceshipAnimationController extends CreatureAnimationController<Spaceship> {
  public SpaceshipAnimationController(Spaceship creature) {
    super(creature, true);
  }

  protected String getCurrentAnimationName() {
    if (!this.getEntity().isDead()) {
      var spaceshipController = (SpaceshipController) this.getEntity().movement();

      if (spaceshipController.isKeyRightDown()) {
        return "spaceship-walk-right";
      } else if (spaceshipController.isKeyLeftDown()) {
        return "spaceship-walk-left";
      }

      return "spaceship-idle";
    }
    return super.getCurrentAnimationName();
  }
}
