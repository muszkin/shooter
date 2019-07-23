package com.shooter.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shooter.game.helpers.PlayerMove;

public class Player extends Actor {

    private final Animation<TextureRegion> walkTop;
    private final Animation<TextureRegion> walkBottom;
    private final Animation<TextureRegion> walkLeft;
    private final Animation<TextureRegion> walkRight;
    private final Animation<TextureRegion> noWalkFace;
    private final Animation<TextureRegion> noWalkBack;
    private final Animation<TextureRegion> noWalkLeft;
    private final Animation<TextureRegion> noWalkRight;
    private Animation<TextureRegion> currentAnimation;
    private float animationTime = 0f;
    private PlayerMove lastMove = PlayerMove.DOWN;
    public float velocity = 2f;
    private final TiledMapTileLayer map;
    private float oldX,oldY;
    private boolean collision = false;

    public Player (float pos_x, float pos_y, TiledMapTileLayer map){
        this.setPosition(pos_x ,pos_y);
        Texture playerSheet = new Texture(Gdx.files.internal("sprite/player.png"));
        TextureRegion[][] textureRegion = TextureRegion.split(playerSheet, playerSheet.getWidth() / 3, playerSheet.getHeight() / 4);
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
        this.map = map;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        TextureRegion currentFrame = currentAnimation.getKeyFrame(animationTime);
        batch.draw(currentFrame,getX(),getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.setPosition(this.getX(),this.getY());
        getInput();
        animationTime += delta;
    }

    private void getInput() {
        boolean notMoving = true;
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP )) {
            up();
            this.lastMove = PlayerMove.UP;
            notMoving = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN )) {
            down();
            this.lastMove = PlayerMove.DOWN;
            notMoving = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT )) {
            left();
            this.lastMove = PlayerMove.LEFT;
            notMoving = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT )) {
            right();
            this.lastMove = PlayerMove.RIGHT;
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
        this.oldX = this.getX();
        this.oldY = this.getY();
        this.moveBy(-velocity, 0);

        if (isCollision(0,0)) {
            this.setX(this.oldX);
            this.setY(this.oldY);
            collision = false;
        }
    }

    private void right() {
        currentAnimation = walkRight;
        this.oldX = this.getX();
        this.oldY = this.getY();
        this.moveBy(velocity, 0);

        if (isCollision(0,0)) {
            this.setX(this.oldX);
            this.setY(this.oldY);
            collision = false;
        }
    }

    private void down() {
        currentAnimation = walkBottom;
        this.oldX = this.getX();
        this.oldY = this.getY();
        this.moveBy(0, -velocity);

        if (isCollision(0,0)) {
            this.setX(this.oldX);
            this.setY(this.oldY);
            collision = false;
        }
    }

    private void up() {
        currentAnimation = walkTop;
        this.oldX = this.getX();
        this.oldY = this.getY();
        this.moveBy(0, velocity);

        if (isCollision(0,0)) {
            this.setX(this.oldX);
            this.setY(this.oldY);
            collision = false;
        }
    }

    private boolean isCollision(int tileX,int tileY) {
        int posX = (int) this.getX();
        int posY = (int) this.getY();
        if (lastMove == PlayerMove.RIGHT) {
            posX += 18;
        }
        if (lastMove == PlayerMove.UP) {
            posY += 18;
        }
        int tileIndexX = (int) ((posX / map.getTileWidth())/2) + tileX;
        int tileIndexY = (int) ((posY / map.getTileHeight())/2) + tileY;
        this.debug();
        TiledMapTileLayer.Cell cell = map.getCell(tileIndexX,tileIndexY);
        if (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("solid")) {
            return true;
        }
        return false;
    }

}
