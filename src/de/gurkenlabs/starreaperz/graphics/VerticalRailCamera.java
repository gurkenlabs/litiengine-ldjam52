package de.gurkenlabs.starreaperz.graphics;

import de.gurkenlabs.litiengine.graphics.LocationLockCamera;
import de.gurkenlabs.starreaperz.entities.Spaceship;
import de.gurkenlabs.starreaperz.entities.SpaceshipController;

import java.awt.geom.Point2D;

public class VerticalRailCamera extends LocationLockCamera {
  /**
   * Initializes a new instance of the {@code LocationLockCamera}.
   *
   * @param entity The entity to which the focus will be locked.
   */
  public VerticalRailCamera(Spaceship entity) {
    super(entity);
  }

  @Override
  protected Point2D getLockedCameraLocation() {
    var location = super.getLockedCameraLocation();

    var spaceship = (Spaceship)this.getLockedEntity();
    double offsetY = 0;
    if(((SpaceshipController)spaceship.movement()).isBoosting()){
      offsetY = -5;
    }
    location.setLocation(location.getX(), location.getY() - (this.getViewportHeight() / 2 - this.getLockedEntity().getHeight() / 2 + offsetY));
    return location;
  }
}
