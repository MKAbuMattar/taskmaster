package com.taskmaster;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Objects;

public class AllTasks extends AppCompatActivity {

  @SuppressLint("RestrictedApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_all_tasks);

    getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
  }

}
