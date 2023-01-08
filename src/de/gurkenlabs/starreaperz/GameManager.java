package de.gurkenlabs.starreaperz;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.starreaperz.entities.EnemySpawner;
import de.gurkenlabs.starreaperz.entities.Energy;
import de.gurkenlabs.starreaperz.entities.EnergyColor;
import de.gurkenlabs.starreaperz.entities.Spaceship;
import de.gurkenlabs.starreaperz.graphics.VerticalRailCamera;

import java.awt.event.KeyEvent;
import java.util.Hashtable;

public class GameManager {
  private static GameManager INSTANCE;

  private final Hashtable<String, Integer> score = new Hashtable<>();

  private final EnemySpawner spawner = new EnemySpawner();

  private GameState state;

  private Spaceship spaceship;

  public static GameManager instance() {
    if (INSTANCE == null) {
      INSTANCE = new GameManager();
    }
    return INSTANCE;
  }

  public void score(int points) {
    var level = Game.world().environment().getMap().getName();
    var currentScore = this.score.get(level) != null ? this.score.get(level) : 0;
    var newScore = currentScore + points;
    this.score.put(level, newScore);
  }

  public int getOverallScore() {
    return this.score.values().stream().reduce(0, Integer::sum);
  }

  public void init() {
    Game.world().onLoaded(this::environmentLoaded);
    Game.loop().attach(this.spawner);
    if (Game.isDebug()) {
      Input.keyboard().onKeyPressed(KeyEvent.VK_E, event -> {
        if (this.spaceship != null) {
          Game.world().environment().add(new Energy(Game.random().next(EnergyColor.class), Game.random().getLocation(Game.world().camera().getViewport())));
        }
      });
    }
  }

  public Spaceship getSpaceship() {
    return spaceship;
  }

  private void environmentLoaded(Environment env) {
    this.spawner.start(env.getMap().getName());

    this.spaceship = env.get(Spaceship.class, "spaceship");
    var verticalRailCamers = new VerticalRailCamera(spaceship);
    verticalRailCamers.setClampToMap(true);
    Game.world().setCamera(verticalRailCamers);
  }

  public void startGame() {
    Game.screens().display("INGAME");
    Game.world().loadEnvironment("level2");
    setState(GameState.INGAME);
  }

  public GameState getState() {
    return state;
  }

  public void setState(GameState state) {
    this.state = state;
  }

}
