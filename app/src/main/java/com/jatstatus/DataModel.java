package com.jatstatus;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataModel extends RecyclerView.Adapter<DataModel.ViewHolder> {

    List<ModelList> modelLists;
    private Context context;

    // Constructor.

    public DataModel(List<ModelList> modelLists, Context context) {
        this.modelLists = modelLists;
        this.context = context;
    }


    @NonNull
    @Override
    public DataModel.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.main_row_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataModel.ViewHolder holder, final int position) {
        final ModelList model = modelLists.get(position);
        holder.button.setText(model.getText());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, statusShow.class);
                intent.putExtra("model", model.getText());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.main_row_button);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int s = modelLists.size();
        if (s % 2 == 0)
            if (position == s)
                return 2;
            else
                return 1;
        else if (position == s - 1)
            return 2;
        else
            return 1;
    }
}