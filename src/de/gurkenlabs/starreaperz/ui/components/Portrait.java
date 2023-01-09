package de.gurkenlabs.starreaperz.ui.components;

import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.ImageScaleMode;
import de.gurkenlabs.litiengine.util.Imaging;
import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.constants.ReaperImageZ;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Portrait extends ImageComponent {
  private final BufferedImage life1 = Imaging.scale(ReaperImageZ.UI_LIFE_1, (int) getWidth(), (int) getHeight());
  private final BufferedImage life2 = Imaging.scale(ReaperImageZ.UI_LIFE_2, (int) getWidth(), (int) getHeight());
  private final BufferedImage life3 = Imaging.scale(ReaperImageZ.UI_LIFE_3, (int) getWidth(), (int) getHeight());
  private final BufferedImage life4 = Imaging.scale(ReaperImageZ.UI_LIFE_4, (int) getWidth(), (int) getHeight());
  private final BufferedImage life5 = Imaging.scale(ReaperImageZ.UI_LIFE_5, (int) getWidth(), (int) getHeight());

  public Portrait(double x, double y, double width, double height) {
    super(x, y, width, height, ReaperImageZ.UI_PORTRAIT);
    setImageScaleMode(ImageScaleMode.FIT);
  }

  @Override protected void initializeComponents() {
    super.initializeComponents();
  }

  @Override public void render(Graphics2D g) {
    super.render(g);
    if (!HUD.showHud()) {
      return;
    }
    BufferedImage life = switch (GameManager.instance().getSpaceship().getHitPoints().get()) {
      case 1 -> life1;
      case 2 -> life2;
      case 3 -> life3;
      case 4 -> life4;
      case 5 -> life5;
      default -> null;
    };
    ImageRenderer.render(g, life, getX(), getY());

  }
}
