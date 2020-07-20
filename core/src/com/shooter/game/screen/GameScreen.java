package com.shooter.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shooter.game.Shooter;
import com.shooter.game.generator.DungeonGenerator;
import com.shooter.game.helpers.Constants;
import com.shooter.game.objects.AbstractObject;
import com.shooter.game.objects.Bullet;
import com.shooter.game.objects.Enemy;
import com.shooter.game.objects.Player;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    private Stage stage;
    private Shooter game;
    private DungeonGenerator dungeonGenerator;
    private Player player;
    private Enemy enemy;
    private TiledMapTileLayer map;
    private List<AbstractObject> objects = new ArrayList<AbstractObject>();
    private final int ENEMY_LIMIT = 5;

    public GameScreen(final Shooter game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport(new OrthographicCamera(Constants.worldSizeX  * 2,Constants.worldSizeY * 2)));
        dungeonGenerator = new DungeonGenerator(stage);
        map = (TiledMapTileLayer) dungeonGenerator.getTiledMap().getLayers().get(0);

        stage.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE){
                    game.setScreen(new OptionsScreen(game,GameScreen.this));
                }
                return true;
            }
        });
        for (int i = 0; i < ENEMY_LIMIT; i++) {
            int countPosition = dungeonGenerator.possibleEnemyPositions.size();
            int randomPosition = (int) (Math.random() * countPosition);
            if (randomPosition > countPosition) randomPosition = countPosition;
            enemy = new Enemy(new Vector2(dungeonGenerator.possibleEnemyPositions.get(randomPosition)[0],dungeonGenerator.possibleEnemyPositions.get(randomPosition)[1]), map);
            objects.add(enemy);
        }

        player = new Player(new Vector2(dungeonGenerator.playerPositionX ,dungeonGenerator.playerPositionY), map);
        objects.add(player);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 255);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        dungeonGenerator.getRenderer().setView((OrthographicCamera) stage.getCamera());
        dungeonGenerator.getRenderer().render();
        if (player.getPosition().x + (float)stage.getViewport().getScreenWidth()/2 >= (dungeonGenerator.grid.getWidth() * map.getTileWidth() * 2) ||
            player.getPosition().x - (float)stage.getViewport().getScreenWidth()/2 <= 0){
        }else{
            stage.getCamera().position.x = player.getPosition().x;
        }
        if (player.getPosition().y + (float)stage.getViewport().getScreenHeight()/2 >= (dungeonGenerator.grid.getHeight() * map.getTileHeight() * 2) ||
            player.getPosition().y - (float)stage.getViewport().getScreenHeight()/2 <= 0) {
        }else {
            stage.getCamera().position.y = player.getPosition().y;
        }
        for (AbstractObject object : objects) {
            object.update(delta);
            object.draw(stage.getBatch(), 1);
        }
        for (Enemy e : this.game.getEnemies()) {
            if (e.getBounds().overlaps(player.getBounds())){
                e.setCollision(true);
            }
        }
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

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
        stage.dispose();
    }
}
