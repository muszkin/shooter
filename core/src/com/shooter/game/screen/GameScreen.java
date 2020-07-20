package com.shooter.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.*;
import com.shooter.game.Shooter;
import com.shooter.game.generator.DungeonGenerator;
import com.shooter.game.helpers.Constants;
import com.shooter.game.objects.AbstractObject;
import com.shooter.game.objects.Bullet;
import com.shooter.game.objects.Enemy;
import com.shooter.game.objects.Player;

public class GameScreen implements Screen {

    private Shooter game;
    private DungeonGenerator dungeonGenerator;
    private TiledMapTileLayer map;
    private final int ENEMY_LIMIT = 5;

    private final Camera camera;
    private final Viewport viewport;
    private final SpriteBatch batch = new SpriteBatch();
    private final Player player;

    public GameScreen(final Shooter game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new ExtendViewport((float)Constants.worldSizeX / 4, (float)Constants.worldSizeY / 4, camera);
        viewport.setScreenBounds(0,0, Constants.worldSizeX, Constants.worldSizeY);
        dungeonGenerator = new DungeonGenerator();
        map = (TiledMapTileLayer) dungeonGenerator.getTiledMap().getLayers().get(0);


        camera.update();

        for (int i = 0; i < ENEMY_LIMIT; i++) {
            int countPosition = dungeonGenerator.possibleEnemyPositions.size();
            int randomPosition = (int) (Math.random() * countPosition);
            if (randomPosition > countPosition) randomPosition = countPosition;
            Enemy enemy = new Enemy(new Vector2(dungeonGenerator.possibleEnemyPositions.get(randomPosition)[0],dungeonGenerator.possibleEnemyPositions.get(randomPosition)[1]), map);
            Shooter.objects.add(enemy);
        }

        player = new Player(new Vector2(dungeonGenerator.playerPositionX ,dungeonGenerator.playerPositionY), map);
        Shooter.objects.add(player);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            game.setScreen(new OptionsScreen(game,GameScreen.this));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.fireDelay -= delta;
            if (player.fireDelay <= 0) {

                Shooter.objects.add(new Bullet(player.getPosition(), map, new Vector2(getMousePosInGameWorld().x, getMousePosInGameWorld().y)));
                player.fireDelay += 0.2;
            }
        }
        Gdx.gl.glClearColor(0, 0, 0, 255);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        dungeonGenerator.getRenderer().setView((OrthographicCamera) getCamera());
        dungeonGenerator.getRenderer().render();

        if (player.getPosition().x - ((float)viewport.getScreenWidth() / 4) >= 0
                && player.getPosition().x + ((float)viewport.getScreenWidth() / 4) <= (dungeonGenerator.grid.getWidth() * map.getTileWidth() * 2)) {
            getCamera().position.set(player.getPosition().x, camera.position.y, camera.position.z);
        }
        if (player.getPosition().y - ((float)viewport.getScreenHeight() / 4) > 0
                && player.getPosition().y + ((float)viewport.getScreenHeight() / 4) <= (dungeonGenerator.grid.getHeight() * map.getTileHeight() * 2)) {
            getCamera().position.set(camera.position.x, player.getPosition().y, camera.position.z);
        }
        getCamera().update();
        getBatch().setProjectionMatrix(camera.combined);
        getBatch().begin();
        for (AbstractObject object : Shooter.objects) {
            object.update(delta);
            object.draw(batch, 0);
        }
        getBatch().end();
        Shooter.objects.addAll(Shooter.awaiting);
        Shooter.awaiting.clear();
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
        for (AbstractObject object : Shooter.objects) {
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

    Vector3 getMousePosInGameWorld() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }
}
