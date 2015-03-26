package forms;

import play.data.validation.Constraints;

public class CourtReviewForm {


  public long rating = 0;

  @Constraints.Required(message = "Review is required")
  public String review = "";

  public CourtReviewForm() {
      // No args constructor
  }

}
