package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;

public class Agent extends Enemy {

  public Agent(EnergyColor color) {
    super(color, ReaperConstantZ.ENEMY_SWARM_ENERGY_AMOUNT);
  }
}
