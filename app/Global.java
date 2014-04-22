import controllers.Users;
import models.Court;
import models.Player;
import models.User;
import models.games.Game;
import models.teams.Team;
import models.teams.TeamDB;
import play.Application;
import play.GlobalSettings;
import views.formdata.PlayerFormData;
import views.formdata.games.GameForm;
import play.Play;
import play.mvc.Http.Context;

/**
 * Initialization for the application. will have three player's bios.
 * 
 * @author scotthonda
 * 
 */
public class Global extends GlobalSettings {

  /**
   * Initialize the app with surfers.
   * 
   * @param app
   */
  public void onStart(Application app) {
    
      if (Court.getCourts().size() == 0) {
        Court.addCourt("Aina Haina", "Private", "123 Somewhere", (float) 1234, (float) 12345.0, "It's Awsome");
        Court.addCourt("B", "Private", "123 Somewhere", (float) 1234, (float) 12345.0, "It's Awsome");
        Court.addCourt("C", "Private", "123 Somewhere", (float) 1234, (float) 12345.0, "It's Awsome");
        Court.addCourt("D", "Private", "123 Somewhere", (float) 1234, (float) 12345.0, "It's Awsome");
        Court.addCourt("E", "Private", "123 Somewhere", (float) 1234, (float) 12345.0, "It's Awsome");
        Court.addCourt("F", "Private", "123 Somewhere", (float) 1234, (float) 12345.0, "It's Awsome");
        Court.addCourt("G", "Private", "123 Somewhere", (float) 1234, (float) 12345.0, "It's Awsome");
        Court.addCourt("H", "Private", "123 Somewhere", (float) 1234, (float) 12345.0, "It's Awsome");
        Court.addCourt("I", "Private", "123 Somewhere", (float) 1234, (float) 12345.0, "It's Awsome");
      }
  
      if (TeamDB.getTeams().size() == 0) {
        TeamDB.addTeam(new Team("teamName", "String location", "String teamType", "String skillLevel", "String roster", "String description"));
        TeamDB.addTeam(new Team("teamName2", "String location", "String teamType", "String skillLevel", "String roster", "String description"));
      }
      
      
      String adminEmail = Play.application().configuration().getString("hihoops.admin.email");
      String adminPassword = Play.application().configuration().getString("hihoops.admin.password");
  
      User user = User.getUser(adminEmail);
  
      if (user == null) {
        User newUser = User.addUser("Alex G.", adminEmail, adminPassword);
        newUser.setActivation_key(null);
        newUser.update();
      
        if (User.getUser("Steve F.") == null) {
          User SteveF = User.addUser("Steve F.", "steve@gmail.com", "password");
          SteveF.setActivation_key(null);
          SteveF.update();
        }
        
        if (User.getUser("Loa P.") == null) {
          User Loa = User.addUser("Loa P.", "loa@gmail.com", "password");
          Loa.setActivation_key(null);
          Loa.update();
        }
        
        if (User.getUser("Keith A.") == null) {
          User Keith = User.addUser("Keith A.", "keith@gmail.com", "password");
          Keith.setActivation_key(null);
          Keith.update();
        }
        
        if (User.getUser("Hector M.") == null) {
          User Hector = User.addUser("Hector M.", "hector@gmail.com", "password");
          Hector.setActivation_key(null);
          Hector.update();
        }
      
      }
  
      if (Court.getCourts().isEmpty()) {
        Court.addCourt("Aina Haina", "Private", "123 Somewhere", (float) 21.2970, (float) -157.8170, "It's Awsome");
        Court.addCourt("B", "Private", "123 Somewhere", (float) 23.2970, (float) -157.8170, "It's Awsome");
        /*
         * Court.addCourt("B", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
         * Court.addCourt("C", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
         * Court.addCourt("D", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
         * Court.addCourt("E", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
         * Court.addCourt("F", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
         * Court.addCourt("G", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
         * Court.addCourt("H", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
         * Court.addCourt("I", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
         */
      }
  
      if (Team.getTeams().size() == 0) {
        Team.addTeam(new Team("Kaimuki Ballas", "Kaimuki Community Park", "Recreational", "Male", "Alex G., Loa P.", ""));
        Team.addTeam(new Team("Kapolei Boys", "Kapolei Community Park", "Recreational", "Male", "Alex G., Loa P.", ""));
        Team.addTeam(new Team("Aina-t Pros", "Aina Haina Community Park", "Recreational", "Female", "Alex G., Loa P.", ""));
        Team.addTeam(new Team("Manoa B-Ballas", "Manoa Community Park", "Recreational", "Co-ed", "Alex G., Loa P.", ""));
        Team.addTeam(new Team("Makiki Kings", "Makiki Community Park", "Recreational", "Male", "Alex G., Loa P.", ""));
      }
  
      if (Game.getGames().size() == 0) {
        Game.addGame(new Game("Kapolei PUG", "3:00PM", "cMarch 12", "Kapolei park", "Public", "Nightly", "Beginner",
            "Loa P., Alex G.", "admin"));
        Game.addGame(new Game("Aina Hina PUG", "3:00PM", "cMarch 10", "Aina Hina park", "Private", "Nightly", "Beginner",
            "Loa P., Alex G.", "admin"));
        Game.addGame(new Game("Waianae Nightly", "6:00PM", "cMarch 11", "Waianae park", "Public", "Nightly", "Beginner",
            "Loa P., Alex G.", "admin"));
        Game.addGame(new Game("Makakilo Lunchtime", "12:00PM", "cMarch 25", "Makakilo Rec", "Public", "Nightly",
            "Beginner", "Loa P., Alex G.", "admin"));
        Game.addGame(new Game("Ewa Beach Practice", "5:00PM", "dApril 2", "Kapolei park", "Private", "Nightly",
            "Beginner", "Loa P., Alex G.", "admin"));
      }
  
      if (Player.getPlayers().size() == 0) {
        Player.addPlayer(new PlayerFormData("Alex G.", "Ali G", "Paki Park", "Intermediate", "Center", 10, 5, "5'5\"",
            "200", "Basketball, I love Basketball.", "Pickup Games", ""));
        Player.addPlayer(new PlayerFormData("Steve F.", "SF", "Kapolei Park", "Beginner", "Power Forward", 15, 5,
            "5'5\"", "200", "Basketball, I love Basketball.", "Pickup Games", ""));
        Player.addPlayer(new PlayerFormData("Loa P.", "LP", "Aulani Park", "Competitive", "Point Gaurd", 5, 5, "5'5\"",
            "200", "Basketball, I love Basketball.", "Pickup Games", ""));
        Player.addPlayer(new PlayerFormData("Keith A.", "Albino", "Kapiolani Park", "College", "Small Forward", 20, 5,
            "5'5\"", "200", "Basketball, I love Basketball.", "Pickup Games", ""));
        Player.addPlayer(new PlayerFormData("Hector M.", "Malaz", "Hawaii Kai Park", "Intermediate", "Shooting Gaurd",
            25, 5, "5'5\"", "200", "Basketball, I love Basketball.", "Pickup Games", ""));
      }
      
  }
}
