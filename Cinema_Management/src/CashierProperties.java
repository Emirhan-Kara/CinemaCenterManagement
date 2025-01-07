import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashierProperties
{
    public static Movie selectMovie;

    public static List<Product> currentProductPrices;

    protected static double totalTicketPrice;
    protected static double totalTicketTax;

    protected static double totalProductPrices;
    protected static double totalProductTax;

    protected static Map<Integer, String> normalTicketMap;
    protected static Map<Integer, String> discountedTicketMap;


    public static void prepareStaticVariables()
    {
        selectMovie = null;
        currentProductPrices = DatabaseConnection.getProducts(null);

        totalTicketPrice = 0;
        totalTicketTax = 0;

        totalProductPrices = 0;
        totalProductTax = 0;

        normalTicketMap = new HashMap<>();
        discountedTicketMap = new HashMap<>();
    }

    public static void fetchCurrentProductPrices()
    {
        currentProductPrices = DatabaseConnection.getProducts(null);
    }
}
