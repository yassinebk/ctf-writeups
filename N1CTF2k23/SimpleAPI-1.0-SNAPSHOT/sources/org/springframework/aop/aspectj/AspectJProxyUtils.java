package org.springframework.aop.aspectj;

import java.util.Iterator;
import java.util.List;
import org.springframework.aop.Advisor;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/aspectj/AspectJProxyUtils.class */
public abstract class AspectJProxyUtils {
    public static boolean makeAdvisorChainAspectJCapableIfNecessary(List<Advisor> advisors) {
        if (!advisors.isEmpty()) {
            boolean foundAspectJAdvice = false;
            Iterator<Advisor> it = advisors.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Advisor advisor = it.next();
                if (isAspectJAdvice(advisor)) {
                    foundAspectJAdvice = true;
                    break;
                }
            }
            if (foundAspectJAdvice && !advisors.contains(ExposeInvocationInterceptor.ADVISOR)) {
                advisors.add(0, ExposeInvocationInterceptor.ADVISOR);
                return true;
            }
            return false;
        }
        return false;
    }

    private static boolean isAspectJAdvice(Advisor advisor) {
        return (advisor instanceof InstantiationModelAwarePointcutAdvisor) || (advisor.getAdvice() instanceof AbstractAspectJAdvice) || ((advisor instanceof PointcutAdvisor) && (((PointcutAdvisor) advisor).getPointcut() instanceof AspectJExpressionPointcut));
    }
}
