package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;

public class SwarmEnemy extends Enemy{

  public SwarmEnemy(EnergyColor color) {
    super(color, ReaperConstantZ.ENEMY_SWARM_ENERGY_AMOUNT);
  }
}
