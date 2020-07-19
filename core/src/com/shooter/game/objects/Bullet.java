package com.shooter.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shooter.game.Shooter;
import com.shooter.game.helpers.Move;

public class Bullet extends Actor {

    public boolean collision = false;
    public float velocity = 2f;
    private final TiledMapTileLayer map;
    private Move direction = null;
    private Rectangle bounds;
    private Shooter game;
    private Texture texture;
    private static final float MAX_SPEED = 5f;
    public float dmg = 5;

    public Bullet(float pos_x, float pos_y, TiledMapTileLayer map, Move direction) {
        this.game = game;
        texture = new Texture(Gdx.files.internal("sprite/bullet.png"));
        this.setPosition(pos_x,pos_y);
        this.setWidth(7);
        this.setHeight(7);
        this.map = map;
        bounds = new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        this.direction = direction;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, getX(), getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        move();
        this.setPosition(this.getX(),this.getY());
        bounds.setX((int)this.getX());
        bounds.setY((int)this.getY());
    }

    public void move() {
        float oldX, oldY = 0;
        oldX = this.getX();
        oldY = this.getY();
        switch (direction) {
            case UP:
                if (!isCollision((int) oldX, (int)(oldY + MAX_SPEED))) {
                    this.setX(oldX);
                    this.setY(oldY + MAX_SPEED);
                }
                break;
            case DOWN:
                if (!isCollision((int) oldX, (int)(oldY - MAX_SPEED))) {
                    this.setX(oldX);
                    this.setY(oldY - MAX_SPEED);
                }break;
            case LEFT:
                if (!isCollision((int) (oldX - MAX_SPEED), (int)oldY)) {
                    this.setX(oldX - MAX_SPEED);
                    this.setY(oldY);
                }
                break;
            case RIGHT:
                if (!isCollision((int) (oldX + MAX_SPEED), (int)oldY)) {
                    this.setX(oldX + MAX_SPEED);
                    this.setY(oldY);
                }
                break;
        }
    }

    private boolean isCollision(int tileX,int tileY) {
        int tileIndexX = (int) ((tileX / map.getTileWidth())/2);
        int tileIndexY = (int) ((tileY / map.getTileHeight())/2);
        System.out.println(tileIndexX);
        System.out.println(tileIndexY);
        TiledMapTileLayer.Cell cell = map.getCell(tileIndexX,tileIndexY);
        if (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("solid")) {
            this.collision = true;
            return true;
        }
        return false;
    }
}
