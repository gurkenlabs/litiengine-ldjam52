package de.gurkenlabs.starreaperz.ui.components;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.ImageScaleMode;
import de.gurkenlabs.litiengine.tweening.TweenFunction;
import de.gurkenlabs.litiengine.tweening.TweenType;
import de.gurkenlabs.litiengine.util.Imaging;
import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.constants.ReaperImageZ;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ShootIndicator extends ImageComponent {
  private BufferedImage aim2;
  private final BufferedImage aim3;
  private BufferedImage aim4;
  private final BufferedImage aim5;

  public ShootIndicator(double x, double y, double width, double height) {
    super(x, y, width, height, Imaging.scale(ReaperImageZ.UI_AIM_1, (int) width, (int) height));
    this.aim2 = Imaging.scale(ReaperImageZ.UI_AIM_2, (int) width, (int) height);
    this.aim3 = Imaging.scale(ReaperImageZ.UI_AIM_3, (int) width, (int) height);
    this.aim2 = Imaging.scale(ReaperImageZ.UI_AIM_4, (int) width, (int) height);
    this.aim5 = Imaging.scale(ReaperImageZ.UI_AIM_5, (int) width, (int) height);
    setImageScaleMode(ImageScaleMode.FIT);
  }

  @Override public void prepare() {
    super.prepare();
  }

  @Override public void render(Graphics2D g) {
    super.render(g);
    if (aim2 == null || aim3 == null || aim4 == null || aim5 == null) {
      return;
    }
    ImageRenderer.renderRotated(g, aim2, getBoundingBox().getCenterX() - aim2.getWidth(), getBoundingBox().getCenterY() - aim2.getHeight(),
        -(Game.time().now() * 0.1 % 360));
    ImageRenderer.renderRotated(g, aim3, getBoundingBox().getCenterX() - aim3.getWidth(), getBoundingBox().getCenterY() - aim3.getHeight(),
        (Game.time().now() * 0.1 % 360));
    ImageRenderer.renderRotated(g, aim4, getBoundingBox().getCenterX() - aim4.getWidth(), getBoundingBox().getCenterY() - aim4.getHeight(),
        (Game.time().now() * 0.4 % 360));
    ImageRenderer.renderRotated(g, aim5, getBoundingBox().getCenterX() - aim5.getWidth(), getBoundingBox().getCenterY() - aim5.getHeight(),
        -(Game.time().now() * 0.7 % 360));
  }

}
