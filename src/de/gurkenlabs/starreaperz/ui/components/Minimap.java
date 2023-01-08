package de.gurkenlabs.starreaperz.ui.components;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.graphics.ShapeRenderer;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.util.Imaging;
import de.gurkenlabs.starreaperz.constants.ReaperColorZ;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.Collection;

public class Minimap extends ImageComponent {
  public Minimap(double x, double y, double width, double height) {
    super(x, y, width, height);
  }

  @Override public void render(Graphics2D g) {
    super.render(g);
    ImageRenderer.render(g, computeMinimapImage(), 0, 0);
  }

  private BufferedImage computeMinimapImage() {
    if (Game.world().environment() == null) {
      return null;
    }
    BufferedImage minimap = Imaging.getCompatibleImage((int) getWidth(), (int) getHeight());
    Graphics2D g = minimap.createGraphics();
    Collection<IEntity> enemies = Game.world().environment().getEntitiesByTag().get("enemy");
    if (enemies == null || enemies.isEmpty()) {
      return null;
    }
    enemies.forEach(e -> {
      double xScaleFactor = getWidth() / Game.world().camera().getViewport().getWidth();
      double yScaleFactor = getHeight() / Game.world().camera().getViewport().getHeight();

      double miniX = e.getX() * xScaleFactor;
      double miniY = e.getY() * yScaleFactor;
      double miniWidth = e.getWidth() * xScaleFactor;
      double miniHeight = e.getHeight() * yScaleFactor;
      g.setColor(ReaperColorZ.ENERGY_BLUE);
      ShapeRenderer.render(g, new Rectangle2D.Double(miniX, miniY, miniWidth, miniHeight));
    });
    return minimap;
  }
}
