package info.nirasan.tricklelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.beardedhen.androidbootstrap.FontAwesomeText;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class StatusActivity extends ActionBarActivity {

    static final int dayNumber = 28;

    Habit habit;
    List<Status> statuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        setHabit();
        setStatuses();
        renderLayout();
    }

    void setHabit() {
        Intent intent = getIntent();
        Long habitId = intent.getLongExtra("HabitId", 0);
        habit = Habit.load(Habit.class, habitId);
        Toast.makeText(this, habit.name, Toast.LENGTH_LONG).show();
    }

    void setStatuses() {
        statuses = new Select()
                .from(Status.class)
                .where("Habit = ?", habit.getId())
                .orderBy("CreatedDate DESC")
                .limit(dayNumber)
                .execute();
    }

    void renderLayout() {
        int statusesIndex = 0;
        Date today = Status.getToday();
        Calendar calender = Calendar.getInstance();
        calender.setTime(today);
        int prevId = 0;
        RelativeLayout parent = (RelativeLayout)findViewById(R.id.relativeLayout);
        for (int i = 0; i < dayNumber; i++) {
            // create Date
            boolean done = false;
            Date date = calender.getTime();
            if (statuses.size() > statusesIndex) {
                Status status = statuses.get(statusesIndex);
                if (status.createdDate.equals(date)) {
                    statusesIndex += 1;
                    done = true;
                }
            }
            // create FontAwesomeText
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            FontAwesomeText t = new FontAwesomeText(this);
            int id = i + 1;
            t.setId(id);
            t.setClickable(true);
            t.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            if (done) {
                t.setIcon("fa-smile-o");
                t.setTextColor(getResources().getColor(R.color.bbutton_success));
            } else {
                t.setIcon("fa-meh-o");
                t.setTextColor(getResources().getColor(R.color.gray));
            }
            // ディスプレイの横幅を取得
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            final float width = displayMetrics.widthPixels / displayMetrics.density;
            // 左端要素の margin left を取得
            int marginTop = 30;
            int marginRight = 30;
            int marginLeft = (int)((width - (t.getWidth() / displayMetrics.density * 7) - (marginRight * 6)) / 2);
            // 位置パラメータのセット
            if (prevId == 0) {
                p.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.TRUE);
                p.setMargins(marginLeft, marginTop, marginRight, 0);
            } else {
                if (i % 7 == 0) {
                    p.addRule(RelativeLayout.BELOW, prevId);
                    p.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.TRUE);
                    p.setMargins(marginLeft, marginTop, marginRight, 0);
                } else {
                    p.addRule(RelativeLayout.ALIGN_TOP, prevId);
                    p.addRule(RelativeLayout.RIGHT_OF, prevId);
                    p.setMargins(0, 0, marginRight, 0);
                }
            }
            parent.addView(t, p);
            prevId = id;
            calender.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}
