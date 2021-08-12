package com.taskmaster.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.taskmaster.R;
import com.taskmaster.adapter.TaskAdapter;
import com.taskmaster.dao.TaskDao;
import com.taskmaster.database.TaskDatabase;
import com.taskmaster.model.Task;

import java.util.List;

public class AllTasks extends AppCompatActivity {

  public static final String TASK_TITLE = "taskTitle";
  public static final String TASK_BODY = "taskBody";
  public static final String TASK_STATUS = "taskStatus";
  public static final String TASK_LIST = "TaskList";

  private TaskDatabase database;
  private TaskDao taskDao;

  private List<Task> taskList;
  private TaskAdapter adapter;

  @SuppressLint("RestrictedApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_all_tasks);

    getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

    database = Room.databaseBuilder(
        this,
        TaskDatabase.class,
        TASK_LIST
    ).allowMainThreadQueries().build();

    taskDao = database.taskDao();


    RecyclerView taskRecyclerView = findViewById(R.id.List_tasks);

    taskList = taskDao.findAll();

    adapter = new TaskAdapter(taskList, new TaskAdapter.OnTaskItemClickListener() {
      @Override
      public void onItemClicked(int position) {
        Intent goToDetailsIntent = new Intent(getApplicationContext(), TaskDetail.class);
        goToDetailsIntent.putExtra(TASK_TITLE, taskList.get(position).getTitle());
        goToDetailsIntent.putExtra(TASK_BODY, taskList.get(position).getBody());
        goToDetailsIntent.putExtra(TASK_STATUS, taskList.get(position).getStatus());
        startActivity(goToDetailsIntent);
      }

      @Override
      public void onDeleteItem(int position) {
        taskDao.delete(taskList.get(position));
        taskList.remove(position);
        listItemDeleted();
      }
    });

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
        this,
        LinearLayoutManager.VERTICAL,
        false);

    taskRecyclerView.setLayoutManager(linearLayoutManager);
    taskRecyclerView.setAdapter(adapter);

  }


  @SuppressLint("NotifyDataSetChanged")
  private void listItemDeleted() {
    adapter.notifyDataSetChanged();
  }
}
