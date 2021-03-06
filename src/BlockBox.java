import java.awt.Graphics2D;

public abstract class BlockBox {

	protected int[][] boxCorners = new int[4][2];
	protected int[][] coord = new int[4][2];
	protected int rotation = 0;
	protected char blockType;
	protected BlockRole blockRole;
	
	public BlockBox(int boxSize, BlockRole br) {
		if(br == BlockRole.REGULAR || br == BlockRole.SHADOW) {
			if(boxSize==4) {
				boxCorners[0][0] = 0;	boxCorners[1][0] = 0;	boxCorners[2][0] = 3;	boxCorners[3][0] = 3;
				boxCorners[0][1] = 3;	boxCorners[1][1] = 6;	boxCorners[2][1] = 6;	boxCorners[3][1] = 3;			
			}
			else if(boxSize == 2) {
				boxCorners[0][0] = 0;	boxCorners[1][0] = 0;	boxCorners[2][0] = 1;	boxCorners[3][0] = 1;
				boxCorners[0][1] = 4;	boxCorners[1][1] = 5;	boxCorners[2][1] = 5;	boxCorners[3][1] = 4;
			}
			else {
				boxCorners[0][0] = 0;	boxCorners[1][0] = 0;	boxCorners[2][0] = 2;	boxCorners[3][0] = 2;
				boxCorners[0][1] = 3;	boxCorners[1][1] = 5;	boxCorners[2][1] = 5;	boxCorners[3][1] = 3;
			}
		}
		else{
			if(boxSize==4) {
				boxCorners[0][0] = 0;	boxCorners[1][0] = 0;	boxCorners[2][0] = 2;	boxCorners[3][0] = 2;
				boxCorners[0][1] = 0;	boxCorners[1][1] = 3;	boxCorners[2][1] = 3;	boxCorners[3][1] = 0;			
			}
			else if(boxSize == 2) {
				boxCorners[0][0] = 1;	boxCorners[1][0] = 1;	boxCorners[2][0] = 2;	boxCorners[3][0] = 2;
				boxCorners[0][1] = 1;	boxCorners[1][1] = 2;	boxCorners[2][1] = 2;	boxCorners[3][1] = 1;
			}
			else {
				boxCorners[0][0] = 1;	boxCorners[1][0] = 1;	boxCorners[2][0] = 2;	boxCorners[3][0] = 2;
				boxCorners[0][1] = 1;	boxCorners[1][1] = 3;	boxCorners[2][1] = 3;	boxCorners[3][1] = 1;
			}
		}
		
		blockRole = br;
	}

	public BlockRole getBlockRole(){
		return blockRole;
	}

	public int getRotation(){
		return rotation;
	}

	public void setRotation(int rotation){
		this.rotation = rotation;
	}

	public int[][] getBoxCorners(){
		int[][] boxCornersClone = new int[4][2];
		for(int i = 0; i<boxCorners.length; i++){
			System.arraycopy(boxCorners[i], 0, boxCornersClone[i], 0, boxCorners[0].length);
		}
		return boxCornersClone;
	}

	public void setBoxCorners(int[][] corners){
		boxCorners = corners;
	}

	public void updateCoord(){
		coord = getCoord();
	}

	public char getBlockType(){
		return blockType;
	}

	public boolean gravity() {
		for(int[] corner: boxCorners) {
			corner[0] += 1;
		}
		updateCoord();
		return true;
	}
	
	public void upShift() {
		for(int[] corner: boxCorners) {
			corner[0] -= 1;
		}
	}
	
	public void rotateRight() {
		int[] temp = boxCorners[0];
		boxCorners[0] = boxCorners[1];
		boxCorners[1] = boxCorners[2];
		boxCorners[2] = boxCorners[3];
		boxCorners[3] = temp;
		
		rotation = (rotation+1)%4;
		updateCoord();
	}
	
	public void rotateLeft() {
		int[] temp = boxCorners[0];
		boxCorners[0] = boxCorners[3];
		boxCorners[3] = boxCorners[2];
		boxCorners[2] = boxCorners[1];
		boxCorners[1] = temp;
		
		if(rotation == 0)
			rotation = 3;
		else
			rotation = (rotation-1)%4;
		updateCoord();
	}
	
	public void moveRight() {
		for(int[] corner: boxCorners) {
			corner[1] += 1;
		}
		updateCoord();
	}
	
	public void moveLeft() {
		for(int[] corner: boxCorners) {
			corner[1] -= 1;
		}
		updateCoord();
	}

	public boolean reachedEnd(){
		for(int[] square: coord) {
			if(square[0]+1 >= Board.board.length ||
					(Board.board[square[0]+1][square[1]] != 0 && Board.board[square[0]+1][square[1]] < 91)) {
				return true;
			}
		}
		return false;
	}

	public void hardDrop(BlockBox shadow){
		setBoxCorners(shadow.getBoxCorners());
		setRotation(shadow.getRotation());
		updateCoord();
	}

	public abstract int[][] getCoord();

	public abstract void draw(Graphics2D g);

	public abstract void endMovement();

	public abstract int checkRows();
	
	
}
