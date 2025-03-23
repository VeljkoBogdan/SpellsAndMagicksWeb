package io.github.illuminatijoe.spellsandmagicks.game.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.illuminatijoe.spellsandmagicks.game.entities.Bat;
import io.github.illuminatijoe.spellsandmagicks.game.entities.Boulder;
import io.github.illuminatijoe.spellsandmagicks.game.entities.Player;
import io.github.illuminatijoe.spellsandmagicks.game.entities.Slime;
import io.github.illuminatijoe.spellsandmagicks.game.entities.components.AttackComponent;
import io.github.illuminatijoe.spellsandmagicks.game.entities.components.ControllableComponent;
import io.github.illuminatijoe.spellsandmagicks.game.entities.components.HealthComponent;
import io.github.illuminatijoe.spellsandmagicks.game.entities.components.PositionComponent;

import java.util.ArrayList;
import java.util.List;

public class EntitySpawnerSystem extends IteratingSystem {
    private final OrthographicCamera camera;
    private final Player player;
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<HealthComponent> healthMapper = ComponentMapper.getFor(HealthComponent.class);
    private final ComponentMapper<AttackComponent> attackMapper = ComponentMapper.getFor(AttackComponent.class);

    public float spawnRate = 1f;
    public float difficultyFactor = 1f;
    public float currentTime = 0f;
    public float wave = 1f;

    public EntitySpawnerSystem(OrthographicCamera camera, Player player) {
        super(Family.all(PositionComponent.class, ControllableComponent.class).get());
        this.camera = camera;
        this.player = player;
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        if (currentTime >= spawnRate) {
            spawnEntity(entity, v);
            currentTime -= spawnRate;
        } else {
            currentTime += v;
        }
    }

    public void increaseDifficulty() {
        wave++;
        if (wave % 5 == 0) {
            spawnRate *= 0.8f;
            difficultyFactor *= 1.2f;
        } else {
            spawnRate *= 0.85f;
            difficultyFactor *= 1.15f;
        }
    }

    public void spawnEntity(Entity entity, float v) {


        List<Entity> entitiesToSpawn = getEntityAccordingToWave();
        for (Entity entityToSpawn : entitiesToSpawn) {
            healthMapper.get(entityToSpawn).maxHealth *= difficultyFactor;
            healthMapper.get(entityToSpawn).health = healthMapper.get(entityToSpawn).maxHealth;
            attackMapper.get(entityToSpawn).damage *= difficultyFactor;
            getEngine().addEntity(entityToSpawn);
        }
    }

    private List<Entity> getEntityAccordingToWave() {
        List<Entity> entityList = new ArrayList<>();
        int lastWaveNumber = (int) (wave % 10);

        if (lastWaveNumber % 3 == 0 || lastWaveNumber % 4 == 0) { // Waves *3, *4, *6, *8, *9
             entityList.add(new Bat(player, getRandomOffScreenPosVector(player)));
        }
        if (lastWaveNumber % 2 == 1 || (lastWaveNumber % 10 <= 3)) { // Waves *1, *3, *5, *7, *9
            entityList.add(new Slime(player, getRandomOffScreenPosVector(player)));
        }
        if (lastWaveNumber % 5 == 0 || lastWaveNumber % 6 == 0) { // Waves *5, *6
            entityList.add(new Boulder(player, getRandomOffScreenPosVector(player)));
        }

        return entityList;
    }

    private Vector2 getRandomOffScreenPosVector(Entity entity) {
        PositionComponent positionComponent = pm.get(entity);
        Vector2 pos = positionComponent.getPosition();

        float xOffset = (camera.viewportWidth * camera.zoom) / 2;
        float yOffset = (camera.viewportHeight * camera.zoom) / 2;

        float leftX = pos.x - xOffset;
        float rightX = pos.x + xOffset;
        float upY = pos.y + yOffset;
        float downY = pos.y - yOffset;

        float margin = 50f;
        return getRandomOffScreenPosition(leftX, rightX, downY, upY, margin);
    }

    public Vector2 getRandomOffScreenPosition(float leftX, float rightX, float downY, float upY, float margin) {
        int side = MathUtils.random(3); // 0 = left, 1 = right, 2 = top, 3 = bottom
        float x, y;

        switch (side) {
            case 0: // left
                x = leftX - margin;
                y = MathUtils.random(downY, upY);
                break;
            case 1: // right
                x = rightX + margin;
                y = MathUtils.random(downY, upY);
                break;
            case 2: // top
                x = MathUtils.random(leftX, rightX);
                y = upY + margin;
                break;
            case 3: // bottom
                x = MathUtils.random(leftX, rightX);
                y = downY - margin;
                break;
            default: throw new IllegalStateException("Unexpected value: " + side);
        };

        return new Vector2(x, y);
    }
}
