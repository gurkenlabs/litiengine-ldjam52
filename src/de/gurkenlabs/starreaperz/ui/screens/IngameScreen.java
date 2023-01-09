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
import de.gurkenlabs.starreaperz.constants.ReaperImageZ;
import de.gurkenlabs.starreaperz.ui.StateDependentUIComponent;
import de.gurkenlabs.starreaperz.ui.components.HUD;
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
  private ImageComponent retryButton;

  private ImageComponent nextLevelButton;


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

  @Override public void prepare() {
    super.prepare();
    ingameMenu.setVisible(false);
    retryButton.setVisible(false);
    nextLevelButton.setVisible(false);
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);
    if (GameManager.instance().getState() == GameState.PAUSE) {
      ImageRenderer.render(g, LOGO, Game.window().getResolution().getWidth() / 2d - LOGO.getWidth() / 2d,
          Game.window().getResolution().getHeight() * 2 / 10d);
    } else if (GameManager.instance().getState() == GameState.WON) {
      if (!Game.world().environment().getMap().getName().equals("level3")) {
        TextRenderer.render(g, GameManager.instance().getCurrentScore() + " ENERGY HARVESTED IN THIS WORLD!", Align.CENTER, Valign.MIDDLE, 0, 0);
      } else{
        TextRenderer.render(g, "YOU ANNIHILATED ALL SPECIES AND HARVESTED " + GameManager.instance().getOverallScore() + " ENERGY!", Align.CENTER, Valign.MIDDLE, 0, 0);
      }
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

    this.retryButton =
        new ImageComponent(menuX, screenHeight * 8 / 10d, menuWidth, screenHeight * 1 / 10d, "Retry");
    retryButton.onClicked(c -> restartLevel());

    nextLevelButton = new ImageComponent(menuX, screenHeight * 8 / 10d, menuWidth, screenHeight * 1 / 10d, "Continue");
    nextLevelButton.onClicked(c -> nextLevel());
    getHud().setVisible(false);
    ingameMenu.setVisible(false);
    retryButton.setVisible(false);
    nextLevelButton.setVisible(false);
    getComponents().add(retryButton);
    getComponents().add(nextLevelButton);
    getComponents().add(ingameMenu);
    getComponents().add(getHud());
  }

  private void nextLevel() {
    GameManager.instance().nextLevel();
  }

  @Override public void updateState() {
    switch (GameManager.instance().getState()) {
      case PAUSE -> {
        getHud().setVisible(false);
        ingameMenu.setVisible(true);
        retryButton.setVisible(false);
        nextLevelButton.setVisible(false);
      }
      case INGAME -> {
        getHud().setVisible(true);
        ingameMenu.setVisible(false);
        retryButton.setVisible(false);
        nextLevelButton.setVisible(false);
      }
      case LOST -> {
        getHud().setVisible(false);
        ingameMenu.setVisible(false);
        retryButton.setVisible(true);
        nextLevelButton.setVisible(false);
      }
      case WON -> {
        getHud().setVisible(false);
        ingameMenu.setVisible(false);
        retryButton.setVisible(false);
        nextLevelButton.setVisible(true);
      }
      default -> {
        getHud().setVisible(false);
        ingameMenu.setVisible(false);
        retryButton.setVisible(false);
        nextLevelButton.setVisible(false);
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
    GameManager.instance().restartLevel();

  }
}
