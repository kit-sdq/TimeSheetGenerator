/*
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2024 Benjamin Claus
 */
package net.justonedev.kit;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DragDropJFrame extends JFrame {

    private static final Color HOVER_COLOR = new Color(160, 160, 160);
    private static final Color DEFAULT_COLOR = UIManager.getColor("Panel.background");

    public DragDropJFrame() {
        // Setting up drag-and-drop functionality
        new DropTarget(this, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                if (isDragAcceptable(dtde)) {
                    setColor(HOVER_COLOR);
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                } else {
                    dtde.rejectDrag();
                }
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                setColor(DEFAULT_COLOR);
            }

            @SuppressWarnings("unchecked")  // :)
            @Override
            public void drop(DropTargetDropEvent dtde) {
                if (isDropAcceptable(dtde)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable transferable = dtde.getTransferable();

                    try {
                        List<File> droppedFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                        if (!droppedFiles.isEmpty()) {
                            File jsonFile = droppedFiles.getFirst();
                            if (jsonFile.getName().toLowerCase().endsWith(".json")) {
                                performActionWithJSON(jsonFile);
                            } else {
                                JOptionPane.showMessageDialog(DragDropJFrame.this, "Only JSON files are accepted.", "Invalid File", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    } catch (UnsupportedFlavorException | IOException ex) {
                        return;
                    } finally {
                        setColor(DEFAULT_COLOR);
                        dtde.dropComplete(true);
                    }
                } else {
                    dtde.rejectDrop();
                }
            }
        });
    }

    private boolean isDragAcceptable(DropTargetDragEvent dtde) {
        return dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    private boolean isDropAcceptable(DropTargetDropEvent dtde) {
        return dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    private void performActionWithJSON(File jsonFile) {
        Main.openFile(jsonFile);
    }

    private void setColor(Color color) {
        getContentPane().setBackground(color);
        Main.setBackgroundColor(color);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DragDropJFrame frame = new DragDropJFrame();
            frame.setVisible(true);
        });
    }
}
