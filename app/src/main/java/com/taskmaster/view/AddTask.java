package com.taskmaster.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.room.Room;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.taskmaster.R;

import java.util.Objects;

public class AddTask extends AppCompatActivity {

  private static final String TAG = "AddTask";
  private String spinner_task_status = null;
  private String teamName = null;
  private Team teamData = null;

  @SuppressLint("RestrictedApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_task);

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

    Objects.requireNonNull(getSupportActionBar()).setDefaultDisplayHomeAsUpEnabled(true);

  }

  private final View.OnClickListener newTaskCreateListener = v -> {
    String taskTitle = ((EditText) findViewById(R.id.newTaskName)).getText().toString();
    String taskBody = ((EditText) findViewById(R.id.newTaskBody)).getText().toString();
    String taskStatus = spinner_task_status;


    getTeamDetailFromAPIByName(teamName);

    try {
      Thread.sleep(1500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Task item = Task.builder().title(taskTitle).description(taskBody).team(teamData).status(taskStatus).build();
    MainActivity.saveTaskToAPI(item);

    TextView successLabel = findViewById(R.id.newTaskSubmitSuccess);
    successLabel.setVisibility(View.VISIBLE);
  };

  @SuppressLint("NonConstantResourceId")
  public void onClickRadioButton(View view) {
    boolean checked = ((RadioButton) view).isChecked();
    switch (view.getId()) {
      case R.id.team1:
        if (checked)
          Log.i(TAG, "onClickRadioButton: team 1");
        teamName = "team 1";
        break;
      case R.id.team2:
        if (checked)
          Log.i(TAG, "onClickRadioButton: team 2");
        teamName = "team 2";
        break;
      case R.id.team3:
        if (checked)
          Log.i(TAG, "onClickRadioButton: team 3");
        teamName = "team 3";
        break;
    }
  }

  public void getTeamDetailFromAPIByName(String name) {
    Amplify.API.query(
        ModelQuery.list(Team.class, Team.NAME.contains(name)),
        response -> {
          for (Team teamDetail : response.getData()) {
            Log.i(TAG, teamDetail.getName());
            teamData = teamDetail;
          }
        },
        error -> Log.e(TAG, "Query failure", error)
    );
  }

}
