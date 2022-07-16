import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FileTest implements Component{
    public FileTest(String name)
    {
        this.name=name;
    }
    private String name;
    @Override
    public void printName(Document document,Element element) {
        Element file = document.createElement("File");
        Attr attr = document.createAttribute("name");
        attr.setValue(this.name);
        file.setAttributeNode(attr);
        element.appendChild(file);
    }
}
