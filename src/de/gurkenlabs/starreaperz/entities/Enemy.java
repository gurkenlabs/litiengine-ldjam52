package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.attributes.Attribute;
import de.gurkenlabs.litiengine.entities.AnimationInfo;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.physics.IMovementController;
import de.gurkenlabs.litiengine.physics.MovementController;

@AnimationInfo(spritePrefix = {"enemy_1"})
@CombatInfo(hitpoints = 5)
public class Enemy extends Creature {

  public Enemy() {
    this.onDeath(this::handleDeath);
  }

  private final Attribute<Integer> energyAmount = new Attribute<>(5);

  @Override
  protected IMovementController createMovementController() {
    return new EnemyController(this);
  }

  public Attribute<Integer> getEnergyAmount() {
    return energyAmount;
  }

  private void handleDeath(ICombatEntity entity) {
    for (int i = 0; i < this.energyAmount.get(); i++) {
      var energy = new Energy(Game.random().next(Energy.EnergyColor.class), Game.random().getLocation(this.getBoundingBox()));
      Game.world().environment().add(energy);
    }
  }

  private static class EnemyController extends MovementController<Enemy> {
    public EnemyController(Enemy mobileEntity) {
      super(mobileEntity);
    }
  }
}
