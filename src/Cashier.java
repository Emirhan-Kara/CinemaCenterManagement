/**
 * Cashier class for the user roles
 * Extends to Employee class
 */
public class Cashier extends Employee
{
    /**
     * Constructor
     * @param id id
     * @param username username
     * @param firstname name
     * @param lastname surname
     * @param password password
     */
    public Cashier(int id, String username, String firstname, String lastname, String password) {
        super(id, "cashier", username, firstname, lastname, password);
    }
}
