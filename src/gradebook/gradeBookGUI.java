package gradebook;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class gradeBookGUI {

    private JFrame frame;
    private JTextArea infoTextArea;
    private JButton addButton, removeButton, updateButton, printButton;
    private JTextField studentIDField, studentFnameField, studentLnameField, studentFinalGradeField;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    gradeBookGUI window = new gradeBookGUI();
                    window.frame.setVisible(true);
                    setup(window);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Helper function to connect to
     * @param window a gradeBookGUI object serves as your application to connect to SQL database.
     */
    protected static void setup(gradeBookGUI window) {
        Database.connection();
    }

    /**
     * Helper function to update the display area JTextArea.
     */
    public void updateDisplay(){
        getInfoTextArea().setText(Database.printPanel());
    }

    /**
     * A simple return statement to get text from the information box.
     * @return JTextArea information to put in textbox located at the top of the application.
     */
    public JTextArea getInfoTextArea() {
        return infoTextArea;
    }

    /**
     * Create Application
     */
    public gradeBookGUI() {
        initialize();
    }

    /**
     * Initializing Application
     */
    private void initialize() {

        // Frame Initialization.
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Grade Book");

        // Buttons: Initialization.
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        updateButton = new JButton("Update");
        printButton = new JButton("Print Table");
        // Buttons: Setting Bounds.
        addButton.setBounds(40, 440, 75, 25);
        removeButton.setBounds(135, 440, 75, 25);
        updateButton.setBounds(240, 440, 75, 25);
        printButton.setBounds(335, 440, 75, 25);

        // Text Fields: Initialization
        studentIDField = new JTextField(10);
        studentFnameField = new JTextField(50);
        studentLnameField = new JTextField(50);
        studentFinalGradeField = new JTextField(3);
        // Text Fields: Setting Bounds
        studentIDField.setBounds(170, 210, 100, 30);
        studentFnameField.setBounds(170, 250, 100, 30);
        studentLnameField.setBounds(170, 290, 100, 30);
        studentFinalGradeField.setBounds(170, 330, 100, 30);

        // Text Area Bundle
        infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setHighlighter(null);
        infoTextArea.setEnabled(false);
        infoTextArea.setBounds(10,10, 430, 200);

        // Labels: Initialization.
        JLabel studentIDLabel = new JLabel("Student ID");
        JLabel studentFnameLabel = new JLabel("First Name");
        JLabel studentLnameLabel = new JLabel("Last Name");
        JLabel studentFinalGradeLabel = new JLabel("Final Grade");
        // Labels: Setting Bounds
        studentIDLabel.setBounds(70, 210, 70, 30);
        studentFnameLabel.setBounds(70, 250, 70, 30);
        studentLnameLabel.setBounds(70, 290, 70, 30);
        studentFinalGradeLabel.setBounds(70, 330, 80, 30);

        frame.add(infoTextArea);
        frame.add(addButton);
        frame.add(removeButton);
        frame.add(updateButton);
        frame.add(printButton);
        frame.add(studentIDField);
        frame.add(studentFnameField);
        frame.add(studentLnameField);
        frame.add(studentFinalGradeField);
        frame.add(studentIDLabel);
        frame.add(studentFnameLabel);
        frame.add(studentLnameLabel);
        frame.add(studentFinalGradeLabel);
        frame.setLayout(null);

        // ButtonListeners:
        // Print Button:
        printButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("Checking Data");
                if(Database.printPanel().isEmpty()){
                    System.out.println("No Data...");
                }else{
                    Database.printPanel();
                    updateDisplay();
                }
            }
        });
        // Add Button:
        addButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("Adding Data...");
                int studentIDTemp, finalGradeTemp;
                if(studentIDField.getText().isEmpty()){
                    studentIDTemp = -1;
                }else{
                    studentIDTemp = Integer.parseInt(studentIDField.getText());
                }

                String fNameTemp = studentFnameField.getText();
                String lNameTemp = studentLnameField.getText();

                if(studentFinalGradeField.getText().isEmpty()){
                    finalGradeTemp = 0;
                }else {
                    finalGradeTemp = Integer.parseInt(studentFinalGradeField.getText());
                }
                System.out.println("Student ID: " + studentIDTemp);
                Database.add(studentIDTemp, fNameTemp, lNameTemp, finalGradeTemp);
                updateDisplay();
            }
        });

        // Remove Button:
        removeButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("Removing Data...");
                int studentIDTemp, finalGradeTemp;
                if(studentIDField.getText().isEmpty()){
                    studentIDTemp = -1;
                }else{
                    studentIDTemp = Integer.parseInt(studentIDField.getText());
                }
                String fNameTemp = studentFnameField.getText();
                String lNameTemp = studentLnameField.getText();
                if(studentFinalGradeField.getText().isEmpty()){
                    finalGradeTemp = -1;
                }else {
                    finalGradeTemp = Integer.parseInt(studentFinalGradeField.getText());
                }
                Database.remove(studentIDTemp, fNameTemp, lNameTemp, finalGradeTemp);
                updateDisplay();
            }
        });

        // Update Button:
        updateButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("Updating Data...");
                int studentIDTemp, finalGradeTemp;

                if(studentIDField.getText().isEmpty()){
                    studentIDTemp = -1;
                }else{
                    studentIDTemp = Integer.parseInt(studentIDField.getText());
                }
                String fNameTemp = studentFnameField.getText();
                String lNameTemp = studentLnameField.getText();
                if(studentFinalGradeField.getText().isEmpty()){
                    finalGradeTemp = 0;
                }else {
                    finalGradeTemp = Integer.parseInt(studentFinalGradeField.getText());
                }

                Database.update(studentIDTemp, fNameTemp, lNameTemp, finalGradeTemp);
                updateDisplay();
            }
        });

    }


}
