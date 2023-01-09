package de.gurkenlabs.starreaperz;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Trigger;
import de.gurkenlabs.litiengine.entities.Trigger.TriggerActivation;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.starreaperz.entities.Core;
import de.gurkenlabs.starreaperz.entities.Enemy;
import de.gurkenlabs.starreaperz.entities.EnemySpawner;
import de.gurkenlabs.starreaperz.entities.EnergyProjectile;
import de.gurkenlabs.starreaperz.entities.LaserProjectile;
import de.gurkenlabs.starreaperz.entities.Spaceship;
import de.gurkenlabs.starreaperz.graphics.VerticalRailCamera;

import de.gurkenlabs.starreaperz.ui.screens.IngameScreen;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.Hashtable;

public class GameManager {
  private static GameManager INSTANCE;

  private final Hashtable<Integer, Integer> score = new Hashtable<>();

  private final EnemySpawner spawner = new EnemySpawner();

  private GameState state;

  private Spaceship spaceship;

  private int currentLevel;

  public static GameManager instance() {
    if (INSTANCE == null) {
      INSTANCE = new GameManager();
    }
    return INSTANCE;
  }

  public void score(int points) {
    var currentScore = this.score.get(getCurrentLevel()) != null ? this.score.get(getCurrentLevel()) : 0;
    var newScore = currentScore + points;
    this.score.put(getCurrentLevel(), newScore);
  }

  public int getOverallScore() {
    return this.score.values().stream().reduce(0, Integer::sum);
  }

  public int getCurrentScore() {
    var current = this.score.get(Game.world().environment().getMap().getName());
    return current == null ? 0 : current;
  }

  public void init() {
    Game.world().onLoaded(this::environmentLoaded);
    Game.loop().attach(this.spawner);
    Game.audio().playMusic(Resources.sounds().get("music2.mp3"));
    if (Game.isDebug()) {
      //      Input.keyboard().onKeyPressed(KeyEvent.VK_E, event -> {
      //        if (this.spaceship != null) {
      //          Game.world().environment()
      //              .add(new Energy(Game.random().next(EnergyColor.class), Game.random().getLocation(Game.world().camera().getViewport())));
      //        }
      //      });

      Input.keyboard().onKeyTyped(KeyEvent.VK_F, event -> spaceship.getHitPoints().setToMax());
      Input.keyboard().onKeyTyped(KeyEvent.VK_Q,
          event -> Game.world().environment().add(new Core(Game.random().getLocation(Game.world().camera().getViewport()))));
    }
  }

  public Spaceship getSpaceship() {
    return spaceship;
  }

  public int getCurrentLevel() {
    return currentLevel;
  }

  private void environmentLoaded(Environment env) {
    this.spawner.start();

    this.spaceship = env.get(Spaceship.class, "spaceship");
    var verticalRailCamera = new VerticalRailCamera(spaceship);
    verticalRailCamera.setClampToMap(true);
    Game.world().setCamera(verticalRailCamera);
    Game.world().environment().add(new Core(new Point2D.Double(Game.world().environment().getMap().getSizeInPixels().getWidth() / 2d - 128, 0)));
    Trigger levelCompleteTrigger = new Trigger(TriggerActivation.COLLISION, "level-complete");
    levelCompleteTrigger.setSize(Game.world().environment().getMap().getSizeInPixels().getWidth(), 64);
    levelCompleteTrigger.setY(256);
    levelCompleteTrigger.addActivator(getSpaceship());
    levelCompleteTrigger.addActivatedListener(l -> winLevel());
    Game.world().environment().add(levelCompleteTrigger);
  }

  public void startGame() {
    Game.screens().display("INGAME");
    Game.world().loadEnvironment("galaxy");
    Game.audio().playMusic(Resources.sounds().get("music1.ogg"));
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
    this.killEverything();

  }

  private void killEverything() {
    int i = 100;
    for (var enemy : Game.world().environment().getEntities(Enemy.class)) {
      if (enemy.getBoundingBox().intersects(Game.world().camera().getViewport())) {
        Game.loop().perform(i, enemy::die);
        i += 100;
      } else {
        enemy.die();
      }
    }

    for (var projectile : Game.world().environment().getEntities(LaserProjectile.class)) {
      Game.world().environment().remove(projectile);
    }

    for (var projectile : Game.world().environment().getEntities(EnergyProjectile.class)) {
      Game.world().environment().remove(projectile);
    }
  }

  public void loseLevel() {
    setState(GameState.LOST);
  }

  public void restartLevel() {
    this.score.put(getCurrentLevel(), 0);
    Game.world().environment().clear();
    Game.world().loadEnvironment(Game.world().reset(Game.world().environment().getMap()));
    setState(GameState.INGAME);
  }

  public void nextLevel() {
    if (getCurrentLevel() == 2) {
      Game.world().environment().clear();
      this.score.clear();
      Game.screens().display("Menu");
      Game.audio().playMusic(Resources.sounds().get("music2.mp3"));
      setState(GameState.MENU);
      // TODO: return to main menu or display win sceen if there is time left
    } else {
      this.currentLevel++;
      Game.world().environment().clear();
      spaceship.getHitPoints().setToMax();
      Game.world().loadEnvironment("galaxy");
      setState(GameState.INGAME);
    }
  }
}
