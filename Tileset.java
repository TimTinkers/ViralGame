package viral;

import java.util.ArrayList;

/**
 *
 * The class to hold tile coordinates with images and helper functions for the
 * playing field.
 *
 * @author Tim Clancy
 * @version 10.18.2013
 */
public class Tileset {

    private int[][] tiles;
    private int x;
    private int y;

    /**
     *
     * Constructs a two dimensional integer array and performs useful operations
     * on it. Intended to store data for tile backgrounds in 2D games.
     *
     * @param x The width of the array.
     * @param y The height of the array.
     */
    Tileset(int x, int y) {
        this.x = x;
        this.y = y;
        tiles = new int[x][y];
    }

    /**
     *
     * Searches a tileset and returns, as an ArrayList, the coordinates of all
     * tiles matching the ID.
     *
     * @param tileID The integer ID of the tile to search for.
     * @return An ArrayList of Vector2 coordinates.
     */
    public ArrayList<Vector2> search(int tileID) {
        ArrayList<Vector2> tileSet = new ArrayList<Vector2>();
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                if (tiles[i][j] == tileID) {
                    Vector2 input = new Vector2(i, j);
                    tileSet.add(input);
                }
            }
        }
        return tileSet;
    }

    /**
     *
     * Finds the tile at x, y and sets its ID to equal value.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @param value The ID to set the tile equal to.
     */
    public void setTile(int x, int y, int value) {
        tiles[x][y] = value;
    }

    /**
     *
     * Gets the ID of the tile at the specified coordinates.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return The integer ID of the tile at the coordinates.
     */
    public int getTile(int x, int y) {
        return tiles[x][y];
    }

    /**
     *
     * Gets the width of the tileset.
     *
     * @return The width, along x axis.
     */
    public int getWidth() {
        return this.x;
    }

    /**
     *
     * Gets the height of the tileset.
     *
     * @return The height, along y axis.
     */
    public int getHeight() {
        return this.y;
    }

    /**
     *
     * A method that takes tile coordinates x,y and determines if it has any
     * tiles with ID matching that provided within radius blocks.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @param ID The ID of the tile to search matches for.
     * @param radius The radius of blocks to check within.
     * @return True if a tile of type ID is found, false otherwise.
     */
    public boolean hasAdjacentTile(int x, int y, int ID, int radius) {
        int minX;
        int minY;
        int maxX;
        int maxY;
        if (x - radius >= 0) {
            minX = x - radius;
        } else {
            minX = 0;
        }
        if (y - radius >= 0) {
            minY = y - radius;
        } else {
            minY = 0;
        }
        if (x + radius < this.x) {
            maxX = x + radius;
        } else {
            maxX = this.x - 1;
        }
        if (y + radius < this.y) {
            maxY = y + radius;
        } else {
            maxY = this.y - 1;
        }
        for (int i = minX; i < maxX + 1; ++i) {
            for (int j = minY; j < maxY + 1; ++j) {
                if (getTile(i, j) == ID) {
                    return true;
                }
            }
        }
        return false;
    }
}