package com.shooter.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.shooter.game.objects.AbstractObject;
import com.shooter.game.objects.Bullet;
import com.shooter.game.objects.Enemy;
import com.shooter.game.screen.MenuScreen;

import java.util.ArrayList;
import java.util.List;

public class Shooter extends Game {

	public static final List<AbstractObject> objects = new ArrayList<AbstractObject>();
	public static final List<AbstractObject> awaiting = new ArrayList<AbstractObject>();

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
