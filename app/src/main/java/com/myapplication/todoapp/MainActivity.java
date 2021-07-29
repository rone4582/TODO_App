package com.myapplication.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.myapplication.todoapp.db.DatabaseClient;
import com.myapplication.todoapp.db.Model;
import com.myapplication.todoapp.screen.ListTodo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextInputEditText goalEditText,categoriesEditText;
    public static ArrayList<Model> modelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goalEditText = findViewById(R.id.goal);
        categoriesEditText = findViewById(R.id.categories);
        Button add =  findViewById(R.id.add_goal);
        Button openListOfTodo = findViewById(R.id.todo_list);

        modelArrayList = new ArrayList<>();

        add.setOnClickListener(view -> {
            saveGoal();
        });

        openListOfTodo.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ListTodo.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_LONG).show();
    }

    private void saveGoal() {
        final String sGoal = goalEditText.getEditableText().toString().trim();
        final String sCategory = categoriesEditText.getEditableText().toString().trim();

        if (sGoal.isEmpty()) {
            goalEditText.setError("Goal required");
            goalEditText.requestFocus();
            return;
        }

        Thread thread = new Thread(() -> {
            Model model = new Model();
            model.setGoal(sGoal);

            if (!sCategory.isEmpty()) {
                model.setCategory(sCategory);
            } else {
                model.setCategory("Others");
            }

            DatabaseClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .modelDao()
                    .insert(model);

        });
        thread.start();

        Toast.makeText(getApplicationContext(), "Goal Saved", Toast.LENGTH_SHORT).show();
        goalEditText.setText("");
        categoriesEditText.setText("");
    }
}
