package de.fileinputstream.none.api.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
	
	 private String HOST;
	    private String DATABASE;
	    private String USER;
	    private String PASSWORD;
	    private Connection con;

	    public MySQL(final String host, final String database, final String user, final String password) {
	        this.HOST = "";
	        this.DATABASE = "";
	        this.USER = "";
	        this.PASSWORD = "";
	        this.HOST = host;
	        this.DATABASE = database;
	        this.USER = user;
	        this.PASSWORD = password;
	        this.connect();
	    }

	    public void connect() {
	        try {
	            this.con = DriverManager.getConnection("jdbc:mysql://" + this.HOST + ":3306/" + this.DATABASE + "?autoReconnect=true", this.USER, this.PASSWORD);
	            System.out.println("[MySQL] Die Verbindung zur MySQL wurde hergestellt!");
	        }
	        catch (SQLException e) {
	            System.out.println("[MySQL] Die Verbindung zur MySQL ist fehlgeschlagen! Fehler: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	    public void close() {
	        try {
	            if (this.con != null) {
	                this.con.close();
	                System.out.println("[MySQL] Die Verbindung zur MySQL wurde Erfolgreich beendet!");
	            }
	        }
	        catch (SQLException e) {
	            System.out.println("[MySQL] Fehler beim beenden der Verbindung zur MySQL! Fehler: " + e.getMessage());
	        }
	    }

	    public void update(final String qry) {
	        try {
	            final Statement st = this.con.createStatement();
	            st.executeUpdate(qry);
	            st.close();
	        }
	        catch (SQLException e) {
	            this.connect();
	            System.err.println(e);
	        }
	    }

	    public ResultSet query(final String qry) {
	        ResultSet rs = null;
	        try {
	            final Statement st = this.con.createStatement();
	            rs = st.executeQuery(qry);
	        }
	        catch (SQLException e) {
	            this.connect();
	            System.err.println(e);
	        }
	        return rs;
	    }

	    public void updateStatement(final PreparedStatement statement) {
	        try {
	            statement.executeUpdate();
	        }
	        catch (SQLException e) {
	            this.connect();
	            e.printStackTrace();
	            try {
	                statement.close();
	            }
	            catch (SQLException e2) {
	                e2.printStackTrace();
	            }
	            return;
	        }
	        finally {
	            try {
	                statement.close();
	            }
	            catch (SQLException e2) {
	                e2.printStackTrace();
	            }
	        }
	        try {
	            statement.close();
	        }
	        catch (SQLException e2) {
	            e2.printStackTrace();
	        }
	    }
	    public PreparedStatement prepareStatement(final String qry) {
	        PreparedStatement st = null;
	        try {
	            st = this.con.prepareStatement(qry);
	        }
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return st;
	    }


	    public ResultSet query(final PreparedStatement statement) {
	        try {
	            return statement.executeQuery();
	        }
	        catch (SQLException e) {
	            this.connect();
	            e.printStackTrace();
	            return null;
	        }
	    }

	    public boolean isConnected() {
	        return con !=null;
	    }
	    
	    public boolean chatlogIDExists(String id)
	    {
	      ResultSet rs = query("SELECT * FROM chatlogs WHERE id = '" + id + "';");
	      try
	      {
	        return rs.next();
	      }
	      catch (SQLException e)
	      {
	        e.printStackTrace();
	      }
	      return false;
	    }

}
