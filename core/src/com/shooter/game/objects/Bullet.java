package com.shooter.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends AbstractObject {

    public boolean collision = false;
    private final TiledMapTileLayer map;
    private final Texture texture;

    private static final float MAX_SPEED = 50f;

    private final Vector2 position = new Vector2();
    private final Vector2 velocity = new Vector2();
    private final Vector2 movement = new Vector2();
    private final Vector2 target = new Vector2();


    public Bullet(Vector2 startPosition, TiledMapTileLayer map, Vector2 mousePosition) {
        texture = new Texture(Gdx.files.internal("sprite/bullet.png"));
        this.setWidth(texture.getWidth());
        this.setHeight(texture.getHeight());
        this.map = map;
        setBounds(new Rectangle((int)position.x, (int)position.y, (int)getWidth(), (int)getHeight()));
        target.set(mousePosition);
        position.set(startPosition);
        Vector2 direction = new Vector2();
        direction.set(target).sub(position).nor();
        velocity.set(direction).scl(MAX_SPEED);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, position.x, position.y);
    }

    @Override
    public void update(float delta) {
        movement.set(velocity).scl(delta);
        if (position.dst2(target) > movement.len2()) {
            position.add(movement);
        } else {
            position.set(target);
        }
        position.add(movement);
        isCollision();
        getBounds().setX((int)position.x);
        getBounds().setY((int)position.y);
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
