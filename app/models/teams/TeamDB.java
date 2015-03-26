package models.teams;

import java.util.ArrayList;
import java.util.List;

public class TeamDB {

  private static List<Team> teamList = new ArrayList<Team>();
  
  public static void addTeam(Team newTeam){
    teamList.add(newTeam);
  }
  
  public static List<Team> getTeams(){
    return teamList;
  }
  
}
