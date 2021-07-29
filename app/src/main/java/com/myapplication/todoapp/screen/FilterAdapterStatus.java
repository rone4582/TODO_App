package com.myapplication.todoapp.screen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.todoapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapterStatus extends RecyclerView.Adapter<FilterAdapterStatus.viewHolder> {
    Context context;
    List<String> stringList;
    public static ArrayList<String> checkBoxSelected = new ArrayList<>();

    public FilterAdapterStatus(Context context, List<String> modelList) {
        this.context = context;
        this.stringList = modelList;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_filter_type, parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FilterAdapterStatus.viewHolder holder, int position) {
        String s = stringList.get(position);
        holder.statusOption.setText(s);

        if (checkBoxSelected.contains(s)){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setEnabled(false);

        holder.linearLayout.setOnClickListener(view -> {
            if (!holder.checkBox.isChecked()){
                checkBoxSelected.add(s);
                holder.checkBox.setChecked(true);
                Toast.makeText(context.getApplicationContext(), s +" added ", Toast.LENGTH_SHORT).show();
            }else {
                notifyDataSetChanged();
                checkBoxSelected.remove(s);
                holder.checkBox.setChecked(false);
                Toast.makeText(context.getApplicationContext(), s +" removed ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView statusOption;
        CheckBox checkBox;
        LinearLayout linearLayout;

        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout);
            statusOption = itemView.findViewById(R.id.filter_single_type);
            checkBox = itemView.findViewById(R.id.filter_type_checkbox);
        }
    }
}
