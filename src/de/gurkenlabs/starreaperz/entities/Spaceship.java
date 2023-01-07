package de.gurkenlabs.starreaperz.entities;

import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.MovementInfo;

@MovementInfo(acceleration = 1500, deceleration = 3000, velocity = 200)
public class Spaceship extends Creature {
    public Spaceship(){
        super("spaceship");

        System.out.println("Spaceship loaded");
        this.addController(new SpaceshipController(this));
    }
}
