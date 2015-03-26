package models.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.avaje.ebean.Query;
import models.Comment;
import models.Player;
import models.leagues.League;
import play.db.ebean.Model;
import views.formdata.teams.TeamForm;

/**
 * Model of a Team object.
 * 
 * @author AJ
 * 
 */
@Entity
@Table(name = "teams")
public class Team extends Model {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Id
  private long id;
  private String teamName;
  private String location;
  private String teamType;
  private String skillLevel;
  private String roster;
  private String description;
  private String imageUrl;
  
  private String record = "0-0";
  private int wins = 0;
  private int losses = 0;
  private int pointsFor = 0;
  private int pointsAgainst = 0;
  private double threePt = 0.0;
  private double twoPt = 0.0;
  private double onePt = 0.0;
  private int rebounds = 0;
  private int steals = 0;
  private int blocks = 0;
  
  private String opponents = "";
  
  @OneToMany(mappedBy = "team")
  private List<Comment> comments = new ArrayList<>();
  
  @ManyToMany(cascade=CascadeType.ALL)
  private List<League> leagues = new ArrayList<>();
  

  /**
   * Default constructor.
   */
  public Team() {
  }

  /**
   * Constructor.
   * 
   * @param teamName team name
   * @param location location
   * @param teamType type of team
   * @param skillLevel skill level
   * @param roster roster
   * @param description description
   */
  public Team(String teamName, String location, String teamType, String skillLevel, String roster, String description,
      String imageUrl) {
    this.setTeamName(teamName);
    this.setLocation(location);
    this.setTeamType(teamType);
    this.setSkillLevel(skillLevel);
    this.setRoster(roster);
    this.setDescription(description);
    this.setImageUrl(imageUrl);
  }
  
  public Team(String teamName, String location, String teamType, String skillLevel, String roster, String description,
      String imageUrl, int wins, int losses, int pointsFor, int pointsAgainst) {
    this.setTeamName(teamName);
    this.setLocation(location);
    this.setTeamType(teamType);
    this.setSkillLevel(skillLevel);
    this.setRoster(roster);
    this.setDescription(description);
    this.setImageUrl(imageUrl);
    this.setWins(wins);
    this.setLosses(losses);
    this.setPointsFor(pointsFor);
    this.setPointsAgainst(pointsAgainst);
  }

  /**
   * Finder.
   * 
   * @return finder
   */
  public static Finder<Long, Team> find() {
    return new Finder<Long, Team>(Long.class, Team.class);
  }

  /**
   * Adds a team to the database if it is new, otherwise edits existing team.
   * 
   * @param tf team form
   */
  public static void addTeam(TeamForm tf) {
    Team team;

    long id = tf.id;

    if (!isTeam(id)) {
      team = new Team(tf.teamName, tf.location, tf.teamType, tf.skillLevel, tf.roster, tf.description, tf.imageUrl);
      team.save();
    }
    else {
      team = getTeam(id);
      team.setTeamName(tf.teamName);
      team.setLocation(tf.location);
      team.setTeamType(tf.teamType);
      team.setSkillLevel(tf.skillLevel);
      team.setRoster(tf.roster);
      team.setDescription(tf.description);
      team.setImageUrl(tf.imageUrl);
      team.setWins(tf.wins);
      team.setLosses(tf.losses);
      team.setPointsFor(tf.pointsFor);
      team.setPointsAgainst(tf.pointsAgainst);
      team.save();
    }
  }

  /**
   * Used in Global.java
   * 
   * @param team the team to be saved.
   */
  public static void addTeam(Team team) {
    team.save();
  }

  /**
   * Returns a Java list of teams.
   * 
   * @return list of teams
   */
  public static List<Team> getTeams() {
    return find().all();
  }

  /**
   * Returns the team associated with the name.
   * 
   * @param teamName team name to be looked for
   * @return the Team if it exists
   */
  public static Team getTeam(long id) {
    return find().where().eq("id", id).findUnique();
  }
  
  public static Team getTeam(String name) {
    return find().where().eq("teamName", name).findUnique();
  }

  /**
   * Used for pagination.
   * 
   * @param sort the sort type
   * @param page the current page
   * @return the page object
   */
  public static Page<Team> find(String sort, int page) {
    return find().where().orderBy(sort).findPagingList(10).setFetchAhead(false).getPage(page);
  }

  /**
   * Searches the Team database with the terms.
   * 
   * @param term search term
   * @param sort sort type, default to "name asc"
   * @param page page number
   * @return a paginglist
   */
  public static Page<Team> find(String term, String sort, int page) {
    term = "%" + term + "%";
    Query<Team> q = Ebean.createQuery(Team.class);
    q.where().disjunction().add(Expr.ilike("teamName", term)).add(Expr.ilike("location", term))
        .add(Expr.ilike("skillLevel", term));
    return q.orderBy(sort).findPagingList(50).setFetchAhead(false).getPage(page);

  }

  /**
   * Checks if teamName belongs to a team.
   * 
   * @param teamName team name
   * @return true if team exists, false otherwise
   */
  public static boolean isTeam(long id) {
    Team team = getTeam(id);
    return !(team == null);
  }

  /**
   * Returns a list of types associated with a team.
   * 
   * @return types as a list
   */
  public static List<String> typeOfTeam() {
    String[] types = { "Mens", "Womens", "Co-ed" };
    return java.util.Arrays.asList(types);
  }

  /**
   * @return the teamName
   */
  public String getTeamName() {
    return teamName;
  }

  /**
   * @param teamName the teamName to set
   */
  public void setTeamName(String teamName) {
    this.teamName = teamName;
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
   * @return the teamType
   */
  public String getTeamType() {
    return teamType;
  }

  /**
   * @param teamType the teamType to set
   */
  public void setTeamType(String teamType) {
    this.teamType = teamType;
  }

  /**
   * @return the skillLevel
   */
  public String getSkillLevel() {
    return skillLevel;
  }

  /**
   * @param skillLevel the skillLevel to set
   */
  public void setSkillLevel(String skillLevel) {
    this.skillLevel = skillLevel;
  }

  /**
   * @return the roster
   */
  public String getRoster() {
    return roster;
  }
  
  

  /**
   * @return the imageUrl
   */
  public String getImageUrl() {
    return imageUrl;
  }

  /**
   * @param imageUrl the imageUrl to set
   */
  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  /**
   * Returns the roster as a java list.
   * 
   * @return the roster as a list
   */
  public List<String> getRosterList() {
    // TODO check if player name is related to a player profile
    List<String> rosterList = java.util.Arrays.asList(roster.split("\\s*,\\s*"));
    return rosterList;
  }
  
  private List<String> noProfile = new ArrayList<>();

  /**
   * @return the players as a java List
   */
  public List<Player> getPlayerProfiles() {
    // TODO check if player name is related to a player profile
    List<String> gamePlayers = java.util.Arrays.asList(roster.split("\\s*,\\s*"));

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
   * @param roster the roster to set
   */
  public void setRoster(String roster) {
    this.roster = roster;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }
  
  public long getId(){
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Adds a comment to the team's list.
   * 
   * @param comment the comment
   */
  public void addComment(Comment comment) {
    this.comments.add(comment);
  }

  /**
   * Returns a java list of comments related to the team.
   * 
   * @return comments
   */
  public List<Comment> getComments() {
    return this.comments;
  }

  /**
   * @return the record
   */
  public String getRecord() {
    return record;
  }

  /**
   * @param record the record to set
   */
  public void setRecord(String record) {
    this.record = record;
  }

  /**
   * @return the threePt
   */
  public double getThreePt() {
    return threePt;
  }

  /**
   * @param threePt the threePt to set
   */
  public void setThreePt(double threePt) {
    this.threePt = threePt;
  }

  /**
   * @return the twoPt
   */
  public double getTwoPt() {
    return twoPt;
  }

  /**
   * @param twoPt the twoPt to set
   */
  public void setTwoPt(double twoPt) {
    this.twoPt = twoPt;
  }

  /**
   * @return the onePt
   */
  public double getOnePt() {
    return onePt;
  }

  /**
   * @param onePt the onePt to set
   */
  public void setOnePt(double onePt) {
    this.onePt = onePt;
  }

  /**
   * @return the rebounds
   */
  public int getRebounds() {
    return rebounds;
  }

  /**
   * @param rebounds the rebounds to set
   */
  public void setRebounds(int rebounds) {
    this.rebounds = rebounds;
  }

  /**
   * @return the steals
   */
  public int getSteals() {
    return steals;
  }

  /**
   * @param steals the steals to set
   */
  public void setSteals(int steals) {
    this.steals = steals;
  }

  /**
   * @return the blocks
   */
  public int getBlocks() {
    return blocks;
  }

  /**
   * @param blocks the blocks to set
   */
  public void setBlocks(int blocks) {
    this.blocks = blocks;
  }

  public int getWins() {
    return wins;
  }

  public void setWins(int wins) {
    this.wins = wins;
  }

  public int getLosses() {
    return losses;
  }

  public void setLosses(int losses) {
    this.losses = losses;
  }

  public int getPointsFor() {
    return pointsFor;
  }

  public void setPointsFor(int pointsFor) {
    this.pointsFor = pointsFor;
  }

  public int getPointsAgainst() {
    return pointsAgainst;
  }

  public void setPointsAgainst(int pointsAgainst) {
    this.pointsAgainst = pointsAgainst;
  }

  public String getOpponents() {
    return opponents;
  }
  
  public String getOpponent() {
    String[] s = opponents.split("~");
    opponents = "";
    String string = s[0];
    for(int i = 1; i < s.length; i++){
      opponents += (s[i] + "~");
    }
    this.save();
    return string;
  }
  
  public void removeOpponent(String opponent){
    String[] s = opponents.split("~");
    opponents = "";
    for(int i = 0; i < s.length; i++){
      if(!s[i].equals(opponent)){
        opponents += (s[i] + "~");
      }
    }
    this.save();
  }
  
  public void addOpponent(String opponent){
    opponents += (opponent + "~");
    this.save();
  }
  
  public void setOpponents(String opp){
    this.opponents = opp;
    this.save();
  }
}
