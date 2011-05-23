package com.zh.snmp.snmpweb.util;

import com.zh.snmp.snmpcore.exception.ExceptionCodesEnum;
import com.zh.snmp.snmpcore.exception.SystemException;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author cserepj
 */
public class WicketUtils {

    private static Logger logger = LoggerFactory.getLogger(WicketUtils.class);

/*    
    protected static <T> Constructor<T> getModalServiceableConstructor(Class<T> panelClass) {
        Constructor<T> c = constructors.get(panelClass);
        if (c == null) {
            long begin = System.currentTimeMillis();
            try {
                c = panelClass.getConstructor(ModalWindow.class, IModel.class);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            long end = System.currentTimeMillis();
            logger.debug("Getting String,IModel constructor for " + panelClass.getName() + " took " + (end - begin) + " ms");
            if (c != null) {
                constructors.put(panelClass, c);
            } else {
                logger.error("Not found String model constructor for class " + panelClass);
            }
        }
        return c;
    }
*/
   
    public static <T extends Panel> T createPanel(String id, Class<T> panelClass, IModel model) {
        Constructor[] cs = panelClass.getConstructors();
        if (cs.length != 1) {
            throw new SystemException(ExceptionCodesEnum.Unsupported, "Need exactly one constructor, to auto generate class " + panelClass);
        }
        Constructor act = cs[0];
        Class[] params = act.getParameterTypes();
        try {
            if (params.length == 1) {
                return (T)act.newInstance(id);
            } else if (params.length == 2) {
                return (T)act.newInstance(id, model);
            }
        } catch (Exception ex) {
            throw new SystemException(ExceptionCodesEnum.ConfigurationException, "Construct panel failed " + panelClass, ex);
        }
        throw new SystemException(ExceptionCodesEnum.ConfigurationException, "Construct panel failed, (String) or (String, IModel) constructor not found to class " + panelClass);
    }    
/*
    public static <T extends Panel> T createModalPopupPanel(Class<T> panelClass, ModalWindow modal, IModel model) {
        try {
            Constructor<T> c = getModalServiceableConstructor(panelClass);
            return c.newInstance(modal, model);
        } catch (Exception ex) {
            throw new SystemException(ExceptionCodesEnum.ConfigurationException, "Construct panel failed " + panelClass, ex);
        }
    }
*/
    public static <T> T createNewInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new SystemException(ExceptionCodesEnum.ConfigurationException, clazz + " does not have emty constructor.", e);
        }
    }
}
