package com.shooter.game.objects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.shooter.game.Shooter;
import com.shooter.game.helpers.Move;
import com.shooter.game.screen.GameScreen;

public class Player extends AbstractObject {

    private final Animation<TextureRegion> walkTop;
    private final Animation<TextureRegion> walkBottom;
    private final Animation<TextureRegion> walkLeft;
    private final Animation<TextureRegion> walkRight;
    private final Animation<TextureRegion> noWalkFace;
    private final Animation<TextureRegion> noWalkBack;
    private final Animation<TextureRegion> noWalkLeft;
    private final Animation<TextureRegion> noWalkRight;
    public float fireDelay;
    private Animation<TextureRegion> currentAnimation;
    private float animationTime = 0f;
    private Move lastMove = Move.DOWN;
    public float velocity = 5f;
    private float oldX,oldY;
    public boolean collision = false;
    private final float MAX_SHOOTING_SPEED = 1/15f;
    private final TiledMapTileLayer map;


    public Player (Vector2 position, TiledMapTileLayer map){
        this.map = map;
        this.setPosition(position);
        Texture playerSheet = new Texture(Gdx.files.internal("sprite/player.png"));
        TextureRegion[][] textureRegion = TextureRegion.split(playerSheet, playerSheet.getWidth() / 3, playerSheet.getHeight() / 4);
        this.setWidth(textureRegion[0][0].getRegionWidth());
        this.setHeight(textureRegion[0][0].getRegionHeight());
        this.walkTop = new Animation<TextureRegion>(0.1f,(textureRegion[3][0]),(textureRegion[3][1]),(textureRegion[3][2]));
        this.walkBottom = new Animation<TextureRegion>(0.1f,(textureRegion[0][0]),(textureRegion[0][1]),(textureRegion[0][2]));
        this.walkLeft = new Animation<TextureRegion>(0.1f,(textureRegion[1][0]),(textureRegion[1][1]),(textureRegion[1][2]));
        this.walkRight = new Animation<TextureRegion>(0.1f,(textureRegion[2][0]),(textureRegion[2][1]),(textureRegion[2][2]));
        this.noWalkFace = new Animation<TextureRegion>(0.1f, textureRegion[0][1]);
        this.noWalkBack = new Animation<TextureRegion>(0.1f, textureRegion[3][1]);
        this.noWalkLeft = new Animation<TextureRegion>(0.1f, textureRegion[1][1]);
        this.noWalkRight = new Animation<TextureRegion>(0.1f, textureRegion[2][1]);
        this.walkTop.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.walkBottom.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.walkRight.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.walkLeft.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.noWalkFace.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.noWalkBack.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.noWalkRight.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.noWalkLeft.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.currentAnimation = noWalkFace;
        this.setBounds(new Rectangle(position.x, position.y, getWidth(), getHeight()));
        fireDelay = MAX_SHOOTING_SPEED;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(animationTime);
        batch.draw(currentFrame,getPosition().x,getPosition().y);
    }

    @Override
    public void update(float delta) {
        getInput(delta);
        getBounds().setX(getPosition().x);
        getBounds().setY(getPosition().y);
        animationTime += delta;
    }

    private void getInput(float delta) {
        boolean notMoving = true;
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP )) {
            up();
            this.lastMove = Move.UP;
            notMoving = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN )) {
            down();
            this.lastMove = Move.DOWN;
            notMoving = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT )) {
            left();
            this.lastMove = Move.LEFT;
            notMoving = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT )) {
            right();
            this.lastMove = Move.RIGHT;
            notMoving = false;
        }
        if (notMoving) {
            switch(this.lastMove){
                case UP:
                    currentAnimation = noWalkBack;
                    break;
                case DOWN:
                    currentAnimation = noWalkFace;
                    break;
                case LEFT:
                    currentAnimation = noWalkLeft;
                    break;
                case RIGHT:
                    currentAnimation = noWalkRight;
                    break;
            }
        }
    }

    private void left() {
        currentAnimation = walkLeft;
        oldX = getPosition().x;
        oldY = getPosition().y;
        getPosition().add(new Vector2(-velocity, 0));

        if (isCollision()) {
            moveBack();
        }
    }

    private void right() {
        currentAnimation = walkRight;
        oldX = getPosition().x;
        oldY = getPosition().y;
        getPosition().add(new Vector2(velocity, 0));

        if (isCollision()) {
            moveBack();
        }
    }

    private void down() {
        currentAnimation = walkBottom;
        oldX = getPosition().x;
        oldY = getPosition().y;
        getPosition().add(new Vector2(0, -velocity));

        if (isCollision()) {
            moveBack();
        }
    }

    private void up() {
        currentAnimation = walkTop;
        oldX = getPosition().x;
        oldY = getPosition().y;
        getPosition().add(new Vector2(0, velocity));

        if (isCollision()) {
            moveBack();
        }
    }

    private boolean isCollision() {
        int posX = (int) getPosition().x;
        int posY = (int) getPosition().y;

        if (lastMove == Move.RIGHT) {
            posX += 18;
        }
        if (lastMove == Move.UP) {
            posY += 18;
        }
        int tileIndexX = (int) ((posX / map.getTileWidth())/2);
        int tileIndexY = (int) ((posY / map.getTileHeight())/2);
        TiledMapTileLayer.Cell cell = map.getCell(tileIndexX,tileIndexY);
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("solid");
    }

    private void moveBack(){
        getPosition().set(new Vector2(oldX, oldY));
        collision = false;
    }

}
