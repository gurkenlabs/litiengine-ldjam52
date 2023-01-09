package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.starreaperz.graphics.emitters.CoreEmitter;
import java.awt.geom.Point2D;


@EntityInfo(width = 256, height = 256)
public class Core extends Entity {

  public Core(Point2D location) {
    super();
    setLocation(location);
    Game.world().environment().add(new CoreEmitter(this, EnergyColor.getLevelColor()));
  }


}
