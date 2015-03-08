package info.nirasan.tricklelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;


public class StatusActivity extends ActionBarActivity {

    Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        Intent intent = getIntent();
        Long habitId = intent.getLongExtra("HabitId", 0);

        habit = Habit.load(Habit.class, habitId);

        Toast.makeText(this, habit.name, Toast.LENGTH_LONG).show();
    }
}
