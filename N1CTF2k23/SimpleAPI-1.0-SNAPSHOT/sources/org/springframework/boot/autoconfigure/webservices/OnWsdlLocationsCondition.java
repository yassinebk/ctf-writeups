package org.springframework.boot.autoconfigure.webservices;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.OnPropertyListCondition;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/webservices/OnWsdlLocationsCondition.class */
class OnWsdlLocationsCondition extends OnPropertyListCondition {
    OnWsdlLocationsCondition() {
        super("spring.webservices.wsdl-locations", () -> {
            return ConditionMessage.forCondition("WSDL locations", new Object[0]);
        });
    }
}
