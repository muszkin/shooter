package com.shooter.game.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractObject {

    public boolean isAlive = true;

    private Rectangle bounds;

    private Vector2 position;

    private Vector2 velocity;

    private float healthPoints = 100;

    private float baseDmg = 5;

    private float width;
    private float height;

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(float healthPoints) {
        this.healthPoints = healthPoints;
    }

    public float getBaseDmg() {
        return baseDmg;
    }

    public void setBaseDmg(float baseDmg) {
        this.baseDmg = baseDmg;
    }

    public void update(float delta){}

    public void dispose(){}

    public void draw(Batch batch, float parentAlpha){}

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
