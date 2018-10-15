package com.shooter.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Surface extends Actor {

    private World world;
    private Body groundBody;

    public Surface(World world,float pos_x,float pos_y,float aWidth,float aHeight,float angle) {
        this.setSize(aWidth,aHeight);
        this.rotateBy(angle);
        this.setPosition(pos_x,pos_y);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(aWidth, aHeight);

        FixtureDef wallFixture = new FixtureDef();
        wallFixture.density = 1f;
        wallFixture.friction = 0.5f;
        wallFixture.restitution = 0.5f;
        wallFixture.shape = groundBox;

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(new Vector2(pos_x,pos_y));
        groundBody = world.createBody(groundBodyDef);
        groundBody.createFixture(wallFixture);

        groundBox.dispose();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(new Texture(Gdx.files.internal("sprite/pave.png")),this.getX(),this.getY(),this.getWidth(),this.getHeight());
    }
}
