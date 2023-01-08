package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.physics.IMovementController;
import de.gurkenlabs.litiengine.physics.MovementController;
import de.gurkenlabs.litiengine.physics.StickyForce;
import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;

@CombatInfo(hitpoints = 1)
@MovementInfo(velocity = 40)
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
    var leader = this.getSwarm().getLeader();
    return this.equals(leader);
  }

  private static class AgentController extends MovementController<Agent> {

    public AgentController(Agent agent) {
      super(agent);
      var force = new StickyForce(agent, getAgentVelocity(), 10);
      this.apply(force);
    }

    private float getAgentVelocity() {
      return (float) ReaperConstantZ.REAPER_VERTICAL_VELOCITY - ((float) ReaperConstantZ.REAPER_VERTICAL_VELOCITY * 0.1f);
    }

    @Override
    public void update() {
      super.update();

      // if we hit the spaceship
      var hitEnemy = Game.world().environment().findCombatEntities(this.getEntity().getHitBox(), e -> e instanceof Spaceship && !e.isDead()).stream().findFirst().orElse(null);
      if (hitEnemy != null && !hitEnemy.wasHit(ReaperConstantZ.REAPER_HIT_COOLDOWN)) {
        hitEnemy.hit(1);
        this.getEntity().hit(1);
        return;
      }

      if (this.getEntity().isLeader()) {
        // try to move away from the spaceship if too close
        var angle = Math.sin(Math.toRadians(Game.time().now() / 3.0)) * 90;
        Game.physics().move(this.getEntity(), angle, this.getEntity().getTickVelocity());
      } else {
        var predecessor = this.getEntity().getSwarm().getPredecessor(this.getEntity());
        if (predecessor == null || predecessor.getCenter().distance(this.getEntity().getCenter()) < this.getEntity().getWidth()*1.15) {
          return;
        }

        Game.physics().move(this.getEntity(), predecessor.getCenter(), this.getEntity().getTickVelocity());
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
      super(EnergyColor.GREEN, swarm);
    }

    @Override
    public String getSpritesheetName() {
      return "spaceship1_green";
    }
  }
}
