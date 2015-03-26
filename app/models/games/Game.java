package models.games;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import models.Player;
import models.User;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.db.ebean.Model;
import views.formdata.games.GameForm;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.avaje.ebean.Query;

/**
 * Game model.
 * 
 * @author AJ
 * 
 */
@Entity
@Table(name = "game")
public class Game extends Model {

  private static final int DAYS_PAST = 0;

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;
  
  private String name;

  private DateTime gameTime;

  private String month;
  private String day;
  private String year;
  private String hour;
  private String minute;
  private String amPmTime;
  private String location;
  private String type;
  private String frequency;
  private String avgSklLvl;
  private String players;
  private String dateCreated;
  private String dateEdit;
  private int updateCount;

  @OneToOne
  private User creator;

  /**
   * Default Constructor.
   */
  public Game() {
  }

  /**
   * Constructor.
   * 
   * @param name name
   * @param month month
   * @param day day
   * @param year year
   * @param hour hour
   * @param minute minute
   * @param location location
   * @param type type
   * @param frequency frequency
   * @param avgSklLvl skill level
   * @param players players
   * @param creator creator
   */
  public Game(String name, String month, String day, String year, String hour, String minute, String location,
      String type, String frequency, String avgSklLvl, String players, User creator) {

    // Month
    int mon = Integer.parseInt(month);
    // Day
    int da = Integer.parseInt(day);
    // Year
    int ye = Integer.parseInt(year);

    int ho = Integer.parseInt(hour);

    int min = Integer.parseInt(minute);

    DateTime event = new DateTime(ye, mon, da, ho, min);
    this.setGameTime(event);
    this.setName(name);
    this.setMonth(month);
    this.setDay(day);
    this.setYear(year);
    this.setHour(hour);
    this.setMinute(minute);
    this.setLocation(location);
    this.setType(type);
    this.setFrequency(frequency);
    this.setAvgSklLvl(avgSklLvl);
    this.setPlayers(players);
    this.setCreator(creator);
    this.setUpdateCount(0);
  }

  /**
   * Finder for a game.
   * 
   * @return a finder object
   */
  public static Finder<Long, Game> find() {
    return new Finder<Long, Game>(Long.class, Game.class);
  }

  /**
   * Adds a game to the database.
   * 
   * @param gf the game form
   * @param user the creator of the game
   */
  public static Game addGame(GameForm gf, User user) {

    Game game;
    DateTime gameDate;
    DateTime date;

    int month = Integer.parseInt(gf.month);
    int day = Integer.parseInt(gf.day);
    int year = Integer.parseInt(gf.year);
    int hour = Integer.parseInt(gf.hour);
    int minute = Integer.parseInt(gf.minute);
    

    if (!isGame(gf.id)) {

      game =
          new Game(gf.name, gf.month, gf.day, gf.year, gf.hour, gf.minute, gf.location, gf.type, gf.frequency,
              gf.avgSklLvl, gf.players, user);

      gameDate = new DateTime(year, month, day, hour, minute);
      game.setGameTime(gameDate);

      date = new DateTime();
      game.setDateCreated(date.toString());
      game.save();

    }
    else {
      game = getGame(gf.id);

      gameDate = new DateTime(year, month, day, hour, minute);
      game.setGameTime(gameDate);

      game.setMonth(gf.month);
      game.setDay(gf.day);
      game.setYear(gf.year);
      game.setHour(gf.hour);
      game.setMinute(gf.minute);
      game.setLocation(gf.location);
      game.setType(gf.type);
      game.setFrequency(gf.frequency);
      game.setAvgSklLvl(gf.avgSklLvl);
      game.setPlayers(gf.players);

      date = new DateTime();
      game.setDateEdit(date.toString());

      int count = game.getUpdateCount();
      count++;
      game.setUpdateCount(count);

      game.save();
    }
    return game;
  }

  /**
   * Add game.
   * 
   * @param game game
   */
  public static void addGame(Game game) {
    game.save();
  }

  /**
   * Checks if given name is a valid game.
   * 
   * @param name the name of the game.
   * @return game if exists
   */
  public static boolean isGame(long id) {
    Game game = getGame(id);
    return !(game == null);
  }

  /**
   * Deletes a game.
   * 
   * @param name the name of the game
   */
  public static void deleteGame(long id) {
    find().ref(id).delete();
  }

  /**
   * Returns the game based on it's name.
   * 
   * @param name name
   * @return the game
   */
  public static Game getGame(long id) {
    return find().where().eq("id", id).findUnique();
  }

  /**
   * Returns a list of all the games.
   * 
   * @return the list of games
   */
  public static List<Game> getGames() {
    return find().all();
  }

  /**
   * Implements pagination.
   * 
   * @param sort sort string
   * @param page page number
   * @return page
   */
  public static Page<Game> find(String sort, int page) {
    return find().where().orderBy(sort).findPagingList(10).setFetchAhead(false).getPage(page);
  }
  
  public static Page<Game> gamesForIndex() {
    return find().where().orderBy("gameTime asc").findPagingList(5).setFetchAhead(false).getPage(0);
  }

  /**
   * Used for search.
   * 
   * @param term the search term
   * @param sort sort type
   * @param page page num
   * @return page
   */
  public static Page<Game> find(String term, String sort, int page) {
    term = "%" + term + "%";
    Query<Game> q = Ebean.createQuery(Game.class);
    // ilike is case insensitive
    q.where().disjunction().add(Expr.ilike("name", term)).add(Expr.ilike("location", term))
        .add(Expr.ilike("players", term));
    return q.orderBy(sort).findPagingList(50).setFetchAhead(false).getPage(page);
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * @param location the location to set
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * Returns the types of games.
   * 
   * @return type of game
   */
  public static List<String> getTypes() {
    String[] types = { "Public", "Private" };
    return java.util.Arrays.asList(types);
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  private List<String> noProfile = new ArrayList<>();

  /**
   * @return the players as a java List
   */
  public List<Player> getPlayerProfiles() {
    // TODO check if player name is related to a player profile
    List<String> gamePlayers = java.util.Arrays.asList(players.split("\\s*,\\s*"));

    List<Player> withProfile = new ArrayList<>();
    for (int x = 0; x < gamePlayers.size(); x++) {
      Player player = Player.getPlayer(gamePlayers.get(x));

      if (player != null) {
        withProfile.add(player);
      }
      else {
        noProfile.add(gamePlayers.get(x));
      }
    }
    return withProfile;
  }

  /**
   * Returns a list of the player names that don't have a profile within the site.
   * 
   * @return noProfile
   */
  public List<String> getNoProfile() {
    return noProfile;
  }

  /**
   * Returns a list of players with profile pages.
   * 
   * @return a list
   */
  public List<String> getListPlayers() {
    List<String> gamePlayers = java.util.Arrays.asList(players.split("\\s*,\\s*"));
    return gamePlayers;
  }

  /**
   * Returns the players.
   * 
   * @return players
   */
  public String getPlayers() {
    return players;
  }

  /**
   * @param players the players to set
   */
  public void setPlayers(String players) {
    this.players = players;
  }

  /**
   * @return the avgSklLvl
   */
  public String getAvgSklLvl() {
    return avgSklLvl;
  }

  /**
   * @param avgSklLvl the avgSklLvl to set
   */
  public void setAvgSklLvl(String avgSklLvl) {
    this.avgSklLvl = avgSklLvl;
  }

  /**
   * Returns a list of frequencies.
   * 
   * @return the list of frequencies.
   */
  public static List<String> getListFrequencies() {
    String[] freqs = { "One Time", "Recurring" };
    return java.util.Arrays.asList(freqs);
  }

  /**
   * @return the frequency
   */
  public String getFrequency() {
    return frequency;
  }

  /**
   * @param frequency the frequency to set
   */
  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the dateCreated
   */
  public String getDateCreated() {
    return dateCreated;
  }

  /**
   * @param dateCreated the dateCreated to set
   */
  public void setDateCreated(String dateCreated) {
    this.dateCreated = dateCreated;
  }

  /**
   * @return the dateEdit
   */
  public String getDateEdit() {
    return dateEdit;
  }

  /**
   * @param dateEdit the dateEdit to set
   */
  public void setDateEdit(String dateEdit) {
    this.dateEdit = dateEdit;
  }

  /**
   * @return the updateCount
   */
  public int getUpdateCount() {
    return updateCount;
  }

  /**
   * @param updateCount the updateCount to set
   */
  public void setUpdateCount(int updateCount) {
    this.updateCount = updateCount;
  }

  /**
   * @return the creator
   */
  public User getCreator() {
    return creator;
  }

  /**
   * @param creator the creator to set
   */
  public void setCreator(User creator) {
    this.creator = creator;
  }

  /**
   * @return the month
   */
  public String getMonth() {
    return month;
  }

  /**
   * @param month2 the month to set
   */
  public void setMonth(String month2) {
    this.month = month2;
  }

  /**
   * @return the day
   */
  public String getDay() {
    return day;
  }

  /**
   * @param day2 the day to set
   */
  public void setDay(String day2) {
    this.day = day2;
  }

  /**
   * @return the hour
   */
  public String getHour() {
    return hour;
  }

  /**
   * @param hour the hour to set
   */
  public void setHour(String hour) {
    this.hour = hour;
  }

  /**
   * @return the minute
   */
  public String getMinute() {
    return minute;
  }

  /**
   * @param minute the minute to set
   */
  public void setMinute(String minute) {
    this.minute = minute;
  }

  /**
   * @return the year
   */
  public String getYear() {
    return year;
  }

  /**
   * @param year the year to set
   */
  public void setYear(String year) {
    this.year = year;
  }

  /**
   * @return the amPmTime
   */
  public String getAmPmTime() {
    return amPmTime;
  }

  /**
   * @param amPm the amPmTime to set
   */
  public void setAmPmTime(String amPm) {
    this.amPmTime = amPm;
  }

  /**
   * @return the gameTime
   */
  public DateTime getGameTime() {
    return gameTime;
  }

  /**
   * @param gameTime the gameTime to set
   */
  public void setGameTime(DateTime gameTime) {
    this.gameTime = gameTime;
  }

  /**
   * @return the game date as a string object.
   */
  public String getGameDateString() {
    String pattern = "MM-dd-yy";
    DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
    String gameDateString = fmt.print(gameTime);
    return gameDateString;
  }

  /**
   * @return the game time as a string object.
   */
  public String getGameTimeString() {
    String pattern = "hh : mm aa";
    DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
    String gameTimeString = fmt.print(gameTime);
    return gameTimeString;

  }

  private static DateTime today = new DateTime();

  public static int getCurrMonth() {
    return today.monthOfYear().get();
  }

  public static int getCurrDay() {
    return today.dayOfMonth().get();
  }

  public static int getCurrYear() {
    return today.year().get();
  }

  public static int getCurrHour() {
    return today.hourOfDay().get();
  }

  public static int getCurrMinute() {
    return today.minuteOfHour().get();
  }

  public static void deletePastGames() {
    List<Game> allGames = find().all();
    for (Game game : allGames) {
      int days = Days.daysBetween(today, game.getGameTime()).getDays();
      if (days < DAYS_PAST) {
        Game.deleteGame(game.getId());
      }
    }
  }
  

  /**
   * Used for player profile.
   * 
   * @param term the search term
   * @param sort sort type
   * @param page page num
   * @return page
   */
  public static List<Game> findPlayerGames(String term) {
    term = "%" + term + "%";
    Query<Game> q = Ebean.createQuery(Game.class);
    // ilike is case insensitive
    q.where().disjunction().add(Expr.ilike("name", term)).add(Expr.ilike("location", term)).add(Expr.ilike("players", term));
    return q.findList();
  }
}
