package viewer;

import org.sqlite.SQLiteDataSource;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class SQLiteViewer extends JFrame {

    public SQLiteViewer() {
        super("SQLite Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        x();
        setVisible(true);
    }

    private void x(){
        JTextField FileNameTextField = new JTextField();
        FileNameTextField.setName("FileNameTextField");
        FileNameTextField.setBounds(10,10,580,20);
        JButton OpenFileButton = new JButton("Open");
        OpenFileButton.setName("OpenFileButton");
        OpenFileButton.setBounds(600,10,70,20);
        OpenFileButton.setBackground(Color.RED);
        JComboBox<String> TablesComboBox = new JComboBox<>();
        TablesComboBox.setName("TablesComboBox");
        TablesComboBox.setBounds(10,50,650,20);
        JTextArea QueryTextArea = new JTextArea();
        QueryTextArea.setName("QueryTextArea");
        QueryTextArea.setBounds(10,90,550,80);
        JButton ExecuteQueryButton = new JButton("Execute");
        ExecuteQueryButton.setName("ExecuteQueryButton");
        ExecuteQueryButton.setBounds(570,90,90,30);

        myTableModel myTableModel = new myTableModel();
        JTable Table = new JTable(myTableModel);
        Table.setName("Table");
        JScrollPane sp = new JScrollPane(Table);
        sp.setBounds(10, 180 , 660,200);



        add(FileNameTextField);
        add(OpenFileButton);
        add(TablesComboBox);
        add(QueryTextArea);
        add(ExecuteQueryButton);
        add(sp);
        QueryTextArea.setEnabled(false);
        ExecuteQueryButton.setEnabled(false);

        OpenFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TablesComboBox.removeAllItems();
                if (new File(FileNameTextField.getText()).isFile()) {
                    ArrayList<String> s = Database.forTableComboBox(FileNameTextField.getText());
                    Database.forTableComboBox(FileNameTextField.getText()).forEach(TablesComboBox::addItem);
                    if (s.size() == 0){
                        QueryTextArea.setEnabled(false);
                        ExecuteQueryButton.setEnabled(false);
                    }
                    if (TablesComboBox.getItemCount() != 0){
                        QueryTextArea.setEnabled(true);
                        ExecuteQueryButton.setEnabled(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(new Frame(), "ERROR MESSAGE");
                }
            }
        });

        TablesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QueryTextArea.setEnabled(false);
                ExecuteQueryButton.setEnabled(false);
                String query = "SELECT * FROM %s;";
                QueryTextArea.setText(String.format(query,TablesComboBox.getSelectedItem()));
            }
        });

        ExecuteQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QueryTextArea.setEnabled(false);
                ExecuteQueryButton.setEnabled(false);
                String error = Database.forTableTable(FileNameTextField.getText(),QueryTextArea.getText(),myTableModel);
                if (error != null) {
                    JOptionPane.showMessageDialog(new Frame(), "ERROR MESSAGE");
                } else {
                    QueryTextArea.setEnabled(true);
                    ExecuteQueryButton.setEnabled(true);
                }
            }
        });

    }
}

class myTableModel extends AbstractTableModel{
    String[] cols = new String[0];
    private ArrayList<Object[]> data = new ArrayList<>(0);

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int columnIndex) {
        return cols[columnIndex];
    }
    public void setTableData(String[] cols, ArrayList<Object[]> data) {
        this.cols = cols;
        this.data = data;
        fireTableStructureChanged();
        fireTableDataChanged();
    }
}

