import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Board {
	
	protected static char[][] board;
	private static int[] ctr;
	private static final int blockSize = Gameplay.blockSize;
	
	static int x0;
	static int y0;
	
	
	public static void setBoard(int row, int col, int x, int y) {
		
		board = new char[row][col];
		ctr = new int[row];
		x0 = x;
		y0 = y;
		
	}
	
	public static void modBoard(int row, int col, char value) {
		
		board[row][col] = value;
		ctr[row]++;
	}
	
	public static boolean checkRow(int row) {
		if(row == 0) {
			if(ctr[0] == board[0].length) {
				for(int j=0; j<board[row].length; j++) {
					board[0][j] = 0;
				}
				ctr[0] = 0;
				return true;
			}
			else {
				return false;
			}
		}
		else if(ctr[row] == board[row].length) {
			for(int i=row; i>0; i--) {
				System.arraycopy(board[i - 1], 0, board[i], 0, board[row].length);
				ctr[i] = ctr[i-1];
			}
			return true;
		}
		else
			return false;
	}
	
	public static boolean gameLost() {
		return ctr[0] != 0;
	}
	
	public static void draw(Graphics2D g, boolean drawBlocks) {

		if(drawBlocks) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					if (board[i][j] > 0) {
						g.setColor(BlockColors.getColor(board[i][j]));
						g.fillRect(x0 + (j * blockSize), y0 + ((i) * blockSize), blockSize, blockSize);
					}
				}
			}
		}
		
		//Grid
		for(int i=0; i<board.length;i++) {
			for(int j=0; j<board[0].length;j++) {
				g.setColor(Color.WHITE);
				g.setStroke(new BasicStroke((float)1));
				g.drawRect(x0+(j*blockSize), y0+((i)*blockSize), blockSize, blockSize);
			}
		}
	}

	public static void draw(Graphics2D g){
		draw(g, true);
	}
	
	

}
