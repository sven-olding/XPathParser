package com.gi.crm.tools;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for XPathParser
 */
public class XPathParserTest {

    private static final String TEST_XML = "<library><books>\r\n\t<book>\r\n    <author>Stephen King</author>\r\n    <title>The Shining</title>\r\n\t\t<year>1977</year>\r\n\t\t<isbn>978-0-385-12167-5</isbn>\r\n\t</book>\r\n\t<book>\r\n    <author>Ernest Hemingway</author>\r\n    <title>The Old Man and the Sea</title>\r\n\t\t<year>1952</year>\r\n\t\t<isbn>0-684-80122-1</isbn>\r\n\t</book>\r\n</books></library>";

    private static XPathParser parser;

    @BeforeClass
    public static void setup() throws IOException, SAXException, ParserConfigurationException {
        parser = new XPathParser(TEST_XML);
    }

    @Test
    public void testEvaluteWithCorrectExpression() throws XPathExpressionException {
        final String expression = "//isbn/text()";
        Vector<String> result = parser.evaluate(expression);
        assertNotNull(result);
        assertNotNull(result.get(0));
        assertEquals(2, result.size());
        assertEquals("978-0-385-12167-5", result.get(0));
    }

    @Test(expected = XPathExpressionException.class)
    public void testEvaluateWithIncorrectExpression() throws XPathExpressionException {
        final String expression = "<foo>";
        Vector<String> result = parser.evaluate(expression);
    }

    @Test
    public void testEvaluateToXmlString() throws XPathExpressionException, ParserConfigurationException, TransformerException {
        final String expression = "//author";
        String result = parser.evaluateToXmlString(expression);
        assertNotNull(result);
        assertEquals(result, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root><author>Stephen King</author><author>Ernest Hemingway</author></root>");
    }

    @Test
    public void testEvaluateToNodeList() throws XPathExpressionException {
        final String expression = "//book";
        NodeList result = parser.evaluateToNodeList(expression);
        assertNotNull(result);
        assertEquals(result.getLength(),2);
        String localName = result.item(1).getLocalName();
        assertEquals(localName, "book");
    }
}
