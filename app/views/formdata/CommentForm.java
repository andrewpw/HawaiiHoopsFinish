package views.formdata;

import javax.persistence.Id;
import play.data.validation.Constraints;

/**
 * Comment form backing.
 * 
 * @author AJ
 * 
 */
public class CommentForm {

  /** Id. **/
  @Id
  public long id = -1;

  /** Comment. **/
  @Constraints.Required(message = "A comment cannot be empty.")
  public String comment;

  /**
   * Empty Constructor.
   */
  public CommentForm() {
  }

  /**
   * Comment Form.
   * 
   * @param comment the content of comment
   */
  public CommentForm(String comment) {
    this.comment = comment;
  }

}
