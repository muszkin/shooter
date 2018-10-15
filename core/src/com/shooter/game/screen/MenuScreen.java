package com.shooter.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {

    private final Game game;
    private Stage stage;

    public MenuScreen(final Game game) {
        this.game = game;
        this.stage =  new Stage(new ScreenViewport());
        int rowHeight = Gdx.graphics.getHeight() / 12;
        int colWidth = Gdx.graphics.getWidth() / 12;

        Label.LabelStyle label1Style = new Label.LabelStyle();
        BitmapFont myFont = new BitmapFont(Gdx.files.internal("font/menu.fnt"));
        label1Style.font = myFont;
        label1Style.fontColor = Color.RED;

        Label label1 = new Label("Shooter",label1Style);
        label1.setSize(Gdx.graphics.getWidth(),rowHeight);
        label1.setPosition(0,Gdx.graphics.getHeight() - rowHeight * 2);
        label1.setAlignment(Align.center);
        stage.addActor(label1);

        Skin mySkin = new Skin(Gdx.files.internal("skin/clean-crispy-ui.json"));

        final Button startButton = new TextButton("Start",mySkin,"default");
        startButton.setSize(colWidth * 4,rowHeight );
        startButton.setPosition((Gdx.graphics.getWidth()/2) - (startButton.getWidth()/2),Gdx.graphics.getHeight() - rowHeight * 6);
        startButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                ((TextButton) startButton).setText("Start");
                game.setScreen(new GameScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                ((TextButton) startButton).setText("Start pressed");
                return true;
            }
        });
        stage.addActor(startButton);

        final Button optionsButton = new TextButton("Options",mySkin,"default");
        optionsButton.setSize(colWidth * 4,rowHeight );
        optionsButton.setPosition((Gdx.graphics.getWidth()/2) - (optionsButton.getWidth()/2),Gdx.graphics.getHeight() - rowHeight * 7);
        optionsButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                ((TextButton) optionsButton).setText("Options");
                game.setScreen(new OptionsScreen(game,MenuScreen.this));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                ((TextButton) optionsButton).setText("Options pressed");
                return true;
            }
        });
        stage.addActor(optionsButton);

        final Button exitButton = new TextButton("Exit",mySkin,"default");
        exitButton.setSize(colWidth * 4,rowHeight );
        exitButton.setPosition((Gdx.graphics.getWidth()/2) - (exitButton.getWidth()/2),Gdx.graphics.getHeight() - rowHeight * 8);
        exitButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                System.exit(0);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                ((TextButton) exitButton).setText("Will exit");
                return true;
            }
        });
        stage.addActor(exitButton);


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
