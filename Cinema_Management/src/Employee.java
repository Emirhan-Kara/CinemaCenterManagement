public class Employee {
    private int id;
    private String userRole; // ENUM in database, String in Java
    private String username;
    private String firstname;
    private String lastname;
    private String password;

    // Constructor
    public Employee(int id, String userRole, String username, String firstname, String lastname, String password) {
        this.id = id;
        this.userRole = userRole;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // toString method
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
