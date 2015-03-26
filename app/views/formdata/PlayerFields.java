/**
 * 
 */
package views.formdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Player;

/**
 * @author scotthonda
 *
 */
public class PlayerFields {
  
  private static String [] skills = {"Beginner", "Intermediate", "Competitive", "College"};
  private static String [] positions = {"Point Gaurd", "Shooting Gaurd", "Center", "Power Forward", "Small Forward"};
  
  /**********************
   * Player Skill Level *
   **********************/
  
  /**
   * returns a initialized playerSkill map with all values as false.
   * @return typeMap
   */
  public static Map<String, Boolean> getSkill() {
    Map<String, Boolean> typeMap = new HashMap<>();
    for (String type : skills) {
      typeMap.put(type, false);
    }
    return typeMap;
  }
  
  /**
   * when a getSkill is passed a string it will set the associated type with a true value.
   * @param playerSkill = skill level of player
   * @return typeMap
   */
  public static Map<String, Boolean> getSkill(String playerSkill) {
    Map<String, Boolean> typeMap = PlayerFields.getSkill();
    if (isSkill(playerSkill)) {
      typeMap.put(playerSkill, true);
    }
    return typeMap;
  }
  
  /**
   * Returns true if playerSkill is a valid skill.
   * @param playerSkill = skill level of player
   * @return true if a valid player skill type, false if invalid player skill type
   */
  public static boolean isSkill(String playerSkill) {
    return PlayerFields.getSkill().keySet().contains(playerSkill);
  }
  
  
  
  /********************
   * Player Positions *
   ********************/
  
  /**
   * returns a initialized playerSkill map with all values as false.
   * @return typeMap
   */
  public static Map<String, Boolean> getPosition() {
    Map<String, Boolean> typeMap = new HashMap<>();
    for (String type : positions) {
      typeMap.put(type, false);
    }
    return typeMap;
  }
  
  /**
   * when a getSkill is passed a string it will set the associated type with a true value.
   * @param playerSkill = skill level of player
   * @return typeMap
   */
  public static Map<String, Boolean> getPosition(String playerPosition) {
    Map<String, Boolean> typeMap = PlayerFields.getPosition();
    if (isPosition(playerPosition)) {
      typeMap.put(playerPosition, true);
    }
    return typeMap;
  }
  
  /**
   * Returns true if playerSkill is a valid skill.
   * @param playerSkill = skill level of player
   * @return true if a valid player skill type, false if invalid player skill type
   */
  public static boolean isPosition(String playerPosition) {
    return PlayerFields.getPosition().keySet().contains(playerPosition);
  }
  
}
