package io.github.illuminatijoe.spellsandmagicks.game.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.illuminatijoe.spellsandmagicks.game.entities.components.*;

public class EnemyMovementSystem extends IteratingSystem {
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private final ComponentMapper<TargetComponent> tm = ComponentMapper.getFor(TargetComponent.class);
    private final ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    private final ComponentMapper<SpeedComponent> speedMapper = ComponentMapper.getFor(SpeedComponent.class);

    public EnemyMovementSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class, TargetComponent.class, SpeedComponent.class,
            AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = pm.get(entity);
        VelocityComponent velocity = vm.get(entity);
        TargetComponent target = tm.get(entity);
        AnimationComponent animationComponent = am.get(entity);

        if (target.target == null) return;

        PositionComponent targetPos = pm.get(target.target);
        if (targetPos == null) return;

        // Calculate movement direction towards the target
        SpeedComponent speedComponent = speedMapper.get(entity);
        velocity.velocity.set(targetPos.position).sub(position.position).nor().scl(speedComponent.speed);

        // Check if the enemy is moving left
        if (velocity.velocity.x < 0) {
            animationComponent.facingLeft = true;
        } else {
            animationComponent.facingLeft = false;
        }
    }

}
