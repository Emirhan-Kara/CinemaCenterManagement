/**
 * Manager class, that extends Employee
 */
public class Manager extends Employee
{
    /**
     * Overload constructor
     * @param id id
     * @param username username
     * @param firstname name
     * @param lastname surname
     * @param password password
     */
    public Manager(int id, String username, String firstname, String lastname, String password) {
        super(id, "manager", username, firstname, lastname, password);
    }
}

