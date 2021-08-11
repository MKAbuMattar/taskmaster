package com.taskmaster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taskmaster.model.Task;

import java.util.List;
import com.taskmaster.R;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

  private final List<Task> taskItems;
  private OnFoodItemClickListener listener;

  public TaskAdapter(List<Task> foodItems, OnFoodItemClickListener listener) {
    this.taskItems = foodItems;
    this.listener = listener;
  }

  public interface OnFoodItemClickListener {
    void onItemClicked(int position);
    void onDeleteItem(int position);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
    return new ViewHolder(view, listener);
  }

  @Override
  public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
    Task item = taskItems.get(position);
    holder.title.setText(item.getTitle());
    holder.body.setText(item.getBody());
    holder.status.setText(item.getStatus());
  }

  @Override
  public int getItemCount() {
    return taskItems.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView body;
    private TextView status;

    ViewHolder(@NonNull View itemView, OnFoodItemClickListener listener) {
      super(itemView);

      title = itemView.findViewById(R.id.title_label);
      body = itemView.findViewById(R.id.body_label);
      status = itemView.findViewById(R.id.status_label);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onItemClicked(getAdapterPosition());
        }
      });
    }
  }
}
