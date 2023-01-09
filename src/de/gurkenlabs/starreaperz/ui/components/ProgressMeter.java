package de.gurkenlabs.starreaperz.ui.components;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.HorizontalSlider;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.ImageScaleMode;
import de.gurkenlabs.litiengine.util.MathUtilities;
import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.GameState;
import de.gurkenlabs.starreaperz.constants.ReaperImageZ;
import java.awt.Graphics2D;

public class ProgressMeter extends ImageComponent {
  ImageComponent slider;

  public ProgressMeter(double x, double y, double width, double height) {
    super(x, y, width, height);
    setImage(ReaperImageZ.UI_SLIDER_1);
  }

  @Override protected void initializeComponents() {
    super.initializeComponents();
    double sliderSize = 0.08 * getWidth();
    this.slider = new ImageComponent(getX(), getY() + getHeight() / 2d - sliderSize / 2d, sliderSize, sliderSize);
    slider.setImage(ReaperImageZ.UI_SLIDER_2);
    slider.setImageScaleMode(ImageScaleMode.FIT);
    getComponents().add(slider);
  }

  @Override public void render(Graphics2D g) {
    super.render(g);
    if (!HUD.showHud()) {
      return;
    }
    double progress =
        (Game.world().environment().getMap().getSizeInPixels().getHeight() - GameManager.instance().getSpaceship().getY()) / Game.world()
            .environment().getMap().getSizeInPixels().getHeight();
    slider.setX(getX() + progress * (getWidth() * 0.9));
  }
}
