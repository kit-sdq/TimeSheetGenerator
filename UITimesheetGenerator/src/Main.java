import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;

public class Main {

    private static JFrame frame;
    private static DefaultListModel<String> listModel;

    private GlobalSettingsBar globalSettingsBar;
    private ActionBar buttonActionBar;

    public Main() {
        initialize();
    }

    private void initialize() {
        // Main Frame
        frame = new JFrame("Custom Swing Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem fileOptionNew = new JMenuItem("New...");
        JMenuItem fileOptionOpen = new JMenuItem("Open");
        JMenuItem fileOptionGlobalSettings = new JMenuItem("Edit Global Settings");
        JMenuItem fileOptionSave = new JMenuItem("Save");
        JMenuItem fileOptionSaveAs = new JMenuItem("Save as...");
        fileMenu.add(fileOptionNew);
        fileMenu.add(fileOptionOpen);
        fileMenu.add(fileOptionGlobalSettings);
        fileMenu.add(fileOptionSave);
        fileMenu.add(fileOptionSaveAs);
        menuBar.add(fileMenu);

        /* Edit Menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem editOption1 = new JMenuItem("Option 1");
        JMenuItem editOption2 = new JMenuItem("Option 2");
        editMenu.add(editOption1);
        editMenu.add(editOption2);
        menuBar.add(editMenu);
        */

        frame.setJMenuBar(menuBar);

        globalSettingsBar = new GlobalSettingsBar();
        frame.add(globalSettingsBar, BorderLayout.NORTH);

        // Row of Buttons with '+'
        buttonActionBar = new ActionBar();
        frame.add(buttonActionBar, BorderLayout.WEST);

        // Main Content Area with Vertical List
        listModel = new DefaultListModel<>();
        JList<String> itemList = new JList<>(listModel);
        itemList.setBorder(new EmptyBorder(10, 10, 10, 10));
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Double-click to Edit Entry
        itemList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = itemList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        showEntryDialog("Edit Entry", listModel.getElementAt(index), index);
                    }
                }
            }
        });

        JScrollPane listScrollPane = new JScrollPane(itemList);
        System.out.printf("Frame height: %d, bar height: %d, settings height: %d, bar Y: %d, settings Y: %d%n", frame.getHeight(), buttonActionBar.getHeight(), globalSettingsBar.getHeight(), buttonActionBar.getLocation().y, globalSettingsBar.getLocation().y);
        listScrollPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() - 160));
        frame.add(listScrollPane, BorderLayout.SOUTH);

        // Action Listeners
        //addButton.addActionListener(e -> showEntryDialog("Add Entry", "", -1));

        fileOptionNew.addActionListener(e -> showSimpleDialog("File Option 1"));
        fileOptionOpen.addActionListener(e -> showSimpleDialog("File Option 2"));

        // Show Frame
        frame.setVisible(true);
    }

    static void showEntryDialog(String title, String existingText, int index) {
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setSize(600, 400);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));
        dialog.setLocationRelativeTo(frame);

        // Day Text Field
        dialog.add(new JLabel("Action:"));
        JTextField taskField = new JTextField(existingText);
        dialog.add(taskField);

        // Day Text Field
        dialog.add(new JLabel("Day:"));
        JTextField dayField = new JTextField(existingText);
        dialog.add(dayField);

        // Start Time
        dialog.add(new JLabel("Start Time:"));
        JTextField startTimeField = new JTextField();
        dialog.add(startTimeField);

        // End Time
        dialog.add(new JLabel("End Time:"));
        JTextField endTimeField = new JTextField();
        dialog.add(endTimeField);

        // Tick Box
        dialog.add(new JLabel("Option:"));
        JCheckBox optionCheckBox = new JCheckBox();
        dialog.add(optionCheckBox);

        // Save and Discard Buttons
        JButton saveButton = new JButton("Save");
        JButton discardButton = new JButton("Discard");
        dialog.add(saveButton);
        dialog.add(discardButton);

        // Action Listeners for Buttons
        saveButton.addActionListener(e -> {
            String entry = dayField.getText();
            if (!entry.isEmpty()) {
                if (index >= 0) {
                    listModel.set(index, entry);
                } else {
                    listModel.addElement(entry);
                }
            }
            dialog.dispose();
        });

        discardButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    static void showEntryDialog2(String title, String existingText, int index) {
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout(10, 10)); // Add padding of ~10
        dialog.setLocationRelativeTo(frame);

        // Create panel for the form elements
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Action Text Field (2 lines maximum)
        gbc.gridy = 0;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Action:"), gbc);
        JTextArea taskField = new JTextArea(2, 20);
        taskField.setLineWrap(true);
        taskField.setWrapStyleWord(true);
        taskField.setFont(taskField.getFont().deriveFont(14f)); // Set bigger text
        formPanel.add(taskField, gbc);

        // Day Text Field (1 line tall)
        gbc.gridy = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Day:"), gbc);
        JTextField dayField = new JTextField(10);
        dayField.setFont(dayField.getFont().deriveFont(14f)); // Set bigger text
        formPanel.add(dayField, gbc);

        // Start Time Field
        gbc.gridy = 2;
        formPanel.add(new JLabel("Start Time:"), gbc);
        JTextField startTimeField = new JTextField(10);
        startTimeField.setFont(startTimeField.getFont().deriveFont(14f));
        formPanel.add(startTimeField, gbc);

        // End Time Field
        gbc.gridy = 3;
        formPanel.add(new JLabel("End Time:"), gbc);
        JTextField endTimeField = new JTextField(10);
        endTimeField.setFont(endTimeField.getFont().deriveFont(14f));
        formPanel.add(endTimeField, gbc);

        // Break Time Field
        gbc.gridy = 4;
        formPanel.add(new JLabel("Break Time:"), gbc);
        JTextField breakTimeField = new JTextField(10);
        breakTimeField.setFont(breakTimeField.getFont().deriveFont(14f));
        formPanel.add(breakTimeField, gbc);

        // Checkbox for Vacation
        gbc.gridy = 5;
        formPanel.add(new JLabel("Vacation:"), gbc);
        JCheckBox vacationCheckBox = new JCheckBox();
        formPanel.add(vacationCheckBox, gbc);

        // Add form panel to dialog
        dialog.add(formPanel, BorderLayout.CENTER);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton saveButton = new JButton("Save");
        JButton discardButton = new JButton("Discard");
        saveButton.setPreferredSize(new Dimension(80, 30)); // Smaller buttons
        discardButton.setPreferredSize(new Dimension(80, 30));
        buttonPanel.add(saveButton);
        buttonPanel.add(discardButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners for Buttons
        saveButton.addActionListener(e -> {
            String entry = dayField.getText();
            if (!entry.isEmpty()) {
                if (index >= 0) {
                    listModel.set(index, entry);
                } else {
                    listModel.addElement(entry);
                }
            }
            dialog.dispose();
        });

        discardButton.addActionListener(e -> dialog.dispose());

        // Set visible
        dialog.setVisible(true);

    }

    static void showEntryDialog3(String title, String existingText, int index) {
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout(10, 10)); // Add padding of ~10
        dialog.setLocationRelativeTo(frame);

// Create panel for the form elements
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

// Action Text Field (2 lines maximum)
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Action:"), gbc);
        JTextArea taskField = new JTextArea(2, 20);
        taskField.setLineWrap(true);
        taskField.setWrapStyleWord(true);
        taskField.setFont(taskField.getFont().deriveFont(14f)); // Set bigger text
        formPanel.add(taskField, gbc);

// Reset gridwidth for other fields
        gbc.gridwidth = 1;

// Day Text Field (1 line tall)
        gbc.gridy = 1;
        formPanel.add(new JLabel("Day:"), gbc);
        gbc.gridx = 1;
        JTextField dayField = new JTextField(10);
        dayField.setFont(dayField.getFont().deriveFont(14f)); // Set bigger text
        formPanel.add(dayField, gbc);

// Start Time Field with limited width
        gbc.gridy = 2;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Start Time:"), gbc);
        gbc.gridx = 1;
        JTextField startTimeField = new JTextField(5);
        startTimeField.setFont(startTimeField.getFont().deriveFont(14f));
        formPanel.add(startTimeField, gbc);

// End Time Field with limited width
        gbc.gridy = 3;
        gbc.gridx = 0;
        formPanel.add(new JLabel("End Time:"), gbc);
        gbc.gridx = 1;
        JTextField endTimeField = new JTextField(5);
        endTimeField.setFont(endTimeField.getFont().deriveFont(14f));
        formPanel.add(endTimeField, gbc);

// Break Time Field with limited width
        gbc.gridy = 4;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Break Time:"), gbc);
        gbc.gridx = 1;
        JTextField breakTimeField = new JTextField(5);
        breakTimeField.setFont(breakTimeField.getFont().deriveFont(14f));
        formPanel.add(breakTimeField, gbc);

// Checkbox for Vacation with label
        gbc.gridy = 5;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Vacation:"), gbc);
        gbc.gridx = 1;
        JCheckBox vacationCheckBox = new JCheckBox();
        formPanel.add(vacationCheckBox, gbc);

// Add form panel to dialog
        dialog.add(formPanel, BorderLayout.CENTER);

// Create panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton saveButton = new JButton("Save");
        JButton discardButton = new JButton("Discard");
        saveButton.setPreferredSize(new Dimension(80, 30)); // Smaller buttons
        discardButton.setPreferredSize(new Dimension(80, 30));
        buttonPanel.add(saveButton);
        buttonPanel.add(discardButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

// Action Listeners for Buttons
        saveButton.addActionListener(e -> {
            String entry = dayField.getText();
            if (!entry.isEmpty()) {
                if (index >= 0) {
                    listModel.set(index, entry);
                } else {
                    listModel.addElement(entry);
                }
            }
            dialog.dispose();
        });

        discardButton.addActionListener(e -> dialog.dispose());

// Set visible
        dialog.setVisible(true);

    }

    public static void showEntryDialog4(String title, String existingText, int index) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setSize(600, 400);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null); // Center the dialog

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding of 10

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10); // Spacing between components

        int row = 0;

        // 3. Action field: multiline text area
        JLabel actionLabel = new JLabel("Action:");
        actionLabel.setHorizontalAlignment(JLabel.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(actionLabel, gbc);

        JTextArea actionTextArea = new JTextArea(2, 20);
        actionTextArea.setText(existingText);
        addPlaceholderText(actionTextArea, "Describe the action"); // 9. Placeholder text

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(actionTextArea), gbc);

        row++;

        // 4. Time fields
        String[] labels = {"Start Time:", "End Time:", "Break Time:"};
        JTextField[] timeFields = new JTextField[3];
        String[] placeholders = {"HH:MM", "HH:MM", "Duration in minutes"};

        for (int i = 0; i < labels.length; i++) {
            JLabel timeLabel = new JLabel(labels[i]);
            timeLabel.setHorizontalAlignment(JLabel.RIGHT);
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(timeLabel, gbc);

            JTextField timeField = new JTextField(20);
            addPlaceholderText(timeField, placeholders[i]); // 9. Placeholder text
            timeFields[i] = timeField;

            gbc.gridx = 1;
            gbc.gridy = row;
            gbc.weightx = 1;
            panel.add(timeField, gbc);

            row++;
        }

        // 5. Vacation checkbox
        JLabel vacationLabel = new JLabel("Vacation:");
        vacationLabel.setHorizontalAlignment(JLabel.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(vacationLabel, gbc);

        JCheckBox vacationCheckBox = new JCheckBox();
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        panel.add(vacationCheckBox, gbc);

        row++;

        // 8. Buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton makeEntryButton = new JButton("Make Entry");
        JButton discardButton = new JButton("Discard");
        buttonPanel.add(makeEntryButton);
        buttonPanel.add(discardButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(buttonPanel, gbc);

        // Action listeners for buttons
        makeEntryButton.addActionListener(e -> {
            // Handle make entry logic here
            dialog.dispose();
        });

        discardButton.addActionListener(e -> {
            dialog.dispose();
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // Helper method to add placeholder text
    public static void addPlaceholderText(JTextComponent component, String placeholder) {
        component.setForeground(Color.GRAY);
        component.setText(placeholder);

        component.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (component.getText().equals(placeholder)) {
                    component.setText("");
                    component.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (component.getText().isEmpty()) {
                    component.setForeground(Color.GRAY);
                    component.setText(placeholder);
                }
            }
        });
    }

    private void showSimpleDialog(String message) {
        JOptionPane.showMessageDialog(frame, message, "Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Ensure the application uses the system look and feel
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new Main();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
