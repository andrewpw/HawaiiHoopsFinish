package models.games;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import models.Player;
import models.User;
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

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Id
  private String name;
  private String time;
  private String date;
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
   * Default constructor.
   */
  public Game() {
  }

  /**
   * Constructs a game.
   * 
   * @param name name
   * @param time time
   * @param date date
   * @param location location
   * @param type type
   * @param freq frequency
   * @param sklLvl skill level
   * @param players players
   */
  public Game(String name, String time, String date, String location, String type, String freq, String sklLvl,
      String players, User creator) {
    this.setName(name);
    this.setTime(time);
    this.setDate(date);
    this.setLocation(location);
    this.setType(type);
    this.setFrequency(freq);
    this.setAvgSklLvl(sklLvl);
    this.setPlayers(players);
    this.setUpdateCount(0);
    this.setCreator(creator);
  }

  /**
   * Finder for a game.
   * 
   * @return a finder object
   */
  public static Finder<String, Game> find() {
    return new Finder<String, Game>(String.class, Game.class);
  }

  /**
   * Adds a game to the database.
   * 
   * @param gf the game form
   */
  public static void addGame(GameForm gf, User user) {
    // TODO currently does not work with editing of games
    Game game;
    Date date;

    String gameDate = gf.month + " " + gf.day;
    String gameTime = gf.hour + ":" + gf.minute + " " + gf.amPm;

    String[] test = gameDate.split("\\s+");
    // System.out.println("test[0]: " + test[0]);
    // System.out.println("test[1]: " + test[1]);

    if (!isGame(gf.name)) {

      game = new Game(gf.name, gameTime, gameDate, gf.location, gf.type, gf.frequency, gf.avgSklLvl, gf.players, user);
      date = new Date();
      game.setDateCreated(date.toString());
      game.save();

    }
    else {
      game = getGame(gf.name);
      game.setTime(gameTime);
      game.setDate(gameDate);
      game.setLocation(gf.location);
      game.setType(gf.type);
      game.setFrequency(gf.frequency);
      game.setAvgSklLvl(gf.avgSklLvl);
      game.setPlayers(gf.players);

      date = new Date();
      game.setDateEdit(date.toString());

      int count = game.getUpdateCount();
      count++;
      game.setUpdateCount(count);

      game.save();
    }
  }

  public static void addGame(Game game) {
    game.save();
  }

  /**
   * Checks if given name is a valid game.
   * 
   * @param name the name of the game.
   * @return game if exists
   */
  public static boolean isGame(String name) {
    Game game = getGame(name);
    return !(game == null);
  }

  /**
   * Deletes a game.
   * 
   * @param name the name of the game
   */
  public static void deleteGame(String name) {
    find().ref(name).delete();
  }

  /**
   * Returns the game based on it's name.
   * 
   * @param name name
   * @return the game
   */
  public static Game getGame(String name) {
    return find().where().eq("name", name).findUnique();
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
   * @return the time
   */
  public String getTime() {
    return time;
  }

  /**
   * @param time the time to set
   */
  public void setTime(String time) {
    this.time = time;
  }

  /**
   * @return the date
   */
  public String getDate() {
    return date;
  }

  /**
   * @param date the date to set
   */
  public void setDate(String date) {
    this.date = date;
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
   * Returns a map of the months.
   * 
   * @return a map of the months
   */
  public static Map<String, Boolean> getMonths() {
    String[] month =
        { "aJanuary", "bFebruary", "cMarch", "dApril", "eMay", "fJune", "gJuly", "hAugust", "iSeptember", "jOctober",
            "kNovember", "lDecember" };
    Map<String, Boolean> months = new LinkedHashMap<>();
    for (int x = 0; x < month.length; x++) {
      months.put(month[x], false);
    }
    return months;
  }

  /**
   * A java Map of days.
   * 
   * @return a map of days
   */
  public static Map<String, Boolean> getDays() {
    Map<String, Boolean> days = new LinkedHashMap<>();
    for (int x = 1; x <= 31; x++) {
      days.put(Integer.toString(x), false);
    }
    return days;
  }

  /**
   * Returns hours.
   * 
   * @return hours
   */
  public static Map<String, Boolean> getHours() {
    Map<String, Boolean> hours = new LinkedHashMap<>();
    for (int x = 1; x <= 12; x++) {
      hours.put(Integer.toString(x), false);
    }
    return hours;
  }

  /**
   * Returns minutes.
   * 
   * @return minutes
   */
  public static Map<String, Boolean> getMinutes() {
    String[] minutes = { "00", "15", "30", "45" };
    Map<String, Boolean> mins = new LinkedHashMap<>();
    for (int x = 0; x < minutes.length; x++) {
      mins.put(minutes[x], false);
    }
    return mins;
  }

  /**
   * Return AmPm.
   * 
   * @return ampm
   */
  public static List<String> getAmPm() {
    String[] amPm = { "am", "pm" };
    return java.util.Arrays.asList(amPm);
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
}
