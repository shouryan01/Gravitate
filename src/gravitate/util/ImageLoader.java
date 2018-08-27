package gravitate.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import gravitate.GameMain;

public class ImageLoader {
	private static GameMain frame;
	private static BufferedImage bg, bgCap;
	private static final double BG_SCROLL_FACTOR = 10.0;
	private static int bgIterations, bgWidth, bgHeight, bgCapWidth, bgCapHeight;
	private static ImageObserver bgObserver;
	private static ClassLoader classLoader;
	
	/**
	 * Loads in files and initializes variables. Call once when the game starts.
	 * @param instance the main class.
	 */
	public static void init(GameMain instance) {
		classLoader = Thread.currentThread().getContextClassLoader();
		frame = instance;
		bg = loadImage("assets/background.png");
		bgObserver = new ImageObserver() {
			
			@Override
			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}
	
	/**
	 * Loads a still image
	 * @param path the path to the image. Usually "assets/filename"
	 * @return the image
	 */
	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(classLoader.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
			//System.exit(0);
		}
		return null;
	}
	
	/**
	 * Loads an animated GIF
	 * @param path filename of the image.
	 * @return an ImageIcon of the GIF
	 */
	public static ImageIcon loadGif(String path) {
		return new ImageIcon(classLoader.getResource("assets/" + path));
	}
	
	/**
	 * Draws a single copy of an image. Useful for sprites.
	 * @param g the Graphics object
	 * @param texture the image to draw
	 * @param x the x coordinate of the center
	 * @param y the y coordinate of the center
	 * @param camOffset the camera offset
	 * @param width the object's width
	 * @param height the object's height
	 */
	public static void drawImage(Graphics g, Image texture, int x, int y, int camOffset, int width, int height) {
		g.drawImage(texture, x - width / 2 + camOffset, y - height / 2, width, height, new ImageObserver() {
			@Override
			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
	
	/**
	 * Sets up the background for a level. Call once when the level is loaded.
	 * @param width the width of the level in pixels
	 */
	public static void setBackgroundSize(int width) {
		int levelBgWidth = (int) ((width - frame.obj.getWidth()) / BG_SCROLL_FACTOR) + frame.obj.getWidth();
		bgIterations = (int) (levelBgWidth / bg.getWidth());
		int extra = levelBgWidth - bgIterations * bg.getWidth();
		//if(bgIterations != 0) extra = (levelBgWidth + frame.obj.getWidth()) % (bgIterations * bg.getWidth());
		//else extra = levelBgWidth + frame.obj.getWidth();
		bgCap = bg.getSubimage(0, 0, extra, bg.getHeight());
		bgWidth = bg.getWidth();
		bgHeight = bg.getHeight();
		bgCapWidth = bgCap.getWidth();
		bgCapHeight = bgCap.getHeight();
	}
	
	/**
	 * Draws the background
	 * @param g the Graphics object
	 * @param offset the camera offset
	 */
	public static void drawBackground(Graphics g, int offset) {
		int bgOffset = (int) (offset / BG_SCROLL_FACTOR);
		for(int i = 0; i < bgIterations; i++) {
			if(bgOffset <= -(i * bgWidth - frame.obj.getWidth()) && bgOffset >= -((i + 1) * bgWidth)) g.drawImage(bg, i * bgWidth + bgOffset, 0, bgWidth, bgHeight, bgObserver);
		}
		if(bgOffset <= -(bgIterations * bgWidth - frame.obj.getWidth())) {
			g.drawImage(bgCap, bgIterations * bgWidth + bgOffset, 0, bgCapWidth, bgCapHeight, bgObserver);
		}
		
	}
	
	/**
	 * Draws an image that repeats horizontally and vertically. Useful for blocks.
	 * @param g the Graphics object
	 * @param texture the image to draw
	 * @param x the x coordinate of the center
	 * @param y the y coordinate of the center
	 * @param camOffset the camera offset
	 * @param width the width of the object
	 * @param height the height of the object
	 */
	public static void drawRepeatingImage(Graphics g, BufferedImage texture, int x, int y, int camOffset, int width, int height) {
		int xOffset = 0, yOffset = 0;
		BufferedImage subTexture;
		
		for(xOffset = 0; xOffset + texture.getWidth() <= width; xOffset += texture.getWidth()) {
			for(yOffset = 0; yOffset + texture.getHeight() <= height; yOffset += texture.getHeight()) {
				g.drawImage(texture, x - width / 2 + xOffset + camOffset, y - height / 2 + yOffset, texture.getWidth(), texture.getHeight(), new ImageObserver() {
					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						// TODO Auto-generated method stub
						return false;
					}
				});
			}
		}
		if(xOffset == 0) yOffset = height - height % texture.getHeight();
		//System.out.println("w " + width + "\theight " + height + "\txOffset " + xOffset + "\tyOffset " + yOffset);

		if(height != yOffset) {
			subTexture = texture.getSubimage(0, 0, texture.getWidth(), height - yOffset);
			for(int i = 0; i + subTexture.getWidth() <= width; i += subTexture.getWidth())
				g.drawImage(subTexture, x - width / 2 + i + camOffset, y - height / 2 + yOffset, subTexture.getWidth(), subTexture.getHeight(), new ImageObserver() {
					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						// TODO Auto-generated method stub
						return false;
					}
				});
		}
		
		if(width != xOffset) {
			subTexture = texture.getSubimage(0, 0, width - xOffset, texture.getHeight());
			for(int i = 0; i + subTexture.getHeight() <= height; i += subTexture.getHeight()) {
				g.drawImage(subTexture, x - width / 2 + xOffset + camOffset, y - height / 2 + i, subTexture.getWidth(), subTexture.getHeight(), new ImageObserver() {
					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						// TODO Auto-generated method stub
						return false;
					}
				});
			}
		}
		
		if(width != xOffset && height != yOffset) {
			subTexture = texture.getSubimage(0, 0, width - xOffset, height - yOffset);
			g.drawImage(subTexture, x - width / 2 + xOffset + camOffset, y - height / 2 + yOffset, subTexture.getWidth(), subTexture.getHeight(), new ImageObserver() {
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					// TODO Auto-generated method stub
					return false;
				}
			});
		}
	}

}
