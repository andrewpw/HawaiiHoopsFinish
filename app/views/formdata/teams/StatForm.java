package views.formdata.teams;

import java.util.ArrayList;
import java.util.List;
import models.teams.Team;
import play.data.validation.ValidationError;

public class StatForm {

  public String record;
  public double threePt;
  public double twoPt;
  public double onePt;
  public int rebounds;
  public int steals;
  public int blocks;
  public String roster;

  public StatForm() {
  }
  
  public StatForm(Team team) {
    this.record = team.getRecord();
    this.threePt = team.getThreePt();
    this.twoPt = team.getTwoPt();
    this.onePt = team.getOnePt();
    this.rebounds = team.getRebounds();
    this.steals = team.getSteals();
    this.blocks = team.getBlocks();
    this.roster = team.getRoster();
  }

  public List<ValidationError> validate() {
    List<ValidationError> errors = new ArrayList<>();
    
    if (!record.matches("\\d-\\d")) {
      errors.add(new ValidationError("record", "Record is invalid.  Must be in the form of \"w-l\""));
    }
    return errors.isEmpty() ? null : errors;
  }
}
