package models.leagues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.avaje.ebean.Query;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import forms.LeagueForm;
import views.formdata.teams.TeamForm;
import models.Court;
import models.teams.Team;

@Entity
public class League extends Model{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Id
  private long id;
  
  private String leagueName;
  
  private int numTeams;

  private int numTeamsInLeague;
  
  private String startDate;
  
  private String endDate;
  
  private String description;
  
  //True for public, false for private
  private String pubOrPrivate; 
  
  private int regStep;
  
  private int numGames;
  
  @ManyToMany(mappedBy = "leagues", cascade=CascadeType.ALL)
  private List<Team> teams = new ArrayList<>();
  
  private String dateList = "";
  
  @ManyToOne
  private Court court;
  
  private String schedule = "";
  
  public League(String name){
    this.leagueName = name;
    this.startDate = "";
    this.endDate = "";
    this.numTeams = 0;
    this.description = "";
    this.pubOrPrivate = "public";
    this.numTeamsInLeague = 0;
    this.regStep = 0;
    this.numGames = 0;
  }
  
  public League(String name, String startDate, String endDate, String pubOrPrivate){
    this.leagueName = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.numTeams = 0;
    this.description = "";
    this.pubOrPrivate = pubOrPrivate;
    this.numTeamsInLeague = 0;
    this.regStep = 0;
    this.numGames = 0;
  }

  public League(long id, String name, String startDate, String endDate, String pubOrPrivate, int regStep){
    this.id = id;
    this.leagueName = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.numTeams = 0;
    this.description = "";
    this.pubOrPrivate = pubOrPrivate;
    this.numTeamsInLeague = 0;
    this.regStep = regStep;
    this.numGames = 0;
  }
  
  public static List<String> typeOfLeague() {
    String[] types = { "public", "private" };
    return java.util.Arrays.asList(types);
  }
  
  public static Map<Integer, Boolean> num(){
    Map<Integer, Boolean> nums = new TreeMap<Integer, Boolean>();
    for(int i = 1; i < 21; i++){
      nums.put(i, false);
    }
    return nums;
  }
  
  public static Map<String, Boolean> getTeamMap(){
    Map<String, Boolean> teamMap = new TreeMap<String, Boolean>();
    List<Team> teamList = Team.getTeams();
    for(int i = 0; i < teamList.size(); i++){
      teamMap.put(teamList.get(i).getTeamName(), false);
    }
    return teamMap;
  }
  
  public static Map<String, Boolean> getCourtMap(){
    Map<String, Boolean> courtMap = new TreeMap<String, Boolean>();
    List<Court> courtList = Court.getCourts();
    for(int i = 0; i < courtList.size(); i++){
      courtMap.put(courtList.get(i).getName(), false);
    }
    return courtMap;
  }
  
  public static Map<String, Boolean> getDateMap(String startDate, String endDate){
    List<LocalDate> dates = new ArrayList<LocalDate>();
    Map<String, Boolean> dateMap = new TreeMap<String, Boolean>();
    LocalDate startld = new LocalDate(Integer.parseInt(startDate.substring(6)), Integer.parseInt(startDate.substring(0, 2)),
        Integer.parseInt(startDate.substring(3, 5)));
    LocalDate endld = new LocalDate(Integer.parseInt(endDate.substring(6)), Integer.parseInt(endDate.substring(0, 2)),
        Integer.parseInt(endDate.substring(3, 5)));
    int days = Days.daysBetween(startld, endld).getDays();
    for (int i=0; i < days; i++) {
        LocalDate d = startld.withFieldAdded(DurationFieldType.days(), i);
        dateMap.put(d.toString("MM/dd/yyyy"), false);
    }
    return dateMap;
  }
  
  public Court getCourt() {
    return court;
  }

  public void setCourt(Court court) {
    this.court = court;
  }

  public List<String> getDateList() {
    List<String> dlList = new ArrayList<>();
    String[] s = dateList.split("~");
    for(int i = 0; i < s.length; i++){
      dlList.add(s[i]);
    }
    return dlList;
  }

  public void setDateList(List<String> dateList) {
    this.dateList = "";
    for(int i = 0; i < dateList.size(); i++){
      this.dateList += dateList.get(i) + "~";
    }
  }
  
  public String getPubOrPrivate() {
    return pubOrPrivate;
  }

  public String getLeagueName(){
    return leagueName;
  }
  
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
  
  public int getNumTeams() {
    return numTeams;
  }

  public void setNumTeams(int numTeams) {
    this.numTeams = numTeams;
  }
  
  public void addNumTeams(int numTeams) {
    numTeams++;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String isPubOrPrivate() {
    return pubOrPrivate;
  }

  public void setPubOrPrivate(String pubOrPrivate) {
    this.pubOrPrivate = pubOrPrivate;
  }

  public List<Team> getTeams() {
    return teams;
  }
  
  public int getNumTeamsInLeague() {
    return numTeamsInLeague;
  }

  public void setTeams(List<Team> teams) {
    this.teams = teams;
  }
  
  public int getRegStep() {
    return regStep;
  }

  public void setRegStep(int regStep) {
    this.regStep = regStep;
  }

  public void setLeagueName(String leagueName) {
    this.leagueName = leagueName;
  }
  
  public void addTeam(Team team){
    if(!teams.contains(team)){
      numTeamsInLeague++;
      teams.add(team);
    }
  }
  
  public int getNumGames() {
    return numGames;
  }

  public void setNumGames(int numGames) {
    this.numGames = numGames;
  }

  
  /**
  public static void addSchedule(LeagueForm lf) {
    League league;

    long id = lf.id;

    if (!isLeague(id)) {
      league = new League(lf.leagueName, id);
      league.save();
    }
    else {
      league = getLeague(id);
      league.setStartDate(lf.startDate);
      league.setEndDate(lf.endDate);
    }
  }
  */

  /**
  private static boolean isLeague(long id2) {
    // TODO Auto-generated method stub
    return false;
  }

  private static League getLeague(long id2) {
    // TODO Auto-generated method stub
    return null;
  }

  private void setEndDate(int endDate) {
    // TODO Auto-generated method stub
    
  }

  private void setStartDate(int startDate) {
    // TODO Auto-generated method stub
    
  }
  */
  
  public static Finder<Long, League> find() {
    return new Finder<Long, League>(Long.class, League.class);
  }
  
  public static Page<League> find(String sort, int page) {
    return find().where().orderBy(sort).findPagingList(10).setFetchAhead(false).getPage(page);
  }
  
  public static Page<League> find(String term, String sort, int page) {
    term = "%" + term + "%";
    Query<League> q = Ebean.createQuery(League.class);
    q.where().disjunction().add(Expr.ilike("leagueName", term)).add(Expr.ilike("pubOrPrivate", term))
        .add(Expr.ilike("id", term));
    return q.orderBy(sort).findPagingList(50).setFetchAhead(false).getPage(page);
  }
  
  public String getSchedule() {
    return schedule;
  }
  
  public void addOpponent(String date, String team, String opponent){
    schedule += date + "~" + team + "~" + opponent + "~";
  }
  
  public boolean containsDateTeam(String date, String team){
    String[] s = schedule.split("~");
    for(int i = 0; i < s.length - 1; i++){
      if(s[i].equals(date)){
        if(s[i+1].equals(team) || s[i+2].equals(team)){
          return true;
        }
      }
    }
    return false;
  }
  
  public List<String> getScheduleList(){
    
    List<String> sched = new ArrayList<String>();
    System.out.println(schedule);
    String[] s = schedule.split("~");
    for(int i = 0; i < s.length; i+=3){
      if(s.length >= 3){
      sched.add(s[i] + "-" + s[i+1] + "~" + s[i+2]);
      }
    }
    return sched;
  }
  
  public void eraseSchedule(){
    schedule = "";
  }
}
