package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Swarm {

  private final List<Agent> agents = new ArrayList<>();

  public Swarm() {
  }

  public void add(Agent agent) {
    this.agents.add(agent);
  }

  public void spawn(Point2D location) {
    for (int i = 0; i < agents.size(); i++) {
      final Agent enemy = agents.get(i);
      enemy.setLocation(location);
      if (i > 0) {
        Game.loop().perform(i * 500, () -> Game.world().environment().add(enemy));
      } else {
        Game.world().environment().add(enemy);
      }
    }
  }

  public Agent getLeader() {
    return this.agents.stream().filter(x -> !x.isDead()).findFirst().orElse(null);
  }

  public Agent getPredecessor(Agent agent) {
    if (agent.isLeader()) {
      return null;
    }

    Agent predecessor = null;
    for (var a : this.agents) {
      if (a.equals(agent)) {
        return predecessor;
      }

      predecessor = a;
    }

    return predecessor;
  }
}
