package io.github.illuminatijoe.spellsandmagicks.game.entities.components;

import com.badlogic.ashley.core.Component;

public class SpeedComponent implements Component {
    public float speed = 50f;

    public SpeedComponent(float speed) {
        this.speed = speed;
    }
}
