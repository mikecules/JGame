/**
 * 
 */
package ca.darkfusion.firstGame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import ca.darkfusion.firstGame.graphics.Screen;
import ca.darkfusion.firstGame.input.Keyboard;

/**
 * @author moncadam
 *
 */
public class Game extends Canvas implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5020628576320886263L;
	public static final int width = 300;
	public static final int height = width / 16 * 9;
	public static final int scale = 3;
	private static final int numberOfBuffers = 3;
	private static final String GAME_TITLE = "HERRO Game!";
	
	private Thread displayThread;
	private JFrame frame;
	private boolean gameIsRunning = false;
	private Keyboard keys;
	
	private int x = 0;
	private int y = 0;
	
	// create image
	private BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
	// translate image created to a int array 
	private int[] pixels =  ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
	
	private Screen screen;
	
	public Game(){
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		frame = new JFrame();
		screen = new Screen(width, height);
		
		keys = new Keyboard();
		addKeyListener(keys);
	}
	
	public synchronized void start() {
		gameIsRunning = true;
		displayThread = new Thread(this, "Display");
		displayThread.start();
	}
	
	public synchronized void stop() {
		
		gameIsRunning = false;
		
		try {
			displayThread.join();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

	@Override
	public void run() {
		
		long lastTimeInNS = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0.0;
		int frames = 0;
		int updates = 0;
		long timer = System.currentTimeMillis();
		
		while (gameIsRunning) {
			long nowInNS = System.nanoTime();
						
			delta += (nowInNS - lastTimeInNS) / ns;
			lastTimeInNS = nowInNS;
			
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			
			render();
			frames++;
			
			long currentMS = System.currentTimeMillis();
			
			if ((currentMS - timer) > 1000) {
				timer = currentMS;
				frame.setTitle(GAME_TITLE + " FPS: " + frames + " Updates: " + updates);
				frames = 0;
				updates = 0;
			}
		}
		
		stop();
		
		System.out.println("Goodbye world!");
	}
	
	// update method
	public void update() {
		keys.update();
		
		if (keys.up) {
			y--;
		} 
		else if (keys.down) {
			y++;
		}
		
		if (keys.left) {
			x--;
		}
		else if (keys.right) {
			x++;
		}
	}
	
	public void render() {
		BufferStrategy bufferStrategy = getBufferStrategy();
		
		if (bufferStrategy == null) {
			createBufferStrategy(numberOfBuffers);
			return;
		}
		
		screen.clear();
		screen.render(x, y);
		
		for (int i = 0; i < pixels.length; i++) {
			 pixels[i] = screen.pixels[i];
		}
		
		Graphics gContext = bufferStrategy.getDrawGraphics();
		//gContext.setColor(Color.BLACK);
		
		// erase the buffer by filling it with black
		//gContext.fillRect(0, 0, getWidth(), getHeight());
		
		// draw the image
		gContext.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);
		
		// free the context display resources
		gContext.dispose();
		
		// show the buffer
		bufferStrategy.show();
	}
	
	public static void main(String[] args) {
		Game myGame = new Game();
		
		myGame.frame.setResizable(false);
		myGame.frame.setTitle(GAME_TITLE);
		myGame.frame.add(myGame);
		myGame.frame.pack();
		myGame.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myGame.frame.setLocationRelativeTo(null);
		myGame.frame.setVisible(true);
		
		myGame.start();
	}
}
