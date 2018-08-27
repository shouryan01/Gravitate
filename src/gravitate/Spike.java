package gravitate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gravitate.util.ImageLoader;

public class Spike extends Terrain {
	BufferedImage texture;

	public Spike(int x, int y, int width, int height) {
		super(x, y, width, height, 1, true);
		texture = ImageLoader.loadImage("assets/spike.png");
	}

	@Override
	public void draw(Graphics g, int offset) {
		g.setColor(Color.RED);
		g.fillRect(getX() - getWidth() / 2 + offset, getY() - getHeight() / 2, getWidth(), getHeight());
		ImageLoader.drawRepeatingImage(g, texture, getX(), getY(), offset, getWidth(), getHeight());

	}

	@Override
	public void update(double time) {
		//Do nothing.
	}

}
