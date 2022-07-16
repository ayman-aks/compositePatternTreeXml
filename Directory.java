import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class Directory implements Component{
    public Directory(String name)
    {
        this.name=name;
    }
    private String name;
    List<Component> components=new ArrayList<Component>();
    public void addComponent(Component component)
    {
        components.add(component);
    }
    public int getComponent()
    {
        return components.size();
    }
    @Override
    public void printName(Document document, Element element) {

        Element root = document.createElement("Directory");
        Attr attr = document.createAttribute("name");
        attr.setValue(this.name);
        root.setAttributeNode(attr);
        for (Component c:components)
        {
            c.printName(document,root);
        }
        element.appendChild(root);
    }
}
