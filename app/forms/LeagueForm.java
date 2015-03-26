package forms;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;

@Entity
public class LeagueForm {

    @Id
    private long id;
  
    /*the name of the league*/
    @Constraints.Required(message = "League name is required")
    public String leagueName;

    /*A short description of the league*/
    @Constraints.Required(message = "Description is required")
    public String description;
    
    /*the number of teams in the league*/
    @Constraints.Required(message = "Number of teams is required")
    public int numTeams;
    
    /*The start date of the league*/
    @Constraints.Required(message = "Start date is required")
    public int startDate;
    
    /*The end date of the league*/
    public int endDate;
    
    /**
     * no arguments constructor
     */
    public LeagueForm(){
      
    }

    /**
     * Constructor for initializing all the fields
     * @param leagueName the name of the new league
     * @param description the short description of the league
     * @param numTeams the number of teams in the league
     * @param startDate the starting date of the league
     * @param endDate the end date of the league
     */
    public LeagueForm(String leagueName, String description, int numTeams, int startDate, int endDate)
    {
        this.leagueName = leagueName;
        this.description = description;
        this.numTeams = numTeams;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    /**
     * Constructor for initializing only the required fields
     * @param leagueName the name of the new league
     * @param description the short description of the league
     * @param numTeams the number of teams in the league
     * @param startDate the starting date of the league
     */
    public LeagueForm(String leagueName, String description, int numTeams, int startDate)
    {
        this.leagueName = leagueName;
        this.description = description;
        this.numTeams = numTeams;
        this.startDate = startDate;
    }

    /**
     * @return the id
     */
    public long getId() {
      return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
      this.id = id;
    }

    /**
     * @return the leagueName
     */
    public String getLeagueName() {
      return leagueName;
    }

    /**
     * @param leagueName the leagueName to set
     */
    public void setLeagueName(String leagueName) {
      this.leagueName = leagueName;
    }

    /**
     * @return the description
     */
    public String getDescription() {
      return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
      this.description = description;
    }

    /**
     * @return the numTeams
     */
    public int getNumTeams() {
      return numTeams;
    }

    /**
     * @param numTeams the numTeams to set
     */
    public void setNumTeams(int numTeams) {
      this.numTeams = numTeams;
    }

    /**
     * @return the startDate
     */
    public int getStartDate() {
      return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(int startDate) {
      this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public int getEndDate() {
      return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(int endDate) {
      this.endDate = endDate;
    }
    
    
}
