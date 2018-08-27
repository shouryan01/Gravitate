package gravitate.util;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONData {
	private String levelRaw = null, terrainRaw = null, entitiesRaw = null;
	
	public JSONData() {
		
	}
	
	/**
	 * Sets the level data.
	 * @param data the level string from the JSON.
	 */
	public void setLevelData(String data) {
		levelRaw = data;
	}

	/**
	 * Sets the terrain data.
	 * @param data the terrain string from the JSON.
	 */
	public void setTerrainData(String data) {
		terrainRaw = data;
	}

	/**
	 * Sets the entity data.
	 * @param data the entities string from the JSON.
	 */
	public void setEntityData(String data) {
		entitiesRaw = data;
	}
	
	/**
	 * Returns the level data.
	 * @return the level data stored in a JSON object
	 */
	public JSONObject level() {
		return new JSONObject(levelRaw);
	}

	/**
	 * Returns the terrain data.
	 * @return the terrain data stored in a JSON array
	 */
	public JSONArray terrain() {
		return new JSONArray(terrainRaw);
	}

	/**
	 * Returns the entity data.
	 * @return the entity data stored in a JSON array
	 */
	public JSONArray entities() {
		return new JSONArray(entitiesRaw);
	}

}
