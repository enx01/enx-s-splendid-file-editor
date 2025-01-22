import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class EnxTree extends JPanel {

    private JTree tree;
    private File currentFile;
    private File projectRoot;

    public EnxTree() {
        setLayout(new BorderLayout());

        tree = new JTree();

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(false);

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    System.out.println("Mouseevent triggered!");
                    showPopup(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    System.out.println("Mouseevent triggered!");
                    showPopup(e);
                }
            }
        });

    }

    public boolean shouldBeVisible() {
        return tree != null;
    }

    public void loadDirectory(File directory) {
        projectRoot = directory;
        createTree(directory);
    }

    private void showPopup(MouseEvent e) {

        System.out.println("Inside showPopup");

        int row = tree.getClosestRowForLocation(e.getX(), e.getY());
        tree.setSelectionRow(row);
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        currentFile = (File) selectedNode.getUserObject();

        if (currentFile == null)
            return;

        JPopupMenu popupMenu = new JPopupMenu();

        System.out.println("File is not null : " + currentFile.toString());

        if (currentFile.isDirectory()) {
            System.out.println("File is dir");
            JMenuItem createDirItem = new JMenuItem("create new directory");
            createDirItem.addActionListener(e1 -> createNewDirectory());
            popupMenu.add(createDirItem);

            JMenuItem createFileItem = new JMenuItem("create new file");
            createDirItem.addActionListener(e1 -> createNewFile());
            popupMenu.add(createFileItem);
        }

        JMenuItem renameItem = new JMenuItem("rename");
        renameItem.addActionListener(e1 -> renameFile());
        popupMenu.add(renameItem);

        JMenuItem deleteItem = new JMenuItem("delete");
        deleteItem.addActionListener(e1 -> deleteFile());
        popupMenu.add(deleteItem);

        popupMenu.show(e.getComponent(), e.getX(), e.getY());

    }

    // private File getFileFromNode(DefaultMutableTreeNode node) {
    // StringBuilder path = new StringBuilder();
    // while (node != null) {
    // path.insert(0, node.getUserObject().toString() + File.separator);
    // node = (DefaultMutableTreeNode) node.getParent();
    // }

    // return new File(path.toString());
    // }

    private void renameFile() {
        String newName = JOptionPane.showInputDialog(this, "enter new name :", currentFile.getName());
        if (newName != null && !newName.trim().isEmpty()) {
            File newFile = new File(currentFile.getParent(), newName.trim());
            if (currentFile.renameTo(newFile)) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath()
                        .getLastPathComponent();
                selectedNode.setUserObject(newFile);
                ((DefaultTreeModel) tree.getModel()).nodeChanged(selectedNode);
            } else {
                JOptionPane.showMessageDialog(this, "failed to rename the file :(", "error!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteFile() {
        int confirm = JOptionPane.showConfirmDialog(this, "do you really want to delete this file?", "confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (currentFile.delete()) {
                // Update the tree model to reflect the deletion
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath()
                        .getLastPathComponent();
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                parentNode.remove(selectedNode);
                ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(parentNode);
            } else {
                JOptionPane.showMessageDialog(this, "failed to delete the file :(", "error!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createNewDirectory() {
        String dirName = JOptionPane.showInputDialog(this, "enter new directory name :");
        if (dirName != null && !dirName.trim().isEmpty()) {
            File newDir = new File(currentFile, dirName.trim());
            if (newDir.mkdir()) {
                // Update the tree model to reflect the new directory
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath()
                        .getLastPathComponent();
                DefaultMutableTreeNode newDirNode = new DefaultMutableTreeNode(newDir);
                selectedNode.add(newDirNode);
                ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(selectedNode);
            } else {
                JOptionPane.showMessageDialog(this, "error creating new directory :(", "error!",
                        JOptionPane.ERROR_MESSAGE);
                ;
            }
        }
    }

    private void createNewFile() {
        String fileName = JOptionPane.showInputDialog(this, "enter new file name :");
        if (fileName != null && !fileName.trim().isEmpty()) {
            File newFile = new File(currentFile, fileName.trim());
            try {
                if (newFile.createNewFile()) {
                    // Update the tree model to reflect the new directory
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath()
                            .getLastPathComponent();
                    DefaultMutableTreeNode newDirNode = new DefaultMutableTreeNode(newFile);
                    selectedNode.add(newDirNode);
                    ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(selectedNode);
                } else {
                    JOptionPane.showMessageDialog(this, "error creating new directory :(", "error!",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void createTree(File directory) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(directory.getName());

        File[] files = directory.listFiles();
        if (files != null) {
            for (File f : files) {
                createTree(root, f, 4);
            }
        }

        tree.setModel(new javax.swing.tree.DefaultTreeModel(root));
        tree.expandRow(0);
    }

    private void createTree(DefaultMutableTreeNode node, File file, int depth) {
        if (depth < 0)
            return;

        DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(file);
        node.add(fileNode);

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    createTree(fileNode, f, depth - 1);
                }
            }
        }
    }
}
