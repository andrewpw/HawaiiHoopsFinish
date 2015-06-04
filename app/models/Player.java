package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import models.teams.Team;
import play.db.ebean.Model;
import play.mvc.Http.Context;
import views.formdata.PlayerFormData;

import com.avaje.ebean.Page;

import controllers.Secured;

/**
 * @author Scott Honda
 */
@Entity
@Table(name = "players")
public class Player extends Model {

	private static final long serialVersionUID = 1L;

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

	@ManyToMany(cascade = CascadeType.ALL)
	private List<Team> teams = new ArrayList<Team>();
	
	@OneToMany(mappedBy = "captain")
	private List<Team> captainedTeams = new ArrayList<Team>();

	/**
	 * Creates a new player.
	 * 
	 * @param skill = skill level of player
	 * @param position = position of player
	 * 
	 */
	public Player(User user, String nickname, String skill, String position, long rating, long votes, String height, String weight,
			String bio, String lookingFor, String picUrl) {
		this.user = user;
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

	public static Player addPlayer(User user, String nickname, String skill, String position, long rating, long votes,
			String height, String weight, String bio, String lookingFor, String picUrl) {
		Player player = new Player(user, nickname, skill, position, rating, votes, height, weight, bio, lookingFor, picUrl);
		player.save();
		return player;
	}

	/**
	 * Updates a player's info
	 */
	public static void updatePlayer(PlayerFormData formData, long id) {

		Player player = getPlayer(id);
		player.setNickname(formData.nickname);
		player.setHomeCourt(formData.homeCourt);
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

	public static Map<String, Boolean> getPlayerMap(){
		Map<String, Boolean> playerMap = new TreeMap<String, Boolean>();
		List<Player> playerList = Player.getPlayers();
		for(int i = 0; i < playerList.size(); i++){
			playerMap.put(playerList.get(i).getNickname(), false);
		}

		return playerMap;
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

	public long getId() {
		return id;
	}

	public Court getHomeCourt() {
		return homeCourt;
	}

	public String getSkill() {
		return skill;
	}

	public String getPosition() {
		return position;
	}

	public long getRating() {
		return rating;
	}

	public Long getVotes() {
		return votes;
	}

	public int getRank(Long rating, Long votes) {
		if (rating == 0 || votes == 0) {
			return 0;
		}
		else {
			return (int) Math.round(this.rating / this.votes);
		}
	}

	public String getBio() {
		return bio;
	}

	public String getHeight() {
		return height;
	}

	public String getWeight() {
		return weight;
	}

	public String getNickname() {
		return nickname;
	}

	public String getLookingFor() {
		return lookingFor;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public User getUser() {
		return user;
	}

	public List<Court> getCourts() {
		return courts;
	}

	public List<Team> getTeams() {
		return teams;
	}

	public List<Team> getCaptainedTeams() {
		return captainedTeams;
	}

	public void setCaptainedTeams(List<Team> captainedTeams) {
		this.captainedTeams = captainedTeams;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	public void setHomeCourt(Court homeCourt) {
		this.homeCourt = homeCourt;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setRating(long rating) {
		this.rating = rating;
	}

	public void setVotes(Long votes) {
		this.votes = votes;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setLookingFor(String lookingFor) {
		this.lookingFor = lookingFor;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setCourts(List<Court> courts) {
		this.courts = courts;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	
	public void addCaptainedTeam(Team team) {
		this.captainedTeams.add(team);
	}
	
	public void addTeam(Team team) {
		this.teams.add(team);
	}
	
	public void addCourt(Court court) {
		this.courts.add(court);
	}
}
