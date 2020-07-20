package com.shooter.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shooter.game.Shooter;

public class Bullet extends Actor {

    private final Vector2 mousePosition;
    private final Vector2 startPosition;
    public boolean collision = false;
    public float velocity = 50f;
    private final TiledMapTileLayer map;
    private Rectangle bounds;
    private Shooter game;
    private Texture texture;
    public float dmg = 5;
    private Vector2 direction;
    private Vector2 position;



    public Bullet(Vector2 startPosition, TiledMapTileLayer map, Vector2 mousePosition) {
        this.game = game;
        System.out.println(String.format("Start(%d,%d)-End(%d,%d)",(int)startPosition.x, (int)startPosition.y, (int) mousePosition.x, (int)mousePosition.y));
        texture = new Texture(Gdx.files.internal("sprite/bullet.png"));
        this.setWidth(7);
        this.setHeight(7);
        this.map = map;
        this.mousePosition = mousePosition;
        this.position = startPosition;
        this.startPosition = startPosition;
        bounds = new Rectangle((int)position.x, (int)position.y, (int)getWidth(), (int)getHeight());
        direction = mousePosition.sub(startPosition).nor();
        direction.nor();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, position.x, position.y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        position = new Vector2(position.x - (direction.x * delta * velocity), position.y - (direction.y * delta* velocity));
        isCollision();
        bounds.setX((int)position.x);
        bounds.setY((int)position.y);
    }

    private void isCollision() {
        int tileIndexX = (int) ((position.x / map.getTileWidth())/2);
        int tileIndexY = (int) ((position.y / map.getTileHeight())/2);
        TiledMapTileLayer.Cell cell = map.getCell(tileIndexX,tileIndexY);
        if (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("solid")) {
            this.collision = true;
        }
    }
}
