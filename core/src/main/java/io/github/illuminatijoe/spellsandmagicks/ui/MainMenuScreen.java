package io.github.illuminatijoe.spellsandmagicks.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.illuminatijoe.spellsandmagicks.SpellsAndMagicksGame;

public class MainMenuScreen implements Screen {
    private final SpellsAndMagicksGame game;
    private final Stage stage;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Texture background;
    private GameScreen gameScreen = null;
    private final Skin skin;

    public MainMenuScreen(SpellsAndMagicksGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.stage = new Stage();
        this.skin = new Skin(Gdx.files.internal("textures/ui/pixthulhu-ui.json"));
        this.background = new Texture("textures/ui/menu_background.png");

        Gdx.input.setInputProcessor(stage);
        updateUI();
    }

    private void updateUI() {
        stage.clear(); // Clear previous UI elements

        float x = Gdx.graphics.getWidth() / 2f - 200;
        TextButton playButton = createButton(gameScreen == null ? "Play" : "Resume", x, Gdx.graphics.getHeight() / 2f - 50, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameScreen == null) {
                    gameScreen = new GameScreen(game, MainMenuScreen.this);
                }
                game.setScreen(gameScreen);
                Gdx.input.setInputProcessor(null);
                updateUI();
            }
        });

        TextButton resetButton = createButton("Reset", x, Gdx.graphics.getHeight() / 2f - 180, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen = new GameScreen(game, MainMenuScreen.this);

                game.setScreen(gameScreen);
                Gdx.input.setInputProcessor(null);
                updateUI();
            }
        });

        stage.addActor(playButton);
        if (gameScreen != null) stage.addActor(resetButton);
    }

    private TextButton createButton(String text, float x, float y, ClickListener clickListener) {
        TextButton button = new TextButton(text, skin);
        button.setSize(500, 120);
        button.setPosition(x - 50, y - button.getLabel().getHeight() / 2f);
        button.addListener(clickListener);
        return button;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        updateUI(); // Ensure UI is updated when returning to the menu
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        if (gameScreen != null) gameScreen.dispose();
        background.dispose();
    }
}
