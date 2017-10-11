package com.mycompany.mavenproject_sql_two;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class FXMLController implements Initializable {

    //=== Labels definition ====== 
    @FXML
    private Label label1TableStrucuture;
    @FXML
    private Label label2ColumnType;
    @FXML
    private Label label3TableName;
    @FXML
    private Label label4ColumnName;
    //======= Combo box declaration ===========================       
    @FXML
    private ComboBox<String> ComboBox3TableCreation;
    ObservableList<String> list1 = FXCollections.observableArrayList("Add", "Delete");
    ObservableList<String> list2 = FXCollections.observableArrayList("INTEGER", "TEXT", "NUMERIC");
    @FXML
    private TextField textField1TableStructure;
    @FXML
    private TextField textField2TableStructure;
    @FXML
    private TextField textField3TableStructure;
    @FXML
    private TextField textField4TableStructure;
    @FXML
    private TextField textFieldSQLexecute;

    @FXML
    private TextArea textAreaOne;
    //=============== List view ===============================
    @FXML
    private ListView<String> listViewOne;
    @FXML
    private ListView<String> listViewTwo;
    @FXML
    private ListView<String> listViewThree;
    @FXML
    private ListView<String> listViewFour;
    @FXML
    private ListView<String> listViewFive;
    @FXML
    private ListView<String> listViewSix;
    //=============== Table view ===============================
    @FXML
    private TableView<TableFillerOne> tableViewOne;
    //=============== Table column =============================
    @FXML
    private TableColumn<TableFillerOne, String> name;

    //=============== Checks present tables in database and displays =======
    SQLCon myCon2 = new SQLCon();
    String selectedItem; // Slected table name in Tables listView
    String selectedColumn; // Slected column name in Column listView   

    //============== Scatteer chart =====================================
    @FXML
    private CategoryAxis myXaxis;
    @FXML
    private NumberAxis myYaxis;
    @FXML
    BarChart BarChartOne;

    //=============== Initialize method =======================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ComboBox3TableCreation.setItems(list2);
        ObservableList<String> items = myCon2.conection1();
        listViewOne.setItems(items);
        listViewFour.setItems(items);
    }

    @FXML
    public void ButtonAction1() { // Creates table        
        if (textField1TableStructure.getText().trim().equals("")) {
            textField2TableStructure.setText("Enter table name. No table created.");
        } else {
            SQLCon myCon1 = new SQLCon();
            String response = myCon1.conection(textField1TableStructure.getText());
            textField2TableStructure.setText(response);
        }
        ObservableList<String> items = myCon2.conection1();
        listViewOne.setItems(items); // Refreshing listView
        listViewFour.setItems(items);
    }

    @FXML
    public void ButtonAction2(MouseEvent e) { // Pick-up selected table        
        try {
            SettingColumns();
            label2ColumnType.setText("");
        } catch (Exception et) {
            System.err.println("Error in ButtonAction2: " + et.getMessage());
        }
        TableColumnFilling();
        label3TableName.setText(listViewOne.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void ButtonAction2B(MouseEvent e) { // Pick-up selected table in listViewFour, first pane        
        try {
            SettingColumns2();
        } catch (Exception et) {
            System.err.println("Error in ButtonAction2B: " + et.getMessage());
        }
    }

    @FXML
    public void ButtonAction3() { // Delete table
        SQLCon myCon1 = new SQLCon();
        String response = myCon1.conection3(selectedItem);
        textField2TableStructure.setText(response);
        ObservableList<String> items = myCon2.conection1();
        listViewOne.setItems(items);
        listViewFour.setItems(items);
        label1TableStrucuture.setText("");
        listViewTwo.setItems(null);
        listViewFive.setItems(null);
        listViewSix.setItems(null);
    }

    @FXML
    public void ButtonAction4() { // Adding column
        String response = "";
        try {
            SQLCon myCon3 = new SQLCon();
            String colName = textField3TableStructure.getText();
            String typeColumn = ComboBox3TableCreation.getSelectionModel().getSelectedItem().toString();
            if (!(colName.equals(""))) {
                response = myCon3.conection5(selectedItem, colName, typeColumn);
                // Refreshing list view
                SettingColumns();
                textField4TableStructure.setText(response);
            } else {
                textField4TableStructure.setText("Enter column name");
            }
        } catch (Exception e) {
            textField4TableStructure.setText("Select column type or change column name.");
        }
        TableColumnFilling();
    }

    @FXML
    public void ButtonAction6(MouseEvent e) { // Pick-up selected column        
        String categoryType = SettingColumns();
        label2ColumnType.setText(categoryType);
        ColumnDataDisplay();
        label4ColumnName.setText(listViewTwo.getSelectionModel().getSelectedItem());

    }

    @FXML
    public void KeyboardAction1(KeyEvent ke) { // Moving up and downe with keyboard in listViewTwo, with columns
        if (ke.getCode() == KeyCode.DOWN || ke.getCode() == KeyCode.UP) {
            String categoryType = SettingColumns();
            label2ColumnType.setText(categoryType);
            ColumnDataDisplay();
            label4ColumnName.setText(listViewTwo.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    public void KeyboardAction2(KeyEvent ke) { // Moving up and downe with keyboard in listViewOne, list with table names
        if (ke.getCode() == KeyCode.DOWN || ke.getCode() == KeyCode.UP) {
            try {
                SettingColumns();
                label2ColumnType.setText("");
            } catch (Exception et) {
                System.err.println("Error in KeboardAction2: " + et.getMessage());
            }
            TableColumnFilling();
            label3TableName.setText(listViewOne.getSelectionModel().getSelectedItem());
        }
    }

    public void KeyboardAction3(KeyEvent ke) { // Moving up and downe with keyboard in listViewFour, list with table names
        if (ke.getCode() == KeyCode.DOWN || ke.getCode() == KeyCode.UP) {
            SettingColumns2();
        }
    }

    @FXML
    public void ButtonAction7() { // Deleting column
        try {
            SQLCon myCon7 = new SQLCon();
            selectedItem = listViewOne.getSelectionModel().getSelectedItem();
            selectedColumn = listViewTwo.getSelectionModel().getSelectedItem();
            String response = myCon7.conection7(selectedItem, selectedColumn);
            textField4TableStructure.setText(response);
            label2ColumnType.setText("");
            System.out.println("jhjiuyouhoih");
            //====== Refereshing =============
            ObservableList<String> items = myCon2.conection1();
            listViewOne.setItems(items); // Refreshing listView    
            listViewFour.setItems(items);
            SettingColumns();
        } catch (Exception et) {
            System.err.println("Error in ButtonAction7: " + et.getMessage());
        }
        TableColumnFilling();
    }

    public void ButtonAction8() { // Execute SQL command

        String command = "";
        command = textFieldSQLexecute.getText();
        SQLCon myCon8 = new SQLCon();
        String response = myCon8.conection9(command);
        textAreaOne.setText("Command: " + command + "\n" + response);
        ObservableList<String> items = myCon2.conection1();
        listViewOne.setItems(items);
        listViewFour.setItems(items);
        TableColumnFilling();
        ColumnDataDisplay();
    }

    public void ButtonAction9() { // Create chart

        BarChartOne.getData().clear();
        XYChart.Series dataSeries1 = new XYChart.Series();
        ObservableList<String> xValues = BarChartDataXvalues();
        ObservableList<String> yValues = BarChartDataYvalues();
        selectedColumn = listViewFive.getSelectionModel().getSelectedItem();
        selectedItem = listViewSix.getSelectionModel().getSelectedItem();

        try {
            myYaxis.setLabel(selectedItem);
            myYaxis.tickLabelFontProperty().set(Font.font(15));
            myXaxis.tickLabelFontProperty().set(Font.font(15));

            dataSeries1.setName(selectedColumn);
            for (int i = 0; i < xValues.size(); i++) {
                if (!(yValues.get(i) == null || xValues.get(i) == null)) {
                    double value = Double.parseDouble(yValues.get(i));
                    dataSeries1.getData().add(new XYChart.Data(xValues.get(i), value));
                }
            }
            BarChartOne.getData().add(dataSeries1);
        } catch (Exception er) {
            System.err.println(er.getMessage());
        }
    }

    //====================== Methods =========================================
    //========================================================================
    public String SettingColumns() {  // This method fill table with column names, is used to copy columns
        selectedItem = listViewOne.getSelectionModel().getSelectedItem(); // Gets table name
        label1TableStrucuture.setText(selectedItem);
        ObservableList<String> items2 = myCon2.conection4(selectedItem);
        ObservableList<String> items3 = FXCollections.observableArrayList();
        ObservableList<String> items4 = FXCollections.observableArrayList();
        for (int i = 0; i < items2.size(); i += 2) {
            items3.add(items2.get(i));
            items4.add(items2.get(i + 1));
        }
        listViewTwo.setItems(items3);

        // Checks property of selected column
        String selectedCategory = listViewTwo.getSelectionModel().getSelectedItem();
        String categoryType = "";
        for (int i = 0; i < items3.size(); i++) {
            if (selectedCategory.equals(items3.get(i))) {
                categoryType = items4.get(i);
            }
        }
        return categoryType;
    }

    //=============== Setting columns in listViewFive and listViewSix
    public void SettingColumns2() {  // 
        selectedItem = listViewFour.getSelectionModel().getSelectedItem(); // Gets table name        
        ObservableList<String> items2 = myCon2.conection4(selectedItem);
        ObservableList<String> items3 = FXCollections.observableArrayList();
        ObservableList<String> items4 = FXCollections.observableArrayList();
        for (int i = 0; i < items2.size(); i += 2) {
            if (items2.get(i + 1).equals("TEXT")) {
                items3.add(items2.get(i));
            }
            if (items2.get(i + 1).equals("INT") || items2.get(i + 1).equals("INTEGER") || items2.get(i + 1).equals("NUMERIC")) {
                items4.add(items2.get(i));
            }
        }
        listViewFive.setItems(items3);
        listViewSix.setItems(items4);
    }

    //=================== Method for filling tableViewOne with columns using observableList ==================

    public void TableColumnFilling() {
        selectedItem = listViewOne.getSelectionModel().getSelectedItem(); // Gets table selected name
        label1TableStrucuture.setText(selectedItem);                      // Sets table name over Delete column
        ObservableList<String> items2 = myCon2.conection4(selectedItem);  // Gets names of columns in table and its type

        tableViewOne.getColumns().clear();
        ObservableList<String> columnValues;   // Values from column
        //ObservableList dataToTableList = FXCollections.observableArrayList();
        SQLCon myCon8 = new SQLCon();

        for (int i = 0; i < items2.size(); i += 2) {
            ObservableList dataToTableList = FXCollections.observableArrayList();
            name = new TableColumn(items2.get(i)); // Gets the name of column from database and set this name to column name in fxml
            name.setCellValueFactory(new PropertyValueFactory<TableFillerOne, String>("firstName")); // Sets the propery of every column
            tableViewOne.getColumns().add(name);
            
            //-------------- Gets value in columns --------------
            columnValues = myCon8.conection8(selectedItem, items2.get(i)); // Gets column values
            for (int j = 0; j < columnValues.size(); j++) {
                dataToTableList.add(new TableFillerOne(columnValues.get(j)));
            }
            tableViewOne.setItems(dataToTableList);
            
        }
    }

    //=================== Method for displaying data stored in columns. Displays in listview ==================
    public void ColumnDataDisplay() {
        ObservableList<String> columnValues;
        SQLCon myCon8 = new SQLCon();
        selectedItem = listViewOne.getSelectionModel().getSelectedItem();
        selectedColumn = listViewTwo.getSelectionModel().getSelectedItem();
        columnValues = myCon8.conection8(selectedItem, selectedColumn);
        listViewThree.setItems(columnValues);
    }

    //=================== Method for getting x values for bar chart ================
    public ObservableList<String> BarChartDataXvalues() {
        ObservableList<String> barChartXValues;
        SQLCon myCon8 = new SQLCon();
        selectedItem = listViewFour.getSelectionModel().getSelectedItem();
        selectedColumn = listViewFive.getSelectionModel().getSelectedItem();  // Geting String
        barChartXValues = myCon8.conection8(selectedItem, selectedColumn);
        return barChartXValues;
    }

    //=================== Method for getting y values for bar chart ================
    public ObservableList<String> BarChartDataYvalues() {
        ObservableList<String> barChartYValues;
        SQLCon myCon8 = new SQLCon();
        selectedItem = listViewFour.getSelectionModel().getSelectedItem();
        selectedColumn = listViewSix.getSelectionModel().getSelectedItem();  // Geting String
        barChartYValues = myCon8.conection8(selectedItem, selectedColumn);
        return barChartYValues;
    }

    //=============== Class for table filling ================
    public static class TableFiller {

        private final SimpleStringProperty firstName;

        private TableFiller(String fName) {
            this.firstName = new SimpleStringProperty(fName);
        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }

    }
}
