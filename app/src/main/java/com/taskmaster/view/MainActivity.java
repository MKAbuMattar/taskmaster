package com.taskmaster.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.taskmaster.R;
import com.taskmaster.adapter.TaskAdapter;
import com.taskmaster.dao.TaskDao;
import com.taskmaster.database.TaskDatabase;
//import com.taskmaster.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  public static final String TASK_TITLE = "taskTitle";
  public static final String TASK_BODY = "taskBody";
  public static final String TASK_STATUS = "taskStatus";
  public static final String TASK_LIST = "TaskList";

//  private TaskDatabase database;
//  private TaskDao taskDao;

  private static List<Task> taskList;
  private static TaskAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    try {
      Amplify.addPlugin(new AWSDataStorePlugin());
      Amplify.configure(getApplicationContext());

      Log.i("Tutorial", "Initialized Amplify");
    } catch (AmplifyException e) {
      Log.e("Tutorial", "Could not initialize Amplify", e);
    }

    setContentView(R.layout.activity_main);

//    database = Room.databaseBuilder(
//        this,
//        TaskDatabase.class,
//        TASK_LIST
//    ).allowMainThreadQueries().build();
//
//    taskDao = database.taskDao();


    RecyclerView taskRecyclerView = findViewById(R.id.List_tasks);
    taskList = new ArrayList<>();
//    taskList.add(new Task("Task 1","get some rest","new"));
//    taskList.add(new Task("Task 2","work on code challenge for today","assigned"));
//    taskList.add(new Task("Task 3","do lab work for today","in progress"));
//    taskList.add(new Task("Task 4","visit family","complete"));
//
    taskList = getDataFromAmplify();

    adapter = new TaskAdapter(taskList, new TaskAdapter.OnTaskItemClickListener() {
      @Override
      public void onItemClicked(int position) {
        Intent goToDetailsIntent = new Intent(getApplicationContext(), TaskDetail.class);
        goToDetailsIntent.putExtra(TASK_TITLE, taskList.get(position).getTitle());
        goToDetailsIntent.putExtra(TASK_BODY, taskList.get(position).getDescription());
        goToDetailsIntent.putExtra(TASK_STATUS, taskList.get(position).getStatus());
        startActivity(goToDetailsIntent);
      }

      @Override
      public void onDeleteItem(int position) {
//        taskDao.delete(taskList.get(position));

        Amplify.DataStore.delete(taskList.get(position),
            success -> Log.i("Tutorial", "item deleted: " + success.item().toString()),
            failure -> Log.e("Tutorial", "Could not query DataStore", failure));

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

    Button newTaskButton = findViewById(R.id.addTaskButton);
    newTaskButton.setOnClickListener(goToNewTaskCreator);

    Button allTasksButton = findViewById(R.id.allTasksButton);
    allTasksButton.setOnClickListener(goToAllTasks);

    Button makeTaskDetailsButton = findViewById(R.id.makeTaskDetailsButton);
    makeTaskDetailsButton.setOnClickListener(goToTaskDetail);

    Button makeTaskDetailsButton1 = findViewById(R.id.makeTaskDetailsButton1);
    makeTaskDetailsButton1.setOnClickListener(goToTaskDetail1);

    Button makeTaskDetailsButton2 = findViewById(R.id.makeTaskDetailsButton2);
    makeTaskDetailsButton2.setOnClickListener(goToTaskDetail2);

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
    getDataFromAmplify();
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
      Button makeTaskDetailsButton = findViewById(R.id.makeTaskDetailsButton);
      String buttonText = makeTaskDetailsButton.getText().toString();
      Intent i = new Intent(getBaseContext(), TaskDetail.class);
      i.putExtra(TASK_TITLE, buttonText);
      startActivity(i);
    }
  };

  private final View.OnClickListener goToTaskDetail1 = new View.OnClickListener() {
    public void onClick(View v) {
      Button makeTaskDetailsButton1 = findViewById(R.id.makeTaskDetailsButton1);
      String buttonText = makeTaskDetailsButton1.getText().toString();
      Intent i = new Intent(getBaseContext(), TaskDetail.class);
      i.putExtra(TASK_TITLE, buttonText);
      startActivity(i);
    }
  };

  private final View.OnClickListener goToTaskDetail2 = new View.OnClickListener() {
    public void onClick(View v) {
      Button makeTaskDetailsButton2 = findViewById(R.id.makeTaskDetailsButton2);
      String buttonText = makeTaskDetailsButton2.getText().toString();
      Intent i = new Intent(getBaseContext(), TaskDetail.class);
      i.putExtra(TASK_TITLE, buttonText);
      startActivity(i);
    }
  };

  private final View.OnClickListener goToSettings = new View.OnClickListener() {
    public void onClick(View v) {
      Intent i = new Intent(getBaseContext(), Settings.class);
      startActivity(i);
    }
  };

  public static void  saveDataToAmplify(String title,String body ,String status){
    Task item = Task.builder().title(title).description(body).status(status).build();

    Amplify.DataStore.save(item,
        success -> Log.i("Tutorial", "Saved item: " + success.item().toString()),
        error -> Log.e("Tutorial", "Could not save item to DataStore", error)
    );
    listItemDeleted();
  }

  public synchronized static List<Task> getDataFromAmplify(){
    System.out.println("In get data");
    List<Task> list = new ArrayList<>();
    Amplify.DataStore.query(Task.class,todos ->{
          while (todos.hasNext()) {
            Task todo = todos.next();
            list.add(todo);
            Log.i("Tutorial", "==== Task ====");
            Log.i("Tutorial", "Name: " + todo.getTitle());
            Log.i("Tutorial", "status: " + todo.getStatus());
            Log.i("Tutorial", "==== Task End ====");
          }
        },                failure -> Log.e("Tutorial", "Could not query DataStore", failure)

    );

    return list;
  }

  @SuppressLint("NotifyDataSetChanged")
  private static void listItemDeleted() {
    adapter.notifyDataSetChanged();
  }
}