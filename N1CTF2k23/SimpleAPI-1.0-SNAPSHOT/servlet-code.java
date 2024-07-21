package org.rwctf;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ObjectServlet extends HttpServlet {
    private ClassLoader appClassLoader;


    public ObjectServlet() {
    }

    public void init(ServletConfig var1) throws ServletException {
        super.init(var1);
        String servletPath = var1.getServletContext().getRealPath("/");

        File libDirectoryOrFile = new File(servletPath + File.separator + "WEB-INF" + File.separator + File.separator + "lib");
        if (libDirectoryOrFile.exists() && libDirectoryOrFile.isDirectory()) {
            File[] libFiles = libDirectoryOrFile.listFiles();
            if (libFiles != null) {
                URL[] libURIs = new URL[libFiles.length + 1];

                for(int i = 0; i < libFiles.length; ++i) {
                    if (libFiles[i].getName().endsWith(".jar")) {
                        try {
                            libURIs[i] = libFiles[i].toURI().toURL();
                        } catch (MalformedURLException var9) {
                            var9.printStackTrace();
                        }
                    }
                }

                File classesPath = new File(servletPath + File.separator + "WEB-INF" + File.separator + File.separator + "classes");
                if (classesPath.exists() && classesPath.isDirectory()) {
                    try {
                        libURIs[libURIs.length - 1] = classesPath.toURI().toURL();
                    } catch (MalformedURLException var8) {
                        var8.printStackTrace();
                    }
                }

                this.appClassLoader = new URLClassLoader(libURIs);
            }
        }

    }

    protected void doPost(HttpServletRequest var1, HttpServletResponse servletPath) throws ServletException, IOException {
        PrintWriter servletWriter = servletPath.getWriter();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.appClassLoader);

        try {
            ClassLoaderObjectInputStream classLoaderInputStream = new ClassLoaderObjectInputStream(this.appClassLoader, var1.getInputStream());
            Object i = classLoaderInputStream.readObject();
            classLoaderInputStream.close();
            servletWriter.print(i);
        } catch (ClassNotFoundException error) {
            error.printStackTrace(servletWriter);
        } finally {
            Thread.currentThread().setContextClassLoader(libFiles);
        }

    }
}
