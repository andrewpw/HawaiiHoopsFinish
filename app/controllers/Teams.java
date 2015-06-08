package controllers;

import java.util.List;

import models.Comment;
import models.teams.Team;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Tags;
import views.formdata.CommentForm;
import views.formdata.SearchFormData;
import views.formdata.teams.StatForm;
import views.formdata.teams.TeamForm;
import views.html.teams.AllTeams;
import views.html.teams.CreateTeam;
import views.html.teams.EditTeamStats;
import views.html.teams.SearchTeams;
import views.html.teams.ShowTeam;

import com.avaje.ebean.Page;

/**
 * Implements the controllers for this application.
 */
public class Teams extends Controller {

  /**
   * Returns the All teams page listing all the teams in the database.
   * 
   * @param sort the sort order
   * @param page the current page number
   * @return AllTeams page
   */
  public static Result allTeams(String sort, Integer page) {

    SearchFormData st = new SearchFormData();
    Form<SearchFormData> stuff = Form.form(SearchFormData.class).fill(st);

    Page<Team> currPage = Team.find(sort, page);
    return ok(AllTeams.render("All teams", currPage, sort, stuff));
  }

  /**
   * Search method.
   * 
   * @param page page number.
   * @return all teams page
   */
  public static Result searchTeams(Integer page) {
    SearchFormData st = new SearchFormData();
    Form<SearchFormData> stuff = Form.form(SearchFormData.class).fill(st);

    Form<SearchFormData> searcher = Form.form(SearchFormData.class).bindFromRequest();
    SearchFormData st2 = searcher.get();

    Page<Team> currPage = Team.find(st2.term, "teamName asc", page);

    return ok(SearchTeams.render(" Teams", currPage, st2.term));
  }

  /**
   * Returns the page containing the create team form.
   * 
   * @return create team form.
   */
  public static Result createTeam() {
    TeamForm teamForm = new TeamForm();
    Form<TeamForm> emptyForm = Form.form(TeamForm.class).fill(teamForm);
    return ok(CreateTeam.render("Create Team", emptyForm));
  }

  /**
   * Adds a team to the database after the create team form has been filled out correctly.
   * 
   * @return the team page related to the team
   */
  public static Result addTeam() {
    Form<TeamForm> teamForm = Form.form(TeamForm.class).bindFromRequest();

    if (teamForm.hasErrors()) {
      return badRequest(CreateTeam.render("Create Team", teamForm));
    }
    else {
      TeamForm tf = teamForm.get();
      Team.addTeam(tf);
      Team newTeam = Team.getTeam(tf.teamName);
      return redirect(routes.Teams.showTeam(newTeam.getId(), Tags.slugify(newTeam.getTeamName())));
    }
  }

  /**
   * Returns the page containing the teams info.
   * 
   * @param teamName the team name
   * @return the team page
   */
  public static Result showTeam(Long id, String teamName) {
    CommentForm cf = new CommentForm();
    Form<CommentForm> empty = Form.form(CommentForm.class).fill(cf);

    Team team = Team.getTeam(id);
    List<Comment> comments = Comment.getComments(team);
    return ok(ShowTeam.render("View Team: " + team.getTeamName(), team, empty, comments));
  }

  /**
   * Posts a comment to the given team's board.
   * 
   * @param teamName the team name
   * @return the team's page if there are no errors.
   */
  public static Result postComment(Long id, String teamName) {
    Form<CommentForm> cf = Form.form(CommentForm.class).bindFromRequest();

    Team team = Team.getTeam(id);
    List<Comment> comments = Comment.getComments(team);

    if (cf.hasErrors()) {
      return badRequest(ShowTeam.render("View Team: " + team.getTeamName(), team, cf, comments));
    }
    else {
      CommentForm com = cf.get();

      Team team2 = Team.getTeam(id);
      Comment.addComment(team2, Secured.getUserInfo(ctx()), com);

      return redirect(routes.Teams.showTeam(id, Tags.slugify(teamName)));
    }

  }

  public static Result editStats(Long id, String teamName) {

    Team team = Team.getTeam(id);
    
    StatForm st = new StatForm(team);
    Form<StatForm> stats = Form.form(StatForm.class).fill(st);

    return ok(EditTeamStats.render("Edit Stats: " + team.getTeamName(), team, stats));
  }


  public static Result postStats(Long id, String teamName) {
    Team team = Team.getTeam(id);

    Form<StatForm> st = Form.form(StatForm.class).bindFromRequest();

    if (st.hasErrors()) {
      return badRequest(EditTeamStats.render("Edit Stats: " + team.getTeamName(), team, st));
    }
    else {
      StatForm stat = st.get();
      team.setRecord(stat.record);
      team.setThreePt(stat.threePt);
      team.setTwoPt(stat.twoPt);
      team.setOnePt(stat.onePt);
      team.setRebounds(stat.rebounds);
      team.setSteals(stat.steals);
      team.setBlocks(stat.blocks);
      team.setRoster(stat.roster);
      team.save();

      return redirect(routes.Teams.showTeam(team.getId(), Tags.slugify(teamName)));
    }
  }

}
