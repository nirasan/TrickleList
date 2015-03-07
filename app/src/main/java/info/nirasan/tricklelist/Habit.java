package info.nirasan.tricklelist;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Habits")
public class Habit extends Model{

    @Column(name = "Name")
    public String name;
    @Column(name = "IsChecked")
    public Boolean isChecked;

    public Habit() {
        super();
    }

    public Habit(String name, Boolean isChecked) {
        super();
        this.name = name;
        this.isChecked = isChecked;
    }
}
