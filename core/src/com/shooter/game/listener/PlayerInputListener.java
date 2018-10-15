package com.shooter.game.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.shooter.game.objects.Player;

public class PlayerInputListener extends InputListener {

    private Player player;

    public PlayerInputListener(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (keycode == Input.Keys.W || keycode == Input.Keys.UP ) {
            player.up();
        }
        if (keycode == Input.Keys.S|| keycode == Input.Keys.DOWN ) {
            player.down();
        }
        if (keycode == Input.Keys.A|| keycode == Input.Keys.LEFT ) {
            player.left();
        }
        if (keycode == Input.Keys.D|| keycode == Input.Keys.RIGHT ) {
            player.right();
        }
        return true;
    }

}
