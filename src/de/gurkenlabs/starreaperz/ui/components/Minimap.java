package de.gurkenlabs.starreaperz.ui.components;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.graphics.ShapeRenderer;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.util.Imaging;
import de.gurkenlabs.starreaperz.constants.ReaperColorZ;
import de.gurkenlabs.starreaperz.constants.ReaperImageZ;
import de.gurkenlabs.starreaperz.entities.Agent;
import de.gurkenlabs.starreaperz.entities.Asteroid;
import de.gurkenlabs.starreaperz.entities.Defender;
import de.gurkenlabs.starreaperz.entities.Enemy;
import de.gurkenlabs.starreaperz.entities.Energy;
import de.gurkenlabs.starreaperz.entities.Spaceship;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Collection;

public class Minimap extends ImageComponent {
  private final BufferedImage border = Imaging.scale(ReaperImageZ.UI_BORDER_1, (int) (getWidth() * 1.5d), (int) (getHeight() * 1.5d));
  private final BufferedImage bg = Imaging.scale(ReaperImageZ.UI_BG_1, (int) (getWidth() * 1.5d), (int) (getHeight() * 1.5d));

  public Minimap(double x, double y, double width, double height) {
    super(x, y, width, height);
  }

  @Override public void render(Graphics2D g) {
    if (!HUD.showHud()) {
      return;
    }
    ImageRenderer.render(g, bg, getX() - getWidth() * 1 / 4d, getY() - getHeight() * 1 / 4d);
    super.render(g);
    ImageRenderer.render(g, computeMinimapImage(), getX(), getY());
    ImageRenderer.render(g, border, getX() - getWidth() * 1 / 4d, getY() - getHeight() * 1 / 4d);
  }

  private BufferedImage computeMinimapImage() {
    if (Game.world().environment() == null) {
      return null;
    }
    BufferedImage minimap = Imaging.getCompatibleImage((int) getWidth(), (int) getHeight());
    Graphics2D g = minimap.createGraphics();
    Collection<IEntity> enemies = Game.world().environment().getEntitiesByTag().get("minimap");
    if (enemies == null || enemies.isEmpty()) {
      return null;
    }
    enemies.forEach(e -> {
      if (!Game.world().camera().getViewport().contains(e.getCenter())) {
        return;
      }
      double xScaleFactor = getWidth() / Game.world().camera().getViewport().getWidth();
      double yScaleFactor = getHeight() / Game.world().camera().getViewport().getHeight();

      double miniX = Game.world().camera().getViewportLocation(e).getX() * xScaleFactor;
      double miniY = Game.world().camera().getViewportLocation(e).getY() * yScaleFactor;
      double miniWidth = e.getWidth() * xScaleFactor;
      double miniHeight = e.getHeight() * yScaleFactor;
      Shape miniShape = null;
      Color miniColor = null;
      if (e instanceof Agent) {
        Path2D tri = new Path2D.Double();
        tri.moveTo(miniX, miniY + miniHeight);
        tri.lineTo(miniX + miniWidth / 2d, miniY);
        tri.lineTo(miniX + miniWidth, miniY + miniHeight);
        tri.closePath();
        miniShape = tri;
        miniColor = ((Agent) e).getColor().toAwtColor();
      } else if (e instanceof Defender) {
        Rectangle2D rect = new Rectangle2D.Double(miniX, miniY, miniWidth, miniHeight);
        miniShape = rect;
        miniColor = ((Defender) e).getColor().toAwtColor();
      } else if (e instanceof Energy) {
        Ellipse2D ellipse = new Ellipse2D.Double(miniX + miniWidth * 1 / 6d, miniY + miniHeight * 1 / 6d, miniWidth * 1 / 3d, miniHeight * 1 / 3d);
        miniShape = ellipse;
        miniColor = ((Energy) e).getColor().toAwtColor();
        miniColor = new Color(miniColor.getRed(), miniColor.getGreen(), miniColor.getBlue(), 100);
      } else if (e instanceof Asteroid) {
        Ellipse2D ellipse = new Ellipse2D.Double(miniX, miniY, miniWidth, miniHeight);
        miniShape = ellipse;
        miniColor = ReaperColorZ.ENERGY_RED;
      } else if (e instanceof Spaceship) {
        Path2D tri = new Path2D.Double();
        tri.moveTo(miniX, miniY + miniHeight);
        tri.lineTo(miniX + miniWidth / 2d, miniY);
        tri.lineTo(miniX + miniWidth, miniY + miniHeight);
        tri.closePath();
        miniShape = tri;
        miniColor = ReaperColorZ.ENERGY_RED;
      } else {
        return;
      }
      g.setColor(miniColor);
      g.setStroke(new BasicStroke(1f));
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      if (e instanceof Enemy) {
        ShapeRenderer.renderOutline(g, miniShape);
      } else if (e instanceof Energy || e instanceof Spaceship || e instanceof Asteroid) {
        ShapeRenderer.render(g, miniShape);
      }
    });
    return minimap;
  }
}
