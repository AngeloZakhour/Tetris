import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

@SuppressWarnings("serial")
public class Gameplay extends JPanel implements KeyListener, ActionListener {

	//Scores
	private int score = 0;
	private final int blocksPerLevel = 20;
	private int blocksToNextLevel = blocksPerLevel;
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

	//Fonts
	private final Font playFont = new Font("Rockwell", Font.PLAIN, 25);
	private final Font pauseFont = new Font("Rockwell", Font.PLAIN, 35);
	private final Font pauseSubFont = new Font("Rockwell", Font.PLAIN, 15);
	private final Font instructionFont = new Font("Rockwell", Font.PLAIN, 17);
	private final Font instructionCmdFont = new Font("Rockwell", Font.PLAIN, 16);
	private final Font volumeFont = new Font("Rockwell", Font.PLAIN, 27);

	//List of instructions
	private final String[] instructionList =
			{"MOVE RIGHT", "MOVE LEFT", "ROTATE RIGHT", "ROTATE LEFT", "SOFT DROP", "HARD DROP", "HOLD"};
	private final String[] commandList =
			{"RIGHT ARROW / D", "LEFT ARROW / A", "UP ARROW / W", "Z", "DOWN ARROW / S", "SPACE", "C"};

	//Text Positioning (GAME PAUSED / INSTRUCTIONS)
	private final int pausedTextX =  leftBorderWidth + 25;
	private final int pausedTextY = topBorderHeight +  (blockSize * 2);
	private final int instructionX = leftBorderWidth + 10;
	private final int firstInstructionY = topBorderHeight + blockSize * 4;
	private final int instructionYIncrement = blockSize;
	private final int commandX = instructionX + 140;
	private final int lastInstructionY = firstInstructionY + instructionList.length*instructionYIncrement;
	private final int volumeX = leftBorderWidth + 90;
	private int volumeY = lastInstructionY+blockSize;

	//Leaderboard
	private final int leadersShown = 10;
	private ArrayList<String> leaderboard = null;
	private final ArrayList<String> leaderboardNames = new ArrayList<>();
	private final ArrayList<String> leaderboardScores = new ArrayList<>();
	private String username = "-";
	private final int lastLeaderY = firstInstructionY + leadersShown*instructionYIncrement;

	//Sound
	static{
		SoundManager.playMusic();
	}


	public Gameplay() {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		Board.setBoard(boardRows, boardCols, leftBorderWidth, topBorderHeight);

		timer = new Timer(initialDelay, this);

		addMouseListener();
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

	private void generateShadow(){
		shadowBlock = switch (block.blockType) {
			case 'I' -> new IBlock(BlockRole.SHADOW);
			case 'O' -> new OBlock(BlockRole.SHADOW);
			case 'J' -> new JBlock(BlockRole.SHADOW);
			case 'L' -> new LBlock(BlockRole.SHADOW);
			case 'S' -> new SBlock(BlockRole.SHADOW);
			case 'Z' -> new ZBlock(BlockRole.SHADOW);
			case 'T' -> new TBlock(BlockRole.SHADOW);
			default -> null;
		};

		assert shadowBlock != null;
		shadowBlock.setRotation(block.getRotation());
		shadowBlock.setBoxCorners(block.getBoxCorners());
		shadowBlock.updateCoord();

		while(true){
			if (!shadowBlock.gravity()) break;
		}
	}
	
	public void paint(Graphics g) {
		// Background
		scaledBg.paintIcon(this, g, 0, 0);

		//Title
		scaledLogo.paintIcon(this, g, leftBorderWidth, 0);

		//Next block / Hold block
		g.setColor(Color.WHITE);
		g.drawRect(nextBlockXStart, nextBlockYStart, 5*blockSize, 4*blockSize);
		g.drawRect(holdBlockXStart, holdBlockYStart, 5*blockSize, 4*blockSize);
		g.setFont(playFont);
		g.drawString("Up Next", nextBlockXStart+blockSize, topBorderHeight+blockSize);
		g.drawString("Hold", (int) (holdBlockXStart+1.5*blockSize), topBorderHeight+blockSize);
		g.setColor(new Color(181, 181, 181, 30));
		g.fillRect(nextBlockXStart, nextBlockYStart, 5*blockSize, 4*blockSize);
		g.fillRect(holdBlockXStart, holdBlockYStart, 5*blockSize, 4*blockSize);

		//Score
		g.setColor(Color.white);
		g.setFont(playFont);
		g.drawString("Score: "+score, rightBorderXStart+23, topBorderHeight+7*blockSize);
		g.drawString("Level: "+level, rightBorderXStart+23, topBorderHeight+8*blockSize);
		g.drawString("Lines: "+lines, rightBorderXStart+23, topBorderHeight+9*blockSize);

		if(play && !gamePaused) {
			//Block
			block.draw((Graphics2D) g);
			shadowBlock.draw((Graphics2D) g);
			nextBlock.draw((Graphics2D) g);

			if(!holdEmpty) {
				holdBlock.draw((Graphics2D) g);
			}

			// Board
			Board.draw((Graphics2D) g);
			return;
		}
		
		if(!play) {
			//Border of board
			g.setColor(Color.WHITE);
			g.drawRect(leftBorderWidth, topBorderHeight, boardCols * blockSize, boardRows * blockSize);

			//Board, hold, and next covers
			g.setColor(new Color(60, 60, 60, 150));
			g.fillRect(leftBorderWidth + 1, topBorderHeight + 1, boardCols * blockSize - 1, boardRows * blockSize - 1);
			g.fillRect(nextBlockXStart + 1, nextBlockYStart + 1, 5 * 30 - 1, 4 * 30 - 1);
			g.fillRect(holdBlockXStart + 1, holdBlockYStart + 1, 5 * 30 - 1, 4 * 30 - 1);

			g.setColor(Color.WHITE);
			g.setFont(pauseFont);

			//Game paused
			if (!gameLost) {

				if (gamePaused) {
					g.drawString(
							"GAME PAUSED",
							pausedTextX,
							pausedTextY
					);
				} else {
					g.drawString(
							"INSTRUCTIONS",
							pausedTextX,
							pausedTextY
					);
				}

				g.setFont(instructionFont);
				int instructions = 0;
				//Controls Types
				for (String s : instructionList) {
					g.drawString(s, instructionX, firstInstructionY + (instructionYIncrement * instructions++));
				}

				//Commands
				g.setFont(instructionCmdFont);
				instructions = 0;
				for (String s : commandList) {
					g.drawString(s, commandX, firstInstructionY + (instructionYIncrement * instructions++));
				}

				volumeY = lastInstructionY+blockSize;
			}

			//Game over
			else {
				g.drawString(
						"Leaderboard",
						pausedTextX+20,
						pausedTextY
				);

				g.setFont(instructionFont);
				for(int i=0; i<leadersShown && i<leaderboard.size(); i++){
					g.drawString(i+1 + ": " + leaderboardNames.get(i), instructionX, firstInstructionY+(instructionYIncrement*i));
				}

				g.setFont(instructionCmdFont);
				for(int i=0; i<leadersShown && i<leaderboard.size(); i++){
					g.drawString(leaderboardScores.get(i), commandX + 60, firstInstructionY+(instructionYIncrement*i));
				}

				if(leaderboard.size() < leadersShown){
					g.setFont(instructionFont);
					for(int i=leaderboard.size(); i < leadersShown; i++){
						g.drawString(i+1 + ": EMPTY" , instructionX, firstInstructionY+(instructionYIncrement*i));
					}
					g.setFont(instructionCmdFont);
					for(int i=leaderboard.size(); i < leadersShown; i++){
						g.drawString("N/A", commandX + 60, firstInstructionY+(instructionYIncrement*i));
					}
				}
				g.setFont(pauseSubFont);
				g.drawString(
						"Press ENTER to Restart",
						leftBorderWidth + 2 * blockSize+10,
						topBorderHeight+blockSize*19
				);

				volumeY = lastLeaderY+blockSize;
			}

			//Sound
			g.setFont(volumeFont);
			g.drawString("VOLUME", volumeX, volumeY);
			SoundDisplay.setPositions(leftBorderWidth, volumeY+blockSize, blockSize*10, 25);
			SoundDisplay.draw(this, (Graphics2D) g);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(play) {
			if(block.reachedEnd()) {
				block.endMovement();
				blockPlaced();
				if(Board.gameLost()) {
					play = false;
					gameLost = true;
					return;
				}
				int ctr = block.checkRows();
				if(ctr == 1) {
					addPoints(100);
					lines += 1;
				}
				else if (ctr == 2) {
					addPoints(250);
					lines += 2;
				}
				else if(ctr == 3) {
					addPoints(750);
					lines += 3;
				}
				else if(ctr == 4) {
					addPoints(2000);
					lines += 4;
				}
				
				block = generateBlock(nextBlock);
				generateShadow();
				nextBlock = generateBlock(BlockRole.NEXT);
				holdUsed = false;
			}
			else
				block.gravity();
			repaint();
		}
		else if(gameLost) {
			timer.stop();
			new EnterNameFrame(this).setVisible(true);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE && ((play && !gamePaused) || (!play && gamePaused))) {
			if (play) {
				play = false;
				gamePaused = true;
				SoundManager.pauseMusic();
			} else {
				play = true;
				gamePaused = false;
				SoundManager.resumeMusic();
			}
			repaint();
		}
		if (!play && !gameLost && !gamePaused &&
				(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)) {
			play = true;
			block = generateBlock(BlockRole.REGULAR);
			generateShadow();
			nextBlock = generateBlock(BlockRole.NEXT);
			timer.start();
			repaint();
			return;
		}
		if (play) {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				block.moveRight();
				generateShadow();
				repaint();
				SoundManager.playSound(SoundType.MOVE);
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				block.moveLeft();
				generateShadow();
				repaint();
				SoundManager.playSound(SoundType.MOVE);
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (block.gravity()) {
					timer.restart();
					repaint();
				}
			} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				block.rotateRight();
				generateShadow();
				repaint();
				SoundManager.playSound(SoundType.ROTATE);
			} else if (e.getKeyCode() == KeyEvent.VK_Z || e.getKeyCode() == KeyEvent.VK_S) {
				block.rotateLeft();
				generateShadow();
				repaint();
				SoundManager.playSound(SoundType.ROTATE);
			} else if (e.getKeyCode() == KeyEvent.VK_C) {
				if (holdEmpty) {
					holdBlock = putOnHold(block);
					block = generateBlock(nextBlock);
					generateShadow();
					nextBlock = generateBlock(BlockRole.NEXT);
					holdEmpty = false;
				} else if (!holdUsed) {
					BlockBox temp = holdBlock;
					holdBlock = putOnHold(block);
					block = generateBlock(temp);
					generateShadow();
					holdUsed = true;
				}
				repaint();
				SoundManager.playSound(SoundType.ROTATE);
			} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				block.hardDrop(shadowBlock);
				this.actionPerformed(new ActionEvent(this, 0, ""));
				timer.restart();
				SoundManager.playSound(SoundType.DROP);
			}
		}
		if (gameLost) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				gameLost = false;
				play = true;

				Board.setBoard(boardRows, boardCols, leftBorderWidth, topBorderHeight);
				block = generateBlock(BlockRole.REGULAR);
				generateShadow();
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

		if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
			SoundManager.increaseVolume();
			repaint();
		}

		if (e.getKeyCode() == KeyEvent.VK_MINUS) {
			SoundManager.decreaseVolume();
			repaint();
		}

	}

	private void addPoints(int points){
		score += points;
	}

	private void blockPlaced(){
		blocksToNextLevel--;
		addPoints(12);
		if(blocksToNextLevel == 0){
			blocksToNextLevel = blocksPerLevel;
			delay -= delayIncrement;
			timer.setDelay(delay);
			level += 1;
		}
	}

	private void addMouseListener(){
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!play) {
					if (SoundDisplay.mouseClicked(getMousePosition())) {
						repaint();
					}
				}
			}
		});
	}

	public void setUsername(String name){
		this.username = name;
	}

	public void showLeaderboard(){
		leaderboard = LeaderboardManager.addScore(username, score);
		String[] leader;
		for(String s : leaderboard){
			leader = s.split(":");
			if(leader[0].length() > 15){
				leader[0] = leader[0].substring(0, 15);
			}
			leaderboardNames.add(leader[0]);
			leaderboardScores.add(leader[1]);
		}
		repaint();
	}

	public int getScore(){
		return score;
	}
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

}
