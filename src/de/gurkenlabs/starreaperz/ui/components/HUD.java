package de.gurkenlabs.starreaperz.ui.components;

import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.constants.ReaperImageZ;
import java.awt.Graphics2D;

public class HUD extends GuiComponent {
  private Minimap minimap;

  public HUD() {
    super(0, 0, Game.window().getResolution().getWidth(), Game.window().getResolution().getHeight());
  }

  @Override public void render(Graphics2D g) {
    super.render(g);
    TextRenderer.render(g, "Score: " + GameManager.instance().getOverallScore(), Align.CENTER, Valign.DOWN, 0, 0);
    ImageRenderer.renderRotated(g, ReaperImageZ.UI_CIRCLE_1, 5, 5, (Game.time().now() * 0.1 % 360));
    ImageRenderer.renderRotated(g, ReaperImageZ.UI_CIRCLE_2, 5, 5, -(Game.time().now() * 0.8 % 360));
  }

  @Override protected void initializeComponents() {
    super.initializeComponents();
    this.minimap = new Minimap(200, 200, 400, 200);
    getComponents().add(minimap);
  }
}
