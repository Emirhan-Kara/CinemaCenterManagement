import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.chart.PieChart;

public class ManagerAccountingController {

    @FXML
    private Button backButton;

    @FXML
    private Button logout;

    @FXML
    private Label netRevenueLabel;

    @FXML
    private PieChart pieChart;

    @FXML
    private BarChart<String, Double> revenueChart;

    @FXML
    private BarChart<String, Double> salesChart;

    @FXML
    private Label taxLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private CategoryAxis xAxisRevenue;

    @FXML
    private CategoryAxis xAxisSales;

    @FXML
    private NumberAxis yAxisRevenue;

    @FXML
    private NumberAxis yAxisSales;

    @FXML
    void backClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "ManagerMainMenu.fxml");
    }

    @FXML
    void backPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "ManagerMainMenu.fxml");
    }

    @FXML
    void logoutClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "Login.fxml");
    }

    @FXML
    void logoutPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "Login.fxml");
    }


    @FXML
    private void initialize()
    {
        List<Product> productList = DatabaseConnection.getProducts(null);

        int sold_normalTicket = productList.get(0).getsold();
        int sold_food = productList.get(1).getsold();
        int sold_beverage = productList.get(2).getsold();
        int sold_toy = productList.get(3).getsold();
        int sold_discountedTicket = productList.get(4).getsold();

        double revenue_normalTicket = productList.get(0).gettotalrevenue();
        double revenue_food = productList.get(1).gettotalrevenue();
        double revenue_beverage = productList.get(2).gettotalrevenue();
        double revenue_toy = productList.get(3).gettotalrevenue();
        double revenue_discountedTicket = productList.get(4).gettotalrevenue();

        double net_revenue_products = revenue_food + revenue_beverage + revenue_toy;
        double net_revenue_tickets = revenue_discountedTicket + revenue_normalTicket;

        double tax_products = net_revenue_products * 0.1;
        double tax_tickets = net_revenue_tickets * 0.2;

        double total_net_revenue = net_revenue_products + net_revenue_tickets;
        double total_tax = tax_products + tax_tickets;

        double total_revenue = total_net_revenue + total_tax;





        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Net Revenue", total_net_revenue),
            new PieChart.Data("Tax Revenue", total_tax)
        );

        this.pieChart.setData(pieChartData);
        pieChartData.get(0).getNode().setStyle("-fx-pie-color: #005008;");   // Net Revenue
        pieChartData.get(1).getNode().setStyle("-fx-pie-color: #500000;");   // Tax Revenue


        // Set Revenue and Tax Labels
        this.netRevenueLabel.setText(String.format("%.2f ₺", total_net_revenue));
        this.taxLabel.setText(String.format("%.2f ₺", total_tax));
        this.totalLabel.setText(String.format("%.2f ₺", total_revenue));


        // Populate BarCharts
        populateBarChart(revenueChart, new double[]{revenue_normalTicket, revenue_food, revenue_beverage, revenue_toy, revenue_discountedTicket});

        populateBarChart(salesChart, new double[]{sold_normalTicket, sold_food, sold_beverage, sold_toy, sold_discountedTicket});        
    
        this.revenueChart.setLegendVisible(false);
        this.salesChart.setLegendVisible(false);

    }

    private String[] productNamesInOrder = {"Normal Ticket", "Food", "Beverage", "Toy", "Discounted Ticket"};
    /**
     * Populates a BarChart with data
     *
     * @param barChart barchart being worked on
     * @param values the values of each category.
     */
    private void populateBarChart(BarChart<String, Double> barChart, double[] values)
    {
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        for (int i = 0; i < productNamesInOrder.length; i++) {
            XYChart.Data<String, Double> data = new XYChart.Data<>(productNamesInOrder[i], values[i]);
            series.getData().add(data);

            // Add value label to the bar
            data.nodeProperty().addListener((_, _, newNode) -> {
                if (newNode != null)
                {
                    newNode.setStyle("-fx-bar-fill: #100050;");
                    StackPane stackPane = (StackPane) newNode;
                    Label valueLabel = new Label(String.format("%.2f", data.getYValue()));
                    valueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12;");
                    stackPane.getChildren().add(valueLabel);
                    StackPane.setAlignment(valueLabel, Pos.TOP_CENTER);
                }
            });
        }

        barChart.getData().add(series);
    }
}
