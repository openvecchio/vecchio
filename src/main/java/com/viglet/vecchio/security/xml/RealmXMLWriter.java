package com.viglet.vecchio.security.xml;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Comment;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;
import com.viglet.vecchio.security.Credentials;
import com.viglet.vecchio.security.SecurityDomainRuntimeException;
import com.viglet.vecchio.security.utils.UnicodeFormatter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * <p/>
 * About this class
 * </p>
 * This component and its source code representation are copyright protected
 * and proprietary to EDC4IT Ltd.
 * This component and source code may be used for instructional and evaluation
 * purposes only. No part of this component or its source code may be sold,
 * transferred, or publicly posted, nor may it be used in a commercial or
 * production environment, without the express written consent of EDC4IT Ltd.
 * <p/>
 * Copyright (c) 2004 EDC4IT Ltd.
 *
 * @author EDC4IT Ltd
 * @version $Revision: 6 $
 */
public class RealmXMLWriter {
  protected Logger logger = Logger.getAnonymousLogger();


  public static void write(Map<String, Credentials> realm, File name) throws IOException {
    Document doc = new Document();
    Comment comment = new Comment("Do not edit this file when the application is running. This file will be overwritten");

    Element e = new Element("realm");

    doc.setRootElement(e);

    doc.addContent(comment);
    Set<Map.Entry<String, Credentials>> entries = realm.entrySet();
    for (Iterator<Map.Entry<String, Credentials>> iterator = entries.iterator(); iterator.hasNext();) {
      Map.Entry<String, Credentials> entry = iterator.next();
      String userName = entry.getKey();
      Credentials credentials = entry.getValue();
      String question = credentials.getQuestion();
      byte[] anser = credentials.getAnser();

      Element userElement = new Element("user");
      Element userNameElement = new Element("username");
      Element questionElement = new Element("question");
      Element anserElement = new Element("answer-digest");
      e.addContent(userElement);
      userElement.addContent(userNameElement);
      userElement.addContent(questionElement);
      userElement.addContent(anserElement);

      userNameElement.setText(userName);
      questionElement.setText(question);

      String str = UnicodeFormatter.bytesToHex(anser);
      anserElement.setText(str);
    }
    XMLOutputter xmlOut = new XMLOutputter();

    Format prettyFormat = Format.getPrettyFormat();

    xmlOut.setFormat(prettyFormat);


    FileOutputStream fos = new FileOutputStream(name);
    try {
      BufferedOutputStream bos = new BufferedOutputStream(fos);
      xmlOut.output(doc, bos);
    } finally {
      fos.close();
    }


  }

  public static Map<String, Credentials> open(File name) throws SecurityDomainRuntimeException{


    try {
      SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
      SAXParser saxParser = saxParserFactory.newSAXParser();
      RealmXMLParser parser = new RealmXMLParser();
      saxParser.parse(name,parser);
      Map<String, Credentials> realm =  parser.getRealm();
      return realm;
    } catch (ParserConfigurationException e) {
      throw new SecurityDomainRuntimeException(e);
    } catch (SAXException e) {
      throw new SecurityDomainRuntimeException(e);
    } catch (IOException e) {
      throw new SecurityDomainRuntimeException(e);
    }

  }
}