package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.constants.ReaperColorZ;
import java.awt.*;

public enum EnergyColor {
  BLUE,
  GREEN,
  YELLOW;

  public Color toAwtColor() {
    return switch (this) {
      case BLUE -> ReaperColorZ.ENERGY_BLUE;
      case GREEN -> ReaperColorZ.ENERGY_GREEN;
      case YELLOW -> ReaperColorZ.ENERGY_YELLOW;
    };

  }

  public static EnergyColor getLevelColor() {
    switch (Game.world().environment().getMap().getName()) {
      case "level1":
        return EnergyColor.YELLOW;
      case "level2":
        return EnergyColor.BLUE;
      case "level3":
        return EnergyColor.GREEN;
      default:
        return null;
    }
  }
}
