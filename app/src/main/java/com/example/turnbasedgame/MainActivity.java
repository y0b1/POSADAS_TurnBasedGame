package com.example.turnbasedgame;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int BOARD_SIZE = 3;
    private Button[][] boardButtons;
    private int currentPlayer = 1; // Player 1 is X, Player 2 is O
    private boolean gameEnded = false;
    private TextView textViewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStatus = findViewById(R.id.text_view_status);
        GridLayout gridLayoutBoard = findViewById(R.id.grid_layout_board);
        boardButtons = new Button[BOARD_SIZE][BOARD_SIZE];

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Button button = new Button(this);
                button.setLayoutParams(new GridLayout.LayoutParams(
                        GridLayout.spec(row, 1f),
                        GridLayout.spec(col, 1f)
                ));
                button.setPadding(0, 0, 0, 0);
                button.setTextSize(40);
                button.setTextColor(Color.BLACK);
                button.setBackgroundColor(Color.LTGRAY);
                button.setOnClickListener(new ButtonClickListener(row, col));
                boardButtons[row][col] = button;
                gridLayoutBoard.addView(button);
            }
        }
    }

    private class ButtonClickListener implements View.OnClickListener {

        private final int row;
        private final int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            if (!gameEnded && button.getText().toString().isEmpty()) {
                if (currentPlayer == 1) {
                    button.setText("X");
                    textViewStatus.setText("Player O's Turn");
                } else {
                    button.setText("O");
                    textViewStatus.setText("Player X's Turn");
                }
                currentPlayer = (currentPlayer % 2) + 1;
                checkForWin();
                checkForDraw();
            }
        }

        private void checkForWin() {
            String[][] board = new String[BOARD_SIZE][BOARD_SIZE];
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    board[i][j] = boardButtons[i][j].getText().toString();
                }
            }

            for (int i = 0; i < BOARD_SIZE; i++) {
                if (!board[i][0].isEmpty() && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                    gameEnded = true;
                    highlightWinningCells(i, 0, i, 1, i, 2);
                    showWinner(board[i][0]);
                    return;
                }
            }

            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!board[0][j].isEmpty() && board[0][j].equals(board[1][j]) && board[1][j].equals(board[2][j])) {
                    gameEnded = true;
                    highlightWinningCells(0, j, 1, j, 2, j);
                    showWinner(board[0][j]);
                    return;                }
            }

            if (!board[0][0].isEmpty() && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
                gameEnded = true;
                highlightWinningCells(0, 0, 1, 1, 2, 2);
                showWinner(board[0][0]);
                return;
            }

            if (!board[0][2].isEmpty() && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
                gameEnded = true;
                highlightWinningCells(0, 2, 1, 1, 2, 0);
                showWinner(board[0][2]);
                return;
            }
        }

        private void highlightWinningCells(int row1, int col1, int row2, int col2, int row3, int col3) {
            boardButtons[row1][col1].setTextColor(Color.RED);
            boardButtons[row2][col2].setTextColor(Color.RED);
            boardButtons[row3][col3].setTextColor(Color.RED);
        }

        private void showWinner(String winner) {
            String message = "Player " + winner + " wins!";
            textViewStatus.setText(message);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        private void checkForDraw() {
            boolean isDraw = true;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (boardButtons[i][j].getText().toString().isEmpty()) {
                        isDraw = false;
                        break;
                    }
                }
            }

            if (isDraw && !gameEnded) {
                gameEnded = true;
                textViewStatus.setText("It's a draw!");
                Toast.makeText(MainActivity.this, "It's a draw!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
