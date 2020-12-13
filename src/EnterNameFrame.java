import javax.swing.*;
import java.awt.event.*;

public class EnterNameFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField nameField;
    private JButton confirmButton;
    private JLabel gameOverLabel;
    private Gameplay caller;
    private EnterNameFrame self;

    //Bounds variables
    private static final int x = 300;
    private static final int y = 200;
    private static final int width = 400;
    private static final int height = 250;

    public EnterNameFrame(Gameplay caller){
        super("Game Over!");
        this.caller = caller;
        this.self = this;
        this.setContentPane(mainPanel);
        this.pack();
        this.setBounds(x, y, width, height);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addActionListeners();

        gameOverLabel.setText(gameOverLabel.getText()+ " " + caller.getScore());
        nameField.selectAll();

    }

    private void addActionListeners() {
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                caller.setUsername(nameField.getText().toUpperCase());
                caller.showLeaderboard();
                self.setVisible(false);
            }
        });

        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                nameField.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {}
        });

        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    confirmButton.doClick();
                }
            }
        });
    }
}
