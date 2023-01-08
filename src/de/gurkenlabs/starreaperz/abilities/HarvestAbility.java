package de.gurkenlabs.starreaperz.abilities;

import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.starreaperz.entities.Energy;

@AbilityInfo(impact = 500)
public class HarvestAbility extends Ability {
  /**
   * Initializes a new instance of the {@code Ability} class.
   *
   * @param executor The executing entity
   */
  protected HarvestAbility(Creature executor) {
    super(executor);
  }

  private static class HarvestEnergyEffect extends Effect {

    protected HarvestEnergyEffect(Ability ability) {
      super(ability, EffectTarget.CUSTOM);
    }

    @Override
    protected boolean customTarget(ICombatEntity entity) {
      return entity instanceof Energy;
    }
  }
}
