import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parent class for all cashier controllers
 * Holds the informations of each scene from the cashier to be able to use them if the user presses back
 */
public class CashierProperties
{
    /**
     * selected movie in the first scene
     */
    public static Movie selectedMovie;

    /**
     * product prices
     */
    public static List<Product> currentProductPrices;


    // arrenged at scene 2 and scene 3
    /**
     * selected seats at the second scene
     */
    protected static ArrayList<Integer> selectedSeatsAtScene2;

    /**
     * listed sessions at the second scene
     */
    protected static List<Sessions> listedSessionsAtScene2;

    /**
     * total ticket prices
     */
    protected static double totalTicketPrice;

    /**
     * total ticket taxes
     */
    protected static double totalTicketTax;

    /**
     * seat id, customer name map for normal tickets
     */
    protected static Map<Integer, String> normalTicketMap;

    /**
     * seat id, customer name map for discounted tickets
     */
    protected static Map<Integer, String> discountedTicketMap;

    /**
     * selected session at the second scene
     */
    protected static Sessions selectedSession;

    // arranged at scene 4
    /**
     * sold beverage number
     */
    protected static int soldBeverageNum;

    /**
     * sold food number
     */
    protected static int soldFoodNum;

    /**
     * sold toy number
     */
    protected static int soldToyNum;

    /**
     * total product prices
     */
    protected static double totalProductPrice;

    /**
     * calculated total product tax
     */
    protected static double totalProductTax;

    // calculated while creating the Bill
    /**
     * total bill price
     */
    protected static double totalBillPrice;

    /**
     * total bill tax
     */
    protected static double totalBillTax;

    /**
     * Bill URI
     */
    protected static String billURL;

    /**
     * To reset or create the static variables
     */
    protected static void prepareStaticVariables()
    {
        selectedMovie = null;
        currentProductPrices = DatabaseConnection.getProducts(null);

        selectedSeatsAtScene2 = new ArrayList<>();
        listedSessionsAtScene2 = new ArrayList<>();

        totalTicketPrice = 0;
        totalTicketTax = 0;

        normalTicketMap = new HashMap<>();
        discountedTicketMap = new HashMap<>();

        selectedSession = null;

        soldBeverageNum = 0;
        soldFoodNum = 0;
        soldToyNum = 0;

        totalProductPrice = 0;
        totalProductTax = 0;

        totalBillPrice = 0;
        totalProductPrice = 0;
    }

    /**
     * get all the product prices and related infos
     */
    protected static void fetchCurrentProductPrices()
    {
        currentProductPrices = DatabaseConnection.getProducts(null);
    }

    /**
     * creates the bill with the bought products
     * @throws Exception load scene exception
     */
    protected static void createBillForScene5() throws Exception
    {
        Bill newBill = new Bill();

        if (normalTicketMap.size() > 0)
            newBill.addTickets(normalTicketMap, selectedSession.getHallId(), selectedSession.getId(), false);
        if (discountedTicketMap.size() > 0)
            newBill.addTickets(discountedTicketMap, selectedSession.getHallId(), selectedSession.getId(), true);
        if (soldFoodNum > 0)
            newBill.addProduct(2, soldFoodNum);
        if (soldBeverageNum > 0)
            newBill.addProduct(3, soldBeverageNum);
        if (soldToyNum > 0)
            newBill.addProduct(4, soldToyNum);

        billURL = newBill.finalizeBill();

        App.loadScene("cashierScene5.fxml");
    }
}
