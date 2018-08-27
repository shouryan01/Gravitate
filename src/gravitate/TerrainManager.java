package gravitate;

import java.awt.Graphics;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Stores all terrain in the level. Manages terrain updates and interactions.
 *
 */
public class TerrainManager {
	private ArrayList<Terrain> terrain = new ArrayList<Terrain>();
	private GameMain instance;
	
	public TerrainManager(GameMain instance) {
		this.instance = instance;
	}
	
	@Deprecated
	/**
	 * Lighter constructor for testing individual classes.
	 * DO NOT USE UNLESS YOU ARE NOT GOING TO DRAW ANYTHING!
	 */
	public TerrainManager() {
		
	}
	
	/**
	 * Loads terrain into the level.
	 * @param data the JSONArray containing the terrain.
	 */
	public void load(JSONArray data) {
		for(int i = 0; i < data.length(); i++) {
			JSONObject obj = data.getJSONObject(i);
			switch(obj.getString("type").trim().toLowerCase()) {
				case "ground":
					terrain.add(new Ground(obj.getInt("x"), obj.getInt("y"), obj.getInt("width"), obj.getInt("height")));
					System.out.println("[TerrainLoader] Successfully loaded object of type \"Ground\" at index " + i + ".");
					break;
				case "wall":
					terrain.add(new Wall(obj.getInt("x"), obj.getInt("y"), obj.getInt("width"), obj.getInt("height")));
					System.out.println("[TerrainLoader] Successfully loaded object of type \"Wall\" at index " + i + ".");
					break;
				case "spike":
					terrain.add(new Spike(obj.getInt("x"), obj.getInt("y"), obj.getInt("width"), obj.getInt("height")));
					System.out.println("[TerrainLoader] Successfully loaded object of type \"Spike\" at index " + i + ".");
					break;
				default:
					System.out.println("[TerrainLoader] Unexpected object of type \"" + obj.getString("type").trim() + "\" at index " + i + ".");
			}
		}
	}
	
	/**
	 * Adds a terrain object into the level.
	 * @param t the terrain object
	 */
	public void add(Terrain t) {
		terrain.add(t);
	}
	
	/**
	 * Returns the terrain object at the specified index.
	 * @param index the index to return
	 * @return the terrain object at index
	 * @throws IndexOutOfBoundsException
	 */
	public Terrain get(int index) throws IndexOutOfBoundsException {
		return terrain.get(index);
	}
	
	/**
	 * Empties the level of terrain.
	 */
	public void clear() {
		terrain.clear();
	}
	
	/**
	 * Updates all terrain in the level.
	 * @param deltaTimeSecs seconds since the last update
	 */
	public void update(double deltaTimeSecs) {
		for(Terrain t : terrain) t.update(deltaTimeSecs);
	}
	
	/**
	 * Draws all terrain in the level.
	 * @param g the Graphics object
	 */
	public void draw(Graphics g) {
		ArrayList<Terrain> bg = new ArrayList<Terrain>();
		ArrayList<Terrain> fg = new ArrayList<Terrain>();
		for(Terrain t : terrain) {
			if(t.isSolid()) fg.add(t);
			else bg.add(t);
		}
		int offset = instance.getCameraOffset();
		for(Terrain t : bg) t.draw(g, offset);
		for(Terrain t : fg) t.draw(g, offset);
	}
	
	/**
	 * Checks collisions between terrain and the given entity and takes any appropriate action (i.e. stop movement).
	 * @param e the entity to check
	 */
	public void checkCollisions(Entity e) {
		e.setOnGround(false);
		for(Terrain t : terrain) {
			Terrain.collideStates collision = t.checkCollision(e);
			if(collision != Terrain.collideStates.NO_COLLISION && t.getDamage() > 0) {
				e.hurt();
			}
			if(collision == Terrain.collideStates.BOTTOM_COLLISION && e.getOrientation() == Entity.orientationStates.UP || collision == Terrain.collideStates.TOP_COLLISION && e.getOrientation() == Entity.orientationStates.DOWN) {
				e.setOnGround(true);
			}
			//if(collision != Terrain.collideStates.NO_COLLISION) System.out.println(collision);
		}
	}
	
	/**
	 * Returns the cumulative damage for all terrain the given entity is touching.
	 * @param e the entity to check
	 * @return the damage dealt
	 */
	public int checkDamage(Entity e) {
		int damage = 0;
		for(Terrain current : terrain) {
			if(current.checkCollision(e) != Terrain.collideStates.NO_COLLISION) damage += current.getDamage();
		}
		return damage;
	}

}
