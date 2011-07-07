/*
 *  *  Copyright (c) 2010 Tesuji Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Tesuji Ltd. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tesuji.
 *
 * Tesuji MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. Tesuji SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.zh.snmp.snmpcore.util;

import com.zh.snmp.snmpcore.exception.ExceptionCodesEnum;
import com.zh.snmp.snmpcore.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

/**
 * JAXB funkciók
 * 
 * @author cserepj
 */
public class JAXBUtil {

    /**
     * Woodstox XML InputFactory
     */
    private static XMLInputFactory xmlif;
    /**
     * Woodstox XML OutputFactory
     */
    private static XMLOutputFactory xmlof;
    /**
     * JAXBContext cache osztályonként eltárolva
     * A JAXBContext gyártása lassú és drága művelet, ezért ezeket eltesszük későbbi használatra.
     */
    private static Map<Class, JAXBContext> cache = new ConcurrentHashMap<Class, JAXBContext>(10);
    /**
     * Logger
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(JAXBUtil.class);
    /**
     * Szemafor ami biztosítja, hogy maximum a processzorok számával meghatározott
     * unmarshal fusson egyszerre az alkalmazásban
     */
    private final static Semaphore sem = new Semaphore(
            2 * Runtime.getRuntime().availableProcessors());

    /**
     * A factory-k inicializálása
     */
    static {
        createFactory();
    }


    /**
     * Az átadott objektum XML reprezentációba konvertálása
     * @param object
     * @return
     */
    public static void marshal(Object object, Writer writer, boolean prettyPrint) {
        marshal(object, object.getClass(), prettyPrint, writer);
    }

    public static String marshal(Object object, boolean prettyPrint) {
        return marshal(object, object.getClass(), prettyPrint);
    }

    /**
     * Az átadott objektum XML reprezentációba konvertálása
     * @param object
     * @param clazz
     * @param prettyPrint
     * @param w Output writer
     */
    protected static void marshal(Object object, Class clazz, boolean prettyPrint, Writer w) {
        XMLStreamWriter writer = null;
        long begin = 0;
        try {
            // szemaforra várunk
            sem.acquire();
            begin = System.currentTimeMillis();
            Marshaller m = getMarshaller(clazz);
            if (prettyPrint) {
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, prettyPrint);
                m.marshal(object, w);
            } else {
                writer = (XMLStreamWriter) xmlof.createXMLStreamWriter(w);
                m.marshal(object, writer);
            }
        } catch (Exception ex) {
            throw new SystemException(ExceptionCodesEnum.UnknownExceptionType, ex);
        } finally {
            long end = System.currentTimeMillis();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Marshall took: " + (end - begin) + "ms: " + object);
            }
            // szemafort elengedjük
            sem.release();
            if (writer != null) {
                try {
                    w.close();
                    writer.close();
                } catch (Exception ioe) {
                    throw new SystemException(ExceptionCodesEnum.UnknownExceptionType, ioe);
                }
            }
        }
    }

    /**
     * Az átadott objektum XML reprezentációba konvertálása
     * @param object
     * @param clazz
     * @param prettyPrint
     * @return
     */
    protected static String marshal(Object object, Class clazz, boolean prettyPrint) {
        StringWriter w = new StringWriter();
        marshal(object, clazz, prettyPrint, w);
        return w.toString();
    }

    public static <T> T unmarshal(String descriptor, Class<T> clz) {
        return unmarshalTyped(new StringReader(descriptor), clz);
    }
    /**w
     * Az átadott reader-ből kiolvasható objektumot adja vissza.
     * 
     * @param reader
     * @param classes
     * @return
     */
    public static Object unmarshal(Reader reader, Class classes) {
        try {
            sem.acquire();
            long begin = System.currentTimeMillis();
            Object o = null;
            try {
                Unmarshaller m = getUnmarshaller(classes);
                XMLStreamReader xsr = xmlif.createXMLStreamReader(reader);
                o = m.unmarshal(xsr);
                xsr.close();
                return o;
            } catch (Exception ex) {
                throw new SystemException(ExceptionCodesEnum.UnknownExceptionType, ex);
            } finally {
                long end = System.currentTimeMillis();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Unmarshall took: " + (end - begin) + "ms: " + o);
                }
                sem.release();
            }
        } catch (InterruptedException ex) {
            LOGGER.error("Thread was interrupted", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Típusos unmarshall az átadott reader-ből. Megadható az XML-ben várt
     * objektum osztálya és objektum verziója. Ha -1-et adunk át verzióként
     * azzal automatikus verziókeresést érünk el ami a stream első 1000
     * karakterében fogja keresni az esetleges verziót jelző stringet.
     * 
     * @param <T>
     * @param reader Reader
     * @param clazz
     * @param version
     * @return
     */
    public static <T> T unmarshalTyped(Reader reader, Class<T> clazz) {
        return (T) unmarshal(reader, clazz);
    }

    private static <T> T unmarshalTyped(InputStream stream, Class<T> clazz) {
        return (T) unmarshal(new InputStreamReader(stream), clazz);
    }

    /**
     * Unmarshaller gyártása az átadott osztályhoz
     * @param clazz
     * @return
     * @throws JAXBException
     */
    private static Unmarshaller getUnmarshaller(Class classes) throws JAXBException {
        JAXBContext c = cache.get(classes);
        if (c == null) {
            c = createNewInstance(classes);
            cache.put(classes, c);
        }
        return c.createUnmarshaller();
    }

    /**
     * Marshaller gyártása az átadott osztályhoz
     * @param classes
     * @return
     * @throws JAXBException
     */
    private static Marshaller getMarshaller(Class classes) throws JAXBException {
        JAXBContext c = cache.get(classes);
        if (c == null) {
            c = createNewInstance(classes);
            cache.put(classes, c);
        }
        return c.createMarshaller();
    }

    private static JAXBContext createNewInstance(Class clz) throws JAXBException {
        return JAXBContext.newInstance(clz);
    }
    /**
     * Factory-k létrehozása
     */
    private static void createFactory() {

        try {
            xmlif = XMLInputFactory.newFactory();
            xmlif.setProperty(
                    XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,
                    Boolean.FALSE);
            xmlif.setProperty(
                    XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,
                    Boolean.FALSE);
            xmlif.setProperty(
                    XMLInputFactory.IS_COALESCING,
                    Boolean.FALSE);
            xmlof = XMLOutputFactory.newFactory();
        } catch (Exception ex) {
            LOGGER.error("Exception creating factory", ex);
        }
    }

    private static <T> T clone(T object) {
        String s = marshal(object, false);
        return (T) unmarshalTyped(new StringReader(s), object.getClass());
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "bookParams")
    private static class ParamMap {

        @XmlElement(name = "param", required = true)
        private final List<Param> a = new ArrayList<Param>();

        public List<Param> getA() {
            return this.a;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "A")
    private static class Param {
        @XmlAttribute(name = "name", required = true)
        private final String key;
        @XmlAttribute(name = "value", required = true)
        private final String value;
        private Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
        private Param() {
            this.key = null;
            this.value = null;
        }
        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
