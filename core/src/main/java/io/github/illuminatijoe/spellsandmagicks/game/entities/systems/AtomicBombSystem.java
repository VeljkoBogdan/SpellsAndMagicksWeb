package io.github.illuminatijoe.spellsandmagicks.game.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.illuminatijoe.spellsandmagicks.game.entities.components.BombMarkComponent;
import io.github.illuminatijoe.spellsandmagicks.game.entities.components.EnemyComponent;
import io.github.illuminatijoe.spellsandmagicks.game.entities.components.ExperienceComponent;

public class AtomicBombSystem extends IteratingSystem {
    public final ComponentMapper<BombMarkComponent> bombMapper = ComponentMapper.getFor(BombMarkComponent.class);
    public float delay = 0.5f;
    public float timer = 0f;

    public AtomicBombSystem() {
        super(Family.all(BombMarkComponent.class, EnemyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (timer <= 0f) {
            timer = delay;

            getEngine().removeEntity(entity);
            getEngine()
                .getEntitiesFor(Family.one(ExperienceComponent.class).get())
                .first()
                .getComponent(ExperienceComponent.class).addXp(15);
        } else {
            timer -= deltaTime;
        }
    }
}
