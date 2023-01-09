package de.gurkenlabs.starreaperz;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.CreatureMapObjectLoader;
import de.gurkenlabs.litiengine.gui.GuiProperties;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.starreaperz.constants.ReaperColorZ;
import de.gurkenlabs.starreaperz.constants.ReaperFontZ;
import de.gurkenlabs.starreaperz.constants.ReaperImageZ;
import de.gurkenlabs.starreaperz.entities.Enemy;
import de.gurkenlabs.starreaperz.entities.Spaceship;
import de.gurkenlabs.starreaperz.ui.screens.IngameScreen;
import de.gurkenlabs.starreaperz.ui.screens.MenuScreen;

public class Program {

  public static void main(String[] args) {
    // set meta information about the game
    Game.info().setName("Star ReaperZ");
    Game.info().setVersion("v1.0.0");
    Game.info().setWebsite("https://github.com/gurkenlabs/litiengine-ldjam52");
    Game.info().setDescription("Woke Sternenschnitter ernten die ganze Energie ey, was geeeeht?");

    // init the game infrastructure
    Game.init(args);

    // set the icon for the game (this has to be done after initialization
    // because the ScreenManager will not be present otherwise)
    Game.window().setIcon(ReaperImageZ.ICON);
    Game.graphics().setBaseRenderScale(3f);

    // load data from the utiLITI game file
    Resources.load("game.litidata");

    CreatureMapObjectLoader.registerCustomCreatureType(Spaceship.class);
    CreatureMapObjectLoader.registerCustomCreatureType(Enemy.class);

    GuiProperties.getDefaultAppearance().setForeColor(ReaperColorZ.ENERGY_RED);
    GuiProperties.getDefaultAppearance().setBackgroundColor1(ReaperColorZ.UI_BG);
    GuiProperties.getDefaultAppearanceHovered().setForeColor(ReaperColorZ.ENERGY_RED.darker());
    GuiProperties.getDefaultAppearanceHovered().setBackgroundColor1(ReaperColorZ.UI_BG.darker());
    GuiProperties.getDefaultAppearanceDisabled().setForeColor(ReaperColorZ.ENERGY_RED.darker().darker());
    GuiProperties.getDefaultAppearanceDisabled().setBackgroundColor1(ReaperColorZ.UI_BG.darker().darker());
    GuiProperties.setDefaultFont(ReaperFontZ.FONT_LOGO_STD.deriveFont(48f));

    if (!Game.config().debug().isDebugEnabled()) {
      Game.screens().add(new MenuScreen());
    }
    Game.screens().add(IngameScreen.instance());

    Game.start();
    GameManager.instance().init();
    if (Game.config().debug().isDebugEnabled()) {
      GameManager.instance().startGame();
    }
  }
}
