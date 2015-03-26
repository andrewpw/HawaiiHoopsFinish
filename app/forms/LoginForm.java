package forms;

import models.User;
import play.data.validation.Constraints.Required;

/**
 * Backing class for the login form.
 */
public class LoginForm
{

  @Required
  public String email;

  @Required
  public String password;

  /**
   * The default constructor.
   */
  public LoginForm()
  {
    // default no arg constructor
  }

  /**
   * Validates Form<LoginForm>.
   * Called automatically in the controller by bindFromRequest().
   * Checks to see that email and password are valid credentials.
   * @return Null if valid, or a List[ValidationError] if problems found.
   */
  public String validate()
  {
    if (!User.authenticate(email, password))
    {
        return "Invalid email or password";
    }
    if (User.getUser(email).getActivation_key() != null)
    {
        System.out.println(User.getUser(email).getActivation_key());
        return "Check your inbox for a validation email.";
    }
    return null;
  }

}
