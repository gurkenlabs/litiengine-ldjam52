package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.GameState;
import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayDeque;
import java.util.Queue;

public class EnemySpawner implements IUpdateable {

  private final Queue<EnemyWave> enemyWaves = new ArrayDeque<>();

  private long lastAsteorid;

  private int asteoridDelay;

  @Override
  public void update() {
    if (GameManager.instance().getState() != GameState.INGAME) {
      return;
    }

    var nextWave = enemyWaves.peek();
    if (nextWave != null && nextWave.triggerYCoord > GameManager.instance().getSpaceship().getY()) {
      enemyWaves.poll();
      nextWave.spawn();
    }

    if (asteoridDelay > 0 && Game.time().since(lastAsteorid) > asteoridDelay && !GameManager.instance().getSpaceship().isDead()) {
      this.lastAsteorid = Game.time().now();
      Game.world().environment().add(new Asteroid());
      if (GameManager.instance().getCurrentLevel() == 2) {
        var spawns = Game.random().nextInt(4);
        for (int i = 0; i < spawns; i++) {
          Game.world().environment().add(new Asteroid());
        }
      }

      this.asteoridDelay = calcAsteoridDelay();
      System.out.println("Spawned astroid");
    }

    // TODO: Starting with level 2 => Spawn meteors
  }

  private int calcAsteoridDelay() {
    return switch (GameManager.instance().getCurrentLevel()) {
      case 1 -> Game.random().nextInt(3500, 6000);
      case 2 -> Game.random().nextInt(2500, 5000);
      default -> 0;
    };
  }

  public void start() {
    this.asteoridDelay = calcAsteoridDelay();

    this.enemyWaves.clear();
    this.enemyWaves.add(new EnemyWave(1, 11500));
    this.enemyWaves.add(new EnemyWave(2, 10500));
    this.enemyWaves.add(new EnemyWave(3, 9500));
    this.enemyWaves.add(new EnemyWave(4, 8500));
    this.enemyWaves.add(new EnemyWave(5, 7500));
    this.enemyWaves.add(new EnemyWave(6, 6500));
    this.enemyWaves.add(new EnemyWave(7, 5500));
    this.enemyWaves.add(new EnemyWave(8, 4500));
    this.enemyWaves.add(new EnemyWave(9, 3500));
    this.enemyWaves.add(new EnemyWave(10, 2500));
    this.enemyWaves.add(new EnemyWave(11, 1500));
    this.enemyWaves.add(new EnemyWave(12, 500));
  }

  private static class EnemyWave {
    private final int number;
    private final int triggerYCoord;

    private final EnergyColor energyColor;

    public EnemyWave(int number, int triggerYCoord) {
      this.energyColor = EnergyColor.getLevelColor();
      this.number = number;
      this.triggerYCoord = triggerYCoord;
    }

    public void spawn() {
      double waveStrength = this.determineWaveStrength();
      System.out.println("Spawned wave " + this.number + " (strength: " + waveStrength + ")");


      int defenders = 0;
      while (waveStrength > 0) {
        var spawnSwarm = Game.random().nextBoolean();
        if (!spawnSwarm) {
          defenders++;
        }

        waveStrength -= spawnSwarm || defenders > Math.ceil(waveStrength) ? this.spawnSwarm(waveStrength) : this.spawnDefender(waveStrength);
      }
    }

    private double spawnDefender(double waveStrength) {
      if (waveStrength < ReaperConstantZ.ENEMY_DEFENDER_WAVESTRENGTH) {
        return waveStrength;
      }

      System.out.println("-- Spawning a defender");

      var defender = Defender.create(this.energyColor);
      defender.setLocation(determineSpawnPoint());
      Game.world().environment().add(defender);
      return ReaperConstantZ.ENEMY_DEFENDER_WAVESTRENGTH;
    }

    private double spawnSwarm(double waveStrength) {
      if (waveStrength < 2 * ReaperConstantZ.ENEMY_AGENT_WAVESTRENGTH) {
        // need at lease 0.5 remaining to spawn 2 agents
        return waveStrength;
      }

      var maxSwarmSizeBasedOnStrength = (int) (waveStrength / ReaperConstantZ.ENEMY_AGENT_WAVESTRENGTH);
      var size = Math.min(Game.random().nextInt(1, 4) * 2, maxSwarmSizeBasedOnStrength);
      System.out.println("-- Spawning a swarm (" + size + ")");

      var swarm = new Swarm();
      for (int i = 0; i < size; i++) {
        Agent.create(this.energyColor, swarm);
      }

      swarm.spawn(determineSpawnPoint());
      return size * ReaperConstantZ.ENEMY_AGENT_WAVESTRENGTH;
    }

    public Point2D determineSpawnPoint() {
      var viewport = Game.world().camera().getViewport();
      final int offsetX = 100;
      var spawnArea = new Rectangle2D.Double(viewport.getX() + offsetX, viewport.getY() - viewport.getHeight() / 4, viewport.getWidth() - 2 * offsetX,
          viewport.getHeight() / 4);
      return Game.random().getLocation(spawnArea);
    }

    /**
     * Determines the wave strength based on the number of this wave. 1----2----3----4----5----6----7----8----9----10---11---12 1 -> 2 -> 3 -> 4 -> 4
     * -> 5 -> 5 -> 5 -> 5 -> 6 -> 6 -> 6
     */
    private int determineWaveStrength() {
      int minStrength = 1;
      int dynamicStrength = (int) Math.round(Math.log(this.number) * 2);

      // Math rnd?
      return minStrength + dynamicStrength;
    }
  }
}
