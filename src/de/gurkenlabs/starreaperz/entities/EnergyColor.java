package de.gurkenlabs.starreaperz.entities;

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
}
