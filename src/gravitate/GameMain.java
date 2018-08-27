package gravitate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gravitate.ui.CustomLevelChooser;
import gravitate.ui.LevelPanel;
import gravitate.ui.levelChooser;
import gravitate.util.AudioManager;
import gravitate.util.ImageLoader;

public class GameMain extends JFrame { 
	
	static JPanel welcomePanel,gamePanel,endGamePanel;
	static LevelPanel levelPanel;
	static JLabel welcomeMessage, endMessage;
	static levelChooser levelChooser;
	static CustomLevelChooser customLevels;
	static JButton startButton,exitButton,quitButton,customLevelButton;
	private static boolean levelIsCustom = false;
	Color color;
	static GridBagConstraints gbc = new GridBagConstraints();
	public static GameMain obj = new GameMain();
	
	private long lastUpdateTime = -1;
	private boolean inLevel = false;
	private static boolean devMode = false;
	private BufferedImage background;
	
	private double bgScroll = 0;
	
	private AudioManager audio;
	private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	
	//How far the character is from the left edge of the screen when the camera is moving
	public static final int PLAYER_LEFT_OFFSET = 500;
	
	public GameMain() {
        ImageLoader.init(this);
		levelPanel = new LevelPanel(this);
		customLevels = new CustomLevelChooser(this);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(5,5,5,5);
		
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		JMenu m0 = new JMenu("MENU");
		m0.setToolTipText("Click to access the menu");
	        menubar.add(m0);
	        JMenuItem scoreno = new JMenuItem("CURRENT SCORE");
	        scoreno.setToolTipText("Click to know the score");
	        scoreno.addActionListener(new ActionListener()
	        {
	            public void actionPerformed(ActionEvent event)
	            {
	               // JOptionPane.showMessageDialog(null,score,"CURRENT SCORE",JOptionPane.INFORMATION_MESSAGE);
	            }
	        }
	        );
	        m0.add(scoreno);    
	        
	    JMenu m1 = new JMenu("OPTIONS");
	    m1.setToolTipText("Click to access the options");
	    menubar.add(m1);    
	    
	    JMenuItem setmColour = new JMenuItem("CHANGE MENUBAR COLOUR");
        setmColour.setToolTipText("Click to change the menu bar colour");
        setmColour.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                Color color=(Color.WHITE);
                color = JColorChooser.showDialog(null,"Pick Colour",color);
//                if(color==null)
//                color=color;
                
                menubar.setBackground(color); //setting the menu bar colour
            }
        }
        );
        m1.add(setmColour);
	   
        JMenu m2 = new JMenu ("HELP");
        menubar.add(m2);
        JMenuItem info = new JMenuItem("INSTRUCTIONS");
        info.setToolTipText("Click to read the instructions of the game");
        info.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                JOptionPane.showMessageDialog(null,"enter instructions here","INSTRUCTIONS",JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(null,"enter instructions here","INSTRUCTIONS",JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(null,"YOU CAN CUSTOMIZE THE GAME USING THE OPTIONS MENU","INSTRUCTIONS",JOptionPane.INFORMATION_MESSAGE); //telling the user the instructions
            }
        }
        );
        m2.add(info);
        
        JMenu m3 =new JMenu("EXIT");
        menubar.add(m3);
        JMenuItem exitm = new JMenuItem("Click to exit the game :(");
        exitm.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0); // exitting the program
            }
        }
        );
        m3.add(exitm);
        
        JMenuItem setmtColour = new JMenuItem("CHANGE MENU BAR TEXT COLOUR");
        setmtColour.setToolTipText("Click to change the menu bar text colour");
        setmtColour.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                Color color=(Color.WHITE);
                color = JColorChooser.showDialog(null,"Pick Colour",color);
                if(color==null)
                color=color;
                m0.setForeground(color);
                m1.setForeground(color);
                m2.setForeground(color);
                m3.setForeground(color); //setting the foreground color of all the menus
            }
        }
        );
        background = ImageLoader.loadImage("assets/background.png");
        
	}
	
	/**
	 * Gets the offset value for the background in the main menu.
	 * @return the offset
	 */
	public double getBgScroll() {
		return bgScroll;
	}
	
	public void update(Graphics g) {
		paint(g);
		
	}
	 public void paint(Graphics g) {
		 //ImageLoader.drawRepeatingImage(g, background, obj.getWidth() / 2, obj.getHeight() / 2, 0, obj.getWidth(), obj.getHeight());
		 if(!inLevel) bgScroll += 70 * resetUpdateTimer();
		 if(Math.round(bgScroll) > background.getWidth()) bgScroll = 0;
		 paintComponents(g);
		 g.dispose();
		 repaint();
	}
	 
	/**
	 * Restarts the update timer. The next call to resetUpdateTimer will be measured relative to when this function was executed.
	 */
	public void restartUpdateTimer() {
		lastUpdateTime = -1;
	}
	
	/**
	 * Gets the time since this function or restartUpdateTimer was last called.
	 * @return the time in seconds
	 */
	public double resetUpdateTimer() {
		if(lastUpdateTime == -1) {
			lastUpdateTime = System.currentTimeMillis();
			return 0;
		}
		long current = System.currentTimeMillis();
		double deltaTime = (System.currentTimeMillis() - lastUpdateTime) / 1000.0;
		lastUpdateTime = current;
		return deltaTime;
	}
	 
	 /**
	  * Kill the level panel if something goes wrong.
	  */
	public void escapeLevel(Exception e) {
		
	}
	
	/**
	 * Gets the offset value to translate all objects in the level by to keep up with the player.
	 * @return the offset in pixels
	 */
	public int getCameraOffset() {
		if(!inLevel) return 0;
		int playerX = levelPanel.getPlayerX();
		int width = obj.getWidth();
		if(playerX < PLAYER_LEFT_OFFSET) return 0;
		if(levelPanel.getLevelWidth() - playerX < width - PLAYER_LEFT_OFFSET) return width - levelPanel.getLevelWidth();
		return PLAYER_LEFT_OFFSET - playerX;
		
	}
	
	/**
	 * Returns whether the game is in dev mode. This means that the player can switch gravity in mid-air.
	 * @return whether the game is in dev mode
	 */
	public boolean inDevMode() {
		return devMode;
	}
	
//	public class drawStage extends JComponent{
//		public drawStage() {
//			setPreferredSize(getMaximumSize());
//		}
//		
//		@Override
//		public void paintComponent(Graphics g){
//			super.paintComponents(g);
//			//for(long i=0;i<10000000l;i++);
//			g.setColor(Color.WHITE);
//			g.fillRect(0, 0, getWidth(), getHeight());
//			stage.draw(g);
//			
//			//After the code executes
//			g.dispose();
//			repaint();
//		}
//	}
	
	/**
	 * The main method. Starts the game.
	 */
	public static void main(String[] args) {
		obj.setPreferredSize(new Dimension(1200, 800));
		obj.setMaximumSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, 800));
		obj.setMinimumSize(new Dimension(800, 800));
		obj.setSize(new Dimension(1200, 800));
		welcomePanel = new JPanel(new GridBagLayout()) {
			@Override
			public void paint(Graphics g) {
				ImageLoader.drawBackground(g, (int) Math.round(obj.getBgScroll()) * -10);
				super.paint(g);
			}
		};
		welcomePanel.setBackground(new Color(0,0,0,0));
		obj.setBackground(Color.CYAN);
		welcomePanel.setVisible(true);
		
		gamePanel = new JPanel(null); //setting the layout
		
		endGamePanel = new JPanel(null);
		endGamePanel.setBackground(Color.RED);
		
		//levelPanel = new LevelPanel();
		
		gamePanel.setBackground(Color.RED);
		
		//gamePanel.add(new drawStage());
		//gamePanel.paintComponent(drawStage());;
      //  gamePanel.add(stage);
		Font vermin = new Font("Freestyle Script",Font.PLAIN,50);
		try {
			vermin = Font.createFont(Font.TRUETYPE_FONT, classLoader.getResourceAsStream("assets/vermin_vibes.ttf")).deriveFont(80f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		} catch (IOException|FontFormatException e) {
			e.printStackTrace();
		}
		
		welcomeMessage=new JLabel("GRAVITATE");
		welcomeMessage.setFont(vermin);
		gbc.gridx=0;
		gbc.gridy=0;
		welcomePanel.add(welcomeMessage,gbc);
		
		levelChooser = new levelChooser(obj);
		levelChooser.setBounds(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
		
		startButton = new JButton("Play");
        startButton.setBackground(Color.GREEN);
        startButton.setEnabled(true);
        startButton.setToolTipText("Click to begin");
        gbc.gridx = 0;
        gbc.gridy = 10;
        welcomePanel.add(startButton,gbc); //creating the start button
        gbc.gridx = 0;
        gbc.gridy = 50;
        customLevelButton = new JButton("Custom Levels");
        customLevelButton.setBackground(Color.GREEN);
        customLevelButton.setEnabled(true);
        welcomePanel.add(customLevelButton,gbc);
        gbc.gridx = 0;
        gbc.gridy = 90;
        quitButton = new JButton("Quit");
        quitButton.setBackground(Color.GREEN);
        quitButton.setEnabled(true);
        welcomePanel.add(quitButton,gbc);

		//levelPanel.setBounds(0, 0, obj.getWidth(), obj.getHeight());
		levelPanel.setBackground(Color.BLUE);
		levelPanel.setVisible(false);
        
        startButton.addActionListener(new ActionListener() //adding functionality to the start button
        {
            public void actionPerformed(ActionEvent event)
            {	
            
                //Toolkit.getDefaultToolkit().beep(); //to tell the user the button has been pressed
                obj.remove(welcomePanel);
                obj.add(levelChooser);
                levelChooser.setVisible(true);
                levelChooser.setFocusable(true);
                obj.setVisible(true); //switching the home panel with the p1 panel
                
            }
        }
        );
        customLevelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gotoCustomLevels();
				
			}
		});
        quitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		GameMain instance = new GameMain();
        
        obj.setTitle("Gravitate");
		obj.setFocusable(true);
		obj.addKeyListener(new KeyListener() {
			
			List<Integer> keysPressed = new ArrayList<Integer>();
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				keysPressed.remove(new Integer(e.getKeyCode()));
			}
			
			@Override
			public void keyPressed(KeyEvent e) { 
				if(!keysPressed.contains(new Integer(e.getKeyCode()))) {
					keysPressed.add(new Integer(e.getKeyCode()));
					if(e.getKeyCode() == KeyEvent.VK_F11) {
						obj.setExtendedState(obj.getExtendedState() == JFrame.NORMAL ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
						return;
					}
					if(obj.inLevel) levelPanel.keyPress(e);
				}
				if(keysPressed.contains(new Integer(KeyEvent.VK_CONTROL)) && keysPressed.contains(new Integer(KeyEvent.VK_SHIFT)) && keysPressed.contains(new Integer(KeyEvent.VK_D)))  {
					devMode = !devMode;
					System.out.println("Dev mode is " + (devMode ? "on!" : "off!"));
				}
			}
		});
		
		obj.addWindowStateListener(new WindowStateListener() {
			
			@Override
			public void windowStateChanged(WindowEvent e) {
				switch(e.getNewState()) {
					case JFrame.NORMAL:
						obj.dispose();
						obj.setUndecorated(false);
						obj.pack();
						Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
						obj.setBounds(screenSize.width / 2 - obj.getWidth() / 2, screenSize.height / 2 - obj.getHeight() / 2, obj.getWidth(), obj.getHeight());
						obj.setVisible(true);
						break;
					case JFrame.MAXIMIZED_BOTH:
						obj.dispose();
						obj.setUndecorated(true);
						obj.setVisible(true);
						break;
				}
				
			}
		});
		
		obj.setVisible(true);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//obj.setExtendedState(JFrame.MAXIMIZED_BOTH);
		obj.add(welcomePanel,BorderLayout.CENTER);
		int verticalInsets = obj.getInsets().top;
		obj.setPreferredSize(new Dimension(1200, 800 + verticalInsets + 21));
		obj.setMaximumSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, 800 + verticalInsets + 21));
		obj.setMinimumSize(new Dimension(800, 800 + verticalInsets + 21));
		obj.setSize(new Dimension(1200, 800 + verticalInsets + 21));
		obj.setResizable(false);
		ImageLoader.setBackgroundSize(2 * obj.background.getWidth() * 10);
        try {
			obj.audio = new AudioManager("assets/Bit Quest.wav");
	        //audio.play();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException | NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes the level chooser panel and starts the level.
	 * @param levelName the file name of the level without the extension
	 */
	public void loadLevel(String levelName){
		levelIsCustom = false;
		levelPanel = new LevelPanel(this);
		levelPanel.setBounds(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
		levelPanel.setVisible(false);
		
		levelPanel.load(levelName);
		levelPanel.setLayout(null);
		levelPanel.setVisible(true);
		levelPanel.setFocusable(true);
        obj.inLevel = true; 
        obj.remove(levelChooser);
        obj.add(levelPanel);
        try {
        	audio.stop();
			audio = new AudioManager("assets/Kick Shock.wav");
			audio.play();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException | NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        levelPanel.start();
	}
	
	/**
	 * Removes the level chooser panel and starts the level. Loads from the custom levels folder.
	 * @param levelName the file name of the level without the extension
	 */
	public void loadCustomLevel(String levelName) {
		levelIsCustom = true;
		levelPanel = new LevelPanel(this);
		levelPanel.setBounds(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
		levelPanel.setVisible(false);
		
		levelPanel.loadCustom(levelName);
		levelPanel.setLayout(null);
		levelPanel.setVisible(true);
		levelPanel.setFocusable(true);
        obj.inLevel = true; 
        obj.remove(customLevels);
        obj.add(levelPanel);
        try {
        	audio.stop();
			audio = new AudioManager("assets/Kick Shock.wav");
			audio.play();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException | NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        levelPanel.start();
	}
	
	/**
	 * Sets the level background color. This inly displays if the background image is missing.
	 * @param color the color to display
	 */
	public void loadLevelColor(Color color){
		levelPanel.setBackground(color);
	}
	
	/**
	 * Closes the level and opens the level chooser. It will open the standard level chooser or the custom level chooser depending on the level being exited.
	 */
	public void gotoLevelChooser() {
        try {
        	System.out.println("foo");
        	audio.stop();
			audio = new AudioManager("assets/Bit Quest.wav");
	        audio.play();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException | NullPointerException e) {
			e.printStackTrace();
		}
		ImageLoader.setBackgroundSize(2 * obj.background.getWidth() * 10);
		restartUpdateTimer();
		obj.remove(levelPanel);
		obj.inLevel = false;
		if(levelIsCustom) {
			customLevels = new CustomLevelChooser(this);
			obj.add(customLevels);
	        customLevels.setVisible(true);
	        customLevels.setFocusable(true);
			
		}
		else {
			levelChooser = new levelChooser(this);
			obj.add(levelChooser);
	        levelChooser.setVisible(true);
	        levelChooser.setFocusable(true);
		}
        obj.setVisible(true);
	}
	
	/**
	 * Opens the custom level chooser.
	 */
	public static void gotoCustomLevels() {
		obj.remove(welcomePanel);
		customLevels = new CustomLevelChooser(obj);
		obj.add(customLevels);
		customLevels.setVisible(true);
		customLevels.setFocusable(true);
		
	}
	/**
	 * Reloads the custom level chooser, causing any changes in the custom levels folder to appear.
	 */
	public void refreshCustomLevels() {
		obj.remove(customLevels);
		customLevels = new CustomLevelChooser(obj);
		obj.add(customLevels);
		customLevels.setVisible(true);
		customLevels.setFocusable(true);
	}
	
	/**
	 * Closes whichever level chooser is open and opens the main menu screen.
	 */
	public void returnToMenu() {
		obj.getContentPane().removeAll();
		inLevel = false;
		obj.add(welcomePanel);
		welcomePanel.setVisible(true);
		welcomePanel.setFocusable(true);
	}
	
	/**
	 * Displays a pop-up box for the player to either return to the level chooser or quit. Not in use.
	 */
	public void displayEndGame() {
		
		obj.remove(levelPanel);
        obj.add(endGamePanel);
        endGamePanel.setVisible(true);
        endGamePanel.setFocusable(true);
     //   JOptionPane.showMessageDialog(null, "Yer DeaD");
        String playAgain=JOptionPane.showInputDialog("PLay Again? Y/N");
        if(playAgain.equalsIgnoreCase("N")){
        	JOptionPane.showMessageDialog(null, "Thanks for playing :)");
        	System.exit(0);
        }
        else if(playAgain.equalsIgnoreCase("Y")){
        	System.out.println("dfgdsf");
        	levelChooser = new levelChooser(obj);
            
        	obj.remove(endGamePanel);
        	obj.add(levelChooser);
        	levelChooser.setVisible(true);
            levelChooser.setFocusable(true);
        	
        	obj.setVisible(true); 
        }
	}
}
