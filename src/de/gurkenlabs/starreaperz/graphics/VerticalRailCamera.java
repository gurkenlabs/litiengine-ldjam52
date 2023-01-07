package de.gurkenlabs.starreaperz.graphics;

import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.graphics.LocationLockCamera;

import java.awt.geom.Point2D;

public class VerticalRailCamera extends LocationLockCamera {
  /**
   * Initializes a new instance of the {@code LocationLockCamera}.
   *
   * @param entity The entity to which the focus will be locked.
   */
  public VerticalRailCamera(IEntity entity) {
    super(entity);
  }

  @Override
  protected Point2D getLockedCameraLocation() {
    var location = super.getLockedCameraLocation();
    location.setLocation(location.getX(), location.getY() - (this.getViewportHeight() / 2 - this.getLockedEntity().getHeight() / 2));
    return location;
  }
}
