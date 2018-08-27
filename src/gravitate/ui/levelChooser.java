package gravitate.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import gravitate.GameMain;
import gravitate.util.ImageLoader;

public class levelChooser extends JPanel {
	GridLayout  gl = new GridLayout( 2, 3,100,100);
	static JButton level1button,level2button,level3button,level4button,level5button,level6button;
	GameMain instance;
	public levelChooser(GameMain instance){
		this.instance=instance;
		setBackground(new Color(0,0,0,0));
		setLayout(gl);
		//setBackground(new GradientPaint(0, 0, Color.RED, getWidth(), getHeight(), Color.BLUE) );
		JButton level1button = new JButton("Level 1");
		JButton level2button = new JButton("Level 2");
		JButton level3button = new JButton("Level 3");
		JButton level4button = new JButton("Level 4");
		JButton level5button = new JButton("Level 5");
		JButton level6button = new JButton("Level 6");
		JButton backButton = new JButton("Return to Menu");
		backButton.setBackground(Color.LIGHT_GRAY);
		this.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
		
		add(level1button);
		add(level2button);
		add(level3button);
		add(level4button);
		add(level5button);
		//add(level6button);
		add(backButton);
		
		level1button.addActionListener(new ActionListener() //adding functionality to the start button
		        {
		            public void actionPerformed(ActionEvent event)
		            {	
		            
		                //Toolkit.getDefaultToolkit().beep(); //to tell the user the button has been pressed
		                instance.loadLevel("Gravitate Level 1");
		             }
		        }
		        );
		
		level2button.addActionListener(new ActionListener() //adding functionality to the start button
		        {
		            public void actionPerformed(ActionEvent event)
		            {	
		            
		               // Toolkit.getDefaultToolkit().beep(); //to tell the user the button has been pressed
		                instance.loadLevel("Gravitate Level 2");
		             }
		        }
		        );
		level3button.addActionListener(new ActionListener() //adding functionality to the start button
		        {
		            public void actionPerformed(ActionEvent event)
		            {	
		            
		                //Toolkit.getDefaultToolkit().beep(); //to tell the user the button has been pressed
		                instance.loadLevel("Gravitate Level 3");
		             }
		        }
		        );
		level4button.addActionListener(new ActionListener() //adding functionality to the start button
		        {
		            public void actionPerformed(ActionEvent event)
		            {	
		            
		                //Toolkit.getDefaultToolkit().beep(); //to tell the user the button has been pressed
		                instance.loadLevel("FINALLVL4");
		             }
		        }
		        );
		level5button.addActionListener(new ActionListener() //adding functionality to the start button
		        {
		            public void actionPerformed(ActionEvent event)
		            {	
		            
		               // Toolkit.getDefaultToolkit().beep(); //to tell the user the button has been pressed
		                instance.loadLevel("level5");
		             }
		        }
		        );
		level6button.addActionListener(new ActionListener() //adding functionality to the start button
		        {
		            public void actionPerformed(ActionEvent event)
		            {	
		            
		                //Toolkit.getDefaultToolkit().beep(); //to tell the user the button has been pressed
		                instance.loadLevel("levelTest3");
		             }
		        }
		        );
		backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				instance.returnToMenu();
				
			}
		});
		
		
		
		
	}
	
	@Override
	public void paint(Graphics g) {
		ImageLoader.drawBackground(g, (int) Math.round(instance.getBgScroll()) * -10);
		super.paint(g);
	}
	
	
}
