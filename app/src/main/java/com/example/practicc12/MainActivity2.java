package com.example.practicc12;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    Context context;
    String motion;
    int game = 1;
    Button startGame;
    Button robot;
    Button statistics;
    ImageButton img_btn;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    TextView rez;
    List<Button> btnList = new ArrayList<>();
    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toast.makeText(MainActivity2.this, " Крестики-нолики с роботом", Toast.LENGTH_SHORT).show();
        rez = findViewById(R.id.rez);
        robot = findViewById(R.id.bot);
        img_btn = findViewById(R.id.image_btn);
        startGame = findViewById(R.id.btn_start);
        btn1 = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);
        btn7 = findViewById(R.id.button7);
        btn8 = findViewById(R.id.button8);
        btn9 = findViewById(R.id.button9);
        statistics = findViewById(R.id.statistik);

        btnList.add(btn1);
        btnList.add(btn2);
        btnList.add(btn3);
        btnList.add(btn4);
        btnList.add(btn5);
        btnList.add(btn6);
        btnList.add(btn7);
        btnList.add(btn8);
        btnList.add(btn9);

        for (int i = 0; i < btnList.size(); i++) {
            btnList.get(i).setEnabled(false);
        }

        startGame.setOnClickListener(l -> {
            for (int i = 0; i < btnList.size(); i++) {
                btnList.get(i).setEnabled(true);
                btnList.get(i).setText("");
            }
        });

        sharedPreferences = getSharedPreferences("STATS", MODE_PRIVATE);
        statistics.setOnClickListener(view -> showStatistics());
        img_btn.setOnClickListener(view -> switchTheme());
        robot.setOnClickListener(view -> playHumanClick());

    }

    private void switchTheme(){
        AlertDialog.Builder statisticsDialog = new AlertDialog.Builder(this);
        statisticsDialog.setTitle("Изменение темы");
        statisticsDialog.setMessage("Для изменения темы необходимо перейти в игру с другом.");

        statisticsDialog.setPositiveButton("Перейти", (dialog, which) -> playHumanClick());
        statisticsDialog.show();
    }

    public void playHumanClick() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onClick1(View view) {
        makeHumanMove(0);
    }

    public void onClick2(View view) {
        makeHumanMove(1);
    }

    public void onClick3(View view) {
        makeHumanMove(2);
    }

    public void onClick4(View view) {
        makeHumanMove(3);
    }

    public void onClick5(View view) {
        makeHumanMove(4);
    }

    public void onClick6(View view) {
        makeHumanMove(5);
    }

    public void onClick7(View view) {
        makeHumanMove(6);
    }

    public void onClick8(View view) {
        makeHumanMove(7);
    }

    public void onClick9(View view) {
        makeHumanMove(8);
    }

    private void makeHumanMove(int cellIndex) {
        if (btnList.get(cellIndex).getText().equals("")) {
            btnList.get(cellIndex).setText("X");
            if (!hasPlayerWon(btnList, "X") && !isGameOver(btnList)) {
                makeBotMove();
            } else {
                updateGameState();
            }
        }
    }

    private void makeBotMove() {
        BotMove botMove = miniMax(btnList, "O");
        Button btn = btnList.get(botMove.index);
        btn.setText("O");
        updateGameState();
    }

    private void updateGameState() {
        Winner();
    }

    private boolean isGameOver(List<Button> board) {
        return hasPlayerWon(board, "X") || hasPlayerWon(board, "O") || getEmptyCells(board).isEmpty();
    }

    private boolean hasPlayerWon(List<Button> board, String player) {
        return (board.get(0).getText().equals(player) && board.get(1).getText().equals(player) && board.get(2).getText().equals(player))
                || (board.get(3).getText().equals(player) && board.get(4).getText().equals(player) && board.get(5).getText().equals(player))
                || (board.get(6).getText().equals(player) && board.get(7).getText().equals(player) && board.get(8).getText().equals(player))
                || (board.get(0).getText().equals(player) && board.get(3).getText().equals(player) && board.get(6).getText().equals(player))
                || (board.get(1).getText().equals(player) && board.get(4).getText().equals(player) && board.get(7).getText().equals(player))
                || (board.get(2).getText().equals(player) && board.get(5).getText().equals(player) && board.get(8).getText().equals(player))
                || (board.get(0).getText().equals(player) && board.get(4).getText().equals(player) && board.get(8).getText().equals(player))
                || (board.get(2).getText().equals(player) && board.get(4).getText().equals(player) && board.get(6).getText().equals(player));
    }

    private List<Integer> getEmptyCells(List<Button> board) {
        List<Integer> emptyCells = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            if (board.get(i).getText().equals("")) {
                emptyCells.add(i);
            }
        }
        return emptyCells;
    }

    private static class BotMove {
        int score;
        int index;

        public BotMove(int score, int index) {
            this.score = score;
            this.index = index;
        }
    }

    private BotMove miniMax(List<Button> board, String currentPlayer) {
        List<Integer> emptyCells = getEmptyCells(board);

        if (hasPlayerWon(board, "X")) {
            return new BotMove(-1, -1);
        } else if (hasPlayerWon(board, "O")) {
            return new BotMove(1, -1);
        } else if (emptyCells.isEmpty()) {
            return new BotMove(0, -1);
        }

        List<BotMove> moves = new ArrayList<>();

        for (Integer cellIndex : emptyCells) {
            Button temp = board.get(cellIndex);
            temp.setText(currentPlayer);

            int score = currentPlayer.equals("O") ? miniMax(board, "X").score : miniMax(board, "O").score;
            moves.add(new BotMove(score, cellIndex));
            temp.setText("");
        }

        BotMove bestMove = moves.get(0);
        for (BotMove move : moves) {
            if ((currentPlayer.equals("O") && move.score > bestMove.score)
                    || (currentPlayer.equals("X") && move.score < bestMove.score)) {
                bestMove = move;
            }
        }
        return bestMove;
    }

    private void Winner() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String result = "";

        if (hasPlayerWon(btnList, "X")) {
            result = "Выиграл крестик";
            int humanWins = sharedPreferences.getInt("humanWins", 0);
            editor.putInt("humanWins", humanWins + 1).apply();
        } else if (hasPlayerWon(btnList, "O")) {
            result = "Выиграл нолик";
            int botWins = sharedPreferences.getInt("botWins", 0);
            editor.putInt("botWins", botWins + 1).apply();
        } else {
            boolean emptyCells = false;
            for (Button btn : btnList) {
                if (btn.getText().equals("")) {
                    emptyCells = true;
                    break;
                }
            }

            if (!emptyCells) {
                result = "Ничья";
                int draws = sharedPreferences.getInt("draws", 0);
                editor.putInt("draws", draws + 1).apply();
            }
        }

        if (!result.isEmpty()) {
            rez.setText(result);
            for (int i = 0; i < btnList.size(); i++) {
                btnList.get(i).setEnabled(false);
            }
        }
    }

    private void showStatistics() {
        int humanWins = sharedPreferences.getInt("humanWins", 0);
        int botWins = sharedPreferences.getInt("botWins", 0);
        int draws = sharedPreferences.getInt("draws", 0);

        AlertDialog.Builder statisticsDialog = new AlertDialog.Builder(this);
        statisticsDialog.setTitle("Статистика");
        statisticsDialog.setMessage("Победы крестика: " + humanWins + "\nПобеды нолика: " + botWins + "\nНичьи: " + draws);

        statisticsDialog.setPositiveButton("ОК", (dialog, which) -> dialog.dismiss());
        statisticsDialog.show();
    }

}
