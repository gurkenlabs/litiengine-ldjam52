package de.gurkenlabs.starreaperz.graphics.emitters;

import de.gurkenlabs.litiengine.entities.EmitterInfo;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.graphics.emitters.EntityEmitter;
import de.gurkenlabs.litiengine.graphics.emitters.particles.SpriteParticle;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.starreaperz.entities.EnergyColor;

@EmitterInfo(duration = 0, activateOnInit = true)
public class CoreEmitter extends EntityEmitter {
  private final EnergyColor color;
  private final SpriteParticle ring;
  private final SpriteParticle core;
  private final SpriteParticle halo1;
  private final SpriteParticle halo2;
  private final SpriteParticle halo3;
  private final SpriteParticle halo4;

  public CoreEmitter(IEntity entity, EnergyColor color) {
    super(entity, true);
    this.color = color;
    String spriteName = String.format("core-%s", color.name().toLowerCase());
    this.ring = new SpriteParticle(Resources.spritesheets().get(spriteName + "-1"));
    this.core = new SpriteParticle(Resources.spritesheets().get(spriteName + "-2"));
    this.halo1 = new SpriteParticle(Resources.spritesheets().get(spriteName + "-3"));
    this.halo2 = new SpriteParticle(Resources.spritesheets().get(spriteName + "-3"));
    this.halo3 = new SpriteParticle(Resources.spritesheets().get(spriteName + "-3"));
    this.halo4 = new SpriteParticle(Resources.spritesheets().get(spriteName + "-3"));
    initParticle(ring, 0, 0);
    initParticle(halo1, 0, -0.1f);
    initParticle(halo2, 45, 0.1f);
    initParticle(halo3, 90, -0.2f);
    initParticle(halo4, 135, 0.3f);
    initParticle(core, 0, 0.1f);
    activate();
  }

  private void initParticle(SpriteParticle p, final float initialAngle, final float deltaAngle) {
    p.setAnimateSprite(true);
    p.setAngle(initialAngle);
    p.setDeltaAngle(deltaAngle);
    addParticle(p);
  }
}
