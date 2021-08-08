package com.taskmaster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddTask extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_task);

    TextView successLabel = findViewById(R.id.newTaskSubmitSuccess);
    successLabel.setVisibility(View.GONE);

    Button newTaskCreateButton = findViewById(R.id.newTaskSubmit);
    newTaskCreateButton.setOnClickListener(newTaskCreateListener);
  }

  private View.OnClickListener newTaskCreateListener = new View.OnClickListener() {
    public void onClick(View v) {
      TextView successLabel = findViewById(R.id.newTaskSubmitSuccess);
      successLabel.setVisibility(View.VISIBLE);
    }
  };
}
