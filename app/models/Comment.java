package models;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import models.teams.Team;
import play.db.ebean.Model;
import views.formdata.CommentForm;

/**
 * A model class for a Comment Object.
 * Does not allow for editing or deletion.  It may eventually...maybe
 * 
 * @author AJ
 * 
 */
@Entity
public class Comment extends Model {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Id
  private Long id;
  @ManyToOne
  private User author;
  private String comment;
  private String date;

  @ManyToOne
  private Team team;

  /**
   * Default constructor.
   */
  public Comment() {
  }

  /**
   * Constructs a comment object.
   * 
   * @param author the author of the comment
   * @param comment the content of the comment
   */
  public Comment(User author, String comment) {
    this.setAuthor(author);
    this.setComment(comment);
    this.setDate((new Date()).toString());
  }

  /**
   * Finder.
   * 
   * @return a finder
   */
  public static Finder<Long, Comment> find() {
    return new Finder<Long, Comment>(Long.class, Comment.class);
  }

  /**
   * @return the author
   */
  public User getAuthor() {
    return author;
  }

  /**
   * @param author the author to set
   */
  public void setAuthor(User author) {
    this.author = author;
  }

  /**
   * @return the comment
   */
  public String getComment() {
    return comment;
  }

  /**
   * @param comment the comment to set
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * @return the date
   */
  public String getDate() {
    return date;
  }

  /**
   * @param date the date to set
   */
  public void setDate(String date) {
    this.date = date;
  }

  /***********************************************************************
   * METHODS FOR ADDING COMMENTS RELATED TO A TEAM
   ***********************************************************************/

  /**
   * Adds a comment to the team's database. Does not allow for editing of comment.
   * 
   * @param team the team
   * @param author author of comment
   * @param commentForm comment form
   */
  public static void addComment(Team team, User author, CommentForm commentForm) {

    Comment comment = new Comment(author, commentForm.comment);
    comment.setTeam(team);
    team.addComment(comment);
    comment.save();
    team.save();
  }

  /**
   * Returns the list of comments associated with a team.
   * 
   * @param team the team
   * @return list of comments
   */
  public static List<Comment> getComments(Team team) {
    return team.getComments();
  }

  /**
   * @return the team
   */
  public Team getTeam() {
    return team;
  }

  /**
   * @param team the team to set
   */
  public void setTeam(Team team) {
    this.team = team;
  }

  /*************************************************************
   * END TEAM RELATED METHODS
   *************************************************************/
}
