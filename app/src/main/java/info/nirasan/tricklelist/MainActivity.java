package info.nirasan.tricklelist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.FontAwesomeText;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity {

    public static MainActivity instance;

    private String TAG = "MainActivity";

    ListView listView;
    BootstrapEditText editText;
    BootstrapButton addButton;
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
        editText = (BootstrapEditText)findViewById(R.id.editText);
        addButton = (BootstrapButton)findViewById(R.id.button);
    }

    protected void setListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(editText.getText().toString());
                editText.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Habit habit = habits.get(position);
                if (habit != null) {
                    Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                    intent.putExtra("HabitId", habit.getId());
                    startActivity(intent);
                }
            }
        });
    }

    protected void addItem(String name) {
        Habit habit = new Habit(name);
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
        habit.delete(); //habit.deleteWithStatus();
        habits.remove(position);
        adapter.notifyDataSetChanged();
    }

    protected void setAdapters() {
        adapter = new HabitAdapter();
        listView.setAdapter(adapter);
    }

    private class HabitAdapter extends BaseAdapter {
        List<String> toastWords = Arrays.asList("Great!", "Awesome!", "Wow!", "Cool!", "Way to go!", "Terrific!", "Right on!");
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
            com.activeandroid.util.Log.setEnabled(true);
            TextView textView;
            FontAwesomeText toggleButton;
            FontAwesomeText deleteButton;
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.activity_main_row, null);
            }
            final Habit habit = (Habit)getItem(position);
            if (habit != null) {
                textView = (TextView)v.findViewById(R.id.textView);
                toggleButton = (FontAwesomeText)v.findViewById(R.id.toggleButton);
                deleteButton = (FontAwesomeText)v.findViewById(R.id.deleteButton);

                final int checkedColor = getResources().getColor(R.color.bbutton_success);
                final int uncheckedColor = getResources().getColor(R.color.gray);
                boolean checked = habit.hasTodayStatus();
                toggleButton.setTextColor(checked ? checkedColor : uncheckedColor);
                toggleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FontAwesomeText t = (FontAwesomeText)v;
                        boolean checked = habit.hasTodayStatus();
                        if (checked) {
                            Status status = habit.getTodayStatus();
                            status.delete();
                        } else {
                            Status s = new Status(habit, Status.getToday(), 1);
                            s.save();
                            Random r = new Random();
                            String word = toastWords.get(r.nextInt(toastWords.size() - 1));
                            Toast.makeText(instance, word, Toast.LENGTH_SHORT).show();
                        }
                        t.setTextColor(!checked ? checkedColor : uncheckedColor);
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
                        builder.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setCancelable(true);
                        builder.create().show();
                    }
                });
                textView.setText(habit.name);
            }
            return v;
        }
    }
}
