package controllers;

import java.util.Collections;
import java.util.List;
import com.avaje.ebean.Page;
import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;
import models.User;
import models.games.Game;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.data.Form;
import views.formdata.SearchFormData;
import views.formdata.games.GameForm;
import views.html.games.SingleGame;
import views.html.games.CreateGame;
import views.html.games.AllGames;
import views.html.games.RequestSent;
import utils.Tags;

/**
 * Implements the controllers for this application.
 */
public class Games extends Controller {

  /**
   * Returns a page containing all upcoming games.
   * 
   * @return the list of games
   */
  @Security.Authenticated(Secured.class)
  public static Result allGames(Integer page, String sort) {

    Game.deletePastGames();

    SearchFormData sfd = new SearchFormData();
    Form<SearchFormData> stuff = Form.form(SearchFormData.class).fill(sfd);

    Page<Game> games = Game.find("gameTime asc", page);
    return ok(AllGames.render("All Games", games, stuff, Secured.isLoggedIn(ctx()), sort));
  }

  /**
   * Brings a page showing info about a particular game.
   * 
   * @param name the name of the game
   * @return the page related to the game
   */
  @Security.Authenticated(Secured.class)
  public static Result getGame(long id, String name) {

    Game game = Game.getGame(id);

    if (game == null) {
      throw new RuntimeException("Not a valid game.");
    }
    else {
      return ok(SingleGame.render("Game: " + game.getName(), game, Secured.isLoggedIn(ctx())));
    }

  }

  /**
   * A page containing the fields required to create a game.
   * 
   * @return the Create Game page.
   */
  @Security.Authenticated(Secured.class)
  public static Result createGame() {
    GameForm gameForm = new GameForm();
    Form<GameForm> formdata = Form.form(GameForm.class).fill(gameForm);

    return ok(CreateGame.render("Create Game", "Create", formdata, Secured.isLoggedIn(ctx())));
  }

  /**
   * Adds a game to the list of upcoming games.
   * 
   * @return the list of upcoming games if the form has no errors
   */
  @Security.Authenticated(Secured.class)
  public static Result addGame() {

    Form<GameForm> gameForm = Form.form(GameForm.class).bindFromRequest();

    if (gameForm.hasErrors()) {
      return badRequest(CreateGame.render("Create Game", "Create", gameForm, Secured.isLoggedIn(ctx())));
    }
    else {
      GameForm game = gameForm.get();

      User user = Secured.getUserInfo(ctx());

      Game addedGame = Game.addGame(game, user);

      return redirect(routes.Games.getGame(addedGame.getId(), Tags.slugify(addedGame.getName())));

    }
  }

  /**
   * Brings up the form required to edit the game.
   * 
   * @param name name of game
   * @return the create game page
   */
  @Security.Authenticated(Secured.class)
  public static Result editGame(long id, String name) {
    GameForm data = new GameForm(Game.find().byId(id));
    Form<GameForm> formdata = Form.form(GameForm.class).fill(data);
    return ok(CreateGame.render("Edit Game", "Edit", formdata, Secured.isLoggedIn(ctx())));

  }

  /**
   * Search for games.
   * 
   * @return All games page with the results
   */
  @Security.Authenticated(Secured.class)
  public static Result searchResults(Integer page) {
    SearchFormData sfd = new SearchFormData();
    Form<SearchFormData> stuff = Form.form(SearchFormData.class).fill(sfd);

    Form<SearchFormData> sfd2 = Form.form(SearchFormData.class).bindFromRequest();
    SearchFormData search = sfd2.get();

    Page<Game> results = Game.find(search.term, "gameTime asc", page);
    return ok(AllGames.render("Results", results, stuff, Secured.isLoggedIn(ctx()), "date asc"));
  }

  /**
   * Adds the logged in user to a public game if user joins.
   * 
   * @param gameName the name of the game
   * @return the game's page
   */
  @Security.Authenticated(Secured.class)
  public static Result joinPublic(long id, String gameName) {

    Game game = Game.getGame(id);
    String name = Secured.getUserInfo(ctx()).getName();

    String players = game.getPlayers();
    StringBuilder sb = new StringBuilder(players);
    sb.append(", " + name);
    game.setPlayers(sb.toString());
    game.save();

    return redirect(routes.Games.getGame(id, Tags.slugify(gameName)));
  }

  @Security.Authenticated(Secured.class)
  public static Result joinPrivate(long id, String gameName) {

    Game game = Game.getGame(id);
    String user = Secured.getUserInfo(ctx()).getName();
    MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();

    // Null pointer when getting user creator
    User creator = game.getCreator();
    String email = creator.getEmail();

    String url = routes.Games.allowPrivate(id, gameName, user).absoluteURL(request());

    mail.setSubject(user + " would like to join: " + gameName);
    // Set recipient to game creator after null pointer is sorted out.
    // Used for testing purposes
    mail.setRecipient(email);
    mail.setFrom("hawaiihoopsnetwork@gmail.com");
    mail.sendHtml("<html> <h1>A player wants to join your game!</h1>" + "<hr><br>" + user
        + " would like to join your game " + gameName + "<br><br><a href='" + url
        + "'>Confirm</a> <br><br>If you don't want this player to join this game, ignore this email.</html>");
    return ok(RequestSent.render("Request Sent", Secured.isLoggedIn(ctx()), gameName, id));
  }

  /**
   * Allows the game creator to add a player to the list if a player requests.
   * 
   * @param gameName the game name
   * @param user user wanting to join game
   * @return the game's page
   */
  public static Result allowPrivate(long id, String gameName, String user) {

    Game game = Game.getGame(id);

    String players = game.getPlayers();

    if (players.contains(user)) {
      return redirect(routes.Games.getGame(id, gameName));
    }

    StringBuilder sb = new StringBuilder(players);
    sb.append(", " + user);
    game.setPlayers(sb.toString());
    game.save();

    return redirect(routes.Games.getGame(id, gameName));
  }

  @Security.Authenticated(Secured.class)
  public static Result unjoin(long id, String gameName) {

    Game game = Game.getGame(id);
    String userName = Secured.getUserInfo(ctx()).getName();
    userName = ", " + userName;

    String players = game.getPlayers();
    StringBuilder sb = new StringBuilder(players);
    int start = players.indexOf(userName);
    int length = userName.length();
    int end = length + start;
    sb.delete(start, end);
    game.setPlayers(sb.toString());
    game.save();
    return redirect(routes.Games.getGame(id, gameName));
  }
}
