import java.util.ArrayList;
import java.util.HashMap;
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
    private NumberAxis yAxisREvenue;

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
        Map<String, Object> hmap = DatabaseConnection.calculateRevenueAndTaxes();

        // product index -> ticket / beverage / Food / Toy
        ArrayList<Product> products = (ArrayList)hmap.get("products");

        double totalRevenue = (double)hmap.get("totalRevenue");
        double totalTax = (double)hmap.get("totalTax");
        double netRevenue = totalRevenue - totalTax;
        
        // number of discounted tickets that are sold
        int discounted_ticket_sale_num = products.get(0).get_agebased_discountedSell();

        // number of non-discounted tickets that are sold
        int nondiscounted_ticket_sale_num = products.get(0).getNormalsell();


        // number of sales
        int ticket_sale_num = discounted_ticket_sale_num + nondiscounted_ticket_sale_num;
        int beverage_sale_num = products.get(1).getNormalsell();
        int food_sale_num = products.get(2).getNormalsell();
        int toy_sale_num = products.get(3).getNormalsell();
        

        // revenues
        double ticket_sale_revenue = products.get(0).getRevenue();
        double beverage_revenue = products.get(1).getRevenue();
        double toy_revenue = products.get(2).getRevenue();
        double food_revenue = products.get(3).getRevenue();


        







        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Net Revenue", netRevenue),
            new PieChart.Data("Tax Revenue", totalTax)
        );

        this.pieChart.setData(pieChartData);

        // Set Revenue and Tax Labels
        this.netRevenueLabel.setText(String.format("%.2f ₺", netRevenue));
        this.taxLabel.setText(String.format("%.2f ₺", totalTax));
        this.totalLabel.setText(String.format("%.2f ₺", totalRevenue));



        
    }
}
