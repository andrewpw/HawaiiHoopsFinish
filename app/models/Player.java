package models;

import javax.persistence.*;
import com.avaje.ebean.Page;
import controllers.Secured;
import play.db.ebean.Model;
import play.mvc.Http.Context;
import views.formdata.PlayerFormData;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Scott Honda
 */
@Entity
@Table(name = "players")
public class Player extends Model {

  @Id
  @GeneratedValue
  private Long id;

  private String nickname;
  private String skill;
  private String position;
  private Long rating;
  private Long votes;
  private String height;
  private String weight;
  private String bio;
  private String lookingFor;
  private String picUrl;

  @OneToOne(mappedBy = "player")
  private User user;

  @ManyToOne
  private Court homeCourt;

  @ManyToMany(mappedBy = "players")
  private List<Court> courts = new ArrayList<Court>();

  // @OneToOne
  // private User user;

  /**
   * Creates a new player.
   * 
   * @param skill = skill level of player
   * @param position = position of player
   * 
   */
  public Player(String nickname, String skill, String position, long rating, long votes, String height, String weight,
      String bio, String lookingFor, String picUrl) {
    this.nickname = nickname;
    this.skill = skill;
    this.position = position;
    this.rating = rating;
    this.setVotes(votes);
    this.height = height;
    this.weight = weight;
    this.bio = bio;
    this.lookingFor = lookingFor;
    this.picUrl = picUrl;
  }

  /**
   * public Player(User user) { this.user = user; }
   * 
   * public static Player addPlayer(User user) { Player player = new Player(user); player.save(); return player; }
   **/

  public static Player addPlayer(String nickname, String skill, String position, long rating, long votes,
      String height, String weight, String bio, String lookingFor, String picUrl) {
    Player player = new Player(nickname, skill, position, rating, votes, height, weight, bio, lookingFor, picUrl);
    player.save();
    return player;
  }

  /**
   * Adds a player to the database
   * 
   * @param formData = the PlayerFormData containing the player's info save's the player's info to the DB
   */
  public static void addPlayer(String name) {
    /**
     * Player player = new Player(name, "", "unknown", "unknown", "unknown", 0, 0, "", "", "", "", ""); player.save();
     **/
  }

  /**
   * Updates a player's info
   */
  public static void updatePlayer(PlayerFormData formData, long id) {

    Player player = getPlayer(id);
    player.setNickname(formData.nickname);
    // player.setHomeCourt(formData.homeCourt);
    player.setSkill(formData.skill);
    player.setPosition(formData.position);
    player.setHeight(formData.height);
    player.setWeight(formData.weight);
    player.setBio(formData.bio);
    player.setLookingFor(formData.lookingFor);
    player.setPicUrl(formData.picUrl);
    player.update();
  }

  /**
   * The EBean ORM finder method for database queries on PlayerList.
   **/
  public static Finder<Long, Player> find() {
    return new Finder<Long, Player>(Long.class, Player.class);
  }

  /**
   * ********************* * Getters and Setters * ********************* *
   **/

  /**
   * The EBean Page finder method to implement pagination for all players.
   * 
   * @param sortOrder = the order to of the sorting
   * @param page = the current page index
   * @return page object of all players
   */
  public static Page<Player> find(String sortOrder, int page) {
    return find().where().ne("user", Secured.getUserInfo(Context.current())).orderBy(sortOrder)
        .findPagingList(10).setFetchAhead(false).getPage(page);
  }

  public static Page<Player> page(int size, int page, long court_id, User user) {
    Page<Player> playerPage;
    if (user != null) {
      playerPage =
          find().where().in("courts", Court.getCourt(court_id)).ne("id", user.getPlayer().getId()).findPagingList(size)
              .getPage(page);
    }
    else {
      playerPage = find().where().in("courts", Court.getCourt(court_id)).findPagingList(size).getPage(page);
    }
    return playerPage;
  }

  /**
   * Temporary for now, used in Global.java Stops multiple addition in database.
   * 
   * DELETE WHEN NEEDED IN ANYMORE
   * 
   * @return java list of players
   */
  public static List<Player> getPlayers() {
    return find().all();
  }

  /**
   * The EBean Page finder method to implement pagination, based on a search of players.
   * 
   * @param sortOrder = the order to of the sorting
   * @param page = the current page index
   * @return page object of all players
   */
  public static Page<Player> find(String field, String search, String sortOrder, int page) {
    return find().where().eq(field, search).orderBy(sortOrder).findPagingList(10).setFetchAhead(false).getPage(page);
  }

  public static Player getPlayer(long id) {
    return find().where().eq("id", id).findUnique();
  }

  /**
   * Used when finding if profile belongs to a name
   * 
   * @param name the player's name
   * @return Player
   */
  public static Player getPlayer(String name) {
    return find().where().eq("name", name).findUnique();
  }

  /**
   * ********************* * Getters and Setters * ********************* *
   */

  /**
   * @return the id
   */
  public long getId() {
    return id;
  }

  public Court getHomeCourt() {
    return homeCourt;
  }

  public void setHomeCourt(Court homeCourt) {
    this.homeCourt = homeCourt;
  }

  /**
   * @return the skill
   */
  public String getSkill() {
    return skill;
  }

  /**
   * @param skill the skill to set
   */
  public void setSkill(String skill) {
    this.skill = skill;
  }

  /**
   * @return the position
   */
  public String getPosition() {
    return position;
  }

  /**
   * @param position the position to set
   */
  public void setPosition(String position) {
    this.position = position;
  }

  /**
   * @return the rating
   */
  public long getRating() {
    return rating;
  }

  /**
   * @param rating the rating to set
   */
  public void setRating(long rating) {
    this.rating = rating;
  }

  /**
   * @return the votes
   */
  public Long getVotes() {
    return votes;
  }

  /**
   * @param votes the votes to set
   */
  public void setVotes(Long votes) {
    this.votes = votes;
  }

  /**
   * @return the rank
   */
  public int getRank(Long rating, Long votes) {
    if (rating == 0 || votes == 0) {
      return 0;
    }
    else {
      return (int) Math.round(this.rating / this.votes);
    }
  }

  /**
   * @return the bio
   */
  public String getBio() {
    return bio;
  }

  /**
   * @param bio the bio to set
   */
  public void setBio(String bio) {
    this.bio = bio;
  }

  /**
   * @return the height
   */
  public String getHeight() {
    return height;
  }

  /**
   * @param height the height to set
   */
  public void setHeight(String height) {
    this.height = height;
  }

  /**
   * @return the weight
   */
  public String getWeight() {
    return weight;
  }

  /**
   * @param weight the weight to set
   */
  public void setWeight(String weight) {
    this.weight = weight;
  }

  /**
   * @return the nickname
   */
  public String getNickname() {
    return nickname;
  }

  /**
   * @param nickname the nickname to set
   */
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  /**
   * @return the lookingFor
   */
  public String getLookingFor() {
    return lookingFor;
  }

  /**
   * @param lookingFor the lookingFor to set
   */
  public void setLookingFor(String lookingFor) {
    this.lookingFor = lookingFor;
  }

  /**
   * @return the picUrl
   */
  public String getPicUrl() {
    return picUrl;
  }

  /**
   * @param picUrl the picUrl to set
   */
  public void setPicUrl(String picUrl) {
    this.picUrl = picUrl;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Court> getCourts() {
    return courts;
  }

  public void setCourts(List<Court> courts) {
    this.courts = courts;
  }
}
