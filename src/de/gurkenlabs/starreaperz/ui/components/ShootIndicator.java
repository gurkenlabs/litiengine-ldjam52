package de.gurkenlabs.starreaperz.ui.components;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.ImageScaleMode;
import de.gurkenlabs.litiengine.util.Imaging;
import de.gurkenlabs.starreaperz.constants.ReaperImageZ;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ShootIndicator extends ImageComponent {
  private BufferedImage aim2;
  private BufferedImage aim3;
  private BufferedImage aim4;
  private BufferedImage aim5;

  public ShootIndicator(double x, double y, double width, double height) {
    super(x, y, width, height, Imaging.scale(ReaperImageZ.UI_AIM_1, (int) width, (int) height));
    setImageScaleMode(ImageScaleMode.FIT);
  }

  @Override public void prepare() {
    super.prepare();
    this.aim2 = Imaging.scale(ReaperImageZ.UI_AIM_2, (int) getWidth(), (int) getHeight());
    this.aim3 = Imaging.scale(ReaperImageZ.UI_AIM_3, (int) getWidth(), (int) getHeight());
    this.aim4 = Imaging.scale(ReaperImageZ.UI_AIM_4, (int) getWidth(), (int) getHeight());
    this.aim5 = Imaging.scale(ReaperImageZ.UI_AIM_5, (int) getWidth(), (int) getHeight());
  }

  @Override public void render(Graphics2D g) {
    super.render(g);
    if (aim2 == null || aim3 == null || aim4 == null || aim5 == null) {
      return;
    }
    ImageRenderer.renderRotated(g, aim2, getX() + getWidth() / 2d - aim2.getWidth() / 2d,
        getY() + getHeight() / 2d - aim2.getHeight() / 2d,
        -(Game.time().now() * 0.1 % 360));
    ImageRenderer.renderRotated(g, aim3, getX() + getWidth() / 2d - aim3.getWidth() / 2d,
        getY() + getHeight() / 2d - aim3.getHeight() / 2d,
        (Game.time().now() * 0.1 % 360));
    ImageRenderer.renderRotated(g, aim4, getX() + getWidth() / 2d - aim4.getWidth() / 2d,
        getY() + getHeight() / 2d - aim4.getHeight() / 2d,
        (Game.time().now() * 0.4 % 360));
    ImageRenderer.renderRotated(g, aim5, getX() + getWidth() / 2d - aim5.getWidth() / 2d,
        getY() + getHeight() / 2d - aim5.getHeight() / 2d,
        -(Game.time().now() * 0.7 % 360));
  }

}
