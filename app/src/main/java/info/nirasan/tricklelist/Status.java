package info.nirasan.tricklelist;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Calendar;
import java.util.Date;

@Table(name = "Statuses")
public class Status extends Model {

    @Column(name = "Habit")
    public Habit habit;

    @Column(name = "CreatedDate")
    public Date createdDate;

    @Column(name = "Score")
    public int score;

    public Status() {
        super();
    }

    public Status(Habit h, Date d, int s) {
        super();
        this.habit = h;
        this.createdDate = d;
        this.score = s;
    }

    public static Date getToday() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
}
