package models;

import org.joda.time.DateTime;
import org.mindrot.jbcrypt.BCrypt;
import play.db.ebean.Model;
import forms.RegistrationForm;
import javax.persistence.*;
import java.util.Date;

/**
 * A simple representation of a user.
 * 
 * @author taylorak
 */
@Entity
@Table(name = "users")
public class User extends Model {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
        private String email;

    private String user_name;

    private String name;

    private String password;

    private boolean admin;

    /** Token used for email registration */
    private String activation_key;

    /** Timestamp of when the user registered */
    private DateTime timestamp;

    @OneToMany(mappedBy="author")
    private Review review;

    // timestamp when the users password needs to be changed
    // private Date pass_expiration;

    // Timestamp when the user email was authenticated default null
    // private Date email_auth;

    @OneToOne
    private Player player;


    /**
     * Initializes new User.
     * 
     * @param name The name.
     * @param email The email.
     * @param password The password.
     */
    public User(String name, String email, String password) {
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        this.name = name;
        this.email = email;
        this.password = passwordHash;
        this.timestamp = new DateTime();
    }

    /**
     * The EBean ORM finder method for database queries on User.
     */
    public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);

    /**
     * Returns true if there is an admin.
     * @return true if admin
     */
    public static boolean adminDefined() {
        User user = find.where().eq("admin", true).findUnique();
        return user != null;
    }

    /**
     * Adds the specified user to the database.
     * 
     * @param name Their name.
     * @param email Their email.
     * @param password Their password.
     */
    public static User addUser(String name, String email, String password) {
        User user = new User(name, email, password);
        user.save();
        return user;
    }

    /**
     * Adds the specified user to the database.
     * 
     * @param formData RegistrationForm info.
     */
    public static User addUser(RegistrationForm formData) {
        User user = new User(formData.first, formData.email, formData.password);
        user.save();
        return user;
    }

    /**
     * Returns the User associated with the email, or null if not found.
     * 
     * @param email The email.
     * @return The UserInfo.
     */
    public static User getUser(String email) {

        // return find.where().eq("email", email).findUnique();
        return find.where().ieq("email", email).findUnique();
    }

    public static User getValidUser(String validation_key) {
        return find.where().eq("activation_key", validation_key).findUnique();
    }

    /**
     * Check if email exists.
     * 
     * @param email email of user
     * @return true if contains key false if not
     * */
    public static boolean contains(String email) {
        return (getUser(email) != null);
    }

    /**
     * Returns true if email and password are valid credentials.
     * 
     * @param email The email.
     * @param password The password.
     * @return True if email is a valid user email and password is valid for that email.
     */
    public static boolean authenticate(String email, String password) {

        // User user = find.where().eq("email", email).findUnique();
        User user = find.where().ieq("email", email).findUnique();
        return user != null && BCrypt.checkpw(password, user.password);
        /*
         * if (user != null && BCrypt.checkpw(password, user.password)) { return true; } else { return false; }
         */

        /*
         * return find.where() .eq("email", email) .eq("password", password) .findUnique() != null;
         */

    }

    public Long getId() {
        return id;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * @param admin the admin to set
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return activation key
     */
    public String getActivation_key() {
        return activation_key;
    }

    /**
     * @param activation_key the email activation key
     */
    public void setActivation_key(String activation_key) {
        this.activation_key = activation_key;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


}