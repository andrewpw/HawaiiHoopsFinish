package forms;

import models.Court;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import java.util.ArrayList;
import java.util.List;

/**
 * Backing class for the court form.
 */
public class CourtForm
{

    @Constraints.Required(message = "Name is required")
    public String name;

    @Constraints.Required(message = "Type is required")
    public String type;

    @Constraints.Required(message = "Description is required")
    public String description;

    /**
     * Default constructor
     */
    public CourtForm()
    {
       // default no arg constructor
    }

    /**
     * Initialize court form.
     * @param name court name.
     * @param description description of the court.
     */
    public CourtForm(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    /**
     * Checks if form is valid.
     * @return null if no errors, List of errors if there are.
     */
    public List<ValidationError> validate()
    {
        List<ValidationError> errors = new ArrayList<>();

        if (Court.contains(name))
        {
            errors.add(new ValidationError("name", "Name already exists."));
        }
        return errors.isEmpty() ? null : errors;
    }
}
