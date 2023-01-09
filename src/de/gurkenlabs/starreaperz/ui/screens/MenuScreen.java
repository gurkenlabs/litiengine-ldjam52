package de.gurkenlabs.starreaperz.ui.screens;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.util.Imaging;
import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.GameState;
import de.gurkenlabs.starreaperz.constants.ReaperImageZ;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class MenuScreen extends Screen {
  private final double scaleFactor = Game.window().getResolution().getWidth() / 1920d;
  private final BufferedImage MENU_BG = Imaging.scale(ReaperImageZ.MAINMENU_BG, Game.window().getResolution().width, Game.window().getResolution().height,
      AffineTransformOp.TYPE_BICUBIC, false);
  private final BufferedImage LOGO = Imaging.scale(ReaperImageZ.LOGO, scaleFactor, AffineTransformOp.TYPE_BICUBIC);
  private final BufferedImage INSTRUCTIONS = Imaging.scale(ReaperImageZ.INSTRUCTIONS, scaleFactor, AffineTransformOp.TYPE_BICUBIC);

  private Menu mainMenu;
  private ImageComponent instructionsImage, instructionsBackButton;

  public MenuScreen() {
    super("Menu");
  }

  @Override public void render(Graphics2D g) {
    ImageRenderer.render(g, MENU_BG, 0, 0);
    ImageRenderer.render(g, LOGO, Game.window().getResolution().getWidth() / 2d - LOGO.getWidth() / 2d,
        Game.window().getResolution().getHeight() * 2 / 10d);
    super.render(g);

  }

  @Override public void prepare() {
    super.prepare();
    showMenu();
    instructionsImage.setImage(INSTRUCTIONS);
  }

  @Override protected void initializeComponents() {
    super.initializeComponents();
    double screenWidth = Game.window().getResolution().getWidth();
    double screenHeight = Game.window().getResolution().getHeight();
    double menuWidth = screenWidth * 2 / 8d;
    double menuHeight = screenHeight * 1 / 5d;
    double menuX = screenWidth / 2d - menuWidth / 2d;
    double menuY = screenHeight / 2d;
    this.mainMenu = new Menu(menuX, menuY, menuWidth, menuHeight, "Play", "Instructions", "Exit");
    mainMenu.onChange(c -> {
      if (c == 0) {
        startGame();
      } else if (c == 1) {
        showInstructions();
      } else if (c == 2) {
        System.exit(0);
      }
    });
    this.instructionsBackButton =
        new ImageComponent(menuX, screenHeight * 8 / 10d, menuWidth, screenHeight * 1 / 10d, "Back");
    instructionsBackButton.onClicked(c -> {
      showMenu();
    });
    this.instructionsImage = new ImageComponent(menuX, menuY, menuWidth, menuHeight);
    getComponents().add(mainMenu);
    getComponents().add(instructionsBackButton);
    getComponents().add(instructionsImage);
  }

  private void showInstructions() {
    instructionsBackButton.setVisible(true);
    instructionsImage.setVisible(true);
    mainMenu.setVisible(false);
    GameManager.instance().setState(GameState.INSTRUCTIONS);
  }

  private void showMenu() {
    instructionsBackButton.setVisible(false);
    instructionsImage.setVisible(false);
    mainMenu.setVisible(true);
    GameManager.instance().setState(GameState.MENU);
  }

  private void startGame() {
    Game.screens().display("INGAME");
    GameManager.instance().startGame();
  }
}
