/**
 * Parent class for all the employees
 */
public class Employee
{
    /**
     * id
     */
    private int id;

    /**
     * user role
     */
    private String userRole; // ENUM in database, String in Java

    /**
     * username
     */
    private String username;

    /**
     * first name
     */
    private String firstname;

    /**
     * surname
     */
    private String lastname;

    /**
     * password
     */
    private String password;

    /**
     * Overlaod constructor
     * @param id id
     * @param userRole role
     * @param username username
     * @param firstname name
     * @param lastname surname
     * @param password password
     */
    public Employee(int id, String userRole, String username, String firstname, String lastname, String password) {
        this.id = id;
        this.userRole = userRole;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }

    // Getters and Setters
    /**
     * Id getter
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Id setter
     * @param id id value
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Role getter
     * @return role
     */
    public String getUserRole() {
        return userRole;
    }

    /**
     * Role setter
     * @param userRole role value
     */
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    /**
     * Username getter
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Username setter
     * @param username username value
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Name getter
     * @return name
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Name setter
     * @param firstname name value
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Surname getter
     * @return surname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Surname setter
     * @param lastname surname value
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Password getter
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Password setter
     * @param password password value
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * To string method for debug
     */
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", userRole='" + userRole + '\'' +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
