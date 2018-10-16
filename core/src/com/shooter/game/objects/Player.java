package com.shooter.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shooter.game.listener.PlayerInputListener;

public class Player extends Actor {

    private static final float MAX_VELOCITY = 10f;
    private World world;
    private Body body;
    private Sprite playerSprite = new Sprite(new Texture(Gdx.files.internal("sprite/player.png")));

    public Player (World world,float pos_x,float pos_y) {
        this.setPosition(pos_x,pos_y);
        this.setWidth(playerSprite.getWidth());
        this.setHeight(playerSprite.getHeight());


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

        this.addListener(new PlayerInputListener(this));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        ((Player)body.getUserData()).setBounds(playerSprite.getX(),playerSprite.getY(),playerSprite.getWidth(),playerSprite.getHeight());
        ((Player)body.getUserData()).getPlayerSprite().draw(batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        ((Player)body.getUserData()).getPlayerSprite().setPosition(body.getPosition().x - ( this.getWidth() /2 ) ,body.getPosition().y - (this.getHeight() / 2));
        ((Player)body.getUserData()).getPlayerSprite().setRotation(body.getAngle() *  MathUtils.radiansToDegrees);
        Gdx.app.log("Pos ",String.format("x: %f,y: %f",body.getPosition().x,body.getPosition().y));
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            Gdx.app.log("key","pressed");
            body.applyForceToCenter(0f, 1e10f, true);
        }
//        if (Gdx.input.isKeyPressed(Input.Keys.D)) right();
//        if (Gdx.input.isKeyPressed(Input.Keys.A)) left();
//        if (Gdx.input.isKeyPressed(Input.Keys.S)) down();
//        if (Gdx.input.isKeyPressed(Input.Keys.W)) up();
    }

    public void left() {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();
        body.applyLinearImpulse(-8e9f, 0, pos.x, pos.y, true);
    }

    public void right() {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();
        body.applyLinearImpulse(8e9f, 0, pos.x, pos.y, true);
    }

    public void down() {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();
        body.applyLinearImpulse(0, -8e9f, pos.x, pos.y, true);
    }

    public void up() {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();
        body.applyLinearImpulse(0, 8e9f, pos.x, pos.y, true);
    }

    public Sprite getPlayerSprite() {
        return playerSprite;
    }

    public void setPlayerSprite(Sprite playerSprite) {
        this.playerSprite = playerSprite;
    }
}
