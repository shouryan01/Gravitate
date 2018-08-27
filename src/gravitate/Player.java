package gravitate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import gravitate.util.ImageLoader;

public class Player extends Entity
{
	private int xd, yd;
	//private GameMain2 instance;
	private int health = 50;
	private boolean immunity = false, dead = false;
	private long immunityTime;
	private static final double MAX_HORIZONTAL_SPEED = 500;
	private static final double HORIZONTAL_ACCELERATION = 500;
	private static final int IMMUNITY_DURATION = 2000;
	private ImageIcon stillTexture, runningTexture, stillUpTexture, runningUpTexture;
	private JLabel sprite;
	
	public Player(int x, int y)
	{
		super(x, y, 54, 48, dir.RIGHT, orientationStates.DOWN, true, true, 0);
		setXSpeed(MAX_HORIZONTAL_SPEED);
		stillTexture = ImageLoader.loadGif("playerStill.gif");
		runningTexture = ImageLoader.loadGif("playerMove.gif");
		stillUpTexture = ImageLoader.loadGif("playerStillUp.gif");
		runningUpTexture = ImageLoader.loadGif("playerMoveUp.gif");
		sprite = new JLabel(runningTexture, JLabel.CENTER);
		sprite.setVisible(true);
		instance().levelPanel.add(sprite);
	}
	
	/**
	 * Moves the player. 
	 * @param deltaTimeSecs seconds since the last update
	 */
	public void move(double deltaTimeSecs)
	{
		setY(getYPrecise() +  ySpeed() * deltaTimeSecs);
		setX(getX() + xSpeed() * deltaTimeSecs);
	}
    
	/**
	 * Accelerates the player horizontally.
	 * @param deltaTimeSecs seconds since the last update
	 */
    public void moveForward(double deltaTimeSecs) {
    	setXSpeed(xSpeed() + HORIZONTAL_ACCELERATION * deltaTimeSecs);
    	if(xSpeed() > MAX_HORIZONTAL_SPEED) setXSpeed(MAX_HORIZONTAL_SPEED);
    }
	
//	public void damage(int damage) {
//		if(immunity) {
//			if(System.currentTimeMillis() - immunityTime >= IMMUNITY_DURATION) immunity = false;
//			else return;
//		}
//		if(damage > 0) {
//			immunity = true;
//			immunityTime = System.currentTimeMillis();
//			health -= damage;
//		}
//	}
	
    /**
     * Kills the player.
     */
	public void hurt() {
		health = 0;
	}
	/**
	 * Checks whether the player is dead.
	 * @return whether the player is dead
	 */
	public boolean isDead() {
		return dead;
	}
	
	/**
	 * Returns the player's health.
	 * @return the player's health
	 */
	public int getHealth() {
		return health;
	}

	@Override
	public void draw(Graphics g, int offset) {
		g.setColor(Color.ORANGE);
		//g.fillRect(getX() - getWidth() / 2 + offset, getY() - getHeight() / 2, getWidth(), getHeight());
		//stillTexture.paintIcon(sprite, g, getX() + offset, getY());
		if(xSpeed() == 0) {
			if(getOrientation() == Entity.orientationStates.DOWN) sprite.setIcon(stillTexture);
			else sprite.setIcon(stillUpTexture);
			sprite.setBounds(getX() - getWidth() / 2 + offset, getY() - getHeight() / 2, getWidth(), getHeight());
		}
		else {
			if(getOrientation() == Entity.orientationStates.DOWN) {
				sprite.setIcon(runningTexture);
				sprite.setBounds(getX() - getWidth() / 2 + offset, getY() - getHeight() / 2 - 6, getWidth(), 54);
			}
			else {
				sprite.setIcon(runningUpTexture);
				sprite.setBounds(getX() - getWidth() / 2 + offset, getY() - getHeight() / 2, getWidth(), 54);
			}
		}
		
		//sprite.setOpaque(true);
	}

	@Override
	public void update(double deltaTimeSecs) {
		gravitate(deltaTimeSecs);
		moveForward(deltaTimeSecs);
	}
}