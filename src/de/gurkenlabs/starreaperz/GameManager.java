package de.gurkenlabs.starreaperz;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.starreaperz.entities.Energy;
import de.gurkenlabs.starreaperz.entities.Spaceship;
import de.gurkenlabs.starreaperz.graphics.VerticalRailCamera;

import java.awt.event.KeyEvent;

public class GameManager {
  private static GameManager INSTANCE;

  private GameState state;

  private Spaceship spaceship;

  public static GameManager instance() {
    if (INSTANCE == null) {
      INSTANCE = new GameManager();
    }
    return INSTANCE;
  }

  public void init() {
    Game.world().onLoaded(this::environmentLoaded);

    if (Game.isDebug()) {
      Input.keyboard().onKeyPressed(KeyEvent.VK_E, event -> {
        if (this.spaceship != null) {
          Game.world().environment().add(new Energy(Game.random().next(Energy.EnergyColor.class), Game.random().getLocation(Game.world().camera().getViewport())));
        }
      });
    }
  }

  public Spaceship getSpaceship() {
    return spaceship;
  }

  private void environmentLoaded(Environment env) {
    this.spaceship = env.get(Spaceship.class, "spaceship");
    var verticalRailCamers = new VerticalRailCamera(spaceship);
    verticalRailCamers.setClampToMap(true);
    Game.world().setCamera(verticalRailCamers);
  }

  public void startGame() {
    Game.screens().display("INGAME");
    Game.world().loadEnvironment("level1");
    setState(GameState.INGAME);
  }

  public GameState getState() {
    return state;
  }

  public void setState(GameState state) {
    this.state = state;
  }

}
