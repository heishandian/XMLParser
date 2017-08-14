import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class XmlParseUtil {

    public static String captureName(String name) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("set");
        stringBuffer.append(name.substring(0, 1).toUpperCase() + name.substring(1));
        return stringBuffer.toString();
    }

    public static Object parXmlByDir(String dir,String objectName) throws Exception{

        Class cls = null;
        Foo foo = null;
        try {
            cls = Class.forName(objectName);
            foo = (Foo) cls.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 获取xml文件的全部节点
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(dir);
            NodeList users = document.getChildNodes(); //获取所有的孩子节点

            for (int i = 0; i < users.getLength(); i++) {
                Node user = users.item(i);
                NodeList userInfo = user.getChildNodes();
                for (int j = 0; j < userInfo.getLength(); j++) {
                    Node node = userInfo.item(j);
                    NodeList userMeta = node.getChildNodes();

                    for (int k = 0; k < userMeta.getLength(); k++) {
                        if(userMeta.item(k).getNodeName() != "#text") {
                            // 调用Foo类中存在的方法，
                            Method setMethod = cls.getDeclaredMethod(captureName(userMeta.item(k).getNodeName()),String.class);
                            try {
                                setMethod.invoke(foo, userMeta.item(k).getTextContent());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //System.out.println(setMethod2.invoke(foo));
        return foo;
    }



    public static void main(String args[]) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String dir = "C:\\Users\\kk\\Desktop\\books.xml"; //配置文件位置
        String str =  "Foo";  //全类名
        try {
            Foo foo =  (Foo) XmlParseUtil.parXmlByDir(dir,str);
            System.out.println(foo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class Foo {
    String name;
    String author;
    String year;
    String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Foo{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", year='" + year + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}