package io.github.illuminatijoe.spellsandmagicks.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.illuminatijoe.spellsandmagicks.SpellsAndMagicksGame;
import io.github.illuminatijoe.spellsandmagicks.game.Game;
import io.github.illuminatijoe.spellsandmagicks.game.entities.components.ExperienceComponent;
import io.github.illuminatijoe.spellsandmagicks.game.entities.systems.AtomicBombShootingSystem;
import io.github.illuminatijoe.spellsandmagicks.graphics.AssetLoader;

public class GameScreen implements Screen {
    private final SpellsAndMagicksGame game;
    private final Game gameLogic;
    private final LevelUpMenu levelUpMenu;
    private final MainMenuScreen mainMenuScreen;
    private final SpriteBatch batch;
    private final Skin skin;
    private final Label timerLabel;
    private final Label levelLabel;
    private final Label bombLabel;

    private float survivalTime = 0f; // Time in seconds

    public GameScreen(SpellsAndMagicksGame game, MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
        this.game = game;
        this.gameLogic = new Game(game, this);
        this.levelUpMenu = new LevelUpMenu(this, gameLogic.engine, gameLogic.player);
        this.batch = new SpriteBatch();
        this.skin = new Skin(Gdx.files.internal("textures/ui/pixthulhu-ui.json"));
        this.timerLabel = new Label("", skin);
        this.levelLabel = new Label("", skin);
        this.bombLabel = new Label("", skin);
    }

    @Override
    public void show() {
    }

    public void onLevelUp() {
        gameLogic.paused = true;
        levelUpMenu.show();
        AssetLoader.getLevelUpSound().play(0.5f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!gameLogic.paused && !levelUpMenu.isVisible()) survivalTime += delta;

        gameLogic.render();

        if (levelUpMenu.isVisible()) levelUpMenu.render(Gdx.graphics.getDeltaTime());

        drawSurvivalTimer();
        drawPlayerLevel();
        if (gameLogic.engine.getSystem(AtomicBombShootingSystem.class) != null) drawBombCooldown();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) game.setScreen(mainMenuScreen);
    }

    private void drawSurvivalTimer() {
        batch.begin();
            String timeText = formatTime(survivalTime);
            timerLabel.setText(timeText);

            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();

            float labelX = screenWidth / 2f - timerLabel.getPrefWidth() / 2f;
            float labelY = screenHeight - 40;

            timerLabel.setPosition(labelX, labelY);
            timerLabel.draw(batch, 1f);
        batch.end();
    }

    private void drawPlayerLevel() {
        batch.begin();
            String levelText = "Level " + gameLogic.player.getComponent(ExperienceComponent.class).level;
            levelLabel.setText(levelText);

            float screenWidth = Gdx.graphics.getWidth();

            float labelX = screenWidth / 2f - levelLabel.getPrefWidth() / 2f;
            float labelY = 40;

            levelLabel.setPosition(labelX, labelY);
            levelLabel.draw(batch, 1f);
        batch.end();
    }

    private void drawBombCooldown() {
        batch.begin();
            String text = "Atomic Bomb Cooldown: " + (int) gameLogic.engine.getSystem(AtomicBombShootingSystem.class).timer + "s";
            bombLabel.setText(text);

            float labelX = 20;
            float labelY = 40;

            bombLabel.setPosition(labelX, labelY);
            bombLabel.draw(batch, 1f);
        batch.end();
    }

    private String formatTime(float timeInSeconds) {
        int minutes = (int) (timeInSeconds / 60);
        int seconds = (int) (timeInSeconds % 60);

        String minutesStr = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
        String secondsStr = seconds < 10 ? "0" + seconds : String.valueOf(seconds);

        return minutesStr + ":" + secondsStr;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        gameLogic.dispose();
        levelUpMenu.dispose();
        batch.dispose();
        skin.dispose();
    }

    public void resumeGame() {
        gameLogic.paused = false;
    }

    public void gameOver() {
        this.game.gameOver();
    }
}
