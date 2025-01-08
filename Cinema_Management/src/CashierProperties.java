import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashierProperties
{
    public static Movie selectedMovie;

    public static List<Product> currentProductPrices;


    // arrenged at scene 2 and scene 3
    protected static ArrayList<Integer> selectedSeatsAtScene2;
    protected static List<Sessions> listedSessionsAtScene2;
    protected static double totalTicketPrice;
    protected static double totalTicketTax;

    protected static Map<Integer, String> normalTicketMap;
    protected static Map<Integer, String> discountedTicketMap;

    protected static Sessions selectedSession;

    // arranged at scene 4
    protected static int soldBeverageNum;
    protected static int soldFoodNum;
    protected static int soldToyNum;

    protected static double totalProductPrice;
    protected static double totalProductTax;

    // calculated while creating the Bill
    protected static double totalBillPrice;
    protected static double totalBillTax;


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

    protected static void fetchCurrentProductPrices()
    {
        currentProductPrices = DatabaseConnection.getProducts(null);
    }

    protected static void createBillForScene5()
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

        newBill.finalizeBill();
    }
}
