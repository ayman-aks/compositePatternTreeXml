import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class Main {

    public static void main(String[] args)
    {
        Directory directory=new Directory("app");
        String path="/home/ayman/Desktop/app/";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        recursiveAdd(directory, path, listOfFiles);
        String xmlFilePath ="/var/CompositePattern/genretedFile.xml";
        pathToXml(directory,xmlFilePath);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                XmlToJtree x2j = new XmlToJtree(xmlFilePath);
                x2j.expandAllNodes();
                x2j.show(x2j.file.getName(), 400, 600);
            }
        });


    }



    public static void recursiveAdd(Directory directory, String path, File[] listOfFiles){
        FileTest fileTest;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                fileTest=new FileTest(listOfFiles[i].getName());
                directory.addComponent(fileTest);
            } else if (listOfFiles[i].isDirectory()) {
                Directory directoryTemp=new Directory(listOfFiles[i].getName());
                String path1=path+listOfFiles[i].getName()+"/";
                File folder = new File(path1);
                File[] listOfFiles1 = folder.listFiles();
                directory.addComponent(directoryTemp);
                recursiveAdd(directoryTemp, path1, listOfFiles1);
            }
        }
    }
    public static void pathToXml(Directory directory,String xmlFilePath){
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            Element root = document.createElement("root");
            document.appendChild(root);

            directory.printName(document,root);


            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

}
