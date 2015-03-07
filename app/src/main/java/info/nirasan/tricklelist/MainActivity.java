package info.nirasan.tricklelist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static MainActivity instance;

    private String TAG = "v";

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
        instance = this;
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

    protected void deleteItem(int position) {
        Habit habit = habits.get(position);
        habit.delete();
        habits.remove(position);
        adapter.notifyDataSetChanged();
    }

    protected void setAdapters() {
        adapter = new HabitAdapter();
        listView.setAdapter(adapter);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView textView;
            ToggleButton toggleButton;
            Button deleteButton;
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.activity_main_row, null);
            }
            final Habit habit = (Habit)getItem(position);
            if (habit != null) {
                textView = (TextView)v.findViewById(R.id.textView);
                toggleButton = (ToggleButton)v.findViewById(R.id.toggleButton);
                deleteButton = (Button)v.findViewById(R.id.deleteButton);
                toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        habit.isChecked = isChecked;
                        habit.save();
                    }
                });
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.instance);
                        builder.setTitle("削除確認");
                        builder.setMessage("削除しますか？");
                        builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem(position);
                            }
                        });
                        builder.setNegativeButton("", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setCancelable(true);
                        builder.create().show();
                    }
                });
                textView.setText(habit.name);
                toggleButton.setChecked(habit.isChecked);
            }
            return v;
        }
    }
}
