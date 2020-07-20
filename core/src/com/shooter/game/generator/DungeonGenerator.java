package com.shooter.game.generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.noise4j.map.Grid;
import com.shooter.game.helpers.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DungeonGenerator {

    private TiledMap tiledMap = new TiledMap();
    private OrthogonalTiledMapRenderer renderer;
    public int playerPositionX = 0;
    public int playerPositionY = 0;
    public List<Float[]> possibleEnemyPositions = new ArrayList<Float[]>();
    public Grid grid;

    public DungeonGenerator(Stage stage) {
        grid = new Grid((int) Constants.worldSizeX / 16, (int) Constants.worldSizeY / 16);
        Texture textureForWalls = new Texture(Gdx.files.internal("sprite/Wall.png"));
        TextureRegion wallTexture[][] = TextureRegion.split(textureForWalls,textureForWalls.getWidth()/21,textureForWalls.getHeight()/51);
        Texture textureForFloors = new Texture(Gdx.files.internal("sprite/Floor.png"));
        TextureRegion floorTexture[][] = TextureRegion.split(textureForFloors,textureForFloors.getWidth()/21,textureForFloors.getHeight()/33);
        com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator dungeonGenerator = new com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator();
        dungeonGenerator.setRoomGenerationAttempts(100);
        dungeonGenerator.setMaxRoomsAmount(10);
        dungeonGenerator.setMaxRoomSize(25);
        dungeonGenerator.setTolerance(10);
        dungeonGenerator.setMinRoomSize(9);
        dungeonGenerator.generate(grid);

        MapLayers mapLayers = getTiledMap().getLayers();
        boolean playerPositionSetted = false;
        StaticTiledMapTile staticTiledMapTile = new StaticTiledMapTile(floorTexture[1][1]);
        TiledMapTileLayer tiledMapTileLayer = new TiledMapTileLayer(Constants.worldSizeX,Constants.worldSizeY,16,16);
        tiledMapTileLayer.setName("map");
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                float val = grid.get(x,y);
                int tileMode = 0;
                if (val == 0f) { tileMode = 0;}else if (val == 0.5f) {tileMode = 1;} else {tileMode = 2;}
                switch (tileMode) {
                    case 0:
                        staticTiledMapTile = new StaticTiledMapTile(floorTexture[7][1]);
                        break;
                    case 1:
                        staticTiledMapTile = new StaticTiledMapTile(floorTexture[14][8]);
                        if (!playerPositionSetted) {
                            this.playerPositionX = (int) ( (x * (tiledMapTileLayer.getTileWidth() * 2)));
                            this.playerPositionY = (int) ( (y * (tiledMapTileLayer.getTileHeight() * 2)));
                            playerPositionSetted = true;
                            staticTiledMapTile = new StaticTiledMapTile(floorTexture[17][8]);
                            staticTiledMapTile.getProperties().put("player",true);
                        }
                        Float[] enemyPosition = new Float[2];
                        enemyPosition[0] = (x * (tiledMapTileLayer.getTileWidth() * 2));
                        enemyPosition[1] = (y * (tiledMapTileLayer.getTileHeight() * 2));
                        possibleEnemyPositions.add(enemyPosition);
                        break;
                    case 2:
                        staticTiledMapTile = new StaticTiledMapTile(wallTexture[3][3]);
                        staticTiledMapTile.getProperties().put("solid",true);
                        break;
                }
                staticTiledMapTile.getProperties().put("x",x);
                staticTiledMapTile.getProperties().put("y",y);
                cell.setTile(staticTiledMapTile);
                tiledMapTileLayer.setCell(x,y,cell);
            }
        }
        mapLayers.add(tiledMapTileLayer);
        renderer = new OrthogonalTiledMapRenderer(tiledMap,2f);

    }

    public StaticTiledMapTile chooseWallTexture(int x, int y,Grid grid,TextureRegion wallTextures[][]) {
        boolean topLeft = false;
        boolean top = false;
        boolean topRight = false;
        boolean left = false;
        boolean right = false;
        boolean bottomLeft = false;
        boolean bottom = false;
        boolean bottomRight = false;

        if (grid.isIndexValid(x - 1,y + 1)) topLeft = grid.get(x - 1,y + 1) < 1f;
        if (grid.isIndexValid(x ,y + 1)) top = grid.get(x,y + 1) < 1f;
        if (grid.isIndexValid(x + 1,y + 1)) topRight = grid.get(x + 1,y + 1) < 1f;
        if (grid.isIndexValid(x - 1,y )) left = grid.get(x - 1,y ) < 1f;
        if (grid.isIndexValid(x + 1,y )) right = grid.get(x + 1,y ) < 1f;
        if (grid.isIndexValid(x - 1,y - 1)) bottomLeft = grid.get(x - 1,y - 1) < 1f;
        if (grid.isIndexValid(x ,y - 1)) bottom = grid.get(x ,y - 1) < 1f;
        if (grid.isIndexValid(x + 1,y - 1)) bottomRight = grid.get(x + 1,y - 1) < 1f;

        if (topLeft && top && topRight && left && right && bottomLeft && bottom && bottomRight) {
            return new StaticTiledMapTile(wallTextures[4][1]);
        }
        return new StaticTiledMapTile(wallTextures[4][1]);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }


    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(OrthogonalTiledMapRenderer renderer) {
        this.renderer = renderer;
    }
}
