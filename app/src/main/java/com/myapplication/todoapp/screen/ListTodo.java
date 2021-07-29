package com.myapplication.todoapp.screen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.todoapp.MainActivity;
import com.myapplication.todoapp.R;
import com.myapplication.todoapp.db.DatabaseClient;
import com.myapplication.todoapp.db.Model;

import java.util.ArrayList;
import java.util.List;

public class ListTodo extends AppCompatActivity {
    private RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_todo);

        recyclerView = findViewById(R.id.rv_list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> categoryIntent = getIntent().getStringArrayListExtra("filterList");
        List<String> statusFilter = getIntent().getStringArrayListExtra("statusFilter");

        if (categoryIntent!=null && statusFilter ==null){
            filteredGoal(categoryIntent);
        }else if (statusFilter != null){
            List<Boolean> booleanList = new ArrayList<>();
            boolean b;
            for (String s:statusFilter){
                if (s.equalsIgnoreCase("completed")){
                    b = true;
                    booleanList.add(b);
                }else {
                    b = false;
                    booleanList.add(b);
                }
            }
            filterStatusCompleted(booleanList);
        }else {
            getGoal();
        }
    }

    private void getGoal(){
        class GetGoals extends AsyncTask<Void, Void, List<Model>>{
            @Override
            protected List<Model> doInBackground(Void... voids) {
                List<Model> modelList = DatabaseClient.getInstance(getApplicationContext())
                        .getAppDatabase()
                        .modelDao()
                        .getAll();
                return modelList;
            }

            @Override
            protected void onPostExecute(List<Model> model) {
                super.onPostExecute(model);
                ListTodoAdapter adapter = new ListTodoAdapter(ListTodo.this, model);
                recyclerView.setAdapter(adapter);
            }
        }
        GetGoals getGoals = new GetGoals();
        getGoals.execute();
    }

    private void filteredGoal(List<String> toFilter) {
        Thread t = new Thread(()->{
            List<Model> fromFilterByCategory = DatabaseClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .modelDao()
                    .getFilterByCategory(toFilter);

            ListTodoAdapter adapter = new ListTodoAdapter(ListTodo.this, fromFilterByCategory);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
        t.start();
    }

    private void filterStatusCompleted(List<Boolean> booleanList) {
        Thread thread = new Thread(() -> {
            List<Model> filterByStatus = DatabaseClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .modelDao()
                    .filterByStatus(booleanList);

            ListTodoAdapter adapter = new ListTodoAdapter(ListTodo.this, filterByStatus);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
        thread.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ListTodo.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.filterBtn){
            Intent intent = new Intent(ListTodo.this,FilterActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}