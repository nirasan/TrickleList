package info.nirasan.tricklelist;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
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
        return new Select()
                .from(Status.class)
                .where("Habit = ? and CreatedDate = ?", this.getId(), today.getTime())
                .execute();
    }

    public Status getTodayStatus() {
        return getTodayStatuses().get(0);
    }

    public boolean hasTodayStatus() {
        List<Status> statuses = getTodayStatuses();
        return statuses.size() > 0;
    }
}
