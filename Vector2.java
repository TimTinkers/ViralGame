package viral;

/**
 *
 * A class to hold two linked integers.
 *
 * @author Tim Clancy
 * @version 10.18.2013
 */
class Vector2 {

    private int x;
    private int y;

    /**
     *
     * Construct the two integers connected in an object.
     *
     * @param x The first integer.
     * @param y The second integer.
     */
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * Returns the first of the two linked integers.
     *
     * @return The first integer.
     */
    public int getX() {
        return x;
    }

    /**
     *
     * Returns the second of the two linked integers.
     *
     * @return The second integer.
     */
    public int getY() {
        return y;
    }

    /**
     *
     * Compares the Vector2 object to another one and determines if they are
     * equivalent.
     *
     * @param o The object to compare with. Can only possibly be true if this is
     * also an object of type Vector2.
     * @return True if the two objects are identical to one another.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Vector2) {
            boolean value = false;
            if (o.hashCode() == this.hashCode()) {
                value = true;
            }
            return value;
        } else {
            return false;
        }
    }

    /**
     *
     * A method which converts all unique data about this object into a usable,
     * single integer.
     *
     * @return The unique integer with regard to data.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        return hash;
    }
}