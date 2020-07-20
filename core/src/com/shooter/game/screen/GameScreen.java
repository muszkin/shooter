package com.shooter.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.*;
import com.shooter.game.Shooter;
import com.shooter.game.generator.DungeonGenerator;
import com.shooter.game.helpers.Constants;
import com.shooter.game.objects.AbstractObject;
import com.shooter.game.objects.Enemy;
import com.shooter.game.objects.Player;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    private Shooter game;
    private DungeonGenerator dungeonGenerator;
    private TiledMapTileLayer map;
    private List<AbstractObject> objects = new ArrayList<AbstractObject>();
    private final int ENEMY_LIMIT = 5;

    private final Camera camera;
    private final Viewport viewport;
    private final SpriteBatch batch = new SpriteBatch();
    private final Player player;

    public GameScreen(final Shooter game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FillViewport((float)Constants.worldSizeX / 2, (float)Constants.worldSizeY / 2, camera);
        dungeonGenerator = new DungeonGenerator();
        map = (TiledMapTileLayer) dungeonGenerator.getTiledMap().getLayers().get(0);

        camera.update();

        for (int i = 0; i < ENEMY_LIMIT; i++) {
            int countPosition = dungeonGenerator.possibleEnemyPositions.size();
            int randomPosition = (int) (Math.random() * countPosition);
            if (randomPosition > countPosition) randomPosition = countPosition;
            Enemy enemy = new Enemy(new Vector2(dungeonGenerator.possibleEnemyPositions.get(randomPosition)[0],dungeonGenerator.possibleEnemyPositions.get(randomPosition)[1]), map);
            objects.add(enemy);
        }

        player = new Player(new Vector2(dungeonGenerator.playerPositionX ,dungeonGenerator.playerPositionY), map);
        objects.add(player);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            game.setScreen(new OptionsScreen(game,GameScreen.this));
        }
        Gdx.gl.glClearColor(0, 0, 0, 255);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        dungeonGenerator.getRenderer().setView((OrthographicCamera) getCamera());
        dungeonGenerator.getRenderer().render();

        getCamera().position.set(player.getPosition().x, player.getPosition().y, 1);
        getCamera().update();
        getBatch().setProjectionMatrix(camera.combined);
        getBatch().begin();
        for (AbstractObject object : objects) {
            object.update(delta);
            object.draw(batch, 0);
        }
        getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height);
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
        for (AbstractObject object : objects) {
            object.dispose();
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
