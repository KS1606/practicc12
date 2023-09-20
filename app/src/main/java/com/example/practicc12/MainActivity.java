package com.example.practicc12;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

public class MainActivity extends AppCompatActivity {

    Context context;
    int player = 1;
    SharedPreferences sharedPreferences;
    Button statistics;
    String motion;
    Button startGame;
    Button robot;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    TextView rez;
    List<Button> btnList = new ArrayList<>();
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    ImageButton img_btn;
    int winsPlayer1 = 0;
    int winsPlayer2 = 0;
    int draws = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        sharedPreferences = this.getSharedPreferences("GAME_STATISTICS", Context.MODE_PRIVATE); // Add this line

        configureSharedPreferences();
        setCurrentTheme();

        setContentView(R.layout.activity_main);

        initializeViews();
        attachButtonListeners();

        Toast.makeText(MainActivity.this, " Крестики-нолики с другом", Toast.LENGTH_SHORT).show();
        winsPlayer1 = sharedPreferences.getInt("WINS_PLAYER_1", 0);
        winsPlayer2 = sharedPreferences.getInt("WINS_PLAYER_2", 0);
        draws = sharedPreferences.getInt("DRAWS", 0);
    }


    private void configureSharedPreferences() {
        settings = getSharedPreferences("SETTINGS", MODE_PRIVATE);

        if (settings.contains("MODE_NIGHT_ON")) {
            setCurrentTheme();
        } else {
            editor = settings.edit();
            editor.putBoolean("MODE_NIGHT_ON", false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(this, "Дневная тема", Toast.LENGTH_SHORT).show();
        }
    }

    private void setCurrentTheme() {
        if (settings.getBoolean("MODE_NIGHT_ON", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.MODE_NIGHT_NO));
        }
    }

    private void initializeViews() {
        img_btn = findViewById(R.id.image_btn);
        if (settings.getBoolean("MODE_NIGHT_ON", false)) {
            img_btn.setImageResource(R.drawable.ic_day);
        } else {
            img_btn.setImageResource(R.drawable.ic_night);
        }

        rez = findViewById(R.id.rez);
        robot = findViewById(R.id.bot);
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
    }

    private void attachButtonListeners() {
        img_btn.setOnClickListener(view -> switchTheme());

        startGame.setOnClickListener(l -> {
            for (int i = 0; i < btnList.size(); i++) {
                btnList.get(i).setEnabled(true);
                btnList.get(i).setText("");
            }
        });

        for (Button button : btnList) {
            button.setOnClickListener(view -> onButtonClick(button));
        }

        robot.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, MainActivity2.class)));
        statistics.setOnClickListener(view -> {
            AlertDialog.Builder statisticsDialog = new AlertDialog.Builder(context);
            statisticsDialog.setTitle("Статистика");
            statisticsDialog.setMessage("Победы игрока 1 (Крестики): " + winsPlayer1 + "\nПобеды игрока 2 (Нолики): " + winsPlayer2 + "\nНичьих: " + draws);
            statisticsDialog.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
            statisticsDialog.show();
        });

    }

    private void switchTheme() {
        editor = settings.edit();

        if (settings.getBoolean("MODE_NIGHT_ON", false)) {
            editor.putBoolean("MODE_NIGHT_ON", false);
        } else {
            editor.putBoolean("MODE_NIGHT_ON", true);
        }

        editor.apply();
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    private void onButtonClick(Button button) {
        if (player == 1) {
            motion = "X";
            player = 2;
        } else if (player == 2) {
            motion = "O";
            player = 1;
        }
        button.setText(motion);
        evaluateGameState();
    }

    private void evaluateGameState() {
        if (checkWinCondition("X")) {
            rez.setText("Выиграл крестик");
            winsPlayer1++;
            saveStatistics();
            disableButtons();
        } else if (checkWinCondition("O")) {
            rez.setText("Выиграл нолик");
            winsPlayer2++;
            saveStatistics();
            disableButtons();
        } else {
            boolean emptyCells = false;
            for (Button btn : btnList) {
                if (btn.getText().equals("")) {
                    emptyCells = true;
                    break;
                }
            }

            if (!emptyCells) {
                rez.setText("Ничья");
                draws++;
                for (int i = 0; i < btnList.size(); i++) {
                    btnList.get(i).setEnabled(false);
                }
                saveStatistics();
            }
        }
    }



    private void saveStatistics() {
        SharedPreferences.Editor statisticsEditor = sharedPreferences.edit();
        statisticsEditor.putInt("WINS_PLAYER_1", winsPlayer1);
        statisticsEditor.putInt("WINS_PLAYER_2", winsPlayer2);
        statisticsEditor.putInt("DRAWS", draws);
        statisticsEditor.apply();
    }

    private void disableButtons() {
        for (int i = 0; i < btnList.size(); i++) {
            btnList.get(i).setEnabled(false);
        }
    }

    private boolean checkWinCondition(String symbol) {
        return (btn3.getText().equals(symbol) && btn2.getText().equals(symbol) && btn8.getText().equals(symbol)
                || btn4.getText().equals(symbol) && btn5.getText().equals(symbol) && btn6.getText().equals(symbol)
                || btn7.getText().equals(symbol) && btn9.getText().equals(symbol) && btn1.getText().equals(symbol)
                || btn3.getText().equals(symbol) && btn6.getText().equals(symbol) && btn9.getText().equals(symbol)
                || btn2.getText().equals(symbol) && btn5.getText().equals(symbol) && btn1.getText().equals(symbol)
                || btn4.getText().equals(symbol) && btn7.getText().equals(symbol) && btn8.getText().equals(symbol)
                || btn3.getText().equals(symbol) && btn7.getText().equals(symbol) && btn5.getText().equals(symbol)
                || btn8.getText().equals(symbol) && btn5.getText().equals(symbol) && btn9.getText().equals(symbol));
    }

}
