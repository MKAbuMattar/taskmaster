package com.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button newTaskButton = findViewById(R.id.addTaskButton);
    newTaskButton.setOnClickListener(goToNewTaskCreator);

    Button allTasksButton = findViewById(R.id.allTasksButton);
    allTasksButton.setOnClickListener(goToAllTasks);

    Button makeDinnerButton = findViewById(R.id.makeTaskDetailsButton);
    makeDinnerButton.setOnClickListener(goToTaskDetail);

    Button settingsButton = findViewById(R.id.settingsButton);
    settingsButton.setOnClickListener(goToSettings);
  }

  @Override
  protected void onResume() {
    super.onResume();

    SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    String username = preference.getString("username", "user") + "'s Tasks";
    TextView userLabel = findViewById(R.id.userTasksLabel);
    userLabel.setText(username);
  }

  private final View.OnClickListener goToNewTaskCreator = new View.OnClickListener() {
    public void onClick(View v) {
      Intent i = new Intent(getBaseContext(), AddTask.class);
      startActivity(i);
    }
  };

  private final View.OnClickListener goToAllTasks = new View.OnClickListener() {
    public void onClick(View v) {
      Intent i = new Intent(getBaseContext(), AllTasks.class);
      startActivity(i);
    }
  };

  private final View.OnClickListener goToTaskDetail = new View.OnClickListener() {
    public void onClick(View v) {
      Intent i = new Intent(getBaseContext(), TaskDetail.class);
      startActivity(i);
    }
  };

  private final View.OnClickListener goToSettings = new View.OnClickListener() {
    public void onClick(View v) {
      Intent i = new Intent(getBaseContext(), Settings.class);
      startActivity(i);
    }
  };
}