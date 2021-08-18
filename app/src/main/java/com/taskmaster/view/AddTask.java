package com.taskmaster.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.room.Room;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.datastore.generated.model.Task;
import com.taskmaster.R;
import com.taskmaster.dao.TaskDao;
import com.taskmaster.database.TaskDatabase;
//import com.taskmaster.model.Task;

public class AddTask extends AppCompatActivity {

  private static final String TAG = "AddTask";
  private String spinner_task_status=null;

//  private TaskDatabase database;
//  private TaskDao taskDao;

  @SuppressLint("RestrictedApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_task);

//    database = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, MainActivity.TASK_LIST)
//        .allowMainThreadQueries().build();
//    taskDao = database.taskDao();

    Spinner spinner = findViewById(R.id.spinner_status);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.task_status_array, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinner_task_status = (String) parent.getItemAtPosition(position);
        System.out.println(spinner_task_status);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        spinner_task_status = (String) parent.getItemAtPosition(0);
      }
    });

    TextView successLabel = findViewById(R.id.newTaskSubmitSuccess);
    successLabel.setVisibility(View.GONE);

    Button newTaskCreateButton = findViewById(R.id.newTaskSubmit);
    newTaskCreateButton.setOnClickListener(newTaskCreateListener);

    getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

  }

  private final View.OnClickListener newTaskCreateListener = new View.OnClickListener() {
    public void onClick(View v) {
      String taskTitle = ((EditText) findViewById(R.id.newTaskName)).getText().toString();
      String taskBody = ((EditText) findViewById(R.id.newTaskBody)).getText().toString();
      String taskStatus = spinner_task_status;


      Task item = Task.builder().title(taskTitle).description(taskBody).status(taskStatus).build();
      MainActivity.saveTaskToAPI(item);

//      taskDao.insertOne(new Task(taskTitle,taskBody,taskStatus));
//      MainActivity.saveDataToAmplify(taskTitle, taskBody, taskStatus);

      TextView successLabel = findViewById(R.id.newTaskSubmitSuccess);
      successLabel.setVisibility(View.VISIBLE);
    }
  };



}
