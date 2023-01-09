package de.gurkenlabs.starreaperz;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Trigger;
import de.gurkenlabs.litiengine.entities.Trigger.TriggerActivation;
import de.gurkenlabs.litiengine.entities.TriggerActivatedListener;
import de.gurkenlabs.litiengine.entities.TriggerEvent;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.starreaperz.entities.Core;
import de.gurkenlabs.starreaperz.entities.EnemySpawner;
import de.gurkenlabs.starreaperz.entities.Energy;
import de.gurkenlabs.starreaperz.entities.EnergyColor;
import de.gurkenlabs.starreaperz.entities.Spaceship;
import de.gurkenlabs.starreaperz.graphics.VerticalRailCamera;

import de.gurkenlabs.starreaperz.ui.components.HUD;
import de.gurkenlabs.starreaperz.ui.screens.IngameScreen;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
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
          Game.world().environment()
              .add(new Energy(Game.random().next(EnergyColor.class), Game.random().getLocation(Game.world().camera().getViewport())));
        }
      });
      Input.keyboard().onKeyTyped(KeyEvent.VK_Q,
          event -> Game.world().environment().add(new Core(Game.random().getLocation(Game.world().camera().getViewport()))));
    }
  }

  public Spaceship getSpaceship() {
    return spaceship;
  }

  private void environmentLoaded(Environment env) {
    this.spawner.start(env.getMap().getName());

    this.spaceship = env.get(Spaceship.class, "spaceship");
    var verticalRailCamera = new VerticalRailCamera(spaceship);
    verticalRailCamera.setClampToMap(true);
    Game.world().setCamera(verticalRailCamera);
    Game.world().environment().add(new Core(new Point2D.Double(Game.world().environment().getMap().getSizeInPixels().getWidth() / 2d - 128, 0)));
    Trigger levelCompleteTrigger = new Trigger(TriggerActivation.COLLISION, "level-complete");
    levelCompleteTrigger.setSize(Game.world().environment().getMap().getSizeInPixels().getWidth(), 64);
    levelCompleteTrigger.setY(256);
    levelCompleteTrigger.addActivator(getSpaceship());
    levelCompleteTrigger.addActivatedListener(l -> {
      winLevel();
    });
    Game.world().environment().add(levelCompleteTrigger);
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
    IngameScreen.instance().updateState();
  }

  public void winLevel() {
    setState(GameState.WON);
  }

  public void loseLevel() {
    setState(GameState.LOST);
  }

  public void restartLevel() {
    Game.world().environment().clear();
    Game.world().loadEnvironment(Game.world().reset(Game.world().environment().getMap()));
    setState(GameState.INGAME);
  }

}
