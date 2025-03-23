package io.github.illuminatijoe.spellsandmagicks.game.spells;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.EntitySystem;
import io.github.illuminatijoe.spellsandmagicks.game.entities.systems.AtomicBombShootingSystem;
import io.github.illuminatijoe.spellsandmagicks.game.entities.systems.AtomicBombSystem;

public class AtomicBombSpell implements Spell{
    public AtomicBombShootingSystem atomicBombShootingSystem = new AtomicBombShootingSystem();
    public AtomicBombSystem atomicBombSystem = new AtomicBombSystem();
    public final String name = "Atomic Bomb";
    public final String description = "Yeah I don't think this is a spell, where did you get this? \n\n" +
        "No for real, why do you have this? (Upgrades reduce the cooldown by 10%)";

    @Override
    public EntitySystem getEntityMovingSystem() {
        return atomicBombSystem;
    }

    @Override
    public EntitySystem getEntityShootingSystem() {
        return atomicBombShootingSystem;
    }

    @Override
    public Component getComponent() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void upgrade() {
        atomicBombShootingSystem.upgrade();
    }

    @Override
    public String getDescription() {
        return description;
    }
}
