package com.shooter.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shooter.game.generator.DungeonGenerator;
import com.shooter.game.helpers.Constants;
import com.shooter.game.objects.Player;

public class GameScreen implements Screen {

    private Stage stage;
    private Game game;
    private DungeonGenerator dungeonGenerator;
    private Player player;

    public GameScreen(final Game game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport(new OrthographicCamera(Constants.worldSizeX  ,Constants.worldSizeY )));


        stage.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE){
                    game.setScreen(new OptionsScreen(game,GameScreen.this));
                }
                return true;
            }
        });

        player = new Player(stage.getWidth()/2 ,stage.getHeight()/2,stage.getViewport().getCamera());
        stage.addActor(player);
        dungeonGenerator = new DungeonGenerator(stage);
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
        stage.getCamera().position.x = player.getX();
        stage.getCamera().position.y = player.getY();
        stage.act();
        stage.draw();
        Gdx.app.log("position (x,y)",String.format("(%f,%f)",player.getX(),player.getY()));
        Gdx.app.log("camera position (x,y)",String.format("(%f,%f)",stage.getCamera().position.x,stage.getCamera().position.y));
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
