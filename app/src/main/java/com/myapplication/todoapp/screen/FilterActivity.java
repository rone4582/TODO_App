package com.myapplication.todoapp.screen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.todoapp.R;
import com.myapplication.todoapp.db.DatabaseClient;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {
    RecyclerView filterByValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ListView filterType = findViewById(R.id.filterType);
        filterByValue = findViewById(R.id.filterByValue);
        Button apply = findViewById(R.id.apply_filter);

        List<String> filterOption = new ArrayList<>();
        filterOption.add("Category");
        filterOption.add("Status");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,filterOption);
        filterType.setAdapter(arrayAdapter);

        filterByValue.setLayoutManager(new LinearLayoutManager(this));
        getGoal();

        filterType.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i){
                case 0:
                        filterByValue.setLayoutManager(new LinearLayoutManager(this));
                        getGoal();
                        break;

                case 1:
                        filterByValue.setLayoutManager(new LinearLayoutManager(this));
                        statusOptions();
            }
        });

        apply.setOnClickListener(view -> {
            Intent intent = new Intent(FilterActivity.this,ListTodo.class);
            if (!FilterAdapter.filterSelected.isEmpty()){
                intent.putStringArrayListExtra("filterList",FilterAdapter.filterSelected);
            }
            if (!FilterAdapterStatus.checkBoxSelected.isEmpty()){
                intent.putStringArrayListExtra("statusFilter", FilterAdapterStatus.checkBoxSelected);
            }
            startActivity(intent);
            Toast.makeText(FilterActivity.this, "Filter Selected", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.clear_filter){
            Toast.makeText(FilterActivity.this, "clear", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getGoal(){
        class GetGoals extends AsyncTask<Void, Void, List<String>> {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected List<String> doInBackground(Void... voids) {
                List<String> modelList = DatabaseClient.getInstance(getApplicationContext())
                        .getAppDatabase()
                        .modelDao()
                        .getDistinctCategory();
                return modelList;
            }

            @Override
            protected void onPostExecute(List<String> model) {
                super.onPostExecute(model);
                FilterAdapter adapter = new FilterAdapter(FilterActivity.this, model);
                filterByValue.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
        GetGoals getGoals = new GetGoals();
        getGoals.execute();
    }

    private void statusOptions(){
        List<String> statusOption = new ArrayList<>();
        statusOption.add("Completed");
        statusOption.add("Incomplete");

        FilterAdapterStatus filterAdapter = new FilterAdapterStatus(this, statusOption);
        filterByValue.setAdapter(filterAdapter);
        filterAdapter.notifyDataSetChanged();
    }
}