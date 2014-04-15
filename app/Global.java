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

    
      String adminEmail = Play.application().configuration().getString("hihoops.admin.email");
      String adminPassword = Play.application().configuration().getString("hihoops.admin.password");

      User user = User.getUser(adminEmail);

      if (user == null) {
          User newUser = User.addUser("user", adminEmail, adminPassword);
          newUser.setActivation_key(null);
          newUser.update();
          
          User SteveF = User.addUser("Steve", "Steve@gmail.com", "password");
          SteveF.setActivation_key(null);
          SteveF.update();
          
          User Loa = User.addUser("Loa", "Loa@gmail.com", "password");
          Loa.setActivation_key(null);
          Loa.update();
          
          User Keith = User.addUser("Keith", "Keith@gmail.com", "password");
          Keith.setActivation_key(null);
          Keith.update();
          
          User Hector = User.addUser("Hector", "Hector@gmail.com", "password");
          Hector.setActivation_key(null);
          Hector.update();
      }

      if (Court.getCourts().isEmpty()) {
          Court.addCourt("Aina Haina", "Private", "123 Somewhere", (float) 21.2970, (float) -157.8170, "It's Awsome");
          Court.addCourt("B", "Private", "123 Somewhere", (float) 23.2970, (float) -157.8170, "It's Awsome");
          /*Court.addCourt("B", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
          Court.addCourt("C", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
          Court.addCourt("D", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
          Court.addCourt("E", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
          Court.addCourt("F", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
          Court.addCourt("G", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
          Court.addCourt("H", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");
          Court.addCourt("I", "Private", "123 Somewhere", (float)1234, (float)12345.0, "It's Awsome");*/
      }
      
      Team team = new Team("Trailblazers", "http://f00.inventorspot.com/images/July%208th%20&%2010th%20-%20Sports%20Logos2.JPG", 0);
      TeamDB.addTeam(team);
      Team team2 = new Team("Rockets", "http://f00.inventorspot.com/images/July%208th%20&%2010th%20-%20Sports%20Logos2.JPG", 0);
      TeamDB.addTeam(team2);
      Team team3 = new Team("Warriors", "http://f00.inventorspot.com/images/July%208th%20&%2010th%20-%20Sports%20Logos2.JPG", 0);
      TeamDB.addTeam(team3);
      Team team4 = new Team("Clippers", "http://f00.inventorspot.com/images/July%208th%20&%2010th%20-%20Sports%20Logos2.JPG", 0);
      TeamDB.addTeam(team4);
      Team team5 = new Team("Suns", "http://f00.inventorspot.com/images/July%208th%20&%2010th%20-%20Sports%20Logos2.JPG", 0);
      TeamDB.addTeam(team5);
      Team team6 = new Team("Thunder", "http://f00.inventorspot.com/images/July%208th%20&%2010th%20-%20Sports%20Logos2.JPG", 0);
      TeamDB.addTeam(team6);
      
    if (Game.getGames().size() == 0) {
      Game.addGame(new Game("Kapolei PUG", "3:00PM", "cMarch 12", "Kapolei park", "Public", "Nightly",
          "Beginner", "Loa P., Alex G.", "admin"));
      Game.addGame(new Game("Aina Hina PUG", "3:00PM", "cMarch 10", "Aina Hina park", "Private", "Nightly",
          "Beginner", "Loa P., Alex G.", "admin"));
      Game.addGame(new Game("Waianae Nightly", "6:00PM", "cMarch 11", "Waianae park", "Public", "Nightly",
          "Beginner", "Loa P., Alex G.", "admin"));
      Game.addGame(new Game("Makakilo Lunchtime", "12:00PM", "cMarch 25", "Makakilo Rec", "Public",
          "Nightly", "Beginner", "Loa P., Alex G.", "admin"));
      Game.addGame(new Game("Ewa Beach Practice", "5:00PM", "dApril 2", "Kapolei park", "Private",
          "Nightly", "Beginner", "Loa P., Alex G.", "admin"));
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
