package gravitate;

import java.awt.Graphics;

/**
 * 
 * @author Ethan Pelkie
 *
 */

public abstract class Entity {
	private int w, h;
	private double x, y;
	private final int damage;
	private boolean experiencesGravity, isSolid;
	private dir facing;
	private orientationStates orientation;
	private double xSpeed = 0, ySpeed = 0;
	private static final double GRAVITY_STRENGTH = 1000;
	private static final double MAX_VERTICAL_SPEED = 1000;
	private boolean onGround = false;
	private GameMain instance;

	public static enum collideStates {
		NO_COLLISION, FRONT_COLLISION, BACK_COLLISION, TOP_COLLISION, BOTTOM_COLLISION
	};

	public static enum dir {
		LEFT, RIGHT
	};

	// Which way is down for the entity
	public static enum orientationStates {
		UP, DOWN
	};

	public Entity(int x, int y, int width, int height, dir facing, orientationStates orientation,
			boolean experiencesGravity, boolean isSolid, int damage) {
		this.x = x;
		this.y = y;
		w = width;
		h = height;
		this.facing = facing;
		this.orientation = orientation;
		this.experiencesGravity = experiencesGravity;
		this.isSolid = isSolid;
		this.damage = damage;
		this.instance = instance;

	}

	/**
	 * Draws the entity.
	 * 
	 * @param g
	 *            the Graphics object
	 * @param offset
	 *            the camera offset
	 */
	public abstract void draw(Graphics g, int offset);

	/**
	 * Updates the entity
	 * 
	 * @param deltaTimeSecs
	 *            seconds since the last update
	 */
	public abstract void update(double deltaTimeSecs);

	/**
	 * Returns the x coordinate of the entity's center point
	 * 
	 * @return the x coordinate in pixels
	 */
	public int getX() {
		return (int) Math.round(x);
	}

	/**
	 * Returns the y coordinate of the entity's center point
	 * 
	 * @return the y coordinate in pixels
	 */
	public int getY() {
		return (int) Math.round(y);
	}

	/**
	 * Returns the precise x coordinate of the entity. Useful for operations that require precision, such as acceleration over small time scales.
	 * @return the precise x coordinate
	 */
	public double getXPrecise() {
		return x;
	}

	/**
	 * Returns the precise y coordinate of the entity. Useful for operations that require precision, such as acceleration over small time scales.
	 * @return the precise y coordinate
	 */
	public double getYPrecise() {
		return y;
	}

	/**
	 * Sets the x coordinate of the entity's center point.
	 * @param newX the new x coordinate
	 */
	public void setX(int newX) {
		x = newX;
	}

	/**
	 * Sets the x coordinate of the entity's center point. A more precise version.
	 * @param newX the new x coordinate
	 */
	public void setX(double newX) {
		x = newX;
	}

	/**
	 * Sets the y coordinate of the entity's center point.
	 * @param newY the new y coordinate
	 */
	public void setY(int newY) {
		y = newY;
	}

	/**
	 * Sets the y coordinate of the entity's center point. A more precise version.
	 * @param newY the new y coordinate
	 */
	public void setY(double newY) {
		y = newY;
	}

	/**
	 * Returns the entity's width.
	 * @return the width in pixels
	 */
	public int getWidth() {
		return w;
	}

	/**
	 * Returns the entity's height.
	 * @return the height in pixels
	 */
	public int getHeight() {
		return h;
	}

	/**
	 * Returns the damage dealt by the entity
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Checks whether this entity is affected by gravity.
	 * @return whether this entity is affected by gravity
	 */
	public boolean experiencesGravity() {
		return experiencesGravity;
	}

	/**
	 * Returns the orientation (up or down) of the entity. This is the down direction for the entity.
	 * @return an orientationStates with the orientation
	 */
	public orientationStates getOrientation() {
		return orientation;
	}

	/**
	 * Returns the entity's horizontal speed.
	 * @return the speed in pixels/second
	 */
	public double xSpeed() {
		return xSpeed;
	}

	/**
	 * Returns the entity's vertical speed. Negative is up.
	 * @return the speed in pixels/second
	 */
	public double ySpeed() {
		return ySpeed;
	}

	/**
	 * Sets the entity's horizontal speed.
	 * @param speed the speed in pixels/second
	 */
	public void setXSpeed(double speed) {
		xSpeed = speed;
	}

	/**
	 * Sets the entity's vertical speed. Negative is up.
	 * @param speed the speed in pixels/second
	 */
	public void setYSpeed(double speed) {
		ySpeed = speed;
	}

	/**
	 * Set whether the entity is on the ground. This determines whether gravity can be switched.
	 * @param onGround whether the entity is on the ground
	 */
	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	/**
	 * Returns whether the entity is on the ground. This determines whether gravity can be switched.
	 * @param onGround whether the entity is on the ground
	 */
	public boolean onGround() {
		return onGround;
	}

	/**
	 * See implementation in child class.
	 */
	public abstract void hurt();

	/**
	 * Returns the GameMain object. Used for interfacing.
	 * @return the GameMain object
	 */
	public GameMain instance() {
		return instance;
	}

	/**
	 * Switches between UP and DOWN orientations
	 * 
	 * @return the new orientation
	 */
	public orientationStates swapOrientation() {
		if (orientation == orientationStates.DOWN)
			orientation = orientationStates.UP;
		else
			orientation = orientationStates.DOWN;
		return orientation;
	}

	/**
	 * Checks whether the entity is touching the given entity. Broken.
	 * @param e the entity to check
	 * @return a collideStates detailing the collision, if any
	 */
	public collideStates checkCollision(Entity e) {
		if (Math.abs(x - e.getX()) <= w / 2 + e.getWidth() / 2) {
			if (x > e.getX()) {
				if (facing == dir.LEFT)
					return collideStates.FRONT_COLLISION;
				return collideStates.BACK_COLLISION;
			}
			if (facing == dir.RIGHT)
				return collideStates.FRONT_COLLISION;
			return collideStates.BACK_COLLISION;
		}
		if (Math.abs(y - e.getY()) <= h / 2 + e.getHeight() / 2) {
			if (y > e.getY()) {
				if (orientation == orientationStates.UP)
					return collideStates.TOP_COLLISION;
				return collideStates.BOTTOM_COLLISION;
			}
			if (orientation == orientationStates.DOWN)
				return collideStates.TOP_COLLISION;
			return collideStates.BOTTOM_COLLISION;
		}
		return collideStates.NO_COLLISION;
	}

	/**
	 * Accelerates the entity gravitationally.
	 * @param deltaTimeSecs seconds since the last update
	 */
	public void gravitate(double deltaTimeSecs) {
		double acceleration = GRAVITY_STRENGTH * deltaTimeSecs;
		switch (orientation) {
		case UP:
			// velocity = velocity.add(new Vector(0, acceleration));
			ySpeed -= acceleration;
			break;
		case DOWN:
			// velocity = velocity.subtract(new Vector(0, acceleration));
			ySpeed += acceleration;
			break;
		}
		if (ySpeed > MAX_VERTICAL_SPEED)
			ySpeed = 1000;
		if (ySpeed < -MAX_VERTICAL_SPEED)
			ySpeed = -1000;

	}

}
