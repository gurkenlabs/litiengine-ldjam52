package de.gurkenlabs.starreaperz;

public class GameManager {
  private static GameManager INSTANCE;

  public static GameManager instance() {
    if (INSTANCE == null) {
      INSTANCE = new GameManager();
    }
    return INSTANCE;
  }
}
