package ca.darkfusion.firstGame.graphics;

public class Sprite {
	public final int SIZE;
	private int originX, originY;
	public int[] pixels;
	private SpriteSheet spriteSheet;
	public static Sprite grass = new Sprite(16, 0, 0, SpriteSheet.tiles);
	
	public Sprite(int size, int x, int y, SpriteSheet spriteSheet) {
		this.SIZE = size;
		this.pixels = new int[size * size];
		this.originX = x * size;
		this.originY = y * size;
		this.spriteSheet = spriteSheet;	
		
		load();
	}
	
	private void load() {
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				 pixels[x + (y * SIZE)] = spriteSheet.pixels[(originX + x) + ( (originY + y) * spriteSheet.SIZE )];
			}
		}
	}
}
