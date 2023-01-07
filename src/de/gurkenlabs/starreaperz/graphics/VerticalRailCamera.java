package de.gurkenlabs.starreaperz.graphics;

import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.graphics.LocationLockCamera;

public class VerticalRailCamera extends LocationLockCamera {
  /**
   * Initializes a new instance of the {@code LocationLockCamera}.
   *
   * @param entity The entity to which the focus will be locked.
   */
  public VerticalRailCamera(IEntity entity) {
    super(entity);
  }
}
