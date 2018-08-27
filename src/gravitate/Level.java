package gravitate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import gravitate.util.FileIO;
import gravitate.util.ImageLoader;
import gravitate.util.JSONData;

public class Level {
	private TerrainManager terrain;
	private EntityManager entities;
	private int levelWidth;
	private String levelName;
	private Player player;
	private GameMain instance;
	private BufferedImage background;
	
	public Level(GameMain instance) {
		this.instance = instance;
		terrain = new TerrainManager(instance);
		entities = new EntityManager(instance);
		background = ImageLoader.loadImage("assets/background.png");
		levelName = "";
	}
	
	@Deprecated
	/**
	 * Lighter constructor for testing individual classes.
	 * DO NOT USE UNLESS YOU ARE NOT GOING TO DRAW ANYTHING!
	 */
	public Level() {
		terrain = new TerrainManager();
		entities = new EntityManager();
		levelName = "";
	}
	
	/**
	 * Loads a level file into the game.
	 * @param fileName the file name without the extension
	 * @throws IOException
	 * @throws JSONException
	 */
	public void load(String fileName) throws IOException, JSONException {
		JSONData data = FileIO.extractLevel(fileName);
		JSONObject levelData = data.level();
		levelName = levelData.getString("name");
		levelWidth = levelData.getInt("width");
		ImageLoader.setBackgroundSize(levelWidth);
		System.out.println("[LevelLoader] Initialized level \"" + levelName + "\" of width " + levelWidth + ".");
		player = new Player(levelData.getJSONObject("startPos").getInt("x"), levelData.getJSONObject("startPos").getInt("y"));
		System.out.println("[LevelLoader] Spawned player at (" + player.getX()  + "," + player.getY() + ").");
		terrain.load(data.terrain());
		entities.load(data.entities());
	}

	/**
	 * Loads a level file into the game. Searches the custom level folder.
	 * @param fileName the file name without the extension
	 * @throws IOException
	 * @throws JSONException
	 */
	public void loadCustom(String fileName) throws IOException, JSONException {
		JSONData data = FileIO.extractCustomLevel(fileName);
		JSONObject levelData = data.level();
		levelName = levelData.getString("name");
		levelWidth = levelData.getInt("width");
		ImageLoader.setBackgroundSize(levelWidth);
		System.out.println("[LevelLoader] Initialized level \"" + levelName + "\" of width " + levelWidth + ".");
		player = new Player(levelData.getJSONObject("startPos").getInt("x"), levelData.getJSONObject("startPos").getInt("y"));
		System.out.println("[LevelLoader] Spawned player at (" + player.getX()  + "," + player.getY() + ").");
		terrain.load(data.terrain());
		entities.load(data.entities());
	}
	
	/**
	 * Resets the level. Clears terrain, entities, level name, and level width.
	 */
	public void clear() {
		levelName = "";
		levelWidth = 0;
		terrain.clear();
		entities.clear();
	}
	/**
	 * Performs mechanical updates on all elements, such as moving, collision detection, and damage checking.
	 * @param deltaTimeSecs - the time in seconds since the last update
	 */
	public void update(double deltaTimeSecs) {
		//System.out.println(deltaTimeSecs);
		terrain.update(deltaTimeSecs);
		entities.update(deltaTimeSecs);
		if(!checkDead() && !checkLevelComplete()) player.update(deltaTimeSecs);
		terrain.checkCollisions(player);
		if(!checkDead() && !checkLevelComplete()) player.move(deltaTimeSecs);
	}
	
	/**
	 * Draws all elements of the level. Call after <code>{@link #update(double) update(...)}</code>.
	 * @param g - the graphics object passed down from <code>{@link gravitate.GameMain#paint() paint(...)}</code>
	 */
	public void draw(Graphics g) {
		//g.setColor(instance.inDevMode() ? Color.CYAN : Color.BLUE);
		//g.fillRect(0, 0, 10000, 10000);
		int offset = instance.getCameraOffset();
		ImageLoader.drawBackground(g, offset);
		terrain.draw(g);
		entities.draw(g);
		if(!checkDead()) player.draw(g, offset);
	}
	
	/**
	 * Checks if the player has died or fallen off the level.
	 * @return whether the player is dead
	 */
	public boolean checkDead() {
		return player.getY() < 0 - player.getHeight() / 2 || player.getY() > instance.obj.getHeight() + player.getHeight() / 2 || player.getHealth() <= 0;
	}
	
	/**
	 * Checks if the player has passed the end of the level.
	 * @return whether the level is complete
	 */
	public boolean checkLevelComplete() {
		return player.getX() > levelWidth + player.getWidth() / 2;
	}
	
//	/**
//	 * A main method designed as a quick and dirty test of loading functionality.
//	 */
//	public static void main(String[] args) {
//		Level foo = new Level();
//		try {
//			foo.load("levelTest");
//		} catch (JSONException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * Returns the level width.
	 * @return the width in pixels
	 */
	public int levelWidth() {
		return levelWidth;
	}
	
	/**
	 * Returns the player's x coordinate.
	 * @return the x coordinate in pixels
	 */
	public int getPlayerX() {
		return player.getX();
	}
	/**
	 * Manages all keyboard input within the level.
	 * @param e - the <code>{@link java.awt.event.KeyEvent KeyEvent}</code> to be processed
	 */
	public void keyPress(KeyEvent e) {
		int c = e.getKeyCode();
		if(c == KeyEvent.VK_SPACE && (player.onGround() || instance.inDevMode())) player.swapOrientation();
	}
	
}
