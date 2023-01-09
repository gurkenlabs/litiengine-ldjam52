package de.gurkenlabs.starreaperz.ui.screens;

import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.EnvironmentListener;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.starreaperz.GameManager;

import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;
import de.gurkenlabs.starreaperz.constants.ReaperImageZ;
import de.gurkenlabs.starreaperz.ui.components.HUD;
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

  @Override protected void initializeComponents() {
    super.initializeComponents();
    getComponents().add(HUD.instance());
  }
}
