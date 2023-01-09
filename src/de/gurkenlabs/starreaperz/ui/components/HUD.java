package de.gurkenlabs.starreaperz.ui.components;

import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.starreaperz.GameManager;
import de.gurkenlabs.starreaperz.GameState;
import de.gurkenlabs.starreaperz.Program;
import de.gurkenlabs.starreaperz.constants.ReaperFontZ;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class HUD extends GuiComponent {
  private Minimap minimap;
  private ShootIndicator shootIndicator;
  private Portrait portrait;
  private ProgressMeter progressMeter;


  public HUD() {
    super(0, 0, Game.window().getResolution().getWidth(), Game.window().getResolution().getHeight());
  }

  @Override public void render(Graphics2D g) {
    super.render(g);
    if (!showHud()) {
      return;
    }
    g.setFont(ReaperFontZ.FONT_LOGO_ACAD.deriveFont(40f));
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    TextRenderer.render(g, "Score: " + GameManager.instance().getOverallScore(), Align.CENTER, Valign.MIDDLE_TOP, 0, -150);
  }

  @Override protected void initializeComponents() {
    super.initializeComponents();
    double screenWidth = Game.window().getResolution().getWidth();
    double screenHeight = Game.window().getResolution().getHeight();
    this.portrait = new Portrait(screenWidth * 1 / 32d, screenHeight * 1 / 32d, screenWidth * 4 / 32d, screenWidth * 4 / 32d);
    this.shootIndicator = new ShootIndicator(portrait.getX() + portrait.getWidth() - portrait.getWidth() / 3d,
        portrait.getY() + portrait.getHeight() - portrait.getWidth() / 3d, screenWidth * 2 / 32d, screenWidth * 2 / 32d);
    this.minimap = new Minimap(screenWidth * 27 / 32d, screenHeight * 1 / 32d, screenWidth * 4 / 32d, screenHeight * 4 / 32d);
    this.progressMeter =
        new ProgressMeter(screenWidth / 2d - screenWidth * 2 / 10d, screenHeight * 1 / 32d, screenWidth * 2 / 5d, screenHeight * 1 / 32d);
    getComponents().add(minimap);
    getComponents().add(shootIndicator);
    getComponents().add(portrait);
    getComponents().add(progressMeter);
  }

  public Minimap getMinimap() {
    return minimap;
  }

  public ShootIndicator getShootIndicator() {
    return shootIndicator;
  }

  public static boolean showHud() {
    return GameManager.instance().getState() == GameState.INGAME
        && Game.world().environment() != null
        && GameManager.instance().getSpaceship() != null;
  }
}
