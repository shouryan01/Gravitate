package gravitate;

import java.awt.Graphics;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class EntityManager {
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private GameMain instance;
	
	public EntityManager(GameMain instance) {
		this.instance = instance;
	}
	
	@Deprecated
	/**
	 * Lighter constructor for testing individual classes.
	 * DO NOT USE UNLESS YOU ARE NOT GOING TO DRAW ANYTHING!
	 */
	public EntityManager() {
		
	}
	
	/**
	 * Loads entities into the level.
	 * @param data JSONArray containing the entity data
	 */
	public void load(JSONArray data) {
		for(int i = 0; i < data.length(); i++) {
			JSONObject obj = data.getJSONObject(i);
			switch(obj.getString("type").trim().toLowerCase()) {
				default:
					System.out.println("[EntityLoader] Unexpected object of type \"" + obj.getString("type").trim() + "\" at index " + i + ".");
			}
		}
	}
	
	/**
	 * Adds an entity to the level.
	 * @param e the entity to be added
	 */
	public void add(Entity e) {
		entities.add(e);
	}
	

	/**
	 * Returns the entity at the specified index.
	 * @param index the index to return
	 * @return the entity at index
	 */
	public Entity get(int index) {
		return entities.get(index);
	}
	
	/**
	 * Returns the number of entities in the level.
	 * @return the number of entities
	 */
	public int size() {
		return entities.size();
	}
	
	/**
	 * Removes all entities from the level.
	 */
	public void clear() {
		entities.clear();
	}
	
	/**
	 * Updates all entities.
	 * @param deltaTimeSecs seconds since the last update
	 */
	public void update(double deltaTimeSecs) {
		for(Entity e : entities) e.update(deltaTimeSecs);
	}
	
	/**
	 * Draws all entities.
	 * @param g the Graphics object
	 */
	public void draw(Graphics g) {
		for(Entity e : entities) e.draw(g, instance.getCameraOffset());
	}
	
	/**
	 * Checks the damage of all entities the given entity is currently touching.
	 * @param e the entity to check
	 * @return the cumulative damage of all entities being touched
	 */
	public int checkDamage(Entity e) {
		int damage = 0;
		for(Entity current : entities) {
			if(current.checkCollision(e) != Entity.collideStates.NO_COLLISION) damage += current.getDamage();
		}
		return damage;
	}

}
