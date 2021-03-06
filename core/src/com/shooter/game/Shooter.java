package com.shooter.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.shooter.game.objects.Bullet;
import com.shooter.game.objects.Enemy;
import com.shooter.game.screen.MenuScreen;

import java.util.ArrayList;
import java.util.List;

public class Shooter extends Game {

	private List<Enemy> enemies =  new ArrayList<Enemy>();
	private List<Bullet> bullets = new ArrayList<Bullet>();

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

	public List<Enemy> getEnemies() {
		return enemies;
	}

	public void addEnemy(Enemy enemy) {
		this.enemies.add(enemy);
	}

	public void setEnemies(List<Enemy> enemies) {
		this.enemies = enemies;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(List<Bullet> bullets) {
		this.bullets = bullets;
	}
}
