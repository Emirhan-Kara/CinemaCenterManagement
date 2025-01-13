public class Admin extends Employee {
    /**
     * Constructor for admin authentication
     * @param id
     * @param username
     * @param firstname
     * @param lastname
     * @param password
     * @return
     */
    public Admin(int id, String username, String firstname, String lastname, String password) {
        super(id, "admin", username, firstname, lastname, password);
    }
}

