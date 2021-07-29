package com.myapplication.todoapp.screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.todoapp.R;
import com.myapplication.todoapp.db.DatabaseClient;
import com.myapplication.todoapp.db.Model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListTodoAdapter extends RecyclerView.Adapter<ListTodoAdapter.GoalsViewHolder>{
    private Context context;
    private List<Model> modelList;

    public ListTodoAdapter(Context context, List<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @NotNull
    @Override
    public GoalsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_view, parent,false);
        return new GoalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListTodoAdapter.GoalsViewHolder holder, int position) {
        Model m = modelList.get(position);
        holder.goal.setText(m.getGoal());
        holder.category.setText(m.getCategory());

        if (m.isStatus()){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (holder.checkBox.isChecked()){
                updateCheckBox(m, b);
            }else{
                updateCheckBox(m,b);
            }
        });

        holder.delete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Are you sure?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                deleteGoal(m);
                modelList.remove(position);
                notifyDataSetChanged();
            });

            builder.setNegativeButton("No", ((dialogInterface, i) -> {

            }));
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


    class GoalsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView goal,category;
        private ImageButton delete;
        private CheckBox checkBox;

        public GoalsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            goal = itemView.findViewById(R.id.goal_list);
            category = itemView.findViewById(R.id.categories_list);
            checkBox = itemView.findViewById(R.id.status);
            delete = itemView.findViewById(R.id.deleteBtn);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Model model = modelList.get(getAdapterPosition());
            Intent intent = new Intent(context,UpdateGoalActivity.class);
            intent.putExtra("model", model);
            context.startActivity(intent);
        }
    }
    public void updateCheckBox(Model m, boolean b){
        Thread thread = new Thread(()->{
            DatabaseClient.getInstance(context.getApplicationContext())
                    .getAppDatabase()
                    .modelDao()
                    .findModelByGoal(m.getGoal());

            m.setStatus(b);

            DatabaseClient.getInstance(context.getApplicationContext())
                    .getAppDatabase()
                    .modelDao()
                    .update(m);
        });
        thread.start();
        Toast.makeText(context.getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
    }

    private void deleteGoal(final Model model){
        @SuppressLint("StaticFieldLeak")
        class DeleteGoal extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context.getApplicationContext())
                        .getAppDatabase()
                        .modelDao()
                        .delete(model);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Toast.makeText(context.getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
            }
        }
        DeleteGoal dg = new DeleteGoal();
        dg.execute();
    }



}
