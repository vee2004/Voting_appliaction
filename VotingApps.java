package votingapps;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class VotingApps {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        // Create the frame
        JFrame frame = new JFrame("Voting Ballot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        // Create the table model
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make only the "Vote" column editable
                return column == 3;
            }
        };
        model.addColumn("Serial No.");
        model.addColumn("Candidate Name");
        model.addColumn("Party Symbol");
        model.addColumn("Vote");

        // Add candidates to the model
        model.addRow(new Object[]{1, "Alice Johnson", loadImage("/images/party1.jpeg"), "Vote"});
        model.addRow(new Object[]{2, "Bob Smith", loadImage("/images/party2.jpeg"), "Vote"});
        model.addRow(new Object[]{3, "Charlie Brown", loadImage("/images/party3.jpeg"), "Vote"});
        model.addRow(new Object[]{4, "Raj", loadImage("/images/partys.jpeg"), "Vote"});

        // Create the table
        JTable table = new JTable(model);

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // Serial No.
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Candidate Name
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Party Symbol
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Vote
        table.setRowHeight(100); // Row height

        // Set custom cell renderer for Party Symbol column
        table.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());

        // Set the table's button column
        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), table));

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Display the frame
        frame.setVisible(true);
    }

    private static ImageIcon loadImage(String imageName) {
        // Load image from resources
        URL imageUrl = VotingApps.class.getResource(imageName);
        if (imageUrl != null) {
            return new ImageIcon(imageUrl);
        } else {
            System.err.println("Image not found: " + imageName);
            return null;
        }
    }
}

// Custom renderer for the "Vote" button
class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Vote" : value.toString());
        return this;
    }
}

// Custom editor for the "Vote" button
class ButtonEditor extends DefaultCellEditor {
    private String label;
    private boolean isPushed;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox, JTable table) {
        super(checkBox);
        this.table = table;
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = (value == null) ? "Vote" : value.toString();
        JButton button = new JButton(label);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            String candidateName = (String) table.getValueAt(table.getEditingRow(), 1);
            JOptionPane.showMessageDialog(null, "You voted for: " + candidateName);
            label = "Voted";
        }
        isPushed = false;
        return new String(label);
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}

// Custom renderer for displaying images
class ImageRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        if (value != null && value instanceof ImageIcon) {
            // Set image icon
            label.setIcon((ImageIcon) value);
        }
        return label;
    }
}
