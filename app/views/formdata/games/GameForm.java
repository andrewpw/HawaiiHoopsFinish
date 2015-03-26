package views.formdata.games;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;
import org.joda.time.DateTime;
import org.joda.time.Days;
import models.games.Game;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

/**
 * The backing class when creating a game.
 * 
 * @author AJ
 * 
 */
public class GameForm {

  private final int DAYS_FORWARD = 60;

  @Id
  public long id;
  
  @Constraints.Required(message = "A name is required.")
  public String name;
  /** Month. **/
  @Constraints.Required(message = "A Month is Required.")
  public String month;
  /** Day. **/
  @Constraints.Required(message = "A day is required.")
  public String day;
  /** Year. **/
  @Constraints.Required(message = "A Year is required.")
  public String year;
  /** Hour. **/
  @Constraints.Required(message = "An hour is required.")
  public String hour;
  /** Minute. **/
  @Constraints.Required(message = "Minutes are required.")
  public String minute;
  /** Location. **/
  @Constraints.Required(message = "A location is required.")
  public String location;
  /** Type. **/
  @Constraints.Required(message = "A type is required.")
  public String type;
  /** Frequency. **/
  public String frequency;
  /** Average Skill Level. **/
  public String avgSklLvl;
  /** Players attending. **/
  public String players;

  /**
   * Default Constructor.
   * 
   */
  public GameForm() {

  }

  /**
   * Main constructor.
   * 
   * @param game the game
   */
  public GameForm(Game game) {
    this.name = game.getName();

    this.month = game.getMonth();
    this.day = game.getDay();
    this.year = game.getYear();
    this.hour = game.getHour();
    this.minute = game.getMinute();
    this.location = game.getLocation();
    this.type = game.getType();
    this.frequency = game.getFrequency();
    this.avgSklLvl = game.getAvgSklLvl();
    this.players = game.getPlayers();
  }

  /**
   * Validation method.
   * 
   * @return a List of errors if any.
   */
  public List<ValidationError> validate() {

    List<ValidationError> errors = new ArrayList<>();

    // Bad choice for variable names...
    int mon = 0;
    int da = 0;
    int ho = 0;
    int min = 0;
    int ye = 0;

    try {
      mon = Integer.parseInt(month);
    }
    catch (NumberFormatException e) {
      errors.add(new ValidationError("month", "Please enter month in numerical form."));
    }

    try {
      da = Integer.parseInt(day);
    }
    catch (NumberFormatException e) {
      errors.add(new ValidationError("day", "Please enter day in numerical form."));
    }

    try {
      ye = Integer.parseInt(year);
    }
    catch (NumberFormatException e) {
      errors.add(new ValidationError("year", "Please enter year in numerical form."));
    }

    try {
      ho = Integer.parseInt(hour);
    }
    catch (NumberFormatException e) {
      errors.add(new ValidationError("hour", "Please enter hours in numerical form."));
    }

    try {
      min = Integer.parseInt(minute);
    }
    catch (NumberFormatException e) {
      errors.add(new ValidationError("minute", "Please enter minutes in numerical form."));
    }

    if (mon < 1 || mon > 12) {
      errors.add(new ValidationError("month", "Not a valid month."));
    }
    DateTime test = new DateTime(ye, mon, 1, 12, 12);
    if (da < 1 || da > test.dayOfMonth().getMaximumValue()) {
      errors.add(new ValidationError("day", "Not a valid day."));
    }
    if (ho < 0 || ho > 23) {
      errors.add(new ValidationError("hour", "Not a valid hour."));
    }
    if (min < 0 || min > 59) {
      errors.add(new ValidationError("minute", "Not a valid minute."));
    }

    if (!errors.isEmpty()) {
      return errors;
    }
    else {
      DateTime curr = new DateTime();
      DateTime date = new DateTime(ye, mon, da, ho, min);

      int days = Days.daysBetween(curr, date).getDays();

      if (days > DAYS_FORWARD) {
        errors.add(new ValidationError("month",
            "Game is scheduled too far in advanced.  Please only schedule games within " + DAYS_FORWARD + " days."));
      }
      if (days < 0) {
        errors.add(new ValidationError("month", "You can't schedule games in the past."));
      }
      if (days == 0) {
        if (ho < Game.getCurrHour()) {
          errors.add(new ValidationError("hour", "You can't schedule games in the past."));
        }
        if (ho == Game.getCurrHour()) {
          if (min < Game.getCurrMinute()) {
            errors.add(new ValidationError("minute", "You can't schedule games in the past."));
          }
        }
      }
      return errors.isEmpty() ? null : errors;
    }
  }
}
