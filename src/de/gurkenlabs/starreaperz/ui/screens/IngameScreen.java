package de.gurkenlabs.starreaperz.ui.screens;

import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.EnvironmentListener;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.util.Imaging;
import de.gurkenlabs.starreaperz.GameManager;

import de.gurkenlabs.starreaperz.GameState;
import de.gurkenlabs.starreaperz.constants.ReaperConstantZ;
import de.gurkenlabs.starreaperz.constants.ReaperImageZ;
import de.gurkenlabs.starreaperz.ui.components.HUD;
import de.gurkenlabs.starreaperz.ui.components.StateDependentUIComponent;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class IngameScreen extends GameScreen implements StateDependentUIComponent {
  private final double scaleFactor = Game.window().getResolution().getWidth() / 1920d;
  private final BufferedImage LOGO = Imaging.scale(ReaperImageZ.LOGO, scaleFactor, AffineTransformOp.TYPE_BICUBIC);
  private static IngameScreen INSTANCE;

  private HUD hud;
  private Menu ingameMenu;


  private IngameScreen() {
    super("INGAME");
    Game.world().addListener(new EnvironmentListener() {
      @Override
      public void loaded(Environment environment) {

      }
    });
    Input.keyboard().onKeyTyped(KeyEvent.VK_ESCAPE, l -> {
      if (GameManager.instance().getState() == GameState.INGAME) {
        pauseGame();
      } else if (GameManager.instance().getState() == GameState.PAUSE) {
        resumeGame();
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
    if (GameManager.instance().getState() == GameState.PAUSE) {
      ImageRenderer.render(g, LOGO, Game.window().getResolution().getWidth() / 2d - LOGO.getWidth() / 2d,
          Game.window().getResolution().getHeight() * 2 / 10d);
    }
  }

  @Override protected void initializeComponents() {
    super.initializeComponents();
    this.hud = new HUD();

    double screenWidth = Game.window().getResolution().getWidth();
    double screenHeight = Game.window().getResolution().getHeight();
    double menuWidth = screenWidth * 2 / 8d;
    double menuHeight = screenHeight * 1 / 5d;
    double menuX = screenWidth / 2d - menuWidth / 2d;
    double menuY = screenHeight / 2d;
    this.ingameMenu = new de.gurkenlabs.litiengine.gui.Menu(menuX, menuY, menuWidth, menuHeight, "Resume", "Restart Level", "Exit");
    ingameMenu.onChange(c -> {
      if (c == 0) {
        resumeGame();
      } else if (c == 1) {
        restartLevel();
      } else if (c == 2) {
        System.exit(0);
      }
    });
    getComponents().add(ingameMenu);
    getComponents().add(getHud());
  }

  @Override public void updateState() {
    switch (GameManager.instance().getState()) {
      case PAUSE -> {
        getHud().setVisible(false);
        ingameMenu.setVisible(true);
      }
      case INGAME -> {
        getHud().setVisible(true);
        ingameMenu.setVisible(false);
      }
      default -> {
        getHud().setVisible(false);
        ingameMenu.setVisible(false);
      }
    }
  }

  public HUD getHud() {
    return hud;
  }

  private void pauseGame() {
    GameManager.instance().setState(GameState.PAUSE);

  }

  private void resumeGame() {
    GameManager.instance().setState(GameState.INGAME);
  }

  private void restartLevel() {
    GameManager.instance().setState(GameState.INGAME);

  }
}
