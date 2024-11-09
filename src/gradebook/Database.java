package gradebook;

import com.mysql.cj.protocol.Resultset;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static Connection con = null;

    /**
     * Connects to the Database using url (Your Database URL), username (usually root),
     * and password for your mySQL localHost.
     */
    public static void connection(){
        String url = "jdbc:mysql://localhost:3306/gradebookdb";
        String username = "root";
        String password = "password";

        try{
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Successful!");
        }catch(Exception e){
            System.out.println("Connection Failed!");
            System.out.println("Exception " + e.getMessage());
        }
    }//End Connection Method

    public static final String studentID = "studentID";
    public static final String fName = "firstName";
    public static final String lName = "LastName";
    public static final String midtermGrade = "midtermGrade";
    public static final String table = "380_grades"; //Insert your table you are using for SQL statements here.

    /**
     * This method prints the database table that is input into the executeQuery().
     * @return A string of the database table in a studentID, fName, lName, midtermGrade format.
     */
    public static String printPanel(){
        String output = "";
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM 380_grades");

            while(rs.next()){
                output += (rs.getString(studentID) + " ");
                output += (rs.getString(fName) + " ");
                output += (rs.getString(lName) + " ");
                output += (rs.getString(midtermGrade)+ " ");
                output += "\n";
            }
        }catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return output;
    }

    /**
     * This method adds the given input of studentID, fName, lName, midtermGrade to your table in INSERT INTO statement
     * @param studentIdNumTemp The studentID of the added student
     * @param fNameIn The first name of the input student
     * @param lNameIn the last name of the input student
     * @param midtermGradeIn The midterm grade of the input student
     */
    public static void add(int studentIdNumTemp, String fNameIn, String lNameIn, int midtermGradeIn) {
        String sql = "INSERT INTO " + table +  " (" + studentID + ", " + fName + ", " + lName + "," + midtermGrade + ")" + " VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentIdNumTemp);
            ps.setString(2, fNameIn);
            ps.setString(3, lNameIn);
            ps.setInt(4, midtermGradeIn);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Make sure studentID is not null.");
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Will remove rows based on what is added to the sql String with append(). If studentID exists, append
     *  " WHERE studentID = ?" Where the ? will be used for the PreparedStatement after all the append checks. The
     *  PreparedStatement will add the appropriate JTextField.getText() Input to the corresponding ? in the sql String
     * @param studentIDIn the ID number of student you want to delete.
     * @param fNameIn First name of all students with that name you want to delete.
     * @param lNameIn Last name of all students with tht last name you want to delete.
     * @param finalGradeIn Any row with this midterm grade will be deleted.
     */
    public static void remove(int studentIDIn, String fNameIn, String lNameIn, int finalGradeIn) {
        // Start building the SQL query
        StringBuilder sql = new StringBuilder("DELETE FROM " + table);

        // Track if there are any conditions to add
        boolean hasConditions = false;

        // WHERE statement based on the provided parameters
        if (studentIDIn != -1) { // -1 means no student ID provided
            sql.append(" WHERE ").append(Database.studentID).append(" = ?");
            hasConditions = true;
        }
        if (fNameIn != null && !fNameIn.isEmpty()) {
            sql.append(hasConditions ? " AND " : " WHERE ").append(Database.fName).append(" = ?");
            hasConditions = true;
        }
        if (lNameIn != null && !lNameIn.isEmpty()) {
            sql.append(hasConditions ? " AND " : " WHERE ").append(Database.lName).append(" = ?");
            hasConditions = true;
        }
        if (finalGradeIn != -1) { // -1 means no final grade provided
            sql.append(hasConditions ? " AND " : " WHERE ").append(Database.midtermGrade).append(" = ?");
            hasConditions = true;
        }

        // If no conditions were added, print an error message and exit
        if (!hasConditions) {
            System.out.println("No parameters provided for deletion. Operation cannot proceed.");
            return;
        }

        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int parameterIndex = 1;

            // Set parameters based on provided values
            if (studentIDIn != -1) {
                ps.setInt(parameterIndex++, studentIDIn);
            }
            if (fNameIn != null && !fNameIn.isEmpty()) {
                ps.setString(parameterIndex++, fNameIn);
            }
            if (lNameIn != null && !lNameIn.isEmpty()) {
                ps.setString(parameterIndex++, lNameIn);
            }
            if (finalGradeIn != -1) {
                ps.setInt(parameterIndex, finalGradeIn);
            }

            // Execute deletion
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     *
     * @param studentIdNum Student ID number of a student to update information about.
     * @param fNameIn Update first name.
     * @param lNameIn Update last name.
     * @param midtermGradeIn Update first name.
     */
    public static void update(int studentIdNum, String fNameIn, String lNameIn, int midtermGradeIn) {
        // Check if the student ID is valid, -1 indicates an invalid ID
        if (studentIdNum == -1) {
            System.out.println("Invalid student ID. Update cannot proceed.");
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");
        boolean isFirst = true;

        // Dynamically build the SQL query based on which parameters are not null / -1
        if (fNameIn != null) {
            sql.append(fName).append(" = ?");
            isFirst = false;
        }
        if (lNameIn != null) {
            if (!isFirst) sql.append(", ");
            sql.append(lName).append(" = ?");
            isFirst = false;
        }
        if (midtermGradeIn != -1) { // Assuming -1 indicates no grade update
            if (!isFirst) sql.append(", ");
            sql.append(midtermGrade).append(" = ?");
        }

        // Complete the SQL query with the WHERE clause
        sql.append(" WHERE ").append(studentID).append(" = ?");

        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Set parameters for fields that are included in the SQL statement
            if (fNameIn != null) {
                ps.setString(paramIndex++, fNameIn);
            }
            if (lNameIn != null) {
                ps.setString(paramIndex++, lNameIn);
            }
            if (midtermGradeIn != -1) {
                ps.setInt(paramIndex++, midtermGradeIn);
            }

            // Set the student ID in the WHERE clause
            ps.setInt(paramIndex, studentIdNum);

            // Execute update
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

}
