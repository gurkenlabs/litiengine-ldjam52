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
import de.gurkenlabs.starreaperz.ui.components.StateDependentUIComponent;
import java.awt.*;

public class IngameScreen extends GameScreen implements StateDependentUIComponent {
  private static IngameScreen INSTANCE;

  private HUD hud;


  private IngameScreen() {
    super("INGAME");
    Game.world().addListener(new EnvironmentListener() {
      @Override
      public void loaded(Environment environment) {

      }
    });
  }

  public static IngameScreen instance() {
    if (INSTANCE == null) {
      INSTANCE = new IngameScreen();
    }
    return INSTANCE;
  }


  @Override
  public void render(Graphics2D g) {

    super.render(g);
  }

  @Override protected void initializeComponents() {
    super.initializeComponents();
    this.hud = new HUD();
    getComponents().add(getHud());
  }

  @Override public void updateState() {
    switch (GameManager.instance().getState()) {
      case PAUSE, LEVEL_OVERVIEW, WON, LOST, SCORE_OVERVIEW, MENU, INSTRUCTIONS -> getHud().setVisible(false);
      case INGAME -> getHud().setVisible(true);
      default -> throw new IllegalStateException("Unexpected value: " + GameManager.instance().getState());
    }
  }

  public HUD getHud() {
    return hud;
  }
}
