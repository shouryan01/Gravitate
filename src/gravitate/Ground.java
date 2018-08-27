package gravitate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Observer;

import gravitate.util.ImageLoader;

public class Ground extends Terrain {
	private BufferedImage texture;

	public Ground(int x, int y, int width, int height) {
		super(x, y, width, height, 0, true);
		texture = ImageLoader.loadImage("assets/ground.png");
	}

	@Override
	public void draw(Graphics g, int offset) {
		g.setColor(Color.GRAY);
		g.fillRect(getX() - getWidth() / 2 + offset, getY() - getHeight() / 2, getWidth(), getHeight());
		ImageLoader.drawRepeatingImage(g, texture, getX(), getY(), offset, getWidth(), getHeight());
	}

	@Override
	public void update(double time) {
		//Do nothing; this is just a chunk of dirt.
	}

}
