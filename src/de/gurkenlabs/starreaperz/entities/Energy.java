package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.AnimationInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.graphics.OverlayPixelsImageEffect;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.physics.IMovementController;
import de.gurkenlabs.litiengine.physics.MovementController;
import de.gurkenlabs.litiengine.physics.StickyForce;
import de.gurkenlabs.litiengine.tweening.TweenType;
import de.gurkenlabs.litiengine.util.MathUtilities;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;
import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;

import java.awt.geom.Point2D;

@AnimationInfo(spritePrefix = {"energy-blue", "energy-green", "energy-yellow"})
public class Energy extends Creature implements SpaceshipListener, IUpdateable {
  private static final String[] collectSounds = new String[] {
      "zoom.wav",
      "zoom2.wav",
  };
  private static final float VELOCITY_DIFF_PERCENT = 0.1f;

  private final StickyForce movementForce;

  private final float velocityDiff;
  private final int originalSize;
  private final double originalDistance;

  private final EnergyColor color;

  public Energy(EnergyColor color, Point2D origin) {
    super("energy-" + color.name().toLowerCase());
    this.color = color;
    this.velocityDiff = Game.random().nextFloat(VELOCITY_DIFF_PERCENT, VELOCITY_DIFF_PERCENT * 3f);
    this.setLocation(origin);

    this.originalSize = Game.random().nextInt(16, 48);
    this.originalDistance = GeometricUtilities.distance(getCenter(), GameManager.instance().getSpaceship().getCenter());
    setSize(originalSize, originalSize);
    animations().setAutoScaling(true);
    Game.tweens().begin(this, TweenType.ANGLE, 10000);
    this.setRenderType(RenderType.OVERLAY);
    this.movementForce = new StickyForce(this, getEnergyVelocity(ReaperConstantZ.REAPER_VERTICAL_VELOCITY), 10);
    this.movement().apply(this.movementForce);
  }

  private float getEnergyVelocity(float spaceshipVelocity) {
    return spaceshipVelocity - (spaceshipVelocity * this.velocityDiff);
  }


  @Override
  public void update() {
    // if we hit the spaceship
    if (Game.world().environment().findCombatEntities(this.getHitBox(), e -> e.equals(GameManager.instance().getSpaceship())).size() > 0) {
      GameManager.instance().score((int) this.getWidth());
      // TODO: ADD POOOOOF EMITTER
      Game.audio().playSound(Game.random().choose(collectSounds));
      GameManager.instance().getSpaceship().animations().add(new OverlayPixelsImageEffect(50, this.getColor().toAwtColor()));
      Game.world().environment().remove(this);
    }

    if (!Game.world().camera().getViewport().intersects(this.getBoundingBox())) {
      Game.world().environment().remove(this);
    }
  }

  @Override
  public void loaded(Environment environment) {
    super.loaded(environment);
    GameManager.instance().getSpaceship().addSpaceshipListener(this);
  }

  @Override
  public void removed(Environment environment) {
    super.removed(environment);
    GameManager.instance().getSpaceship().removeSpaceshipListener(this);
  }

  @Override
  public void velocityChanged(float speed) {
    this.movementForce.setStrength(getEnergyVelocity(speed));
  }

  @Override
  protected IMovementController createMovementController() {
    return new EnergyController(this);
  }

  public EnergyColor getColor() {
    return color;
  }

  private static class EnergyController extends MovementController<Energy> {

    public EnergyController(Energy energy) {
      super(energy);
    }

    @Override
    public void update() {
      super.update();
      if (GameManager.instance().getSpaceship().isHarvesting()) {
        Game.physics().move(getEntity(), GameManager.instance().getSpaceship().getCenter(), getEntity().getTickVelocity());
        double newSize = MathUtilities.clamp(
            (getEntity().originalSize / getEntity().originalDistance) * GeometricUtilities.distance(getEntity().getCenter(),
                GameManager.instance().getSpaceship().getCenter()), 4, getEntity().originalSize);
        if (newSize < getEntity().getWidth()) {
          getEntity().setSize(newSize, newSize);
        }
      }
    }
  }
}
