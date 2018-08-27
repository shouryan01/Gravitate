package gravitate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gravitate.util.ImageLoader;

public class Wall extends Terrain {
	private BufferedImage texture;

	public Wall(int x, int y, int width, int height) {
		super(x, y, width, height, 0, false);
		texture = ImageLoader.loadImage("assets/wall.png");
	}

	@Override
	public void draw(Graphics g, int offset) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(getX() - getWidth() / 2 + offset, getY() - getHeight() / 2, getWidth(), getHeight());
		ImageLoader.drawRepeatingImage(g, texture, getX(), getY(), offset, getWidth(), getHeight());
	}

	@Override
	public void update(double time) {
		//Do nothing; this is just a chunk of dirt in the background.
	}

}
