import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;


public class IBlock extends BlockBox{
	
	public IBlock(BlockRole br) {
		super(4, br);
		
		coord = getCoord();
		
		//Checking for block at original spawn
		for(int[] square: coord) {
			if(Board.board[square[0]][square[1]] != 0) {
				super.upShift();
				coord = getCoord();
				break;
				
			}
		}
		
		blockType = blockRole == BlockRole.SHADOW ? 'i' : 'I';
	}
	
	public int[][] getCoord(){
		
		if(rotation == 0) {
			coord[0][0] = boxCorners[0][0]+1;	coord[0][1] = boxCorners[0][1];
			coord[1][0] = coord[0][0]; 			coord[1][1] = boxCorners[0][1]+1;
			coord[2][0] = coord[0][0]; 			coord[2][1] = boxCorners[0][1]+2;
			coord[3][0] = coord[0][0]; 			coord[3][1] = boxCorners[0][1]+3;
		}
		else if(rotation == 1) {
			coord[0][0] = boxCorners[0][0];		coord[0][1] = boxCorners[0][1]-1;
			coord[1][0] = boxCorners[0][0]+1; 	coord[1][1] = coord[0][1];
			coord[2][0] = boxCorners[0][0]+2; 	coord[2][1] = coord[0][1];
			coord[3][0] = boxCorners[3][0]+3; 	coord[3][1] = coord[0][1];
		}
		else if(rotation == 2) {
			coord[0][0] = boxCorners[0][0]-1;	coord[0][1] = boxCorners[0][1];
			coord[1][0] = coord[0][0]; 			coord[1][1] = boxCorners[0][1]-1;
			coord[2][0] = coord[0][0]; 			coord[2][1] = boxCorners[0][1]-2;
			coord[3][0] = coord[0][0]; 			coord[3][1] = boxCorners[0][1]-3;
		}
		else if(rotation == 3) {
			coord[0][0] = boxCorners[0][0];		coord[0][1] = boxCorners[0][1]+1;
			coord[1][0] = boxCorners[0][0]-1; 	coord[1][1] = coord[0][1];
			coord[2][0] = boxCorners[0][0]-2; 	coord[2][1] = coord[0][1];
			coord[3][0] = boxCorners[0][0]-3; 	coord[3][1] = coord[0][1];
		}

		return coord;
		
	}
	
	public boolean gravity() {
		//Checking borders
		if(coord[0][0]+1 == Board.board.length || coord[3][0]+1 == Board.board.length) {
			return false;
		}//Checking other blocks on the board
		else if(Board.board[coord[0][0]+1][coord[0][1]] != 0 || Board.board[coord[1][0]+1][coord[1][1]] != 0 || 
				Board.board[coord[2][0]+1][coord[2][1]] != 0 || Board.board[coord[3][0]+1][coord[3][1]] != 0) {
			return false;
		}
		else {
			super.gravity();
			return true;
		}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(BlockColors.getColor(blockType));
		
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
					Board.x0+(coord[1][1]*Gameplay.blockSize),
					Board.y0+(coord[1][0]*Gameplay.blockSize),
					Gameplay.blockSize,
					Gameplay.blockSize
			);
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
		else if(coord[3][0] < 0) {
			g.fillRect(
					Board.x0+(coord[1][1]*Gameplay.blockSize),
					Board.y0+(coord[1][0]*Gameplay.blockSize),
					Gameplay.blockSize,
					Gameplay.blockSize
			);
			g.fillRect(
					Board.x0+(coord[2][1]*Gameplay.blockSize),
					Board.y0+(coord[2][0]*Gameplay.blockSize),
					Gameplay.blockSize,
					Gameplay.blockSize
			);
			g.fillRect(
					Board.x0+(coord[0][1]*Gameplay.blockSize),
					Board.y0+(coord[0][0]*Gameplay.blockSize),
					Gameplay.blockSize,
					Gameplay.blockSize
			);
		}
		for(int[] square: coord) {
			g.fillRect(
					Board.x0+(square[1]*Gameplay.blockSize),
					Board.y0+(square[0]*Gameplay.blockSize),
					Gameplay.blockSize,
					Gameplay.blockSize
			);
		}
	}
	
	public void rotateRight() {
		
		int boxMovedLeft = 0;
		int boxMovedRight = 0;
		int boxMovedUp = 0;
		
		//Adjusting for the right border
		while(boxCorners[0][1] >= Board.board[0].length || boxCorners[2][1] >= Board.board[0].length) {
			super.moveLeft();
			boxMovedLeft += 1;
		}
		
		//Adjusting for the left border
		while(boxCorners[0][1] < 0 || boxCorners[2][1] < 0) {
			super.moveRight();
			boxMovedRight += 1;
		}
		
		//Adjusting for the bottom border
		while(boxCorners[0][0] >= Board.board.length || boxCorners[2][0] >= Board.board.length) {
			super.upShift();
			boxMovedUp += 1;
		}
		super.rotateRight();
		
		//Checking for other block in the way
		for(int[] square: coord) {
			if(square[0] >= 0 && Board.board[square[0]][square[1]] != 0) {
				
				while(boxMovedLeft > 0) {
					super.moveRight();
					boxMovedLeft -= 1;
				}
				while(boxMovedRight > 0) {
					super.moveLeft();
					boxMovedRight -= 1;
				}
				while(boxMovedUp > 0) {
					super.gravity();
					boxMovedUp -= 1;
				}
				super.rotateLeft();
				return;
			}
		}
	}
	
	public void rotateLeft() {
		
		int boxMovedLeft = 0;
		int boxMovedRight = 0;
		int boxMovedUp = 0;
		if(boxCorners[0][0] < 0) {
			return;
		}
		//Adjusting for the right border
		while(boxCorners[0][1] >= Board.board[0].length || boxCorners[2][1] >= Board.board[0].length) {
			super.moveLeft();
			boxMovedLeft += 1;
		}
		//Adjusting for the left border
		while(boxCorners[0][1] < 0 || boxCorners[2][1] < 0) {
			super.moveRight();
			boxMovedRight += 1;
		}
		//Adjusting for the bottom border
		while(boxCorners[0][0] >= Board.board.length || boxCorners[2][0] >= Board.board.length) {
			super.upShift();
			boxMovedUp += 1;
		}
		super.rotateLeft();
		
		//Checking for other block in the way
		for(int[] square: coord) {
			if(square[0] >= 0 && Board.board[square[0]][square[1]] != 0) {
				
				while(boxMovedLeft > 0) {
					super.moveRight();
					boxMovedLeft -= 1;
				}
				while(boxMovedRight > 0) {
					super.moveLeft();
					boxMovedRight -= 1;
				}
				while(boxMovedUp > 0) {
					super.gravity();
					boxMovedUp -= 1;
				}
				super.rotateRight();
				return;
			}
		}

	}
	
	public void moveRight() {
		//Checking borders
		if(coord[0][1]+1 >= Board.board[0].length ||
				coord[1][1]+1 >= Board.board[0].length ||
				coord[2][1]+1 >= Board.board[0].length ||
				coord[3][1]+1 >= Board.board[0].length)
		{
			return;
		}
		//Checking other blocks on the board
		else if((coord[0][0] >= 0 && Board.board[coord[0][0]][coord[0][1]+1] != 0) ||
				Board.board[coord[1][0]][coord[1][1]+1] != 0 ||
				Board.board[coord[2][0]][coord[2][1]+1] != 0 ||
				(coord[3][0] >= 0 && Board.board[coord[3][0]][coord[3][1]+1] != 0))
		{
			return;
		}
		else {
			super.moveRight();
		}
	}
	
	public void moveLeft() {
		//Checking borders
		if(coord[0][1]-1 < 0 || coord[1][1]-1 < 0 || coord[2][1]-1 < 0 || coord[3][1]-1 < 0) {
			return;
		}//Checking other blocks on the board
		else if((coord[0][0] >= 0 && Board.board[coord[0][0]][coord[0][1]-1] != 0 )||
				Board.board[coord[1][0]][coord[1][1]-1] != 0 ||
				Board.board[coord[2][0]][coord[2][1]-1] != 0 ||
				(coord[3][0] >= 0 && Board.board[coord[3][0]][coord[3][1]-1] != 0))
		{
			return;
		}
		else {
			super.moveLeft();
		}
	}

	public void endMovement() {
		if(coord[0][0] < 0) {
			Board.modBoard(coord[1][0], coord[1][1], blockType);
			Board.modBoard(coord[2][0], coord[2][1], blockType);
			Board.modBoard(coord[3][0], coord[3][1], blockType);
		}
		else if(coord[3][0] < 0) {
			Board.modBoard(coord[1][0], coord[1][1], blockType);
			Board.modBoard(coord[2][0], coord[2][1], blockType);
			Board.modBoard(coord[0][0], coord[0][1], blockType);
		}
		else {
			for(int[] square: coord) {
				Board.modBoard(square[0], square[1], blockType);
			}
		}
	}
	
	public int checkRows() {
		int ctr = 0;
		if(rotation == 0 || rotation == 2) {
			if(Board.checkRow(coord[0][0])) {
				ctr++;
			}
		}
		else if(rotation == 1) {
			for(int i=0; i<4; i++) {
				if(Board.checkRow(coord[i][0])) {
					ctr++;
				}
			}
		}
		else {
			for(int i=3; i>=0; i--) {
				if(Board.checkRow(coord[i][0])) {
					ctr++;
				}
			}
		}
		return ctr;
	}

}
