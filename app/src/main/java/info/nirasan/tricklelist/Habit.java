package info.nirasan.tricklelist;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

@Table(name = "Habits")
public class Habit extends Model{

    @Column(name = "Name")
    public String name;

    public Habit() {
        super();
    }

    public Habit(String name) {
        super();
        this.name = name;
    }

    public List<Status> getTodayStatuses() {
        Date today = Status.getToday();
        Log.d("Habit", String.valueOf(today.getTime()));
        From f = new Select()
                .from(Status.class)
                .where("Habit = ? and CreatedDate = ?", this.getId(), today.getTime());
        Log.d("Habit", f.toString());
        return f.execute();
    }

    public Status getTodayStatus() {
        return getTodayStatuses().get(0);
    }

    public boolean hasTodayStatus() {
        List<Status> statuses = getTodayStatuses();
        return statuses.size() > 0;
    }

    public void deleteWithStatus() {
        new Delete().from(Status.class).where("Habit = ?", this.getId()).execute();
        this.delete();
    }
}
