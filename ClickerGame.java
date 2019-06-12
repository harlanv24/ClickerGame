package clickerGame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JFrame;

public class ClickerGame extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "Candy Clicker";
	private int candies = 0;
	private int numHelpers = 0;
	private int numFarms = 0;
	private int numMines = 0;
	private int numCorporations = 0;
	private int numEmpires = 0;
	private boolean clickFactoryStatus = false;
	private int candiesPerSecond = 0;
	private boolean wonGame = false;
	
	private boolean running = false;
	private Thread thread;
	
	
	private BufferedImage spriteSheet = null;
	private BufferedImage background;
	private BufferedImage candy;
	private BufferedImage candyHelper;
	private BufferedImage candyFarm;
	private BufferedImage candyMine;
	private BufferedImage clickFactory;
	private BufferedImage candyCorporation;
	private BufferedImage candyEmpire;
	
	  public ClickerGame() {
	      addMouseListener(new MouseAdapter() { 
	          public void mousePressed(MouseEvent e) { 
	      		int[] candyX = {200, 200, 235, 235, 260, 300, 330, 360, 360, 330, 310, 270, 260};
	    		int[] candyY = {270, 240, 240, 200, 180, 180, 145, 175, 210, 235, 270, 285, 315};
	    		Polygon candyHitbox = new Polygon(candyX, candyY, 13);
	    		if(candyHitbox.contains(e.getPoint()) && isClickFactory()) {
	    			candies+=10;
	    		}
	    		else if(candyHitbox.contains(e.getPoint())){
	    			candies++;
	    		}
	          }
	        }
	      );
          addMouseListener(new MouseAdapter() {
        	  public void mousePressed(MouseEvent e) { 
        		  int[] buttonX = {500, 570, 635, 570};
        		  int[] helperY = {70, 45, 70, 95};
        		  Polygon helperHitbox = new Polygon(buttonX, helperY, 4);
        		  if(helperHitbox.contains(e.getPoint()) && candies > 9) {
        			  candies-= 10;
        			  numHelpers++;
        			  }
        	  }
          });
          addMouseListener(new MouseAdapter() {
        	  public void mousePressed(MouseEvent e) { 
        		  int[] buttonX = {500, 570, 635, 570};
        		  int[] farmY = {140, 115, 140, 165};
        		  Polygon farmHitbox = new Polygon(buttonX, farmY, 4);
        		  if(farmHitbox.contains(e.getPoint()) && candies > 99) {
        			  candies-= 100;
        			  numFarms++;
        			  }
        	  }
          });
          addMouseListener(new MouseAdapter() {
        	  public void mousePressed(MouseEvent e) { 
        		  int[] buttonX = {500, 570, 635, 570};
        		  int[] mineY = {210, 185, 210, 235};
        		  Polygon mineHitbox = new Polygon(buttonX, mineY, 4);
        		  if(mineHitbox.contains(e.getPoint()) && candies > 2499) {
        			  candies-= 2500;
        			  numMines++;
        			  }
        	  }
          });
          addMouseListener(new MouseAdapter() {
        	  public void mousePressed(MouseEvent e) { 
        		  int[] buttonX = {500, 570, 635, 570};
        		  int[] corporationsY = {280, 255, 280, 305};
        		  Polygon corporationsHitbox = new Polygon(buttonX, corporationsY, 4);
        		  if(corporationsHitbox.contains(e.getPoint()) && candies > 49999) {
        			  candies-= 50000;
        			  numCorporations++;
        			  }
        	  }
          });
          addMouseListener(new MouseAdapter() {
        	  public void mousePressed(MouseEvent e) { 
        		  int[] buttonX = {500, 570, 635, 570};
        		  int[] empiresY = {350, 325, 350, 375};
        		  Polygon empireHitbox = new Polygon(buttonX, empiresY, 4);
        		  if(empireHitbox.contains(e.getPoint()) && candies > 499999) {
        			  candies-= 500000;
        			  numEmpires++;
        			  }
        	  }
          });
          addMouseListener(new MouseAdapter() {
        	  public void mousePressed(MouseEvent e) { 
        		  int[] buttonX = {500, 570, 635, 570};
        		  int[] clickFactoryY = {420, 395, 420, 445};
        		  Polygon clickFactoryHitbox = new Polygon(buttonX, clickFactoryY, 4);
        		  if(clickFactoryHitbox.contains(e.getPoint()) && candies > 2499 && !isClickFactory()) {
        			  candies-= 2500;
        			  setClickFactory(true);
        			  }
        	  }
          });
	  }
	  

	  public boolean isWonGame() {
		  return wonGame;
		  }

	  public boolean setWonGame(boolean wonGame) {
		  this.wonGame = wonGame;
		  return wonGame;
		  }

	  public boolean isClickFactory() {
		  return clickFactoryStatus;
		  }
	  
	  public boolean setClickFactory(boolean clickFactoryStatus) {
		  this.clickFactoryStatus = clickFactoryStatus;
		  return clickFactoryStatus;
		  }
	  
	public void update() {
		candiesPerSecond = 2*numHelpers + 5*numFarms + 25*numMines + 1000*numCorporations + 7500*numEmpires;
	}
	
	public void init() {
		BufferedImageLoader loader = new BufferedImageLoader();
		try {	
			spriteSheet = loader.loadImage("sprite_sheet.png");
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		SpriteSheet ss = new SpriteSheet(spriteSheet);
		candy = ss.grabImage(1, 1, 32, 32);
		candyHelper = ss.grabImage(2, 1, 32, 32);
		candyFarm = ss.grabImage(3, 1, 32, 32);
		candyMine = ss.grabImage(4, 1, 32, 32);
		candyCorporation = ss.grabImage(5, 1, 32, 32);
		candyEmpire = ss.grabImage(6, 1, 32, 32);
		clickFactory = ss.grabImage(7, 1, 32, 32);
		background = ss.grabImage(8, 1, 32, 32);
		
	}
	
	public String currentCandies() {
		String numCandies = "" + this.candies;
		return "Candies: " + numCandies;
	}
	
	public String currentCandiesPerSecond() {
		return "" + this.candiesPerSecond;
	}
	
	public String clickFactoryOwned() {
		if(clickFactoryStatus == true) {
			return "Owned";
		}
		else {
			return "Not Owned";
		}
	}
	
	private synchronized void start() {
		if(running)
			return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	private synchronized void stop() {
		if(!running)
			return;
		
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  

	public void run() {
		init();
		long prevTime = System.nanoTime();
		final double numOfTicks = 60.0;
		double ns = 1000000000 / numOfTicks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		while(running) {
			long currentTime = System.nanoTime();
			delta += (currentTime - prevTime) / ns;
			prevTime = currentTime;
			if(delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				update();
				candies += candiesPerSecond;
				timer += 1000;
				System.out.println(updates + " Ticks, FPS " + frames);
				updates = 0;
				frames = 0;
			}
			
			
		}
		stop();
	}	
	
	private void tick() {
		
		
	}
	
	public void render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		g.drawImage(resize(candy, 256, 256), 140, 100, this);
		g.drawImage(resize(candyHelper, 128, 128), 500, 10, this);
		g.drawImage(resize(candyFarm, 128, 128), 500, 80, this);
		g.drawImage(resize(candyMine, 128, 128), 500, 150, this);
		g.drawImage(resize(candyCorporation, 128, 128), 500, 220, this);
		g.drawImage(resize(candyEmpire, 128, 128), 500, 290, this);
		g.drawImage(resize(clickFactory, 128, 128), 500, 360, this);
		g.setColor(Color.white);
		g.setFont(new Font("Rockwell", Font.BOLD+Font.ITALIC, 40));
		g.drawString(currentCandies(), 140, 55);
		g.setFont(new Font("Rockwell", Font.BOLD+Font.ITALIC, 30));
		g.drawString("Buy", 540, 40);
		g.setFont(new Font("Rockwell", Font.BOLD+Font.ITALIC, 20));
		g.drawString("# of Helpers: " + numHelpers, 40, 95);
		g.drawString("# of Farms: " + numFarms, 40, 120);
		g.drawString("# of Mines: " + numMines, 40, 145);
		g.drawString("# of Corporations: " + numCorporations, 40, 170);
		g.drawString("# of Empires: " + numEmpires, 40, 195);
		g.drawString("Click Factory Status: " + clickFactoryOwned(), 40, 420);
		g.drawString("Current Candies per Second:" + currentCandiesPerSecond(), 40, 445);
		g.setColor(Color.magenta);
		g.drawString("Helper", 535, 77);
		g.setColor(Color.green);
		g.drawString("Farm", 540, 147);
		g.setColor(Color.pink);
		g.drawString("Mine", 540, 217);
		g.setColor(Color.orange);
		g.drawString("Corp", 540, 287);
		g.setColor(Color.blue);
		g.drawString("Empire", 533, 357);
		g.setColor(Color.cyan);
		g.drawString("Factory", 532, 427);
		g.setFont(new Font("Rockwell", Font.BOLD+Font.ITALIC, 12));
		g.setColor(Color.white);
		g.drawString("Cost: 10 Candies", 516, 111);
		g.drawString("Cost: 100 Candies", 514, 181);
		g.drawString("Cost: 2500 Candies", 512, 251);
		g.drawString("Cost: 50000 Candies", 510, 321);
		g.drawString("Cost: 500000 Candies", 508, 391);
		g.drawString("Cost: 2500 Candies", 514, 461);
		g.drawString("+2/Sec", 455, 75);
		g.drawString("+5/Sec", 455, 145);
		g.drawString("+25/Sec", 448, 215);
		g.drawString("+1000/Sec", 434, 285);
		g.drawString("+7500/Sec", 434, 355);
		g.drawString("10/Click", 444, 425);
		if(candies > 9999999) {
			setWonGame(true);
		}
		if(isWonGame()) {
			g.setFont(new Font("Rockwell", Font.BOLD+Font.ITALIC, 20));
			g.setColor(Color.blue);
			g.drawString("Congratulations! You have won the game!", 40, 395);
		}
		

		
		
		g.dispose();
		bs.show();
		
	}
	
	
	
	public static void main(String args[]) {
		ClickerGame clickerGame = new ClickerGame();
		
		clickerGame.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		clickerGame.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		clickerGame.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		JFrame frame = new JFrame(clickerGame.TITLE);
		frame.add(clickerGame);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		clickerGame.start();
	}

}
