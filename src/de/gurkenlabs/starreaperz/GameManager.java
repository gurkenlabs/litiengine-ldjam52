package de.gurkenlabs.starreaperz;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.starreaperz.entities.Spaceship;
import de.gurkenlabs.starreaperz.graphics.VerticalRailCamera;

public class GameManager {
  private static GameManager INSTANCE;

  public static GameManager instance() {
    if (INSTANCE == null) {
      INSTANCE = new GameManager();
    }
    return INSTANCE;
  }

  public void init() {
    Game.world().onLoaded(this::environmentLoaded);
  }

  private void environmentLoaded(Environment env) {
    var spaceship = env.get(Spaceship.class, "spaceship");
    var verticalRailCamers = new VerticalRailCamera(spaceship);
    verticalRailCamers.setClampToMap(true);
    Game.world().setCamera(verticalRailCamers);
  }

  public void startGame() {
    Game.screens().display("INGAME");
    Game.world().loadEnvironment("level1");
  }

}
