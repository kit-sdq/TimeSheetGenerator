/* Licensed under MIT 2024. */
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DragDropJFrame extends JFrame {

	private static final Color HOVER_COLOR = new Color(160, 160, 160);
	private static final Color DEFAULT_COLOR = UIManager.getColor("Panel.background");

	private final UserInterface parentUI;

	public DragDropJFrame(UserInterface parentUI) {
		this.parentUI = parentUI;

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
				// Needs to be implemented, but we're not doing anything here
			}

			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) {
				// Needs to be implemented, but we're not doing anything here
			}

			@Override
			public void dragExit(DropTargetEvent dte) {
				setColor(DEFAULT_COLOR);
			}

			@Override
			public void drop(DropTargetDropEvent dtde) {
				handleDroppedFileEvent(dtde);
			}
		});
	}

	@SuppressWarnings("unchecked") // :)
	private void handleDroppedFileEvent(DropTargetDropEvent dtde) {
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
						JOptionPane.showMessageDialog(DragDropJFrame.this, "Only JSON files are accepted.", "Invalid File",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			} catch (UnsupportedFlavorException | IOException ignored) {
				// Catch exception, but just ignore it. Nothing happens, we just go back to normal
			} finally {
				setColor(DEFAULT_COLOR);
				dtde.dropComplete(true);
			}
		} else {
			dtde.rejectDrop();
		}
	}

	private boolean isDragAcceptable(DropTargetDragEvent dtde) {
		return dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
	}

	private boolean isDropAcceptable(DropTargetDropEvent dtde) {
		return dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
	}

	private void performActionWithJSON(File jsonFile) {
		parentUI.openFile(jsonFile);
	}

	private void setColor(Color color) {
		getContentPane().setBackground(color);
		parentUI.setBackgroundColor(color);
	}
}
