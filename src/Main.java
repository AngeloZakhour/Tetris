import javax.swing.JFrame;

public class Main {
	
	static int frameWidth = 700;
	static int frameHeight = 760;
	
	static JFrame frame = new JFrame();
	static Gameplay gameplay = new Gameplay();
	
	

	public static void main(String[] args) {
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(500,25,frameWidth, frameHeight);
		frame.setTitle("TETRIS");
		frame.setResizable(false);
		frame.setVisible(true);
		frame.add(gameplay);

	}

}


