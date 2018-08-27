package gravitate.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;

import gravitate.GameMain;
import gravitate.Level;

public class LevelPanel extends JPanel {
	private Level level;
	private GameMain instance;
	private boolean running = false, dead = false, complete = false, endPanelDoneGrowing = false;
	private int levelEndPanelHeight = 1;
	
	public LevelPanel(GameMain instance) {
		this.instance = instance;
		level = new Level(instance);
		running = false;
		levelEndPanelHeight = 1;
	}
	
	/**
	 * Loads a level. Used for interfacing with GameMain.
	 * @param fileName the file name without the extension.
	 */
	public void load(String fileName) {
		try {
			level.load(fileName);
			running = true;
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			instance.escapeLevel(e);
			level.clear();
		}
	}
	
	/**
	 * Loads a level. Used for interfacing with GameMain. Searches the custom level folder.
	 * @param fileName the file name without the extension.
	 */
	public void loadCustom(String fileName) {
		try {
			level.loadCustom(fileName);
			running = true;
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			instance.escapeLevel(e);
			level.clear();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.fillRect(0, 0, instance.obj.getWidth(), instance.obj.getHeight());
		if(running) update(instance.resetUpdateTimer());
		checkEndConditions();
		level.draw(g);
		if(dead) drawDeadPanel(g);
		if(complete) drawCompletePanel(g);
		paintComponents(g);
	}
	
	/**
	 * Checks if a level
	 */
	public void checkEndConditions() {
		dead = level.checkDead();
		complete = level.checkLevelComplete();
	}
	
	/**
	 * Updates all elements of the level. Call before paint.
	 * @param deltaTime
	 */
	public void update(double deltaTime) {
		level.update(deltaTime);
	}

	/**
	 * Passes key events to Level. Used for interfacing with GameMain.
	 * @param e the KeyEvent
	 */
	public void keyPress(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_P) {
			if(running) pause();
			else start();
		}
		else level.keyPress(e);
	}

	/**
	 * Returns the level's width. Used for interfacing.
	 * @return the width in pixels
	 */
	public int getLevelWidth() {
		return level.levelWidth();
	}
	
	/**
	 * Returns the player's x coordinate. Used for interfacing.
	 * @return the x coordinate in pixels
	 */
	public int getPlayerX() {
		return level.getPlayerX();
	}
	
	/**
	 * Restarts the update timer and starts the level.
	 */
	public void start() {
		instance.restartUpdateTimer();
		running = true;
	}
	
	/**
	 * Pauses the level.
	 */
	public void pause() {
		running = false;
	}
	
	JLabel title;
	/**
	 * Draws the death panel. In reality, returns to the menu.
	 * @param g the Graphics object
	 */
	public void drawDeadPanel(Graphics g) {
		instance.gotoLevelChooser();
		
		//All below here is old and abandoned.
		
		
		try {
			this.remove(title);
		} catch(Exception e) {}
		//g.setColor(new Color(50, 50, 50, 100));
		//g.fillRect(0, 0, instance.obj.getWidth(), instance.obj.getHeight());
		g.setColor(new Color(220, 220, 220));
		//g.fillRoundRect(instance.obj.getWidth() / 2 - 200, instance.obj.getHeight() / 2 - 300, 400, 600, 10, 10);
		if(!endPanelDoneGrowing) g.fillRect(0, instance.obj.getHeight() / 2 - levelEndPanelHeight / 2, instance.obj.getWidth(), levelEndPanelHeight);
		else g.fillRect(0, 0, instance.obj.getWidth(), instance.obj.getHeight());
		if(levelEndPanelHeight < instance.obj.getHeight()) levelEndPanelHeight += 20;
		else endPanelDoneGrowing = true;
		this.setLayout(new BorderLayout());
		//JPanel deadPanel = new JPanel(new BorderLayout());
		//deadPanel.setLayout(new BoxLayout(deadPanel, BoxLayout.LINE_AXIS));
		//deadPanel.add(Box.createRigidArea(new Dimension(350, 550)));
		//deadPanel.setPreferredSize(new Dimension(350, 550));
		//deadPanel.setBackground(Color.RED);
		//deadPanel.setBorder(new EmptyBorder(instance.obj.getHeight() / 2 - 270, instance.obj.getWidth() / 2 - 180, instance.obj.getHeight() / 2 - 270, instance.obj.getWidth() / 2 - 180));
		///this.add(deadPanel, BorderLayout.CENTER);
		title = new JLabel("You Died", JLabel.CENTER);
	    title.setFont(new Font("Courier", Font.BOLD, 36));
	    //title.setOpaque(true);
	    title.setBackground(Color.WHITE);
	    title.setBorder(new EmptyBorder(instance.obj.getHeight() / 2 - 270, 0, 0, 0));
		//text.setText("<html><span style=\"font-size:18px;color:black;text-align:center;\">foo</span></html>");
        //deadPanel.add(title, BorderLayout.NORTH);
		this.add(title, BorderLayout.NORTH);
		title.setVisible(true);
		//text.setSize(150, 20);
        //text.setLocation(500, 500);
        //System.out.println(text.getLocation());
        paintComponents(g);
	}
	
	/**
	 * Draws the level complete panel. In reality, returns to the menu.
	 * @param g the Graphics object
	 */
	public void drawCompletePanel(Graphics g) {
		g.setColor(new Color(50, 50, 50, 100));
		g.fillRect(0, 0, instance.obj.getWidth(), instance.obj.getHeight());
		g.setColor(new Color(220, 220, 220));
		g.fillRoundRect(instance.obj.getWidth() / 2 - 200, instance.obj.getHeight() / 2 - 300, 400, 600, 10, 10);
		//Above is old and abandoned.
		instance.gotoLevelChooser();
		
	}

}
