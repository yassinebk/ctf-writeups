package javax.el;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.Properties;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/FactoryFinder.class */
class FactoryFinder {
    FactoryFinder() {
    }

    private static Object newInstance(String className, ClassLoader classLoader, Properties properties) {
        Class<?> spiClass;
        try {
            if (classLoader == null) {
                spiClass = Class.forName(className);
            } else {
                spiClass = classLoader.loadClass(className);
            }
            if (properties != null) {
                Constructor<?> constr = null;
                try {
                    constr = spiClass.getConstructor(Properties.class);
                } catch (Exception e) {
                }
                if (constr != null) {
                    return constr.newInstance(properties);
                }
            }
            return spiClass.newInstance();
        } catch (ClassNotFoundException x) {
            throw new ELException("Provider " + className + " not found", x);
        } catch (Exception x2) {
            throw new ELException("Provider " + className + " could not be instantiated: " + x2, x2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object find(String factoryId, String fallbackClassName, Properties properties) {
        InputStream is;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String serviceId = "META-INF/services/" + factoryId;
            try {
                if (classLoader == null) {
                    is = ClassLoader.getSystemResourceAsStream(serviceId);
                } else {
                    is = classLoader.getResourceAsStream(serviceId);
                }
                if (is != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String factoryClassName = reader.readLine();
                    reader.close();
                    if (factoryClassName != null && !"".equals(factoryClassName)) {
                        return newInstance(factoryClassName, classLoader, properties);
                    }
                }
            } catch (Exception e) {
            }
            try {
                String javah = System.getProperty("java.home");
                String configFileName = javah + File.separator + "lib" + File.separator + "el.properties";
                File configFile = new File(configFileName);
                if (configFile.exists()) {
                    Properties props = new Properties();
                    props.load(new FileInputStream(configFile));
                    return newInstance(props.getProperty(factoryId), classLoader, properties);
                }
            } catch (Exception e2) {
            }
            try {
                String systemProp = System.getProperty(factoryId);
                if (systemProp != null) {
                    return newInstance(systemProp, classLoader, properties);
                }
            } catch (SecurityException e3) {
            }
            if (fallbackClassName == null) {
                throw new ELException("Provider for " + factoryId + " cannot be found", null);
            }
            return newInstance(fallbackClassName, classLoader, properties);
        } catch (Exception x) {
            throw new ELException(x.toString(), x);
        }
    }
}
