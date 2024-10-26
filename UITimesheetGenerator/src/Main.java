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
                        DialogueHelper.showEntryDialog("Edit Entry", listModel.getElementAt(index));
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
