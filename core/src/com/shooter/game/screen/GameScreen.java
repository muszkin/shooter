package com.shooter.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shooter.game.objects.Player;
import com.shooter.game.objects.Surface;

public class GameScreen implements Screen {

    private Stage stage;
    private Game game;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    public GameScreen(final Game game) {
        this.game = game;
        this.stage = new Stage(new FillViewport(1920,1080));

        stage.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE){
                    game.setScreen(new OptionsScreen(game,GameScreen.this));
                }
                return true;
            }
        });

        Box2D.init();

        this.world = new World(new Vector2(0,0),true);

        Player player = new Player(world,Gdx.graphics.getWidth()/4 ,Gdx.graphics.getHeight()/4);
        player.setBounds(player.getX(),player.getY(),player.getWidth(),player.getHeight());
        stage.addActor(player);
        stage.addActor(new Surface(world,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/10,0));
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 255);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(1/60f, 6, 6);
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
