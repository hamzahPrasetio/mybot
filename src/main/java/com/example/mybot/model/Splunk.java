package com.example.mybot.model;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class Splunk {
    private String username;
    private String password;
    private String address;
    private Document xml;

    public Splunk callRestAPI(String search, String uri, HttpMethod httpMethod, BodyInserters.FormInserter<String> bodyInserters) throws ParserConfigurationException, IOException, SAXException {
        HttpClient httpclient = null;
        try {
            SslContext sslcontext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            httpclient = HttpClient.create().secure(t -> t.sslContext(sslcontext));
        } catch (SSLException e) {
            log.error(e.getMessage());
        }
        WebClient client = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpclient)).baseUrl(this.address).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).build();
        String xmlString = client.method(httpMethod).uri(uri).body(bodyInserters).headers(headers -> headers.setBasicAuth(username, password)).retrieve().bodyToMono(String.class).block();
        System.out.println(xmlString);
        this.xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xmlString)));
        return this;
    }

    public String getOneValue() {
        String ret = null;
        try {
            ret = xml.getElementsByTagName("text").item(0).getTextContent().trim();
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
        return ret;
    }

    public List<String> getOneRow() {
        List<String> list = new ArrayList<String>();
        try {
            NodeList nodes = xml.getElementsByTagName("result").item(0).getChildNodes();
            int nodesLen = nodes.getLength(), counter = 0;
            while (counter < nodesLen) {
                if (nodes.item(counter).getNodeName().equalsIgnoreCase("field")) {
                    list.add(nodes.item(counter).getTextContent().trim());
                }
                counter++;
            }
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            list = null;
        }
        return list;
    }

    public List<String> getOneCol() {
        List<String> list = new ArrayList<String>();
        try {
            NodeList nodes = xml.getElementsByTagName("result");
            int nodesLen = nodes.getLength(), counter = 0;
            while (counter < nodesLen) {
                if (nodes.item(counter).getNodeName().equalsIgnoreCase("result")) {
                    list.add(nodes.item(counter).getTextContent().trim());
                }
                counter++;
            }
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            list = null;
        }
        return list;
    }

    public List<List<String>> extractAllFieldFromXml() {
        List<List<String>> list = new ArrayList<>();
        try {
            NodeList result = xml.getElementsByTagName("result");
            int resultLen = result.getLength(), counter = 0;
            while (counter < resultLen) {
                if (result.item(counter).getNodeName().equalsIgnoreCase("result")) {
                    List<String> list2 = new ArrayList<String>();
                    NodeList fieldList = result.item(counter).getChildNodes();
                    int fieldLen = fieldList.getLength(), counter2 = 0;
                    while (counter2 < fieldLen) {
                        if (fieldList.item(counter2).getNodeName().equalsIgnoreCase("field")) {
                            list2.add(fieldList.item(counter2).getTextContent().trim());
                        }
                        counter2++;
                    }
                    list.add(list2);
                }
                counter++;
            }
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            list = null;
        }
        if (list.isEmpty()) return null;
        return list;
    }

    public List<String> getAvailFieldFromXml() {
        List<String> list = new ArrayList<String>();
        try {
            NodeList fieldList = xml.getElementsByTagName("fieldOrder").item(0).getChildNodes();
            int maxNode = fieldList.getLength(), counter = 0;
            while (counter < maxNode) {
                if (fieldList.item(counter).getNodeName().equalsIgnoreCase("field")) {
                    list.add(fieldList.item(counter).getTextContent().trim());
                }
                counter++;
            }

        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
        return list;
    }
}
