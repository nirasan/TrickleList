package info.nirasan.tricklelist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    ListView listView;
    EditText editText;
    Button addButton;
    static List<Habit> habits;
    HabitAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this);
        setContentView(R.layout.activity_main);
        findViews();
        setListeners();
        getItems();
        setAdapters();
    }

    protected void findViews() {
        listView = (ListView)findViewById(R.id.listView);
        editText = (EditText)findViewById(R.id.editText);
        addButton = (Button)findViewById(R.id.button);
    }

    protected void setListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(editText.getText().toString());
            }
        });
    }

    protected void addItem(String name) {
        Habit habit = new Habit(name, false);
        habit.save();
        habits.add(habit);
        adapter.notifyDataSetChanged();
    }

    protected void getItems() {
        habits = new Select()
                .from(Habit.class)
                .execute();
    }

    protected void setAdapters() {
        adapter = new HabitAdapter();
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class HabitAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return habits.size();
        }
        @Override
        public Object getItem(int position) {
            return habits.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            ToggleButton toggleButton;
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.activity_main_row, null);
            }
            Habit habit = (Habit)getItem(position);
            if (habit != null) {
                textView = (TextView)v.findViewById(R.id.textView);
                toggleButton = (ToggleButton)v.findViewById(R.id.toggleButton);
                textView.setText(habit.name);
                toggleButton.setChecked(habit.isChecked);
            }
            return v;
        }
    }
}
