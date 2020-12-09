import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.*;

@SuppressWarnings("serial")
public class Gameplay extends JPanel implements KeyListener, ActionListener {

	//Scores
	private int score = 0;
	private final int pointsPerLevel = 2500;
	private int pointsToNextLevel = pointsPerLevel;
	private int level = 1;
	private int lines = 0;
	
	//To play or not to play
	private boolean play = false;
	private boolean gamePaused = false;
	private boolean gameLost = false;
	
	private boolean holdUsed = false;
	private boolean holdEmpty = true;
	
	
	private Random random = new Random();
	public static int blockSize = 30;
	
	//Blocks
	private BlockBox block;
	private BlockBox nextBlock;
	private BlockBox holdBlock;
	
	// Layout
	static int bottomBorderYStart = Main.frameHeight-50;
	static int bottomBorderHeight = Main.frameHeight-bottomBorderYStart;
	static int topBorderHeight = bottomBorderYStart-(20*blockSize)-1;//-1 for adjustment
	static int leftBorderWidth = (Main.frameWidth/2)-(5*blockSize);
	static int rightBorderXStart = leftBorderWidth + (10*blockSize);
	static int rightBorderWidth = Main.frameWidth - rightBorderXStart;
	
	static int sideBoxesXIncrement = 23;
	static int nextBlockYStart = topBorderHeight+blockSize+10;
	static int nextBlockXStart = rightBorderXStart+sideBoxesXIncrement;
	static int holdBlockYStart = topBorderHeight+blockSize+10;
	static int holdBlockXStart = leftBorderWidth-5*blockSize-sideBoxesXIncrement;
	
	// Timer
	Timer timer;
	private final int initialDelay = 750;
	private final int delayIncrement = 50;
	private int delay = initialDelay;
	
	// Board
	private int boardRows = 20;
	private int boardCols = 10;
	
	//Logo
	private final ImageIcon logo = new ImageIcon("src/logo/tetris-logo.png");
	int scaledWidth = Math.toIntExact(Math.round(logo.getIconWidth()/1.17))-1;
	int scaledHeight = Math.toIntExact(Math.round(logo.getIconHeight()/1.17));
	private final ImageIcon scaledLogo = new ImageIcon(logo.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));

	//Background
	private final ImageIcon bg = new ImageIcon("src/logo/bg.png");
	int scaledBgWidth = Math.toIntExact(Math.round(bg.getIconWidth()*1.22));
	int scaledBgHeight = Math.toIntExact(Math.round(bg.getIconHeight()*1.22));
	private final ImageIcon scaledBg = new ImageIcon(bg.getImage().getScaledInstance(scaledBgWidth, scaledBgHeight, Image.SCALE_SMOOTH));
	
	public Gameplay() {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		Board.setBoard(boardRows, boardCols, leftBorderWidth, topBorderHeight);
		
		block = generateBlock();
		nextBlock = generateNextBlock();

		
		timer = new Timer(initialDelay, this);
		timer.setInitialDelay(initialDelay);
	}
	
	private BlockBox generateBlock() {
		return generateBlock(BlockRole.REGULAR);
	}
	
	private BlockBox generateBlock(BlockBox bb) {

		return switch (bb.blockType) {
			case 'I' -> new IBlock(BlockRole.REGULAR);
			case 'O' -> new OBlock(BlockRole.REGULAR);
			case 'J' -> new JBlock(BlockRole.REGULAR);
			case 'L' -> new LBlock(BlockRole.REGULAR);
			case 'S' -> new SBlock(BlockRole.REGULAR);
			case 'Z' -> new ZBlock(BlockRole.REGULAR);
			case 'T' -> new TBlock(BlockRole.REGULAR);
			default -> null;
		};
	}
	
	private BlockBox generateNextBlock() {
		return generateBlock(BlockRole.NEXT);
	}

	private BlockBox generateBlock(BlockRole role){
		int x = random.nextInt(7)+1;
		return switch (x) {
			case 1 -> new IBlock(role);
			case 2 -> new OBlock(role);
			case 3 -> new JBlock(role);
			case 4 -> new LBlock(role);
			case 5 -> new SBlock(role);
			case 6 -> new ZBlock(role);
			case 7 -> new TBlock(role);
			default -> null;
		};
	}
	
	private BlockBox putOnHold(BlockBox bb) {
		
		switch(bb.blockType) {
		case 'I': return new IBlock(BlockRole.HOLD);
		case 'O': return new OBlock(BlockRole.HOLD);
		case 'J': return new JBlock(BlockRole.HOLD);
		case 'L': return new LBlock(BlockRole.HOLD);
		case 'S': return new SBlock(BlockRole.HOLD);
		case 'Z': return new ZBlock(BlockRole.HOLD);
		case 'T': return new TBlock(BlockRole.HOLD);
		}
		
		return null;
	}
	
	public void paint(Graphics g) {
		// Background
		scaledBg.paintIcon(this, g, 0, 0);

		//Title
		scaledLogo.paintIcon(this, g, leftBorderWidth, 0);

		//Next block / Hold block
		g.setColor(Color.BLACK);
		g.fillRect(nextBlockXStart, nextBlockYStart, 5*blockSize, 4*blockSize);
		g.fillRect(holdBlockXStart, holdBlockYStart, 5*blockSize, 4*blockSize);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 25));
		g.drawString("Up Next", nextBlockXStart+blockSize, topBorderHeight+blockSize);
		g.drawString("Hold", (int) (holdBlockXStart+1.5*blockSize), topBorderHeight+blockSize);
		
		if(play && !gamePaused) {
			//Block
			block.draw((Graphics2D) g);
			nextBlock.draw((Graphics2D) g);
		}
		
		if(!holdEmpty) {
			holdBlock.draw((Graphics2D) g);
		}
		
		// Board
		Board.draw((Graphics2D) g);	
		
		//Score
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 25));
		g.drawString("Score: "+score, rightBorderXStart+23, topBorderHeight+7*blockSize);
		g.drawString("Level: "+level, rightBorderXStart+23, topBorderHeight+8*blockSize);
		g.drawString("Lines: "+lines, rightBorderXStart+23, topBorderHeight+9*blockSize);
		
		
		//Game paused
		if(gamePaused) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(leftBorderWidth, topBorderHeight, boardCols*blockSize, boardRows*blockSize);
			g.fillRect(nextBlockXStart, nextBlockYStart, 5*30, 4*30);
			g.fillRect(holdBlockXStart, holdBlockYStart, 5*30, 4*30);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 35));
			g.drawString("GAME PAUSED", leftBorderWidth+25, ((bottomBorderYStart-topBorderHeight)/2)+topBorderHeight);
		}

		//Game over
		if(gameLost) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(leftBorderWidth, topBorderHeight+8*blockSize, 10*blockSize, 4*blockSize);
			
			g.setColor(Color.RED);
			g.setFont(new Font("Arial", Font.BOLD, 35));
			g.drawString("GAME OVER!", leftBorderWidth + blockSize + blockSize/2, ((bottomBorderYStart-topBorderHeight)/2)+topBorderHeight);
			
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString("Press ENTER to Restart", leftBorderWidth+2*blockSize, ((bottomBorderYStart-topBorderHeight)/2)+topBorderHeight+blockSize);	
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(play) {
			
			if(block.reachedEnd()) {
				block.endMovement();
				if(Board.gameLost()) {
					play = false;
					gameLost = true;
					return;
				}
				int ctr = block.checkRows();
				if(ctr == 1) {
					score += 100;
					pointsToNextLevel -= 100;
					lines += 1;
				}
				else if (ctr == 2) {
					score += 250;
					pointsToNextLevel -= 250;
					lines += 2;
				}
				else if(ctr == 3) {
					score += 750;
					pointsToNextLevel -= 750;
					lines += 3;
				}
				else if(ctr == 4) {
					score += 2000;
					pointsToNextLevel -= 2000;
					lines += 4;
				}
				if(pointsToNextLevel <= 0) {
					pointsToNextLevel = pointsPerLevel-score%pointsPerLevel;
					delay -= delayIncrement;
					timer.setDelay(delay);
					level += 1;
				}
				
				block = generateBlock(nextBlock);
				nextBlock = generateNextBlock();
				holdUsed = false;
			}
			else
				block.gravity();
			repaint();
		}
		else if(gameLost) {
			repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE && !gameLost) {
			if(play) {
				play = false;
				gamePaused = true;
				repaint();
			}
			else {
				play = true;
				gamePaused = false;
				repaint();
			}
		}
		if(!play && !gameLost && (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)) {
			play=true;
			timer.start();
			repaint();
			return;
		}
		if(play) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				block.moveRight();
				repaint();		
			}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				block.moveLeft();
				repaint();
			}
			else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				if(block.gravity()) {
					timer.restart();
					repaint();
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				block.rotateRight();
				repaint();
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
				block.rotateLeft();
				repaint();
			}
			else if(e.getKeyCode() == KeyEvent.VK_C) {
				if(holdEmpty) {
					holdBlock = putOnHold(block);
					block = generateBlock(nextBlock);
					nextBlock = generateNextBlock();
					holdEmpty = false;
					holdUsed = true;
					repaint();
				}
				else if(!holdUsed){
					BlockBox temp = holdBlock;
					holdBlock = putOnHold(block);
					block = generateBlock(temp);
					temp = null;
					holdUsed = true;
					repaint();
				}
			}
		}
		if(gameLost) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				gameLost = false;
				play = true;
				
				Board.setBoard(boardRows, boardCols, leftBorderWidth, topBorderHeight);
				block = generateBlock();
				nextBlock = generateNextBlock();
				holdBlock = null;
				holdEmpty = true;
				holdUsed = false;
				delay = initialDelay;
				score = 0;
				level = 1;
				lines = 0;
				//timer.restart();
				timer.stop();
				timer = new Timer(initialDelay, this);
				repaint();
			}
		}
		
	}
		
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

}
