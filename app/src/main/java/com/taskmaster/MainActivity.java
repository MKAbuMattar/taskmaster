package com.taskmaster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button newTaskButton = findViewById(R.id.addTaskButton);
    newTaskButton.setOnClickListener(goToNewTaskCreator);

    Button allTasksButton = findViewById(R.id.allTasksButton);
    allTasksButton.setOnClickListener(goToAllTasks);
  }

  private View.OnClickListener goToNewTaskCreator = new View.OnClickListener() {
    public void onClick(View v) {
      Intent i = new Intent(getBaseContext(), AddTask.class);
      startActivity(i);
    }
  };

  private View.OnClickListener goToAllTasks = new View.OnClickListener() {
    public void onClick(View v) {
      Intent i = new Intent(getBaseContext(), AllTasks.class);
      startActivity(i);
    }
  };


}