package views.formdata.leagues;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import models.leagues.League;
import models.teams.Team;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

@Entity
public class LeagueForm {

	@Id
	/*the id number of the league*/
	public long id;
	
	/*the name of the league*/
	//@Constraints.Required(message = "League name is required")
    public String leagueName;

	/*A short description of the league*/
    //@Constraints.Required(message = "Description is required")
    public String description;
    
    /*the number of teams allowed in the league*/
    //@Constraints.Required(message = "Number of teams is required")
    public int numTeams;
    
    /*the number of teams currently in the league*/
    public int numTeamsInLeague;
    
    /*The start date of the league*/
    //@Constraints.Required(message = "Start date is required")
    public String startDate;
    
    /*The end date of the league*/
    public String endDate;
    
    public String pubOrPrivate;
    
    public int regStep;

    public int numGames;
    
    public List<String> teams = new ArrayList<>();
    
    public List<String> dateList = new ArrayList<>();
    
    public String court;
    
    /**
     * no arguments constructor
     */
    public LeagueForm(){
    	
    }

    /**
     * Constructor for defining all the attributes of the league
     * 
     * @param leagueName the name of the league
     * @param description a short description of the league
     * @param numTeams the number of teams in the league
     * @param startDate the starting date of the league
     * @param endDate the ending date of the league
     */
    
    public LeagueForm(League league)
    {
        this.leagueName = league.getLeagueName();
        this.description = league.getDescription();
        this.numTeams = league.getNumTeams();
        this.startDate = league.getStartDate();
        this.endDate = league.getEndDate();
        this.pubOrPrivate = league.getPubOrPrivate();
        this.numTeamsInLeague = league.getNumTeamsInLeague();
        this.regStep = league.getRegStep();
        this.numGames = league.getNumGames();
        if(league.getCourt()!=null){
          this.court = league.getCourt().getName();
        }
        if(league.getTeams() != null){
          for(int i = 0; i < league.getTeams().size(); i++){
            this.teams.add(league.getTeams().get(i).getTeamName());
          }
        }
        this.dateList = league.getDateList();
    }
    
    public List<ValidationError> validate() {
      List<ValidationError> errors = new ArrayList<>();

      // Milestone 3, check if team being added already exists in the database.
      return errors.isEmpty() ? null : errors;
    }
}
