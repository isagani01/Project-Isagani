import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;

public class HangmanGameGUI extends JFrame implements ActionListener {
    private final String wordToGuess = "COMPUTER";
    private final HashSet<Character> guessedLetters = new HashSet<>();
    private final JLabel wordDisplay = new JLabel();
    private final JTextField inputField = new JTextField(1);
    private final JLabel messageLabel = new JLabel("Guess a letter:");
    private final JLabel attemptsLabel = new JLabel("Attempts left: 6");
    private final HangmanPanel hangmanPanel = new HangmanPanel();
    private int attemptsLeft = 6;

    public HangmanGameGUI() {
        setTitle("Hangman Game with Stickman");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(4, 1));
        wordDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        wordDisplay.setFont(new Font("Arial", Font.BOLD, 28));

        inputField.addActionListener(this);
        inputField.setHorizontalAlignment(SwingConstants.CENTER);
        inputField.setFont(new Font("Arial", Font.PLAIN, 18));

        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        attemptsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        topPanel.add(wordDisplay);
        topPanel.add(messageLabel);
        topPanel.add(inputField);
        topPanel.add(attemptsLabel);

        updateWordDisplay();

        add(topPanel, BorderLayout.NORTH);
        add(hangmanPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String input = inputField.getText().toUpperCase();
        inputField.setText("");

        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
            messageLabel.setText("Please enter a single letter.");
            return;
        }

        char guessedChar = input.charAt(0);

        if (guessedLetters.contains(guessedChar)) {
            messageLabel.setText("Already guessed that letter.");
            return;
        }

        guessedLetters.add(guessedChar);

        if (wordToGuess.indexOf(guessedChar) >= 0) {
            messageLabel.setText("Correct!");
        } else {
            attemptsLeft--;
            messageLabel.setText("Wrong guess!");
        }

        updateWordDisplay();
        attemptsLabel.setText("Attempts left: " + attemptsLeft);
        hangmanPanel.setWrongGuesses(6 - attemptsLeft);
        hangmanPanel.repaint();

        if (isWordGuessed()) {
            messageLabel.setText("You win! The word was: " + wordToGuess);
            inputField.setEnabled(false);
        } else if (attemptsLeft <= 0) {
            messageLabel.setText("Game Over! Word: " + wordToGuess);
            inputField.setEnabled(false);
        }
    }

    private void updateWordDisplay() {
        StringBuilder display = new StringBuilder();
        for (char c : wordToGuess.toCharArray()) {
            if (guessedLetters.contains(c)) {
                display.append(c).append(" ");
            } else {
                display.append("_ ");
            }
        }
        wordDisplay.setText(display.toString());
    }

    private boolean isWordGuessed() {
        for (char c : wordToGuess.toCharArray()) {
            if (!guessedLetters.contains(c)) {
                return false;
            }
        }
        return true;
    }

    // Drawing Panel for Stickman
    static class HangmanPanel extends JPanel {
        private int wrongGuesses = 0;

        public void setWrongGuesses(int wrongGuesses) {
            this.wrongGuesses = wrongGuesses;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Gallows
            g.drawLine(50, 300, 200, 300); // base
            g.drawLine(125, 300, 125, 50); // pole
            g.drawLine(125, 50, 200, 50);  // top beam
            g.drawLine(200, 50, 200, 80);  // rope

            // Stickman Parts
            if (wrongGuesses >= 1) g.drawOval(175, 80, 50, 50); // head
            if (wrongGuesses >= 2) g.drawLine(200, 130, 200, 200); // body
            if (wrongGuesses >= 3) g.drawLine(200, 150, 170, 180); // left arm
            if (wrongGuesses >= 4) g.drawLine(200, 150, 230, 180); // right arm
            if (wrongGuesses >= 5) g.drawLine(200, 200, 170, 250); // left leg
            if (wrongGuesses >= 6) g.drawLine(200, 200, 230, 250); // right leg
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HangmanGameGUI::new);
    }
}
