package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.constants.ReaperColorZ;
import java.awt.Color;

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
    return switch (GameManager.instance().getCurrentLevel()) {
      case 0 -> EnergyColor.YELLOW;
      case 1 -> EnergyColor.BLUE;
      case 2 -> EnergyColor.GREEN;
      default -> null;
    };
  }
}
