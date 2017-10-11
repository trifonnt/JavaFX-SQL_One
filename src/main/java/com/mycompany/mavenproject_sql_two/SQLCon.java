package com.mycompany.mavenproject_sql_two;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author jaroslaw.ziolkowski
 */
public class SQLCon {

	private static final String JDBC_URL = "jdbc:sqlite:DatabaseJarek.db";
	private static final String JDBC_DRIVER_CLASS_NAME = "org.sqlite.JDBC";

	// === Method for table creation
	public String conection(String name) {
		String response = "";
		try {
			Class.forName(JDBC_DRIVER_CLASS_NAME);
			Connection c = DriverManager.getConnection(JDBC_URL);
			Statement stm = c.createStatement();
			String sqlCreate = "CREATE TABLE " + name + " (Name text," + "Surname text," + "Age integer);";
			stm.execute(sqlCreate);
			stm.close();
			c.close();
			response = "Table created";
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			response = "Table already exists!!!";
		}
		return response;
	}

	// === Method for reviewing table names
	public ObservableList<String> conection1() {
		ObservableList<String> items = FXCollections.observableArrayList();
		Connection conn = null;
		try {
			Class.forName(JDBC_DRIVER_CLASS_NAME);
			conn = DriverManager.getConnection(JDBC_URL);
			DatabaseMetaData md = conn.getMetaData();
			String[] types = { "TABLE" };
			ResultSet rs = md.getTables(null, null, "%", types);
			while (rs.next()) {
				items.add(rs.getString(3));
			}
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		} finally {
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { /* ignored */ }
			}
		}
		return items;
	}

	// ===================== Method for deleting table
	public String conection3(String tableName) {
		String response = "";
		try {
			Class.forName(JDBC_DRIVER_CLASS_NAME);
			Connection c = DriverManager.getConnection(JDBC_URL);
			Statement stm = c.createStatement();
			String sqlCreate = "DROP TABLE " + tableName;
			stm.execute(sqlCreate);
			stm.close();
			c.close();
			response = "Table deleted";
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			response = "Select table to delete";
		}
		return response;
	}

	// ===================== Method for reviewing columns in chosen table. It gets
	// column name and its type ===========
	public ObservableList<String> conection4(String tableName) {
		ObservableList<String> items = FXCollections.observableArrayList();
		try {
			Class.forName(JDBC_DRIVER_CLASS_NAME);
			Connection conn = DriverManager.getConnection(JDBC_URL);
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rsColumn = md.getColumns(null, null, tableName, null);
			while (rsColumn.next()) {
				items.add(rsColumn.getString("COLUMN_NAME"));
				items.add(rsColumn.getString("TYPE_NAME"));
			}
			conn.close();
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
		return items;
	}
	// ===================== Method for adding column to table ===============

	public String conection5(String tableName, String columnName, String typeColumn) {
		String response = "";
		try {
			Class.forName(JDBC_DRIVER_CLASS_NAME);
			Connection c = DriverManager.getConnection(JDBC_URL);
			Statement stm = c.createStatement();
			String sqlCreate = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + typeColumn;
			stm.execute(sqlCreate);
			stm.close();
			c.close();
			response = "Column added";
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			response = "Column not added";
		}
		return response;
	}

	// ===================== Method for deleting column to table ===============
	public String conection6(String tableName, String columnName) {
		String response = "";
		try {
			String copyName = "ThisIsCopyName";

			Class.forName(JDBC_DRIVER_CLASS_NAME);
			Connection c = DriverManager.getConnection(JDBC_URL);
			Statement stm = c.createStatement();

			String sqlCreate = "ALTER TABLE " + tableName + " DROP COLUMN " + columnName;
			stm.execute(sqlCreate);
			stm.close();
			c.close();
			response = "Column deleted";
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			response = "Column not deleted";
		}
		return response;
	}

	// ===================== Method for copying table without deleted column
	// ===============
	public String conection7(String tableName, String columnName) {
		String response = "";
		ObservableList<String> items2 = conection4(tableName);
		ObservableList<String> items3 = FXCollections.observableArrayList();
		ObservableList<String> items4 = FXCollections.observableArrayList();
		for (int i = 0; i < items2.size(); i += 2) {
			items3.add(items2.get(i));
			items4.add(items2.get(i + 1));
		}
		// === Deleting column ========
		items3.remove(columnName);
		// =============== Creating temp table ======================
		try {
			Class.forName(JDBC_DRIVER_CLASS_NAME);
			Connection c = DriverManager.getConnection(JDBC_URL);
			Statement stm = c.createStatement();
			String sqlCreate = "CREATE TABLE FakeTable AS SELECT";

			// =========== Create columns in temp table ======================
			for (int i = 0; i < items3.size(); i++) {
				if (i == (items3.size() - 1)) {
					sqlCreate += " " + items3.get(i);
				} else {
					sqlCreate += " " + items3.get(i) + ", ";
				}
			}
			sqlCreate += " FROM " + tableName;
			stm.execute(sqlCreate);
			// ============= Removing old table =========================
			conection3(tableName);
			stm.close();
			c.close();
			// ========== Happens to fast that's why I close statement creation =====
			c = DriverManager.getConnection(JDBC_URL);
			stm = c.createStatement();
			sqlCreate = "ALTER TABLE FakeTable RENAME TO " + tableName;
			stm.execute(sqlCreate);
			// ==================================================
			stm.close();
			c.close();
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
		return "Column removed from table " + tableName;
	}

	// ========================= Method for getting values from column
	// =================
	public ObservableList<String> conection8(String tableName, String columnName) {
		// List<String> columnValues = new ArrayList<String>();
		ObservableList<String> columnValues = FXCollections.observableArrayList();
		try {
			Class.forName(JDBC_DRIVER_CLASS_NAME);
			Connection c = DriverManager.getConnection(JDBC_URL);
			Statement stm = c.createStatement();
			String sqlCreate = "SELECT " + columnName + " FROM " + tableName;
			stm.execute(sqlCreate);
			ResultSet rs = stm.getResultSet();
			// ==============
			while (rs.next()) {
				columnValues.add(rs.getString(columnName));
			}
			// ==============
			stm.close();
			c.close();
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
		return columnValues;
	}

	// ========================= Method for getting values from column
	// =================
	public ArrayList<String> conection8ArrayList(String tableName, String columnName) {
		// List<String> columnValues = new ArrayList<String>();
		ArrayList<String> columnValues = new ArrayList<String>();
		try {
			Class.forName(JDBC_DRIVER_CLASS_NAME);
			Connection c = DriverManager.getConnection(JDBC_URL);
			Statement stm = c.createStatement();
			String sqlCreate = "SELECT " + columnName + " FROM " + tableName;
			stm.execute(sqlCreate);
			ResultSet rs = stm.getResultSet();
			// ==============
			while (rs.next()) {
				columnValues.add(rs.getString(columnName));
			}
			// ==============
			stm.close();
			c.close();
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
		return columnValues;
	}

	// ========================= Method for implementing SQL statement
	public String conection9(String SQLcommand) {
		String response = "";
		try {
			Class.forName(JDBC_DRIVER_CLASS_NAME);
			Connection c = DriverManager.getConnection(JDBC_URL);
			Statement stm = c.createStatement();
			String sqlCreate = SQLcommand;

			stm.execute(sqlCreate);
			stm.close();
			c.close();
			response = "SQL command executed";
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			response = e.getMessage();
		}
		return response;
	}

}
