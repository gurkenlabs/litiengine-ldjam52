package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.physics.IMovementController;
import de.gurkenlabs.litiengine.physics.MovementController;
import de.gurkenlabs.litiengine.physics.StickyForce;
import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;

@CombatInfo(hitpoints = 5)
public abstract class Defender extends Enemy {
  Defender(EnergyColor color) {
    super(color, ReaperConstantZ.ENEMY_DEFENDER_ENERGY_AMOUNT);
  }

  public static Defender create(EnergyColor color) {

    var defender = switch (color) {
      case YELLOW -> new Defender.YellowDefender();
      case BLUE -> new Defender.BlueDefender();
      case GREEN -> new Defender.GreenDefender();
    };

    return defender;
  }

  @Override
  protected IMovementController createMovementController() {
    return new DefenderController(this);
  }

  private class DefenderController extends MovementController<Defender> {

    public DefenderController(Defender defender) {
      super(defender);
      var force = new StickyForce(defender, getDefenderVelocity(ReaperConstantZ.REAPER_VERTICAL_VELOCITY), 10);
      this.apply(force);
    }

    private float getDefenderVelocity(float spaceshipVelocity) {
      return spaceshipVelocity - (spaceshipVelocity * 0.1f);
    }

    @Override
    public void update() {
      super.update();
    }
  }
  static class YellowDefender extends Defender {

    public YellowDefender() {
      super(EnergyColor.YELLOW);
    }

    @Override
    public String getSpritesheetName() {
      return "spaceship2_yellow";
    }
  }

  static class BlueDefender extends Defender {

    public BlueDefender() {
      super(EnergyColor.BLUE);
    }

    @Override
    public String getSpritesheetName() {
      return "spaceship2_blue";
    }
  }

  static class GreenDefender extends Defender {

    public GreenDefender() {
      super(EnergyColor.GREEN);
    }

    @Override
    public String getSpritesheetName() {
      return "spaceship2_green";
    }
  }
}