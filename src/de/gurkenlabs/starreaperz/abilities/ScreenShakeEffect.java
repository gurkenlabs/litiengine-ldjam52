package de.gurkenlabs.starreaperz.abilities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import java.awt.Shape;

public class ScreenShakeEffect extends Effect {

  private final int duration;

  private final double intensity;

  public ScreenShakeEffect(final Ability ability, final double intensity, final int duration) {
    super(ability);
    this.duration = duration;
    this.intensity = intensity;
  }

  @Override
  public void apply(final Shape impactArea) {
    super.apply(impactArea);
    Game.world().camera().shake(this.intensity, 30, this.duration);
  }
}
