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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shooter.game.Shooter;
import com.shooter.game.generator.DungeonGenerator;
import com.shooter.game.helpers.Constants;
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
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private final int ENEMY_LIMIT = 15;

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
            enemy = new Enemy(dungeonGenerator.possibleEnemyPositions.get(randomPosition)[0],dungeonGenerator.possibleEnemyPositions.get(randomPosition)[1], map, game);
            enemyList.add(enemy);
            stage.addActor(enemy);
        }

        player = new Player(dungeonGenerator.playerPositionX ,dungeonGenerator.playerPositionY ,map, game);
        stage.addActor(player);
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
        if (player.getX() + stage.getViewport().getScreenWidth()/2 >= (dungeonGenerator.grid.getWidth() * map.getTileWidth() * 2) ||
            player.getX() - stage.getViewport().getScreenWidth()/2 <= 0){
        }else{
            stage.getCamera().position.x = player.getX();
        }
        if (player.getY() + stage.getViewport().getScreenHeight()/2 >= (dungeonGenerator.grid.getHeight() * map.getTileHeight() * 2) ||
            player.getY() - stage.getViewport().getScreenHeight()/2 <= 0) {
        }else {
            stage.getCamera().position.y = player.getY();
        }
        for (Enemy e : this.game.getEnemies()) {
            if (e.getBounds().overlaps(player.getBounds())){
                e.setIsCollision(true);
            }
        }
        for (Actor actor: this.stage.getActors()) {
            if (actor instanceof Bullet) {
                Bullet bullet = (Bullet) actor;
                if (bullet.collision) {
                    actor.remove();
                }
                for (Enemy enemy : game.getEnemies() ) {
                    if (bullet.getBounds().overlaps(enemy.getBounds())) {
                        enemy.hp -= bullet.dmg;
                        bullet.remove();
                        if (enemy.hp <= 0) {
                            enemy.dead = true;

                        }
                    }
                }
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
