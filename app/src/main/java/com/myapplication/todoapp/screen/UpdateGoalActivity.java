package com.myapplication.todoapp.screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myapplication.todoapp.R;
import com.myapplication.todoapp.db.DatabaseClient;
import com.myapplication.todoapp.db.Model;

public class UpdateGoalActivity extends AppCompatActivity {

    private EditText editTextTask, editTextDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_goal);

        editTextTask = findViewById(R.id.editTextTask);
        editTextDesc = findViewById(R.id.editTextDesc);
        Button update = findViewById(R.id.button_update);


        final Model model = (Model) getIntent().getSerializableExtra("model");

        loadGoal(model);

        update.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
            updateGoal(model);
        });
    }

    private void loadGoal(Model model){
        editTextTask.setText(model.getGoal());
        editTextDesc.setText(model.getCategory());
    }

    private void updateGoal(final Model model){
        final String sGoal = editTextTask.getText().toString().trim();
        final String sCategory = editTextDesc.getText().toString().trim();

        if (sGoal.isEmpty()){
            editTextTask.setError("Goal required");
            editTextTask.requestFocus();
            return;
        }

        @SuppressLint("StaticFieldLeak")
        class UpdateGoal extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                model.setGoal(sGoal);
                if (sCategory.isEmpty()){
                    model.setCategory("Others");
                }else {
                    model.setCategory(sCategory);
                }

                DatabaseClient.getInstance(getApplicationContext())
                        .getAppDatabase()
                        .modelDao()
                        .update(model);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdateGoalActivity.this, ListTodo.class));
                finish();
            }
        }
        UpdateGoal ug = new UpdateGoal();
        ug.execute();
    }
}