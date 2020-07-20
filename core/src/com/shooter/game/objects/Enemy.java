package com.shooter.game.objects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shooter.game.Shooter;
import com.shooter.game.helpers.Move;

import static com.shooter.game.helpers.Move.*;
import static com.shooter.game.helpers.Move.RIGHT;

public class Enemy extends Actor {

  private final Animation<TextureRegion> walkTop;
  private final Animation<TextureRegion> walkBottom;
  private final Animation<TextureRegion> walkLeft;
  private final Animation<TextureRegion> walkRight;
  private final Animation<TextureRegion> noWalkFace;
  private final Animation<TextureRegion> noWalkBack;
  private final Animation<TextureRegion> noWalkLeft;
  private final Animation<TextureRegion> noWalkRight;
  public boolean dead = false;
  private Animation<TextureRegion> currentAnimation;
  private float animationTime = 0f;
  private float oldX,oldY;
  private boolean collision = false;
  public float velocity = 2f;
  private Move lastMove = DOWN;
  private final TiledMapTileLayer map;
  private Move direction = null;
  private Rectangle bounds;
  private Shooter game;
  public float hp = 100;


  public Enemy(float pos_x, float pos_y, TiledMapTileLayer map, Shooter game) {
    this.game = game;
    this.setPosition(pos_x,pos_y);
    Texture enemySheet = new Texture(Gdx.files.internal("sprite/player.png"));
    TextureRegion[][] textureRegion = TextureRegion.split(enemySheet, enemySheet.getWidth() / 3, enemySheet.getHeight() / 4);
    this.setWidth(textureRegion[0][0].getRegionWidth());
    this.setHeight(textureRegion[0][0].getRegionHeight());
    this.walkTop = new Animation<TextureRegion>(0.1f,(textureRegion[0][0]),(textureRegion[0][1]),(textureRegion[0][2]));
    this.walkBottom = new Animation<TextureRegion>(0.1f,(textureRegion[2][0]),(textureRegion[2][1]),(textureRegion[2][2]));
    this.walkLeft = new Animation<TextureRegion>(0.1f,(textureRegion[3][0]),(textureRegion[3][1]),(textureRegion[3][2]));
    this.walkRight = new Animation<TextureRegion>(0.1f,(textureRegion[1][0]),(textureRegion[1][1]),(textureRegion[1][2]));
    this.noWalkFace = new Animation<TextureRegion>(0.1f, textureRegion[2][0]);
    this.noWalkBack = new Animation<TextureRegion>(0.1f, textureRegion[0][0]);
    this.noWalkLeft = new Animation<TextureRegion>(0.1f, textureRegion[1][0]);
    this.noWalkRight = new Animation<TextureRegion>(0.1f, textureRegion[3][0]);
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
    bounds = new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    game.addEnemy(this);
  }

  public Rectangle getBounds() {
    return bounds;
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
    move();
    this.setPosition(this.getX(),this.getY());
    bounds.setX((int)this.getX());
    bounds.setY((int)this.getY());
    animationTime += delta;
    if (dead) {
      bounds.set(0,0,0,0);
      remove();
    }
  }

  private void move() {
    boolean notMoving = true;

    if (this.direction == null || isCollision()) {
      getDirection();
    }
    if (this.direction != null) {
      switch (this.direction) {
        case UP:
          up();
          this.lastMove = UP;
          notMoving = false;
          break;
        case DOWN:
          down();
          this.lastMove = DOWN;
          notMoving = false;
          break;
        case LEFT:
          left();
          this.lastMove = LEFT;
          notMoving = false;
          break;
        case RIGHT:
          right();
          this.lastMove = RIGHT;
          notMoving = false;
          break;
      }
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

  public void getDirection() {
    int direction = (int) (Math.random() * 5);

    switch (direction){
      case 1:
        this.direction = UP;
        break;
      case 2:
        this.direction = DOWN;
        break;
      case 3:
        this.direction = LEFT;
        break;
      case 4:
        this.direction = RIGHT;
        break;
    }
    setIsCollision(false);
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
    if (lastMove == RIGHT) {
      posX += 18;
    }
    if (lastMove == UP) {
      posY += 18;
    }
    int tileIndexX = (int) ((posX / map.getTileWidth())/2) + tileX;
    int tileIndexY = (int) ((posY / map.getTileHeight())/2) + tileY;
    TiledMapTileLayer.Cell cell = map.getCell(tileIndexX,tileIndexY);
    if (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("solid")) {
      getDirection();
      return true;
    }
    int chance = (int) (Math.random() * 100);
    if (chance % 20 == 0) {
      getDirection();
    }
    return false;
  }

  public boolean isCollision() {
    return collision;
  }

  public void setIsCollision(boolean isCollision) {
    this.collision = isCollision;
  }
}
