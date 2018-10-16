package com.shooter.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shooter.game.helpers.PlayerMove;

public class Player extends Actor {

    private static final float MAX_VELOCITY = 10f;
    private final TextureRegion textureRegion[][];
    private final Animation<TextureRegion> walkTop;
    private final Animation<TextureRegion> walkBottom;
    private final Animation<TextureRegion> walkLeft;
    private final Animation<TextureRegion> walkRight;
    private final Animation<TextureRegion> noWalkFace;
    private final Animation<TextureRegion> noWalkBack;
    private Animation<TextureRegion> currentAnimation;
    private World world;
    private Body body;
    private float animationTime = 0f;
    private PlayerMove lastMove;

    public Player (World world,float pos_x,float pos_y) {
        this.setPosition(pos_x,pos_y);
        this.textureRegion = TextureRegion.split(new Texture(Gdx.files.internal("sprite/image.png")),192,192);
        this.walkTop = new Animation(0.1f,(textureRegion[1][1]),(textureRegion[1][2]),(textureRegion[1][3]));
        this.walkBottom = new Animation(0.1f,(textureRegion[0][1]),(textureRegion[0][2]),(textureRegion[0][3]));
        this.walkLeft = new Animation(0.1f,(textureRegion[2][0]),(textureRegion[2][1]),(textureRegion[2][2]));
        this.walkRight = new Animation(0.1f,(textureRegion[2][3]),(textureRegion[3][0]),(textureRegion[3][1]));
        this.noWalkFace = new Animation(0.1f,textureRegion[0][0]);
        this.noWalkBack = new Animation(0.1f,textureRegion[1][0]);
        this.walkTop.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.walkBottom.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.walkRight.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.walkLeft.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.noWalkFace.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.noWalkBack.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.currentAnimation = noWalkFace;
        this.setWidth(textureRegion[1][1].getTexture().getWidth());
        this.setHeight(textureRegion[1][1].getTexture().getHeight());
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos_x,pos_y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(this.getWidth() / 2,this.getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;
        body.createFixture(fixtureDef);
        body.setUserData(this);
        shape.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        TextureRegion currentFrame = currentAnimation.getKeyFrame(animationTime);
        batch.draw(currentFrame,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.setPosition(body.getPosition().x - ( this.getWidth() /2 ) ,body.getPosition().y - (this.getHeight() / 2));
        this.setRotation(body.getAngle() *  MathUtils.radiansToDegrees);
        Gdx.app.log("Pos ",String.format("x: %f,y: %f",body.getPosition().x,body.getPosition().y));
        getInput();
        animationTime += delta;
    }

    private void getInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP )) {
            up();
            this.lastMove = PlayerMove.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN )) {
            down();
            this.lastMove = PlayerMove.DOWN;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT )) {
            left();
            this.lastMove = PlayerMove.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT )) {
            right();
            this.lastMove = PlayerMove.RIGHT;
        }
    }

    public void left() {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();
        currentAnimation = walkLeft;
        body.applyLinearImpulse(-8e9f, 0, pos.x, pos.y, true);
    }

    public void right() {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();
        currentAnimation = walkRight;
        body.applyLinearImpulse(8e9f, 0, pos.x, pos.y, true);
    }

    public void down() {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();
        currentAnimation = walkBottom;
        body.applyLinearImpulse(0, -8e9f, pos.x, pos.y, true);
    }

    public void up() {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();
        currentAnimation = walkTop;
        body.applyLinearImpulse(0, 8e9f, pos.x, pos.y, true);
    }

}
