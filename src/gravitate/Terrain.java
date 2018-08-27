package gravitate;

import java.awt.Graphics;

public abstract class Terrain {
    private int x, y, w, h;
    private final int damage;
    private final boolean isSolid;

    public static enum collideStates {NO_COLLISION, LEFT_COLLISION, RIGHT_COLLISION, TOP_COLLISION, BOTTOM_COLLISION};
    
    public Terrain(int x, int y, int width, int height, int damage, boolean isSolid) {
        this.x = x;
        this.y = y;
        w = width;
        h = height;
        this.damage = damage;
        this.isSolid = isSolid;
    }
    
    /**
     * Draws this terrain object.
     * @param g the Graphics object
     * @param offset the camera offset
     */
    public abstract void draw(Graphics g, int offset);
    
    /**
     * Updates this terrain object.
     * @param time seconds since the last update
     */
    public abstract void update(double time);
    
    /**
     * Checks collisions with the given entity and takes any appropriate action.
     * @param e the entity to check
     * @return a collideStates object detailing the collision, if any
     */
    public collideStates checkCollision(Entity e) {
    	collideStates collision = collideStates.NO_COLLISION;
    	if(isSolid && Math.abs(x - e.getX()) <= w / 2 + e.getWidth() / 2 && Math.abs(y - e.getY()) <= h / 2 + e.getHeight() / 2) {
    		if(e.getY() > y + h / 2 - 2) {
				collision = collideStates.BOTTOM_COLLISION;
				e.setY(y + e.getHeight() / 2 + h / 2);
				if(e.ySpeed() < 0) e.setYSpeed(0);
			}
			else if(e.getY() < y - h / 2 + 2) {
				collision = collideStates.TOP_COLLISION;
				e.setY(y - e.getHeight() / 2 - h / 2);
				if(e.ySpeed() > 0) e.setYSpeed(0);
			}
			else {
				collision = collideStates.LEFT_COLLISION;
				e.setXSpeed(0);
				e.setX(x - e.getWidth() / 2 - w / 2);
			}
    	}
    	return collision;
    }
    
    /**
     * Returns the x coordinate of the terrain's center point.
     * @return the x coordinate in pixels
     */
    public int getX() {
        return x;
    }
    /**
     * Returns the y coordinate of the terrain's center point.
     * @return the y coordinate in pixels
     */
    public int getY() {
        return y;
    }
    /**
     * Returns the terrain's width.
     * @return the width pixels
     */
    public int getWidth() {
        return w;
    }
    /**
     * Returns the terrain's height.
     * @return the height pixels
     */
    public int getHeight() {
        return h;
    }
    /**
     * Returns the damage dealt by this terrain.
     * @return the damage value
     */
    public int getDamage() {
    	return damage;
    }
    
    /**
     * Returns whether this terrain can be collided with.
     * @return whether the terrain is solid
     */
    public boolean isSolid() {
    	return isSolid;
    }
    
    @Override
    public String toString() {
    	return "[x:" + x + ", y:" + y + ", width:" + w + ", height:" + h + "]";
    }

}
