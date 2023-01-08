package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.physics.IMovementController;
import de.gurkenlabs.litiengine.physics.MovementController;
import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;
@CombatInfo(hitpoints = 1)
public abstract class Agent extends Enemy {
  private final Swarm swarm;

  Agent(EnergyColor color, Swarm swarm) {
    super(color, ReaperConstantZ.ENEMY_SWARM_ENERGY_AMOUNT);
    this.swarm = swarm;
  }

  public static Agent create(EnergyColor color, Swarm swarm) {

    var agent = switch (color) {
      case YELLOW -> new YellowAgent(swarm);
      case BLUE -> new BlueAgent(swarm);
      case GREEN -> new GreenAgent(swarm);
    };

    swarm.add(agent);
    return agent;
  }

  @Override
  protected IMovementController createMovementController() {
    return new AgentController(this);
  }

  public Swarm getSwarm() {
    return swarm;
  }

  public boolean isLeader() {
    return this.equals(this.getSwarm().getLeader());
  }

  private class AgentController extends MovementController<Agent> {

    public AgentController(Agent mobileEntity) {
      super(mobileEntity);
    }

    @Override
    public void update() {
      super.update();
      if (this.getEntity().isLeader()) {
      // TODO: DO DIS!
      } else {
        Game.physics().move(this.getEntity(), this.getEntity().getSwarm().getLeader().getCenter(), this.getEntity().getTickVelocity());
      }
    }
  }

  static class YellowAgent extends Agent {

    public YellowAgent(Swarm swarm) {
      super(EnergyColor.YELLOW, swarm);
    }

    @Override
    public String getSpritesheetName() {
      return "spaceship1_yellow";
    }
  }

  static class BlueAgent extends Agent {

    public BlueAgent(Swarm swarm) {
      super(EnergyColor.BLUE, swarm);
    }

    @Override
    public String getSpritesheetName() {
      return "spaceship1_blue";
    }
  }

  static class GreenAgent extends Agent {

    public GreenAgent(Swarm swarm) {
      super(EnergyColor.YELLOW, swarm);
    }

    @Override
    public String getSpritesheetName() {
      return "spaceship1_green";
    }
  }
}
