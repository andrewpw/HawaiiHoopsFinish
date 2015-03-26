package controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.avaje.ebean.Page;
import models.Comment;
import models.Court;
import models.leagues.League;
import models.leagues.LeagueDB;
import models.teams.Team;
import models.teams.TeamDB;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.formdata.CommentForm;
import views.formdata.SearchFormData;
import views.formdata.leagues.LeagueForm;
import views.formdata.teams.TeamForm;
import views.html.leagues.LeagueList;
import views.html.leagues.LeagueSchedule;
import views.html.leagues.AllLeagues;
import views.html.leagues.SearchLeagues;
import views.html.teams.AllTeams;
import play.mvc.Security;

public class Leagues extends Controller{

  /**
   * returns the LeaguesPage
   * 
   * @return the LeaguesPage
   */
  @Security.Authenticated(Secured.class)
  public static Result leagues(Long id) {
    System.out.println("leagues id " + id);
    League league = LeagueDB.getLeague(id);
    List<Team> listTeam = league.getTeams();
    LeagueForm leagueForm = new LeagueForm();
    if(id != 1){
      leagueForm = new LeagueForm(league);
    }
    Form<LeagueForm> emptyForm = Form.form(LeagueForm.class).fill(leagueForm);
    
    return ok(LeagueList.render("Leagues", league, emptyForm, listTeam, Secured.isLoggedIn(ctx())));
  }
  
  @Security.Authenticated(Secured.class)
  public static Result searchLeagues(Integer page) {
    SearchFormData st = new SearchFormData();
    Form<SearchFormData> stuff = Form.form(SearchFormData.class).fill(st);

    Form<SearchFormData> searcher = Form.form(SearchFormData.class).bindFromRequest();
    SearchFormData st2 = searcher.get();

    Page<League> currPage = League.find(st2.term, "leagueName asc", page);

    return ok(SearchLeagues.render("Leagues", currPage, Secured.isLoggedIn(ctx()), st2.term));
  }
 
  public static Result editSchedule(){
    LeagueForm leagueForm = new LeagueForm();
    Form<LeagueForm> emptyForm = Form.form(LeagueForm.class).fill(leagueForm);
    return ok(LeagueSchedule.render("Create Schedule", emptyForm, Secured.isLoggedIn(ctx())));
  }
  
public static void generateSchedule(League league){
    
    List<Team> opp = league.getTeams();
    List<Team> home = new ArrayList<Team>();
    List<Team> away = new ArrayList<Team>();

    if(opp == null || opp.size() == 1){
      
    }
    else if(opp.size() < 3){
      for(int i = 0; i < league.getDateList().size(); i++){
        List<String> dates = league.getDateList();
        league.addOpponent(dates.get(i), opp.get(0).getTeamName(), opp.get(1).getTeamName());
      }
    }
    else{
      for(int i = 0, j = opp.size() / 2; i < opp.size() / 2; i++, j++){
        home.add(opp.get(i));
        away.add(opp.get(j));
        if((i+1)*2+1 == opp.size()){
          home.add(opp.get((i+1)*2));
          away.add(null);
        }
      }
      
      
      List<String> dates = league.getDateList();
      for(int i = 0; i < league.getDateList().size(); i++){
        for(int j = 0; j < home.size(); j++){
          if(away.get(j) != null && home.get(j) != null){
            league.addOpponent(dates.get(i), home.get(j).getTeamName(), away.get(j).getTeamName());
          }
          else if(away.get(j) == null){
            league.addOpponent(dates.get(i), home.get(j).getTeamName(), "null");
          }
          else {
            league.addOpponent(dates.get(i), away.get(j).getTeamName(), "null");
          }
        }
        away.add(home.remove(home.size() -1));
        home.add(1, away.remove(0));
      }
    }
    
  }

  public static Result editLeague(long id){
    League league = LeagueDB.getLeague(id);
    Form<LeagueForm> tempForm = Form.form(LeagueForm.class).bindFromRequest();
    int regStep = league.getRegStep();

    switch(regStep){
    case 1:
      league.setNumTeams(tempForm.get().numTeams);
      league.setNumGames(tempForm.get().numGames);
      league.setRegStep(2);
      break;
    case 2:
      league.setDateList(tempForm.get().dateList);
      league.setRegStep(3);
      break;
    case 3:
      List<String> teamList = tempForm.get().teams;
      
      for(int i = 0; i < teamList.size(); i++){
        if(!league.getTeams().contains(Team.getTeam(teamList.get(i)))){
          league.addTeam(Team.getTeam(teamList.get(i)));
        }
      }
      league.eraseSchedule();
      generateSchedule(league);
      league.setRegStep(4);
      break;
    case 4:
      league.setCourt(Court.getCourt(tempForm.get().court));
      league.setDescription(tempForm.get().description);
      league.setRegStep(5);
      break;
    case 5:
      break;
    }
    
    
    LeagueDB.addLeague(league);
    LeagueForm leagueForm = new LeagueForm(league);
    Form<LeagueForm> form = Form.form(LeagueForm.class).fill(leagueForm);
    return ok(LeagueList.render("League Team", league, form, league.getTeams(), Secured.isLoggedIn(ctx())));
  }
  
  public static Result addTeam(long id){
    League league = LeagueDB.getLeague(id);
    Form<LeagueForm> tempForm = Form.form(LeagueForm.class).bindFromRequest();
    league.addTeam(Team.getTeam(tempForm.get().teams.get(0)));
    league.eraseSchedule();
    generateSchedule(league);
    LeagueDB.addLeague(league);
    LeagueForm leagueForm = new LeagueForm(league);
    Form<LeagueForm> form = Form.form(LeagueForm.class).fill(leagueForm);
    return ok(LeagueList.render("League Team", league, form, league.getTeams(), Secured.isLoggedIn(ctx())));
  }
  
  public static Result changeSchedule(){
    League league = LeagueDB.getLeague(1);
    
    Form<LeagueForm> leagueForm = Form.form(LeagueForm.class).bindFromRequest();

    if (leagueForm.hasErrors()) {
      return badRequest(LeagueSchedule.render("Create Schedule", leagueForm, Secured.isLoggedIn(ctx())));
    }
    else {
      LeagueForm schedule = leagueForm.get();
      //League.addSchedule(schedule);
      List<Team> listTeam = league.getTeams();
      return ok(LeagueList.render("League Team", league, leagueForm, listTeam, Secured.isLoggedIn(ctx())));
    }
  }
  
  @Security.Authenticated(Secured.class)
  public static Result addLeague(){
    Form<LeagueForm> leagueForm = Form.form(LeagueForm.class).bindFromRequest();
    List<Team> listTeam = LeagueDB.getLeague(1).getTeams();

    if (leagueForm.hasErrors()) {
      return badRequest(LeagueList.render("League Team", LeagueDB.getLeague(1), leagueForm, listTeam, Secured.isLoggedIn(ctx())));
    }
    else {
      LeagueForm form = leagueForm.get();
      LeagueDB.addLeague(form);
      if(LeagueDB.getLeague(leagueForm.get().id) == null){
        listTeam = LeagueDB.getTeamsInLeague(1);
      }
      else{
        listTeam = LeagueDB.getTeamsInLeague(leagueForm.get().id);
      }
      
      //return ok(LeagueList.render("League Team", LeagueDB.getLeague(leagueForm.get().id), leagueForm, listTeam, Secured.isLoggedIn(ctx())));
      return redirect("/leagues/view/" + form.id);
    }
  }
  
  public static Result showLeague(Long id) {
    
    League league = LeagueDB.getLeague(id);
    List<Team> listTeam = TeamDB.getTeams();
    LeagueForm leagueForm = new LeagueForm();
    Form<LeagueForm> emptyForm = Form.form(LeagueForm.class).fill(leagueForm);
    
    return ok(LeagueList.render("League Team", league, emptyForm, listTeam, Secured.isLoggedIn(ctx())));
  }
  
  @Security.Authenticated(Secured.class)
  public static Result allLeagues(String sort, Integer page) {

    SearchFormData st = new SearchFormData();
    Form<SearchFormData> stuff = Form.form(SearchFormData.class).fill(st);

    Page<League> currPage = League.find(sort, page);
    return ok(AllLeagues.render("All leagues", currPage, sort, stuff, Secured.isLoggedIn(ctx())));
  }
}
