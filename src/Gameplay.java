import java.awt.*;
import java.awt.event.*;
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

	private final Random random = new Random();
	public final static int blockSize = 30;
	
	//Blocks
	private BlockBox block;
	private BlockBox nextBlock;
	private BlockBox holdBlock;
	private BlockBox shadowBlock;
	
	// Layout
	static int bottomBorderYStart = Main.frameHeight-50;
	static int topBorderHeight = bottomBorderYStart-(20*blockSize)-1;//-1 for adjustment
	static int leftBorderWidth = (Main.frameWidth/2)-(5*blockSize);
	static int rightBorderXStart = leftBorderWidth + (10*blockSize);
	
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
	private final int boardRows = 20;
	private final int boardCols = 10;
	
	//Logo
	private final ImageIcon logo = new ImageIcon("src/images/tetris-logo.png");
	private final int scaledWidth = Math.toIntExact(Math.round(logo.getIconWidth()/1.17))-1;
	private final int scaledHeight = Math.toIntExact(Math.round(logo.getIconHeight()/1.17));
	private final ImageIcon scaledLogo =
			new ImageIcon(logo.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));

	//Background
	private final ImageIcon bg = new ImageIcon("src/images/bg.png");
	int scaledBgWidth = Math.toIntExact(Math.round(bg.getIconWidth()*1.22));
	int scaledBgHeight = Math.toIntExact(Math.round(bg.getIconHeight()*1.22));
	private final ImageIcon scaledBg =
			new ImageIcon(bg.getImage().getScaledInstance(scaledBgWidth, scaledBgHeight, Image.SCALE_SMOOTH));

	//Sound
	static{
		SoundManager.playMusic();
	}


	public Gameplay() {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		Board.setBoard(boardRows, boardCols, leftBorderWidth, topBorderHeight);
		
		block = generateBlock(BlockRole.REGULAR);
		nextBlock = generateBlock(BlockRole.NEXT);

		timer = new Timer(initialDelay, this);

		addMouseListener();
	}
	
	private BlockBox generateBlock(BlockBox bb, BlockRole role) {

		return switch (bb.blockType) {
			case 'I' -> new IBlock(role);
			case 'O' -> new OBlock(role);
			case 'J' -> new JBlock(role);
			case 'L' -> new LBlock(role);
			case 'S' -> new SBlock(role);
			case 'Z' -> new ZBlock(role);
			case 'T' -> new TBlock(role);
			default -> null;
		};
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

		return switch (bb.blockType) {
			case 'I' -> new IBlock(BlockRole.HOLD);
			case 'O' -> new OBlock(BlockRole.HOLD);
			case 'J' -> new JBlock(BlockRole.HOLD);
			case 'L' -> new LBlock(BlockRole.HOLD);
			case 'S' -> new SBlock(BlockRole.HOLD);
			case 'Z' -> new ZBlock(BlockRole.HOLD);
			case 'T' -> new TBlock(BlockRole.HOLD);
			default -> null;
		};

	}
	
	public void paint(Graphics g) {
		// Background
		scaledBg.paintIcon(this, g, 0, 0);

		//Title
		scaledLogo.paintIcon(this, g, leftBorderWidth, 0);

		//Sound
		SoundDisplay.draw(this, (Graphics2D) g);

		//Next block / Hold block
		g.setColor(Color.WHITE);
		g.drawRect(nextBlockXStart, nextBlockYStart, 5*blockSize, 4*blockSize);
		g.drawRect(holdBlockXStart, holdBlockYStart, 5*blockSize, 4*blockSize);
		g.setFont(new Font("Arial", Font.BOLD, 25));
		g.drawString("Up Next", nextBlockXStart+blockSize, topBorderHeight+blockSize);
		g.drawString("Hold", (int) (holdBlockXStart+1.5*blockSize), topBorderHeight+blockSize);
		g.setColor(new Color(181, 181, 181, 30));
		g.fillRect(nextBlockXStart, nextBlockYStart, 5*blockSize, 4*blockSize);
		g.fillRect(holdBlockXStart, holdBlockYStart, 5*blockSize, 4*blockSize);

		//Score
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 25));
		g.drawString("Score: "+score, rightBorderXStart+23, topBorderHeight+7*blockSize);
		g.drawString("Level: "+level, rightBorderXStart+23, topBorderHeight+8*blockSize);
		g.drawString("Lines: "+lines, rightBorderXStart+23, topBorderHeight+9*blockSize);

		if(play && !gamePaused) {
			//Block
			block.draw((Graphics2D) g);
			nextBlock.draw((Graphics2D) g);

			if(!holdEmpty) {
				holdBlock.draw((Graphics2D) g);
			}

			// Board
			Board.draw((Graphics2D) g);
			return;
		}
		
		
		//Game paused
		if(gamePaused) {
			Board.draw((Graphics2D) g, false);
			g.setColor(new Color(60, 60, 60, 100));
			g.fillRect(leftBorderWidth, topBorderHeight, boardCols*blockSize, boardRows*blockSize);
			g.fillRect(nextBlockXStart, nextBlockYStart, 5*30, 4*30);
			g.fillRect(holdBlockXStart, holdBlockYStart, 5*30, 4*30);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 35));
			g.drawString(
					"GAME PAUSED",
					leftBorderWidth+25,
					((bottomBorderYStart-topBorderHeight)/2)+topBorderHeight
			);


			return;
		}

		Board.draw((Graphics2D) g);


		//Game over
		if(gameLost) {
			g.setColor(new Color(60, 60, 60, 200));
			g.fillRect(leftBorderWidth, topBorderHeight, boardCols*blockSize, boardRows*blockSize);
			
			g.setColor(new Color(230, 0, 0));
			g.setFont(new Font("Arial", Font.BOLD, 35));
			g.drawString(
					"GAME OVER!",
					leftBorderWidth + blockSize + blockSize/2,
					((bottomBorderYStart-topBorderHeight)/2)+topBorderHeight
			);
			
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString(
					"Press ENTER to Restart",
					leftBorderWidth+2*blockSize,
					((bottomBorderYStart-topBorderHeight)/2)+topBorderHeight+blockSize
			);
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
				
				block = generateBlock(nextBlock, BlockRole.REGULAR);
				nextBlock = generateBlock(BlockRole.NEXT);
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
				SoundManager.pauseMusic();
			}
			else {
				play = true;
				gamePaused = false;
				SoundManager.resumeMusic();
			}
			repaint();
		}
		if(!play && !gameLost && !gamePaused && (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)) {
			play=true;
			timer.start();
			repaint();
			return;
		}
		if(play) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				block.moveRight();
				repaint();
				SoundManager.playSound(SoundType.MOVE);
			}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				block.moveLeft();
				repaint();
				SoundManager.playSound(SoundType.MOVE);
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
				SoundManager.playSound(SoundType.ROTATE);
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
				block.rotateLeft();
				repaint();
				SoundManager.playSound(SoundType.ROTATE);
			}
			else if(e.getKeyCode() == KeyEvent.VK_C) {
				if(holdEmpty) {
					holdBlock = putOnHold(block);
					block = generateBlock(nextBlock, BlockRole.REGULAR);
					nextBlock = generateBlock(BlockRole.NEXT);
					holdEmpty = false;
				}
				else if(!holdUsed){
					BlockBox temp = holdBlock;
					holdBlock = putOnHold(block);
					block = generateBlock(temp, BlockRole.REGULAR);
					holdUsed = true;
				}
				repaint();
				SoundManager.playSound(SoundType.ROTATE);
			}
		}
		if(gameLost) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				gameLost = false;
				play = true;
				
				Board.setBoard(boardRows, boardCols, leftBorderWidth, topBorderHeight);
				block = generateBlock(BlockRole.REGULAR);
				nextBlock = generateBlock(BlockRole.NEXT);
				holdBlock = null;
				holdEmpty = true;
				holdUsed = false;
				delay = initialDelay;
				score = 0;
				level = 1;
				lines = 0;
				timer.stop();
				timer = new Timer(initialDelay, this);
				repaint();
			}
		}

		if(e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_EQUALS){
			SoundManager.increaseVolume();
			repaint();
		}

		if(e.getKeyCode() == KeyEvent.VK_MINUS){
			SoundManager.decreaseVolume();
			repaint();
		}
		
	}

	private void addMouseListener(){
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SoundDisplay.mouseClicked(getMousePosition())){
					repaint();
				}
			}
		});
	}
		
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

}
