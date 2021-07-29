package com.myapplication.todoapp.screen;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.todoapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.viewHolder> {
    Context context;
    List<String> list;
    static ArrayList<String> filterSelected = new ArrayList<>();

    public FilterAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_filter_type, parent,false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull @NotNull FilterAdapter.viewHolder holder, int position) {
        String m = list.get(position);
        holder.filter_type.setText(m);

        if (filterSelected.contains(m)){
            holder.filter_type_checkbox.setChecked(true);
        }else{
            holder.filter_type_checkbox.setChecked(false);
        }

        holder.filter_type_checkbox.setEnabled(false);

        holder.layout.setOnClickListener(view -> {
            if (!holder.filter_type_checkbox.isChecked()){
                filterSelected.add(m);
                holder.filter_type_checkbox.setChecked(true);
                Toast.makeText(context.getApplicationContext(), m +" added ", Toast.LENGTH_SHORT).show();
            }else {
                    filterSelected.remove(m);
                holder.filter_type_checkbox.setChecked(false);
                Toast.makeText(context.getApplicationContext(), m +" removed ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView filter_type;
        CheckBox filter_type_checkbox;
        LinearLayout layout;
        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            filter_type = itemView.findViewById(R.id.filter_single_type);
            filter_type_checkbox = itemView.findViewById(R.id.filter_type_checkbox);
        }
    }
}
