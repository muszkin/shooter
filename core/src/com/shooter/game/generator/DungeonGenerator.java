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

public class DungeonGenerator {

    private TiledMap tiledMap = new TiledMap();
    private String layers[] = new String[2];
    private OrthogonalTiledMapRenderer renderer;
    public int playerPositionX = 0;
    public int playerPositionY = 0;

    public DungeonGenerator(Stage stage) {
        layers[0] = "Floor";
        layers[1] = "Wall";
        Grid grid = new Grid(100);
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
        for (int l = 0;l < layers.length; l++) {
            TiledMapTileLayer tiledMapTileLayer = new TiledMapTileLayer(Constants.worldSizeX,Constants.worldSizeY,16,16);
            tiledMapTileLayer.setName(layers[l]);
            for (int x = 0; x < grid.getWidth(); x++) {
                for (int y = 0; y < grid.getHeight(); y++) {
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    float val = grid.get(x,y);
                    if (l == 0 ) {
                        if (val == 0f) {
                            staticTiledMapTile = new StaticTiledMapTile(floorTexture[7][1]);
                            staticTiledMapTile.getProperties().put("solid", false);
                        }
                        if (val == 0.5f) {
                            staticTiledMapTile = new StaticTiledMapTile(floorTexture[14][8]);
                            if (!playerPositionSetted) {
                                this.playerPositionX = (x * 16 * 2) + 32;
                                this.playerPositionY = (y * 16 * 2) + 32;
                                playerPositionSetted = true;
                            }
                        }
                        cell.setTile(staticTiledMapTile);
                        tiledMapTileLayer.setCell(x,y,cell);
                    }else{
                        if (val == 1f) {
                            staticTiledMapTile = new StaticTiledMapTile(floorTexture[3][3]);
                            staticTiledMapTile.getProperties().put("solid",true);
                            cell.setTile(staticTiledMapTile);
                            tiledMapTileLayer.setCell(x, y, cell);
                        }
                    }

                }
            }
            mapLayers.add(tiledMapTileLayer);
        }
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
