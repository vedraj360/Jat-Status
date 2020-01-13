package com.jatstatus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class statusShowAdapter extends RecyclerView.Adapter<statusShowAdapter.ViewHolder> {
    private List<Sheet1> sheet1s;
    String title;
    private Context context;

    statusShowAdapter(List<Sheet1> sheet1s, String title, Context context) {
        this.sheet1s = sheet1s;
        this.title = title;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.status_show_row_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Sheet1 sheet1 = sheet1s.get(position);
        String value = sheet1.getStatus();
        String s = getFirst5Words(value);
        String ButtonValue = s + "...";
        holder.button.setText(ButtonValue);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SwipeData.class);
                String[] data = new String[sheet1s.size()];
                for (int i = 0; i < sheet1s.size(); i++) {
                    Sheet1 sheet = sheet1s.get(i);
                    data[i] = sheet.getStatus();
                }
                intent.putExtra("DATA", data);
                intent.putExtra("Position", position);

                intent.putExtra("Title", title);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sheet1s.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        Button button;

        ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.status_row_button);
        }
    }

    private String getFirst5Words(String arg) {
        Pattern pattern = Pattern.compile("([\\S]+\\s*){1,6}");
        Matcher matcher = pattern.matcher(arg);
        matcher.find();
        return matcher.group();
    }

}
