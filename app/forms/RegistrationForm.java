package forms;

import models.User;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import java.util.ArrayList;
import java.util.List;

public class RegistrationForm
{

    @Email(message = "Enter a valid email.")
    @Required(message = "Email is required.")
    public String email;

    @Required(message = "Enter first name.")
    @MaxLength(value = 20, message = "First name is too long.")
    public String first;

    @Required(message = "Enter last name.")
    public String last;

    @Required(message = "Password is required.")
    @MinLength(value = 6, message = "Password must be at least 6 characters long.")
    public String password;

    @Required(message = "Confirmation password is required.")
//  @MinLength(value=6, message="Password must be at least 6 characters long.")
    public String confirmPassword;

    /**
     * The default constructor.
     */
    public RegistrationForm()
    {
        // default no arg constructor
    }

    /**
     * Initializes registration form.
     *
     * @param first    first name
     * @param last     last name
     * @param email    email address
     * @param password password
     * @param confirm  confirmation password
     */
    public RegistrationForm(String first, String last, String email, String password, String confirm)
    {
        this.first = first;
        this.last = last;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirm;
    }

    /**
     * Checks if form is valid.
     *
     * @return null if no errors, List of errors if there are.
     */
    public List<ValidationError> validate()
    {
        List<ValidationError> errors = new ArrayList<>();

        if (User.contains(email))
        {
            errors.add(new ValidationError("email", "Email already exists."));
        }
        if (!password.equals(confirmPassword))
        {
            errors.add(new ValidationError("confirmPassword", "Passwords did not match."));
        }
        return errors.isEmpty() ? null : errors;
    }
}
