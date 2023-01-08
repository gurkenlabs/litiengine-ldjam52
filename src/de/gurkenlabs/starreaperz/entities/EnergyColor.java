package de.gurkenlabs.starreaperz.entities;

import java.awt.*;

public enum EnergyColor {
  BLUE,
  GREEN,
  YELLOW;

  public Color toAwtColor() {
    return switch (this) {
      case BLUE -> new Color(46, 134, 189, 100);
      case GREEN -> new Color(105, 173, 82, 100);
      case YELLOW -> new Color(232, 217, 38, 100);
    };

  }
}
