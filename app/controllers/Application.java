package controllers;

import models.Court;
import models.User;
import models.games.Game;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;
import com.typesafe.plugin.*;
import views.html.Index;
import views.html.footer.TermsOfUse;
import views.html.footer.AboutUs;
import views.html.footer.ContactUs;
import views.html.footer.MessageSent;
import forms.ContactUsForm;
import forms.RegistrationForm;

import java.util.List;


/**
 * Implements the controllers for this application.
 */
public class Application extends Controller {


    private static final Form<RegistrationForm> registrationForm = Form.form(RegistrationForm.class);

    /**
     * Returns the colorblock page.
     *
     * @return The resulting colorblock page.
     */
    public static Result index() {
        //Deletes games that passed a certain period.
        Game.deletePastGames();
        List<Court> courts;

        if(Secured.isLoggedIn(ctx())) {
            User user = Secured.getUserInfo(ctx());
            courts = user.getPlayer().getCourts();
        } else {
            courts = null;
        }
        return ok(Index.render("Hawaii Hoops Network", registrationForm, courts , Secured.isLoggedIn(ctx())));
    }
  
  /*public static Result home() {
    List<Game> games = Game.getGames();
    return ok(Home.render("Home", games, Secured.isLoggedIn(ctx())));
  }*/

    /**
     * Returns the terms of use page.
     *
     * @return the terms of use page
     */
    public static Result terms() {
        return ok(TermsOfUse.render("Terms Of Use", Secured.isLoggedIn(ctx())));
    }

    /**
     * Returns the contact us page.
     *
     * @return the contact page
     */
    public static Result contact() {

        ContactUsForm contact = new ContactUsForm();
        Form<ContactUsForm> formData = Form.form(ContactUsForm.class).fill(contact);

        return ok(ContactUs.render("Contact Us", formData, Secured.isLoggedIn(ctx())));
    }

    /**
     * Sends a message to admin.
     *
     * @return the page if any errors, index otherwise
     */
    public static Result sendMessage() {

        Form<ContactUsForm> formData = Form.form(ContactUsForm.class).bindFromRequest();

        if (formData.hasErrors()) {
            return badRequest(ContactUs.render("Contact Us", formData, Secured.isLoggedIn(ctx())));
        }
        else {
            ContactUsForm message = formData.get();

            MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
            mail.setSubject("New message from: " + message.name);
            mail.setRecipient("HiHoops <hawaiihoopsnetwork@gmail.com>", "hawaiihoopsnetwork@gmail.com");
            mail.setFrom(message.email);
            mail.send(message.message);

            return ok(MessageSent.render("Message Sent", Secured.isLoggedIn(ctx())));
        }
    }

    /**
     * Returns the about us page.
     *
     * @return the about us page
     */
    public static Result about() {
        return ok(AboutUs.render("About Us", Secured.isLoggedIn(ctx())));
    }

}
