package javax.el;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/BeanNameResolver.class */
public abstract class BeanNameResolver {
    public boolean isNameResolved(String beanName) {
        return false;
    }

    public Object getBean(String beanName) {
        return null;
    }

    public void setBeanValue(String beanName, Object value) throws PropertyNotWritableException {
        throw new PropertyNotWritableException();
    }

    public boolean isReadOnly(String beanName) {
        return true;
    }

    public boolean canCreateBean(String beanName) {
        return false;
    }
}
