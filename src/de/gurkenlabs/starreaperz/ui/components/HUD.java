package de.gurkenlabs.starreaperz.ui.components;

import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.starreaperz.GameManager;
import java.awt.Graphics2D;

public class HUD extends GuiComponent {
  private Minimap minimap;
  private ShootIndicator shootIndicator;


  public HUD() {
    super(0, 0, Game.window().getResolution().getWidth(), Game.window().getResolution().getHeight());
  }

  @Override public void render(Graphics2D g) {
    super.render(g);
    TextRenderer.render(g, "Score: " + GameManager.instance().getOverallScore(), Align.CENTER, Valign.DOWN, 0, 0);
  }

  @Override protected void initializeComponents() {
    super.initializeComponents();
    this.minimap = new Minimap(600, 200, Game.window().getResolution().getWidth() * 1 / 10d, Game.window().getResolution().getHeight() * 1 / 10d);
    this.shootIndicator = new ShootIndicator(10, 10, 250, 250);
    getComponents().add(minimap);
    getComponents().add(shootIndicator);
  }

  public Minimap getMinimap() {
    return minimap;
  }

  public ShootIndicator getShootIndicator() {
    return shootIndicator;
  }
}
