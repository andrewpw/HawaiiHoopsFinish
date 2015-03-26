package models;

import org.joda.time.DateTime;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * A model representing a duration in time
 * @author taylorak
 */
@Entity
@Table(name = "court_hours")
public class Hours extends Model {

    @Id
    @GeneratedValue
    private Long id;

    private int start_day;

    private DateTime start_time;
    private DateTime end_time;

    @ManyToMany
    private List<Court> courts = new ArrayList<>();

    /**
    * The EBean ORM finder method for database queries on Hours.
    **/
    public static Finder<Long, Hours> find = new Finder<>(
           Long.class, Hours.class
    );

    /**
     * Class constructor specifying the start and end times as joda DateTime objects.
     * @param start_time joda DateTime object with hour and minutes set to start time.
     * @param end_time joda DateTime object with hour and minutes set to end time.
     */
    public Hours(DateTime start_time, DateTime end_time) {
       this.start_time = start_time;
       this.end_time = end_time;
       this.start_day = start_time.getDayOfWeek();
    }

    /**
     * Class constructor specifying the start and end times as integers.
     * @param start_day the day when the event starts.
     * @param start_hour the hour when the event starts.
     * @param start_minutes the minute when the event starts.
     * @param end_day the day when the event ends. Should be the same as the start day.
     * @param end_hour the hour when the event ends.
     * @param end_minutes the minute when the event ends.
     */
    public Hours(int start_day, int start_hour, int start_minutes, int end_day, int end_hour, int end_minutes) {
        this.start_time = new DateTime(0,1,1,0,0,0,0).withDayOfWeek(start_day).withTime(start_hour, start_minutes, 0, 0);
        this.end_time = new DateTime(0,1,1,0,0,0,0).withDayOfWeek(end_day).withTime(end_hour, end_minutes, 0, 0);
        this.start_day = start_day;
    }

    /**
     * Adds hours to the hours database.
     * @param start_day the day of the week when the event starts. 1 == Monday, 2 == Tuesday, etc..
     * @param start_hour the hour when the event starts.
     * @param start_minutes the minute when the event starts.
     * @param end_day the day of the week when the event ends. Should be the same as the start day.
     * @param end_hour the hour when the event ends.
     * @param end_minutes the minute when the event ends.
     * @return the hours added to the database.
     */
    public static Hours addDuration(int start_day, int start_hour, int start_minutes, int end_day, int end_hour, int end_minutes) {
        Hours hours = new Hours(start_day,start_hour, start_minutes, end_day, end_hour, end_minutes);
        hours.save();
        return hours;
    }

    /**
     * Specifies a court to be associated with a certain open hours time.
     * @param court the court to be associated with the hours.
     */
    public void addCourt(Court court) {
        this.courts.add(court);
        this.save();
    }

    public void setStartTime(int hour, int minutes) {
        start_time  = start_time.withTime(hour, minutes, 0, 0);
    }

    public void setEndTime(int hour, int minutes) {
        end_time  = end_time.withTime(hour, minutes, 0, 0);
    }

    public int getDay() {
       return start_day;
    }

    public void setDay(int start_day, int end_day) {
        start_time = start_time.withDayOfWeek(start_day);
        end_time = end_time.withDayOfWeek(end_day);
        this.start_day = start_day;
    }

    /**
     * Returns a hours for a specific court for a particular day of the week.
     * @param court_id the id of the specified court.
     * @param start_day the day when the event starts.
     * @return a list of open hours for a court on a particular day.
     */
    public static List<Hours> getSchedule(long court_id, int start_day) {
        return find
                .where()
                    .in("courts", Court.getCourt(court_id))
                    .eq("start_day", start_day)
                    .orderBy("start_day asc")
                .findList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(DateTime start_time) {
        this.start_time = start_time;
    }

    public DateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(DateTime end_time) {
        this.end_time = end_time;
    }

    public int getStart_day() {
        return start_day;
    }

    public void setStart_day(int start_day) {
        this.start_day = start_day;
    }

    public List<Court> getCourts() {
        return courts;
    }

    public void setCourts(List<Court> courts) {
        this.courts = courts;
    }
}
