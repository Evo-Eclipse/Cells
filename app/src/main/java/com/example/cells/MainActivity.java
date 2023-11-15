package com.example.cells;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button[][] cells;
    Boolean[][] land;
    int WIDTH = 5;
    int HEIGHT = 5;
    int totalmines = 10;
    int landmines = 10;
    int marks = 12;
    byte STATUS = 0;
    Random random = new Random();
    TextView mines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generate();
        mines = (TextView) findViewById(R.id.Mines);
        mines.setText("" + totalmines + "/" + landmines);
    }

    private int sonar(int i, int j) {
        int count = 0;

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                int ni = i + x;
                int nj = j + y;
                if (ni >= 0 && ni < HEIGHT && nj >= 0 && nj < WIDTH && land[ni][nj]) {
                    count++;
                }
            }
        }
        return count;
    }

    public void generate() {
        cells = new Button[HEIGHT][WIDTH];
        land = new Boolean[HEIGHT][WIDTH];

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                land[i][j] = false;
            }
        }

        int boxmines = landmines;

        while (boxmines > 0) {
            int i = random.nextInt(HEIGHT);
            int j = random.nextInt(WIDTH);

            if (!land[i][j]) {
                land[i][j] = true;
                boxmines --;
            }
        }

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        GridLayout cellsLayout = (GridLayout) findViewById(R.id.Grid);
        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(WIDTH);

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                cells[i][j] = (Button) inflater.inflate(R.layout.cell, cellsLayout, false);
                final int x = i;
                final int y = j;

                cells[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (STATUS == 0) {
                            if (land[x][y]) {
                                mines.setText("Бум 💥");
                                view.setBackgroundColor(Color.RED);
                                ((Button) view).setText("💥");
                                STATUS = -1; // lost
                            } else {
                                int count = sonar(x, y);
                                ((Button) view).setText(String.valueOf(count));
                                view.setBackgroundColor(Color.CYAN);
                            }
                        }
                    }
                });

                cells[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (STATUS == 0) {
                            ((Button) view).setText("⚐");
                            view.setBackgroundColor(Color.YELLOW);
                            marks --;
                            if (land[x][y]) landmines--;
                            mines.setText("" + landmines + "/" + totalmines);
                            if (landmines == 0) {
                                if (marks < 0) {
                                    mines.setText("Поражение - слишком много флажков");
                                } else {
                                    mines.setText("Победа ✨");
                                }
                                STATUS = 1;
                            }
                        }
                        return true;
                    }
                });

                cellsLayout.addView(cells[i][j]);
            }
        }
    }
}