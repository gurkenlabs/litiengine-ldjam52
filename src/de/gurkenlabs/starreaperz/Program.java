package de.gurkenlabs.starreaperz;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.CreatureMapObjectLoader;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.starreaperz.entities.Spaceship;
import de.gurkenlabs.starreaperz.ui.screens.IngameScreen;

public class Program {

  public static void main(String[] args) {
    // set meta information about the game
    Game.info().setName("Star ReaperZ");
    Game.info().setSubTitle("Fick die Sterne, Alter!");
    Game.info().setVersion("v0.69");
    Game.info().setWebsite("https://github.com/gurkenlabs/litiengine-ldjam52");
    Game.info().setDescription("Woke Sternenschnitter ernten die ganze Energie ey, was geeeeht?");

    // init the game infrastructure
    Game.init(args);

    // set the icon for the game (this has to be done after initialization
    // because the ScreenManager will not be present otherwise)
    Game.window().setIcon(Resources.images().get("icon.png"));
    Game.graphics().setBaseRenderScale(3f);

    // load data from the utiLITI game file
    Resources.load("game.litidata");

    CreatureMapObjectLoader.registerCustomCreatureType(Spaceship.class);
    //        Game.screens().add(new MenuScreen());
    Game.screens().add(new IngameScreen());

    Game.start();

    Game.world().loadEnvironment("level1");
  }
}
