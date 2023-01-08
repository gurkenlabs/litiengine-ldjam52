package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.attributes.Attribute;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.entities.Tag;
import de.gurkenlabs.litiengine.entities.Tags;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.emitters.EntityEmitter;
import de.gurkenlabs.litiengine.graphics.emitters.xml.EmitterLoader;

@Tag("enemy")
public abstract class Enemy extends Creature {

  private final EnergyColor color;
  private final Attribute<Integer> energyAmount;

  public Enemy(EnergyColor color, int energyAmount) {
    this.onDeath(this::handleDeath);
    this.color = color;
    this.energyAmount = new Attribute<>(energyAmount);
  }

  public Attribute<Integer> getEnergyAmount() {
    return energyAmount;
  }

  private void handleDeath(ICombatEntity entity) {
    for (int i = 0; i < this.energyAmount.get(); i++) {
      var energy = new Energy(this.getColor(), Game.random().getLocation(this.getBoundingBox()));
      Game.world().environment().add(energy);
    }
    setVisible(false);
    Game.world().environment().remove(this);
    String explosionEmitterName = String.format("explosion-%s", getColor().name().toLowerCase());
    EntityEmitter explosion = new EntityEmitter(this, EmitterLoader.get(explosionEmitterName));
    explosion.setRenderType(RenderType.OVERLAY);
    Game.world().environment().add(explosion);
  }

  public EnergyColor getColor() {
    return color;
  }
}
