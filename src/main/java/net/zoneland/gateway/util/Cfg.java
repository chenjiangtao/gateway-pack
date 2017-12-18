/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * @author liuzhenxing
 * @version $Id: Cfg.java, v 0.1 2012-5-11 下午4:28:06 liuzhenxing Exp $
 */
public class Cfg {
    private static DocumentBuilderFactory factory;
    private static DocumentBuilder        builder;
    private static final String           XML_HEAD = String.valueOf(String
                                                       .valueOf(new StringBuffer(
                                                           "<?xml version=\"1.0\" encoding=\"")
                                                           .append("UTF-8").append("\"?>")));

    private static String                 indent   = "  ";
    private boolean                       isDirty;
    private Document                      doc;
    private Element                       root;
    private String                        file;

    public Cfg(String url) throws IOException {
        this(url, false);
    }

    public Cfg(String url, boolean create) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("url is null");
        }
        if (url.indexOf(58) > 1)
            this.file = url;
        else {
            this.file = new File(url).toURL().toString();
        }

        new URL(this.file);
        try {
            load();
        } catch (FileNotFoundException ex) {
            if (!(create)) {
                throw ex;
            }
            loadXMLParser();
            this.doc = builder.newDocument();
            this.root = this.doc.createElement("config");
            this.doc.appendChild(this.root);
            this.isDirty = true;
            flush();
            return;
        }
    }

    public Args getArgs(String key) {
        Map args = new HashMap();
        String[] children = childrenNames(key);
        for (int i = 0; i < children.length; ++i) {
            args.put(
                children[i],
                get(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String
                    .valueOf(key))).append('/').append(children[i]))), null));
        }
        return new Args(args);
    }

    private static void writeIndent(PrintWriter pw, int level) {
        for (int i = 0; i < level; ++i)
            pw.print(indent);
    }

    private static void writeNode(Node node, PrintWriter pw, int deep) {
        switch (node.getNodeType()) {
            case 8:
                writeIndent(pw, deep);
                pw.print("<!--");
                pw.print(node.getNodeValue());
                pw.println("-->");
                return;
            case 3:
                String value = node.getNodeValue().trim();
                if (value.length() == 0) {
                    return;
                }
                writeIndent(pw, deep);
                for (int i = 0; i < value.length(); ++i) {
                    char c = value.charAt(i);
                    switch (c) {
                        case '<':
                            pw.print("&lt;");
                            break;
                        case '>':
                            pw.print("&lt;");
                            break;
                        case '&':
                            pw.print("&amp;");
                            break;
                        case '\'':
                            pw.print("&apos;");
                            break;
                        case '"':
                            pw.print("&quot;");
                            break;
                        default:
                            pw.print(c);
                    }
                }
                pw.println();
                return;
            case 1:
                if (!(node.hasChildNodes())) {
                    return;
                }
                for (int i = 0; i < deep; ++i) {
                    pw.print(indent);
                }
                String nodeName = node.getNodeName();
                pw.print('<');
                pw.print(nodeName);

                NamedNodeMap nnm = node.getAttributes();
                if (nnm != null) {
                    for (int i = 0; i < nnm.getLength(); ++i) {
                        Node attr = nnm.item(i);
                        pw.print(' ');
                        pw.print(attr.getNodeName());
                        pw.print("=\"");
                        pw.print(attr.getNodeValue());
                        pw.print('"');
                    }

                }

                if (node.hasChildNodes()) {
                    NodeList children = node.getChildNodes();
                    if (children.getLength() == 0) {
                        pw.print('<');
                        pw.print(nodeName);
                        pw.println("/>");
                        return;
                    }
                    if (children.getLength() == 1) {
                        Node n = children.item(0);
                        if (n.getNodeType() == 3) {
                            String v = n.getNodeValue();
                            if (v != null) {
                                v = v.trim();
                            }
                            if ((v == null) || (v.length() == 0)) {
                                pw.println(" />");
                                return;
                            }
                            pw.print('>');
                            pw.print(v);
                            pw.print("</");
                            pw.print(nodeName);
                            pw.println('>');
                            return;
                        }
                    }

                    pw.println(">");
                    for (int i = 0; i < children.getLength(); ++i) {
                        writeNode(children.item(i), pw, deep + 1);
                    }
                    for (int i = 0; i < deep; ++i) {
                        pw.print(indent);
                    }
                    pw.print("</");
                    pw.print(nodeName);
                    pw.println(">");
                } else {
                    pw.println("/>");
                }
                return;
            case 9:
                pw.println(XML_HEAD);
                NodeList nl = node.getChildNodes();
                for (int i = 0; i < nl.getLength(); ++i) {
                    writeNode(nl.item(i), pw, 0);
                }
                return;
            case 2:
            case 4:
            case 5:
            case 6:
            case 7:
        }
    }

    private Node findNode(String key) {
        Node ancestor = this.root;
        StringTokenizer st = new StringTokenizer(key, "/");
        if (st.hasMoreTokens()) {
            String nodeName = st.nextToken();
            NodeList nl = ancestor.getChildNodes();
            for (int i = 0;; ++i) {
                if (i < nl.getLength())
                    ;
                Node n = nl.item(i);
                if (nodeName.equals(n.getNodeName())) {
                    ancestor = n;
                    if (!(st.hasMoreTokens()))
                        ;
                    return n;
                }
            }

        }

        return null;
    }

    private Node createNode(String key) {
        Node ancestor = this.root;

        StringTokenizer st = new StringTokenizer(key, "/");
        if (st.hasMoreTokens()) {
            String nodeName = st.nextToken();
            NodeList nl = ancestor.getChildNodes();
            for (int i = 0; i < nl.getLength(); ++i) {
                Node n = nl.item(i);
                if (nodeName.equals(n.getNodeName())) {
                    ancestor = n;
                    if (!(st.hasMoreTokens()))
                        ;
                    return ancestor;
                }

            }

            while (true) {
                Node n = this.doc.createElement(nodeName);
                ancestor.appendChild(n);
                ancestor = n;
                if (!(st.hasMoreTokens())) {
                    return ancestor;
                }
                nodeName = st.nextToken();
            }
        }
        return null;
    }

    private Node createNode(Node ancestor, String key) {
        StringTokenizer st = new StringTokenizer(key, "/");
        if (st.hasMoreTokens()) {
            String nodeName = st.nextToken();
            NodeList nl = ancestor.getChildNodes();
            for (int i = 0; i < nl.getLength(); ++i) {
                if (nodeName.equals(nl.item(i).getNodeName())) {
                    ancestor = nl.item(i);
                }
            }

            return null;
        }
        return ancestor;
    }

    public String get(String key, String def) {
        if (key == null) {
            throw new NullPointerException("parameter key is null");
        }

        Node node = findNode(key);
        if (node == null) {
            return def;
        }
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); ++i) {
            if (nl.item(i).getNodeType() == 3) {
                return nl.item(i).getNodeValue().trim();
            }
        }
        node.appendChild(this.doc.createTextNode(def));
        return def;
    }

    public void put(String key, String value) {
        if (key == null) {
            throw new NullPointerException("parameter key is null");
        }
        if (value == null) {
            throw new NullPointerException("parameter value is null");
        }
        value = value.trim();
        Node node = createNode(key);

        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); ++i) {
            Node child = nl.item(i);
            if (child.getNodeType() == 3) {
                String childValue = child.getNodeValue().trim();
                if (childValue.length() == 0) {
                    continue;
                }

                if (childValue.equals(value)) {
                    return;
                }
                child.setNodeValue(value);
                this.isDirty = true;
                return;
            }

        }

        if (nl.getLength() == 0) {
            node.appendChild(this.doc.createTextNode(value));
        } else {
            Node f = node.getFirstChild();
            if (f.getNodeType() == 3)
                f.setNodeValue(value);
            else {
                node.insertBefore(this.doc.createTextNode(value), f);
            }
        }
        this.isDirty = true;
    }

    public boolean getBoolean(String key, boolean def) {
        String str = String.valueOf(def);

        String resstr = get(key, str);
        Boolean resboolean = Boolean.valueOf(resstr);
        boolean result = resboolean.booleanValue();
        return result;
    }

    public int getInt(String key, int def) {
        String str = String.valueOf(def);
        String resstr = get(key, str);
        int result;
        try {
            result = Integer.parseInt(resstr);
        } catch (NumberFormatException e) {
            return def;
        }
        return result;
    }

    public float getFloat(String key, float def) {
        String str = String.valueOf(def);
        String resstr = get(key, str);
        float result;
        try {
            result = Float.parseFloat(resstr);
        } catch (NumberFormatException e) {
            return def;
        }
        return result;
    }

    public double getDouble(String key, double def) {
        String str = String.valueOf(def);
        String resstr = get(key, str);
        double result;
        try {
            result = Double.parseDouble(resstr);
        } catch (NumberFormatException e) {
            return def;
        }
        return result;
    }

    public long getLong(String key, long def) {
        String str = String.valueOf(def);
        String resstr = get(key, str);
        long result;
        try {
            result = Long.parseLong(resstr);
        } catch (NumberFormatException e) {
            return def;
        }
        return result;
    }

    public byte[] getByteArray(String key, byte[] def) {
        String str = new String(def);
        String resstr = get(key, str);
        byte[] result = resstr.getBytes();
        return result;
    }

    public void putBoolean(String key, boolean value) {
        String str = String.valueOf(value);
        try {
            put(key, str);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public void putInt(String key, int value) {
        String str = String.valueOf(value);
        try {
            put(key, str);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public void putFloat(String key, float value) {
        String str = String.valueOf(value);
        try {
            put(key, str);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public void putDouble(String key, double value) {
        String str = String.valueOf(value);
        try {
            put(key, str);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public void putLong(String key, long value) {
        String str = String.valueOf(value);
        try {
            put(key, str);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public void putByteArray(String key, byte[] value) {
        put(key, Base64.encode(value));
    }

    public void removeNode(String key) {
        Node node = findNode(key);
        if (node == null) {
            return;
        }
        Node parentnode = node.getParentNode();
        if (parentnode != null) {
            parentnode.removeChild(node);
            this.isDirty = true;
        }
    }

    public void clear(String key) {
        Node node = findNode(key);
        if (node == null)
            throw new RuntimeException("InvalidName");
        Node lastnode = null;

        while (node.hasChildNodes()) {
            lastnode = node.getLastChild();
            node.removeChild(lastnode);
        }

        if (lastnode != null)
            this.isDirty = true;
    }

    public String[] childrenNames(String key) {
        Node node = findNode(key);
        if (node == null) {
            return new String[0];
        }
        NodeList nl = node.getChildNodes();
        LinkedList list = new LinkedList();
        for (int i = 0; i < nl.getLength(); ++i) {
            Node child = nl.item(i);
            if ((child.getNodeType() == 1) && (child.hasChildNodes())) {
                list.add(child.getNodeName());
            }
        }
        String[] ret = new String[list.size()];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = ((String) list.get(i));
        }
        return ret;
    }

    public boolean nodeExist(String key) {
        Node theNode = findNode(key);
        if (theNode == null) {
            return false;
        }

        return (theNode.hasChildNodes());
    }

    private void loadXMLParser() throws IOException {
        if (builder != null)
            return;
        try {
            factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new IOException("XML Parser load error:".concat(String.valueOf(String.valueOf(ex
                .getLocalizedMessage()))));
        }
    }

    public void load() throws IOException {
        loadXMLParser();
        try {
            InputSource is = new InputSource(new InputStreamReader(new URL(this.file).openStream()));

            is.setEncoding("UTF-8");
            this.doc = builder.parse(is);
        } catch (SAXException ex) {
            ex.printStackTrace();
            String message = ex.getMessage();
            Exception e = ex.getException();
            if (e != null) {
                message = String.valueOf(String.valueOf(message)).concat(
                    String.valueOf(String.valueOf("embedded exception:".concat(String
                        .valueOf(String.valueOf(e))))));
            }
            throw new IOException("XML file parse error:".concat(String.valueOf(String
                .valueOf(message))));
        }
        this.root = this.doc.getDocumentElement();
        if (!("config".equals(this.root.getNodeName())))
            throw new IOException("Config file format error, root node must be <config>");
    }

    public void flush() throws IOException {
        if (this.isDirty) {
            String proc = new URL(this.file).getProtocol().toLowerCase();
            if (!(proc.equalsIgnoreCase("file"))) {
                throw new UnsupportedOperationException(
                    "Unsupport write config URL on protocal ".concat(String.valueOf(String
                        .valueOf(proc))));
            }
            String fileName = new URL(this.file).getPath();
            Debug.dump(new URL(this.file).getPath());
            Debug.dump(new URL(this.file).getFile());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName),
                2048);

            PrintWriter pw = new PrintWriter(bos);
            writeNode(this.doc, pw, 0);
            pw.flush();
            pw.close();
            this.isDirty = false;
        }
    }

    private String change(String str) throws IOException {
        if ((str.indexOf(38) != -1) || (str.indexOf(60) != -1) || (str.indexOf(62) != -1)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());

            byte[] ba1 = { 38, 97, 109, 112, 59 };
            byte[] ba2 = { 38, 108, 116, 59 };
            byte[] ba3 = { 38, 103, 116, 59 };
            byte temp;
            while ((temp = (byte) bis.read()) != -1) {
                switch (temp) {
                    case 38:
                        bos.write(ba1);
                        break;
                    case 60:
                        bos.write(ba2);
                        break;
                    case 62:
                        bos.write(ba3);
                        break;
                }
                bos.write(temp);
            }

            return bos.toString();
        }
        return str;
    }

    public static void main(String[] args) throws Exception {
        Cfg c = new Cfg("testcfg.xml", true);
        c.put("a/b", "汉字");
        c.put("c", "");
        c.put("a", "avalusaaaaaaaaae");
        c.flush();
        c = new Cfg("testcfg.xml", true);
        System.out.println("Config file content:");
        BufferedReader in = new BufferedReader(new FileReader("testcfg.xml"));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
    }
}
