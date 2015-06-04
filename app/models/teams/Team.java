package models.teams;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import models.Comment;
import models.Player;
import models.leagues.League;
import play.db.ebean.Model;
import views.formdata.teams.TeamForm;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.avaje.ebean.Query;

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
	private String homeCourt;
	private String teamType;
	private String skillLevel;
	private String description;
	private String imageUrl;

	private String record = "0-0";
	private int wins = 0;
	private int losses = 0;
	private int pointsFor = 0;
	private int pointsAgainst = 0;

	@Column(columnDefinition = "NUMERIC")
	private double threePt = 0.0;

	@Column(columnDefinition = "NUMERIC")
	private double twoPt = 0.0;

	@Column(columnDefinition = "NUMERIC")
	private double freeThrow = 0.0;
	private int rebounds = 0;
	private int steals = 0;
	private int blocks = 0;
	private int assists = 0;

	private String opponents = "";

	@ManyToOne
	private Player captain;
	
	@OneToMany(mappedBy = "team")
	private List<Comment> comments = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL)
	private List<League> leagues = new ArrayList<>();

	@ManyToMany(mappedBy = "teams", cascade = CascadeType.ALL)
	private List<Player> roster = new ArrayList<>();

	/**
	 * Default constructor.
	 */
	public Team() {
	}

	/**
	 * Constructor.
	 * 
	 * @param teamName team name
	 * @param homeCourt location
	 * @param teamType type of team
	 * @param skillLevel skill level
	 * @param roster roster
	 * @param description description
	 */
	public Team(Player captain, String teamName, String homeCourt, String teamType, String skillLevel, List<Player> roster, String description,
			String imageUrl) {
		this.captain = captain;
		this.setTeamName(teamName);
		this.setHomeCourt(homeCourt);
		this.setTeamType(teamType);
		this.setSkillLevel(skillLevel);
		this.setRoster(roster);
		this.setDescription(description);
		this.setImageUrl(imageUrl);
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
			team = new Team(tf.captain, tf.teamName, tf.homeCourt, tf.teamType, tf.skillLevel, tf.roster, tf.description, tf.imageUrl);
			team.save();
		}
		else {
			team = getTeam(id);
			team.setCaptain(tf.captain);
			team.setTeamName(tf.teamName);
			team.setHomeCourt(tf.homeCourt);
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
	 * Finder.
	 * 
	 * @return finder
	 */
	public static Finder<Long, Team> find() {
		return new Finder<Long, Team>(Long.class, Team.class);
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
		q.where().disjunction().add(Expr.ilike("teamName", term)).add(Expr.ilike("homeCourt", term))
		.add(Expr.ilike("skillLevel", term));
		return q.orderBy(sort).findPagingList(50).setFetchAhead(false).getPage(page);
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
	 * Returns the team associated with the team id.
	 * 
	 * @param id team id to be looked for
	 * @return the Team if it exists
	 */
	public static Team getTeam(long id) {
		return find().where().eq("id", id).findUnique();
	}

	/**
	 * Returns the team associated with the name.
	 * 
	 * @param teamName team name to be looked for
	 * @return the Team if it exists
	 */
	public static Team getTeam(String name) {
		return find().where().eq("teamName", name).findUnique();
	}

	/**
	 * Checks if id belongs to a team.
	 * 
	 * @param id the teams id
	 * @return true if team exists, false otherwise
	 */
	public static boolean isTeam(long id) {
		Team team = getTeam(id);
		return !(team == null);
	}

	/**
	 * Checks if teamName belongs to a team.
	 * 
	 * @param teamName team name
	 * @return true if team exists, false otherwise
	 */
	public static boolean isTeam(String teamName) {
		Team team = getTeam(teamName);
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
	 * @return the location
	 */
	public String getHomeCourt() {
		return homeCourt;
	}

	/**
	 * @return the teamType
	 */
	public String getTeamType() {
		return teamType;
	}

	/**
	 * @return the skillLevel
	 */
	public String getSkillLevel() {
		return skillLevel;
	}

	/**
	 * @return the roster
	 */
	public List<Player> getRoster() {
		return roster;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * Returns the roster as a java String list.
	 * 
	 * @return the roster as a list
	 */
	public List<String> getRosterList() {
		List<String> rosterList =  new ArrayList<>();
		for (Player player : roster) {
			//TODO Change nickname to actual name
			rosterList.add(player.getUser().getName());
		}
		return rosterList;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
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
	 * @return the threePt
	 */
	public double getThreePt() {
		return threePt;
	}

	/**
	 * @return the twoPt
	 */
	public double getTwoPt() {
		return twoPt;
	}

	/**
	 * @return the onePt
	 */
	public double getFreeThrow() {
		return freeThrow;
	}

	/**
	 * @return the rebounds
	 */
	public int getRebounds() {
		return rebounds;
	}

	/**
	 * @return the steals
	 */
	public int getSteals() {
		return steals;
	}

	/**
	 * @return the blocks
	 */
	public int getBlocks() {
		return blocks;
	}

	public int getWins() {
		return wins;
	}

	public int getLosses() {
		return losses;
	}

	public int getPointsFor() {
		return pointsFor;
	}

	public int getPointsAgainst() {
		return pointsAgainst;
	}

	public String getOpponents() {
		return opponents;
	}

	public long getId() {
		return id;
	}

	public String getOpponent() {
		String[] s = opponents.split("~");
		opponents = "";
		String string = s[0];
		for (int i = 1; i < s.length; i++) {
			opponents += (s[i] + "~");
		}
		this.save();
		return string;
	}
	
	public int getAssists() {
		return assists;
	}

	public Player getCaptain() {
		return captain;
	}

	public List<League> getLeagues() {
		return leagues;
	}

	public void setAssists(int assists) {
		this.assists = assists;
	}

	public void setCaptain(Player captain) {
		this.captain = captain;
	}

	public void setLeagues(List<League> leagues) {
		this.leagues = leagues;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	/**
	 * @param teamName the teamName to set
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	/**
	 * @param location the location to set
	 */
	public void setHomeCourt(String homeCourt) {
		this.homeCourt = homeCourt;
	}

	/**
	 * @param teamType the teamType to set
	 */
	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	/**
	 * @param skillLevel the skillLevel to set
	 */
	public void setSkillLevel(String skillLevel) {
		this.skillLevel = skillLevel;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @param roster the roster to set
	 */
	public void setRoster(List<Player> roster) {
		this.roster = roster;
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
	 * @param record the record to set
	 */
	public void setRecord(String record) {
		this.record = record;
	}

	/**
	 * @param threePt the threePt to set
	 */
	public void setThreePt(double threePt) {
		this.threePt = threePt;
	}

	/**
	 * @param twoPt the twoPt to set
	 */
	public void setTwoPt(double twoPt) {
		this.twoPt = twoPt;
	}

	/**
	 * @param onePt the onePt to set
	 */
	public void setFreeThrow(double onePt) {
		this.freeThrow = onePt;
	}

	/**
	 * @param rebounds the rebounds to set
	 */
	public void setRebounds(int rebounds) {
		this.rebounds = rebounds;
	}

	/**
	 * @param steals the steals to set
	 */
	public void setSteals(int steals) {
		this.steals = steals;
	}

	/**
	 * @param blocks the blocks to set
	 */
	public void setBlocks(int blocks) {
		this.blocks = blocks;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public void setPointsFor(int pointsFor) {
		this.pointsFor = pointsFor;
	}

	public void setPointsAgainst(int pointsAgainst) {
		this.pointsAgainst = pointsAgainst;
	}

	public void removeOpponent(String opponent) {
		String[] s = opponents.split("~");
		opponents = "";
		for (int i = 0; i < s.length; i++) {
			if (!s[i].equals(opponent)) {
				opponents += (s[i] + "~");
			}
		}
		this.save();
	}

	/**
	 * Adds a comment to the team's list.
	 * 
	 * @param comment the comment
	 */
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	public void addOpponent(String opponent) {
		opponents += (opponent + "~");
		this.save();
	}

	public void setOpponents(String opp) {
		this.opponents = opp;
		this.save();
	}
}
