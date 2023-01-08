package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayDeque;
import java.util.Queue;

public class EnemySpawner implements IUpdateable {

  private final Queue<EnemyWave> enemyWaves = new ArrayDeque<>();

  private String currentLevel;

  @Override
  public void update() {
    var nextWave = enemyWaves.peek();
    if (nextWave != null && nextWave.triggerYCood > GameManager.instance().getSpaceship().getY()) {
      enemyWaves.poll();
      nextWave.spawn();
    }

    // TODO: Starting with level 2 => Spawn meteors
  }

  public void start(String name) {
    this.currentLevel = name;

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

  private class EnemyWave {
    private final int number;
    private final int triggerYCood;

    private final EnergyColor energyColor;

    public EnemyWave(int number, int triggerYCoord) {
      switch (EnemySpawner.this.currentLevel) {
        case "level2":
          this.energyColor = EnergyColor.BLUE;
          break;
        case "level3":
          this.energyColor = EnergyColor.GREEN;
          break;
        case "level1":
        default:
          this.energyColor = EnergyColor.YELLOW;
          break;
      }

      this.number = number;
      this.triggerYCood = triggerYCoord;
    }

    public void spawn() {
      double waveStrength = this.determineWaveStrength();
      System.out.println("Spawned wave " + this.number + " (strength: " + waveStrength + ")");

      while (waveStrength > 0) {
        waveStrength -= Game.random().nextBoolean() ? this.spawnSwarm(waveStrength) : this.spawnDefender(waveStrength);
      }
    }

    private double spawnDefender(double waveStrength) {
      if (waveStrength < ReaperConstantZ.ENEMY_DEFENDER_WAVESTRENGTH) {
        return waveStrength;
      }

      System.out.println("-- Spawning a defender");
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
      var agents = new Agent[size];
      for (int i = 0; i < size; i++) {
        agents[i] = Agent.create(this.energyColor, swarm);
      }


      swarm.spawn(determineSpawnPoint());
      return size * ReaperConstantZ.ENEMY_AGENT_WAVESTRENGTH;
    }

    public Point2D determineSpawnPoint() {
      var viewport = Game.world().camera().getViewport();

      var spawnArea = new Rectangle2D.Double(viewport.getX(), viewport.getY() - viewport.getHeight() / 4, viewport.getWidth(), viewport.getHeight() / 2);
      return Game.random().getLocation(spawnArea);
    }

    /**
     * Determines the wave strength based on the number of this wave.
     * 1----2----3----4----5----6----7----8----9----10---11---12
     * 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 5 -> 5 -> 5 -> 6 -> 6 -> 6
     */
    private int determineWaveStrength() {
      int minStrength = 1;
      int dynamicStrength = (int) Math.round(Math.log(this.number) * 2);

      // Math rnd?
      return minStrength + dynamicStrength;
    }
  }
}
