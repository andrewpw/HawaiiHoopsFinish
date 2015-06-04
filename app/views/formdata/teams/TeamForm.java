package views.formdata.teams;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import models.Player;
import models.teams.Team;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

/**
 * The backing class for the team form.
 * 
 * @author AJ
 * 
 */
public class TeamForm {

  @Id
  public long id;

  @Constraints.Required(message = "A team name is required.")
  public String teamName;

  @Constraints.Required(message = "A team type is required.")
  public String teamType;

  @Constraints.Required(message = "Players are required for a team, aren't they?")
  public List<Player> roster = new ArrayList<>();

  public String homeCourt;
  
  public String skillLevel;
  
  public String description;

  public String imageUrl;
  
  public Player captain;
  
  public int wins;
  
  public int losses;
  
  public int pointsFor;
  
  public int pointsAgainst;

  public TeamForm() {
  }

  public TeamForm(Team team) {
	  this.description = team.getDescription();
	  this.imageUrl = team.getImageUrl();
	  this.homeCourt = team.getImageUrl();
	  this.losses = team.getLosses();
	  this.pointsAgainst = team.getPointsAgainst();
	  this.pointsFor = team.getPointsFor();
	  this.roster = team.getRoster();
	  this.skillLevel = team.getSkillLevel();
	  this.teamName = team.getTeamName();
	  this.teamType = team.getTeamType();
	  this.wins = team.getWins();
	  this.captain = team.getCaptain();
  }
  
  public List<ValidationError> validate() {
    List<ValidationError> errors = new ArrayList<>();

    // Milestone 3, check if team being added already exists in the database.
    return errors.isEmpty() ? null : errors;
  }
}
