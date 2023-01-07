package de.gurkenlabs.starreaperz.ui.screens;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.util.Imaging;
import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.constants.ReaperImageZ;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class MenuScreen extends Screen {
  private final double scaleFactor = Game.window().getResolution().getWidth() / 1920d;
  private BufferedImage MENU_BG = Imaging.scale(ReaperImageZ.MAINMENU_BG, Game.window().getResolution().width, Game.window().getResolution().height,
      AffineTransformOp.TYPE_BICUBIC, false);
  private BufferedImage LOGO = Imaging.scale(ReaperImageZ.LOGO, scaleFactor, AffineTransformOp.TYPE_BICUBIC);

  private Menu mainMenu;

  public MenuScreen() {
    super("Menu");
  }

  @Override public void render(Graphics2D g) {
    ImageRenderer.render(g, MENU_BG, 0, 0);
    ImageRenderer.render(g, LOGO, Game.window().getResolution().getWidth() / 2d - LOGO.getWidth() / 2d,
        Game.window().getResolution().getHeight() * 2 / 10d);
    super.render(g);

  }

  @Override protected void initializeComponents() {
    super.initializeComponents();
    double menuWidth = Game.window().getResolution().getWidth() * 2 / 8d;
    double menuHeight = Game.window().getResolution().getHeight() * 1 / 5d;
    double menuX = Game.window().getResolution().getWidth() / 2d - menuWidth / 2d;
    double menuY = Game.window().getResolution().getHeight() / 2d;
    this.mainMenu = new Menu(menuX, menuY, menuWidth, menuHeight, "Play", "Instructions", "Exit");
    getComponents().add(mainMenu);
    mainMenu.onChange(c -> {
      if (c == 0) {
        Game.screens().display("INGAME");
        GameManager.instance().startGame();
      }
    });
  }
}
