package com.example.cubes;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView mYourScoresTextView;
    TextView mComputerScoresTextView;
    TextView mLeftCubeValueTextView;
    TextView mRightCubeValueTextView;
    Button mThrowButton;
    Random random = new Random();

    int mYourScores = 0;
    int mComputerScores = 0;
    int mLeftRandom;
    int mRightRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mYourScoresTextView = findViewById(R.id.your_score_text_view);
        mComputerScoresTextView = findViewById(R.id.computer_score_text_view);
        mLeftCubeValueTextView = findViewById(R.id.left_cude_text_view);
        mRightCubeValueTextView = findViewById(R.id.right_cube_text_view);
        mThrowButton = findViewById(R.id.throw_button);
        resetGameValues();
    }

    public void onClick(View view) {

        //Внутри условия выполняется бросок пользователя. Если при проске выпал дубль, то...
        if (!addUserPoints()) {
            //Дубль не выпал. Очередь компьютера

            //Блокируем кнопку
            mThrowButton.setText(R.string.computer_turn_label);
            mThrowButton.setEnabled(false);
            mThrowButton.setBackgroundColor(Color.parseColor("#5B8BC34A"));

            //Для реалистичности используем класс Handler для небольшой задержки
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Булево значение, показывающее, есть ли у компьютера шаг
                    boolean hasComputerTurn = true;
                    //Пока выполняются дубли, цикл выполняется
                    while (hasComputerTurn) {
                        hasComputerTurn = addRobotPoints();
                    }
                    //Дубли завершились. Разблокируем кнопку
                    mThrowButton.setBackgroundColor(Color.parseColor("#4CAF50"));
                    mThrowButton.setText(R.string.throw_cubes_button_label);
                    mThrowButton.setEnabled(true);

                    //Проверяем, не выйграл ли компьютер, пока ходил
                    if (MainActivity.this.mComputerScores >= 100) {
                        Intent looseActivityIntent = new Intent(MainActivity.this, LooseActivity.class);
                        resetGameValues();
                        startActivity(looseActivityIntent);
                    }
                }
            }, 1000);


        }
        if (mYourScores >= 100) {
            Intent winActivityIntent = new Intent(this, WinActivity.class);
            //Обнуление значений
            resetGameValues();
            //Переход на активность, отображающую победу пользователя
            startActivity(winActivityIntent);
        }
    }

    public boolean addUserPoints() {
        mLeftRandom = 1 + random.nextInt(6);
        mLeftCubeValueTextView.setText(getResources().getQuantityString(R.plurals.scores,mLeftRandom, mLeftRandom));
        mYourScores += mLeftRandom;
        mRightRandom = 1 + random.nextInt(6);
        mRightCubeValueTextView.setText(getResources().getQuantityString(R.plurals.scores,mRightRandom, mRightRandom));
        mYourScores += mRightRandom;
        mYourScoresTextView.setText(getResources().getQuantityString(R.plurals.scores,mYourScores, mYourScores));
        return mLeftRandom == mRightRandom;
    }

    public boolean addRobotPoints() {
        mLeftRandom = 1 + random.nextInt(6);
        mLeftCubeValueTextView.setText(getResources().getQuantityString(R.plurals.scores,mLeftRandom, mLeftRandom));
        mComputerScores += mLeftRandom;
        mRightRandom = 1 + random.nextInt(6);
        mRightCubeValueTextView.setText(getResources().getQuantityString(R.plurals.scores,mRightRandom, mRightRandom));
        mComputerScores += mRightRandom;
        mComputerScoresTextView.setText(getResources().getQuantityString(R.plurals.scores,mComputerScores, mComputerScores));
        return mLeftRandom == mRightRandom;
    }

    public void resetGameValues() {
        mYourScoresTextView.setText(getResources().getQuantityString(R.plurals.scores,0, 0));
        mComputerScoresTextView.setText(getResources().getQuantityString(R.plurals.scores,0, 0));
        mLeftCubeValueTextView.setText(getResources().getQuantityString(R.plurals.scores,0, 0));
        mRightCubeValueTextView.setText(getResources().getQuantityString(R.plurals.scores,0, 0));
        mYourScores = 0;
        mComputerScores = 0;
        mLeftRandom = 0;
        mRightRandom = 0;
    }
}