package info.nirasan.tricklelist;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.beardedhen.androidbootstrap.FontAwesomeText;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class StatusActivity extends ActionBarActivity {

    static final int dayNumber = 30;

    Habit habit;
    List<Status> statuses;

    int successCount7days = 0;
    int successCount30days = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        setHabit();
        setStatuses();
        createTexts();
    }

    void setHabit() {
        Intent intent = getIntent();
        Long habitId = intent.getLongExtra("HabitId", 0);
        habit = Habit.load(Habit.class, habitId);
        //Toast.makeText(this, habit.name, Toast.LENGTH_LONG).show();
        TextView textView = (TextView)findViewById(R.id.habitNameTextView);
        textView.setText(habit.name);
    }

    void setStatuses() {
        statuses = new Select()
                .from(Status.class)
                .where("Habit = ?", habit.getId())
                .orderBy("CreatedDate DESC")
                .limit(dayNumber)
                .execute();
    }

    void createTexts() {
        int statusesIndex = 0;
        Date today = Status.getToday();
        Calendar calender = Calendar.getInstance();
        calender.setTime(today);
        RelativeLayout parent = (RelativeLayout) findViewById(R.id.relativeLayout);
        int prevId = 0;
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
            // count success
            if (done) {
                if (i < 7)  { successCount7days  += 1; }
                if (i < 30) { successCount30days += 1; }
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
            t.setAlpha(0);
            // ディスプレイの横幅を取得
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            final float width = displayMetrics.widthPixels / displayMetrics.density;
            String TAG = "MainActivity";
            // 左端要素の margin left を取得
            int textWidth = 31; //(int)(t.getMeasuredWidth() / displayMetrics.density);
            int marginTop = 10;
            int marginRight = 10;
            int marginLeft = (int)((width - (textWidth * 7 + marginRight * 6 + 32)) / 2);
            int marginTopPx = (int)(marginTop * displayMetrics.density);
            int marginRightPx = (int)(marginRight * displayMetrics.density);
            int marginLeftPx = (int)(marginLeft * displayMetrics.density);
            // 位置パラメータのセット
            if (prevId == 0) {
                p.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.TRUE);
                p.setMargins(marginLeftPx, marginTopPx, marginRightPx, 0);
            } else {
                if (i % 7 == 0) {
                    p.addRule(RelativeLayout.BELOW, prevId);
                    p.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.TRUE);
                    p.setMargins(marginLeftPx, marginTopPx, marginRightPx, 0);
                } else {
                    p.addRule(RelativeLayout.ALIGN_TOP, prevId);
                    p.addRule(RelativeLayout.RIGHT_OF, prevId);
                    p.setMargins(0, 0, marginRightPx, 0);
                }
            }
            // alphaプロパティを0fから1fに変化させる
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(t, "alpha", 0f, 1f);
            objectAnimator.setDuration(i * 500);
            objectAnimator.start();
            // 前の要素の更新
            prevId = t.getId();
            parent.addView(t, p);
            calender.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 成功率の表示
        int rate7days = successCount7days * 100 / 7;
        TextView successRate7days = (TextView)findViewById(R.id.successRate7days);
        successRate7days.setText(String.format("%3d%%", rate7days));
        int rate30days = successCount30days * 100 / 30;
        TextView successRate30days = (TextView)findViewById(R.id.successRate30days);
        successRate30days.setText(String.format("%3d%%", rate30days));
    }
}
