package gravitate.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import gravitate.GameMain;
import gravitate.util.ImageLoader;

public class CustomLevelChooser extends JPanel {
	private File customLevels;
	private File[] files;
	private GameMain instance;
	private JPanel levels;
	private JScrollPane levelPane;
	private JButton returnToMenu, refresh, openLevelBuilder, openFolder;

	public CustomLevelChooser(GameMain instance) {
		setLayout(new BorderLayout());
		setBackground(new Color(0,0,0,0));
		this.instance = instance;
		customLevels = new File("./customLevels");
		if(!customLevels.exists()) customLevels = new File("../customLevels");
		files = customLevels.listFiles();
		levels = new JPanel();
		int buttonsHeight = 0;
		if(files != null) {
			for(File f : files) {
				String extension = f.getName().substring(f.getName().lastIndexOf('.') + 1).toLowerCase();
				if(extension.equals("level") || extension.equals("zip")) {
					JButton btn = new JButton(f.getName().substring(0, f.getName().lastIndexOf('.')));
					btn.setPreferredSize(new Dimension(400,100));
					btn.setBorder(new EmptyBorder(0, 0, 0, 0));
					btn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							instance.loadCustomLevel(btn.getText());
						}
					});
					levels.add(btn);
					buttonsHeight += 105;
				}
			}
		}
		levels.setBackground(Color.GRAY);
		levels.setPreferredSize(new Dimension(400, buttonsHeight));
		levels.setVisible(true);
		levelPane = new JScrollPane(levels);
		//levelPane.setPreferredSize(new Dimension(410,700));
		levelPane.setBackground(new Color(0,0,0,0));
		levelPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		levelPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		levelPane.setBorder(new EmptyBorder(15, 100, 15, 15));
		add(levelPane, BorderLayout.WEST);
		
		JPanel buttonsPane = new JPanel();
		buttonsPane.setBackground(new Color(0,0,0,0));
		
		refresh = new JButton("Refresh Levels");
		refresh.setPreferredSize(new Dimension(200, 50));
		refresh.setBorder(new EmptyBorder(0, 0, 20, 0));
		refresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				instance.refreshCustomLevels();
				
			}
		});
		buttonsPane.add(refresh);
		
		openLevelBuilder = new JButton("Open Level Builder");
		openLevelBuilder.setPreferredSize(new Dimension(200, 50));
		openLevelBuilder.setBorder(new EmptyBorder(0, 0, 20, 0));
		openLevelBuilder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Process levelBuilder;
					File path = new File("./LevelBuilder/LevelBuilder.exe");
					if(path.exists()) Runtime.getRuntime().exec("cmd /c start ./LevelBuilder/LevelBuilder.exe");
					else  Runtime.getRuntime().exec("cmd /c start ../LevelBuilder/LevelBuilder.exe");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		buttonsPane.add(openLevelBuilder);
		
		openFolder = new JButton("Open Custom Levels Folder");
		openFolder.setPreferredSize(new Dimension(200, 50));
		openFolder.setBorder(new EmptyBorder(0, 0, 20, 0));
		openFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Runtime.getRuntime().exec("explorer.exe /open," + customLevels.getAbsolutePath());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		buttonsPane.add(openFolder);
		
		returnToMenu = new JButton("Return to Menu");
		returnToMenu.setPreferredSize(new Dimension(200, 50));
		returnToMenu.setBorder(new EmptyBorder(0, 0, 20, 0));
		returnToMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				instance.returnToMenu();
				
			}
		});
		buttonsPane.add(returnToMenu);
		
		buttonsPane.setBorder(new EmptyBorder(15, 0, 0, 0));
		buttonsPane.setPreferredSize(new Dimension(200, 700));
		add(buttonsPane);
		
		
	}
	
	@Override
	public void paint(Graphics g) {
		ImageLoader.drawBackground(g, (int) Math.round(instance.getBgScroll()) * -10);
		super.paint(g);
	}

}
