package ca.darkfusion.firstGame.graphics;

import java.util.Random;

public class Screen {
	private int width, height;
	public int[] pixels;
    public final int TILE_SIZE = 64;
    public final int TILE_SIZE_MASK = TILE_SIZE - 1; 
	public int[] tiles = new int[ TILE_SIZE * TILE_SIZE ];
	private Random random = new Random();
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
		
		for (int i = 0; i < (TILE_SIZE * TILE_SIZE); i++) {
			tiles[i] = random.nextInt(0xffffff);
		}
		
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	public void render(int xOffset, int yOffset) {
		
		for (int y = 0; y < height; y++) {
			
			int yy = y + yOffset;
			
			if (yy < 0 || yy >= height) {
				//break;
			}
			
			
			for (int x = 0; x < width; x++) {
				int xx = x + xOffset;
				
				if (xx < 0 || xx >= width) {
					//break;
				}
				
				int tileIndex = (xx >> 4 & TILE_SIZE_MASK) + (yy >> 4 & TILE_SIZE_MASK) * TILE_SIZE;
				
				int pixelIndex = x + (y * width);
				pixels[pixelIndex] = tiles[tileIndex];
			}
		}
	}
}
