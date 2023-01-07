package de.gurkenlabs.starreaperz.ui.screens;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.EnvironmentListener;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;

import java.awt.*;

public class IngameScreen extends GameScreen {

  public IngameScreen() {
    super("INGAME");
    Game.world().addListener(new EnvironmentListener() {
      @Override
      public void loaded(Environment environment) {

      }
    });
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);
  }
}
