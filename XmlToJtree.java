import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class XmlToJtree {

        File file;
        private Document document;
        private DefaultTreeModel treeModel;
        private JTree treeView;

        /**
         * Constructor.
         *
         * @param filePath
         */
        public XmlToJtree(String path) {
            if (path != null) {
                file = new File(path);
            }
            if (file != null && file.canRead()) {
                document = parseDomFromXml(file);
            }
            if (document != null) {
                treeModel = new DefaultTreeModel(buildTreeNode(document));
            }
            if (treeModel != null) {
                treeView = new JTree(treeModel);
            }
        }

        /**
         * Expand all tree nodes.
         */
        public void expandAllNodes() {
            if (treeView != null) {
                for (int i = 0; i < treeView.getRowCount(); i++) {
                    treeView.expandRow(i);
                }
            }
        }

        /**
         * Show window.
         *
         * @param title
         * @param width
         * @param height
         */
        public void show(String title, int width, int height) {
            if (treeView != null) {
                JFrame frame = new JFrame(title);
                frame.setSize(width, height);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.add(new JScrollPane(treeView));
                frame.add(new JPanel(new GridLayout(1, 1)), BorderLayout.SOUTH);
                frame.pack();
                frame.setVisible(true);
            }
        }

        /**
         * Recursively build tree node from document node. Add attributes as leaves
         *
         * @param docNode
         * @return A default mutable tree node.
         */
        private DefaultMutableTreeNode buildTreeNode(Node docNode) {
            DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(
                    docNode.getNodeName());


            if (docNode.hasAttributes()) {
                NamedNodeMap attributes = docNode.getAttributes();

                for (int j = 0; j < attributes.getLength(); j++) {
                    String attr = attributes.item(j).getNodeValue();
                    treeNode.add(new DefaultMutableTreeNode(attr));
                }
            }

            if (docNode.hasChildNodes()) {
                NodeList childNodes = docNode.getChildNodes();

                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node childNode = childNodes.item(i);
                    short childNodeType = childNode.getNodeType();

                    if (childNodeType == Node.ELEMENT_NODE) {
                        treeNode.add(buildTreeNode(childNode));
                    } else if (childNodeType == Node.TEXT_NODE) {
                        String text = childNode.getTextContent().trim();
                        if (!text.equals("")) {
                            treeNode.add(new DefaultMutableTreeNode(text));
                        }
                    } else if (childNodeType == Node.COMMENT_NODE) {
                        String comment = childNode.getNodeValue().trim();
                        treeNode.add(new DefaultMutableTreeNode(comment));
                    }
                }
            }
            return treeNode;
        }

        /**
         * Parse an XML file into document object model.
         *
         * @param filePath
         * @return A DOM document.
         */
        private Document parseDomFromXml(File file) {
            Document document = null;
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                document = dbf.newDocumentBuilder().parse(file);
                document.normalizeDocument();
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
            return document;
        }

}
