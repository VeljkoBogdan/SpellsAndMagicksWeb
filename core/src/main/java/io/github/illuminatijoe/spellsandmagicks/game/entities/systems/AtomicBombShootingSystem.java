package io.github.illuminatijoe.spellsandmagicks.game.entities.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import io.github.illuminatijoe.spellsandmagicks.game.entities.components.BombMarkComponent;
import io.github.illuminatijoe.spellsandmagicks.game.entities.components.EnemyComponent;
import io.github.illuminatijoe.spellsandmagicks.game.entities.components.PlayerComponent;

public class AtomicBombShootingSystem extends IteratingSystem {
    public float cooldown = 40f;
    public float timer = 40f;
    public ImmutableArray<Entity> entitiesToBeAnnihilated;

    public AtomicBombShootingSystem() {
        super(Family.all(PlayerComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (timer <= 0f) {
            timer = cooldown;

            entitiesToBeAnnihilated = getEngine().getEntitiesFor(Family.all(EnemyComponent.class).get());
            for (Entity enemy : entitiesToBeAnnihilated) {
                enemy.add(new BombMarkComponent());
            }
        } else {
            timer -= deltaTime;
        }
    }

    public void upgrade() {
        this.cooldown *= 0.9f;
        this.timer *= 0.9f;
    }
}
