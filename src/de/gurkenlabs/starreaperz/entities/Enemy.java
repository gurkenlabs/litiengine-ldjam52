package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.entities.AnimationInfo;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.physics.IMovementController;
import de.gurkenlabs.litiengine.physics.MovementController;

@AnimationInfo(spritePrefix = {"enemy_1"})
@CombatInfo(hitpoints = 1)
public class Enemy extends Creature {

  @Override
  protected IMovementController createMovementController() {
    return new EnemyController(this);
  }

  private static class EnemyController extends MovementController<Enemy> {
    public EnemyController(Enemy mobileEntity) {
      super(mobileEntity);
    }
  }
}
