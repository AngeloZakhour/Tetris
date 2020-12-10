import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class OBlock extends BlockBox{

	protected int[][] coord = new int[4][2];
	
	public OBlock(BlockRole br) {
		super(2, br);
		
		//Checking for block at original spawn
		if(br == BlockRole.REGULAR) {
			for(int[] c: getCoord()) {
				if(Board.board[c[0]][c[1]] != 0) {
					super.upShift();
					break;
				}
			}
		}
		
		coord = getCoord();
		blockType = 'O';
	}
	
	
	public int[][] getCoord(){
		
		coord[0][0] = boxCorners[0][0];	coord[0][1] = boxCorners[0][1];
		coord[1][0] = boxCorners[1][0];	coord[1][1] = boxCorners[1][1];
		coord[2][0] = boxCorners[2][0];	coord[2][1] = boxCorners[2][1];
		coord[3][0] = boxCorners[3][0];	coord[3][1] = boxCorners[3][1];

		return coord;
		
	}
	
	public boolean gravity() {
		//Checking borders
		if(coord[3][0]+1 == Board.board.length) {
			return false;
		}//Checking other blocks on the board
		else if(Board.board[coord[2][0]+1][coord[2][1]] != 0 || Board.board[coord[3][0]+1][coord[3][1]] != 0) {
			return false;
		}
		else {
			for(int[] square: coord) {
				square[0] += 1;
			}
			return true;
		}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(BlockColors.getOColor());
		
		if(blockRole == BlockRole.NEXT) {
			for(int[] square: coord) {
				g.fillRect(
						Gameplay.nextBlockXStart+Gameplay.blockSize/2+(square[1]*Gameplay.blockSize),
						Gameplay.nextBlockYStart+(square[0]*Gameplay.blockSize),
						Gameplay.blockSize,
						Gameplay.blockSize
				);
			}
			for(int[] square: coord) {
				g.setColor(Color.LIGHT_GRAY);
				g.setStroke(new BasicStroke((float)1));
				g.drawRect(
						Gameplay.nextBlockXStart+Gameplay.blockSize/2+(square[1]*Gameplay.blockSize),
						Gameplay.nextBlockYStart+(square[0]*Gameplay.blockSize),
						Gameplay.blockSize,
						Gameplay.blockSize
				);
			}			
			return;
		}
		
		if(blockRole == BlockRole.HOLD) {
			for(int[] square: coord) {
				g.fillRect(
						Gameplay.holdBlockXStart+Gameplay.blockSize/2+(square[1]*Gameplay.blockSize),
						Gameplay.holdBlockYStart+(square[0]*Gameplay.blockSize),
						Gameplay.blockSize,
						Gameplay.blockSize
				);
			}
			for(int[] square: coord) {
				g.setColor(Color.LIGHT_GRAY);
				g.setStroke(new BasicStroke((float)1));
				g.drawRect(
						Gameplay.holdBlockXStart+Gameplay.blockSize/2+(square[1]*Gameplay.blockSize),
						Gameplay.holdBlockYStart+(square[0]*Gameplay.blockSize),
						Gameplay.blockSize,
						Gameplay.blockSize
				);
			}			
			return;
		}
		
		if(coord[0][0] < 0) {
			g.fillRect(
					Board.x0+(coord[2][1]*Gameplay.blockSize),
					Board.y0+(coord[2][0]*Gameplay.blockSize),
					Gameplay.blockSize,
					Gameplay.blockSize
			);
			g.fillRect(
					Board.x0+(coord[3][1]*Gameplay.blockSize),
					Board.y0+(coord[3][0]*Gameplay.blockSize),
					Gameplay.blockSize,
					Gameplay.blockSize
			);
		}
		else {
			for(int[] square: coord) {
				g.fillRect(
						Board.x0+(square[1]*Gameplay.blockSize),
						Board.y0+(square[0]*Gameplay.blockSize),
						Gameplay.blockSize,
						Gameplay.blockSize
				);
			}	
		}
	}
	
	public boolean rotateRight() {
		return false;
	}
	
	public boolean rotateLeft() {
		return false;
	}
	
	public void moveRight() {
		//Checking borders
		if(coord[1][1]+1 >= Board.board[0].length) {
			return;
		}//Checking other blocks on the board
		else if(Board.board[coord[1][0]][coord[1][1]+1] != 0 || Board.board[coord[2][0]][coord[2][1]+1] != 0) {
			return;
		}
		else {
			for(int[] square: coord) {
				square[1] += 1;
			}
		}
	}
	
	public void moveLeft() {
		//Checking borders
		if(coord[0][1]-1 < 0 || coord[3][1]-1 < 0) {
			return;
		}//Checking other blocks on the board
		else if(Board.board[coord[0][0]][coord[0][1]-1] != 0 || Board.board[coord[3][0]][coord[3][1]-1] != 0) {
			return;
		}
		else {
			for(int[] square: coord) {
				square[1] -= 1;
			}
		}
	}

	public boolean reachedEnd() {
		if(coord[2][0]+1 >= Board.board.length ||
			Board.board[coord[2][0]+1][coord[2][1]] != 0 ||
				Board.board[coord[3][0]+1][coord[3][1]] != 0)
		{
			return true;
		}
		return false;
	}

	public void endMovement() {
		if(coord[0][0] < 0) {
			Board.modBoard(coord[2][0], coord[2][1], 2);
			Board.modBoard(coord[3][0], coord[3][1], 2);
		}
		else {
			for(int[] square: coord) {
				Board.modBoard(square[0], square[1], 2);
			}
		}
	}
	
	public int checkRows() {
		int ctr = 0;
		
		if(Board.checkRow(coord[0][0])) {
			ctr++;
		}
		if(Board.checkRow(coord[3][0])) {
			ctr++;
		}
		
		return ctr;
	}

}
