package com.taskmaster;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class TaskDetail extends AppCompatActivity {

  @SuppressLint("RestrictedApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_detail);

    getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

    String taskName = getIntent().getStringExtra(MainActivity.TASKNAMA);
    TextView taskTitle = findViewById(R.id.taskDetailTitle);
    taskTitle.setText(taskName);
  }
}
