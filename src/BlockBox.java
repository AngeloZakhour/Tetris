import java.awt.Graphics2D;

public abstract class BlockBox {

	protected int[][] boxCorners = new int[4][2];
	protected int rotation = 0;
	protected char blockType;
	protected BlockRole blockRole;
	
	
	public BlockBox(int boxSize, BlockRole br) {
		if(br == BlockRole.REGULAR) {
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
	
	public boolean gravity() {
		for(int[] corner: boxCorners) {
			corner[0] += 1;
		}
		return true;
	}
	
	public void upShift() {
		for(int[] corner: boxCorners) {
			corner[0] -= 1;
		}
	}
	
	public boolean rotateRight() {
		int[] temp = boxCorners[0];
		boxCorners[0] = boxCorners[1];
		boxCorners[1] = boxCorners[2];
		boxCorners[2] = boxCorners[3];
		boxCorners[3] = temp;
		
		rotation = (rotation+1)%4;
		return true;
	}
	
	public boolean rotateLeft() {
		int[] temp = boxCorners[0];
		boxCorners[0] = boxCorners[3];
		boxCorners[3] = boxCorners[2];
		boxCorners[2] = boxCorners[1];
		boxCorners[1] = temp;
		
		if(rotation == 0)
			rotation = 3;
		else
			rotation = (rotation-1)%4;
		return true;
	}
	
	public void moveRight() {
		for(int[] corner: boxCorners) {
			corner[1] += 1;
		}
	}
	
	public void moveLeft() {
		for(int[] corner: boxCorners) {
			corner[1] -= 1;
		}
	}

	public abstract void draw(Graphics2D g);

	public abstract boolean reachedEnd();

	public abstract void endMovement();

	public abstract int checkRows();
	
	
}
