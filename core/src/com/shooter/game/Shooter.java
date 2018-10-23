package com.shooter.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.shooter.game.screen.MenuScreen;

public class Shooter extends Game {

	@Override
	public void create () {
		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
