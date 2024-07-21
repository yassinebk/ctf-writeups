package org.springframework.boot.context.properties.bind.validation;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.bind.AbstractBindHandler;
import org.springframework.boot.context.properties.bind.BindContext;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.DataObjectPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationProperty;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.core.ResolvableType;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.Validator;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/validation/ValidationBindHandler.class */
public class ValidationBindHandler extends AbstractBindHandler {
    private final Validator[] validators;
    private final Map<ConfigurationPropertyName, ResolvableType> boundTypes;
    private final Map<ConfigurationPropertyName, Object> boundResults;
    private final Set<ConfigurationProperty> boundProperties;
    private BindValidationException exception;

    public ValidationBindHandler(Validator... validators) {
        this.boundTypes = new LinkedHashMap();
        this.boundResults = new LinkedHashMap();
        this.boundProperties = new LinkedHashSet();
        this.validators = validators;
    }

    public ValidationBindHandler(BindHandler parent, Validator... validators) {
        super(parent);
        this.boundTypes = new LinkedHashMap();
        this.boundResults = new LinkedHashMap();
        this.boundProperties = new LinkedHashSet();
        this.validators = validators;
    }

    @Override // org.springframework.boot.context.properties.bind.AbstractBindHandler, org.springframework.boot.context.properties.bind.BindHandler
    public <T> Bindable<T> onStart(ConfigurationPropertyName name, Bindable<T> target, BindContext context) {
        this.boundTypes.put(name, target.getType());
        return super.onStart(name, target, context);
    }

    @Override // org.springframework.boot.context.properties.bind.AbstractBindHandler, org.springframework.boot.context.properties.bind.BindHandler
    public Object onSuccess(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) {
        this.boundResults.put(name, result);
        if (context.getConfigurationProperty() != null) {
            this.boundProperties.add(context.getConfigurationProperty());
        }
        return super.onSuccess(name, target, context, result);
    }

    @Override // org.springframework.boot.context.properties.bind.AbstractBindHandler, org.springframework.boot.context.properties.bind.BindHandler
    public Object onFailure(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Exception error) throws Exception {
        Object result = super.onFailure(name, target, context, error);
        if (result != null) {
            clear();
            this.boundResults.put(name, result);
        }
        validate(name, target, context, result);
        return result;
    }

    private void clear() {
        this.boundTypes.clear();
        this.boundResults.clear();
        this.boundProperties.clear();
        this.exception = null;
    }

    @Override // org.springframework.boot.context.properties.bind.AbstractBindHandler, org.springframework.boot.context.properties.bind.BindHandler
    public void onFinish(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) throws Exception {
        validate(name, target, context, result);
        super.onFinish(name, target, context, result);
    }

    private void validate(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) {
        if (this.exception == null) {
            Object validationTarget = getValidationTarget(target, context, result);
            Class<?> validationType = target.getBoxedType().resolve();
            if (validationTarget != null) {
                validateAndPush(name, validationTarget, validationType);
            }
        }
        if (context.getDepth() == 0 && this.exception != null) {
            throw this.exception;
        }
    }

    private Object getValidationTarget(Bindable<?> target, BindContext context, Object result) {
        if (result != null) {
            return result;
        }
        if (context.getDepth() == 0 && target.getValue() != null) {
            return target.getValue().get();
        }
        return null;
    }

    private void validateAndPush(ConfigurationPropertyName name, Object target, Class<?> type) {
        Validator[] validatorArr;
        ValidationResult result = null;
        for (Validator validator : this.validators) {
            if (validator.supports(type)) {
                result = result != null ? result : new ValidationResult(name, target);
                validator.validate(target, result);
            }
        }
        if (result != null && result.hasErrors()) {
            this.exception = new BindValidationException(result.getValidationErrors());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/validation/ValidationBindHandler$ValidationResult.class */
    public class ValidationResult extends AbstractBindingResult {
        private final ConfigurationPropertyName name;
        private Object target;

        protected ValidationResult(ConfigurationPropertyName name, Object target) {
            super(null);
            this.name = name;
            this.target = target;
        }

        @Override // org.springframework.validation.AbstractBindingResult, org.springframework.validation.Errors
        public String getObjectName() {
            return this.name.toString();
        }

        @Override // org.springframework.validation.AbstractBindingResult, org.springframework.validation.BindingResult
        public Object getTarget() {
            return this.target;
        }

        @Override // org.springframework.validation.AbstractBindingResult, org.springframework.validation.AbstractErrors, org.springframework.validation.Errors
        public Class<?> getFieldType(String field) {
            try {
                ResolvableType type = (ResolvableType) ValidationBindHandler.this.boundTypes.get(getName(field));
                Class<?> resolved = type != null ? type.resolve() : null;
                if (resolved != null) {
                    return resolved;
                }
            } catch (Exception e) {
            }
            return super.getFieldType(field);
        }

        @Override // org.springframework.validation.AbstractBindingResult
        protected Object getActualFieldValue(String field) {
            try {
                return ValidationBindHandler.this.boundResults.get(getName(field));
            } catch (Exception e) {
                return null;
            }
        }

        private ConfigurationPropertyName getName(String field) {
            return this.name.append(DataObjectPropertyName.toDashedForm(field));
        }

        ValidationErrors getValidationErrors() {
            Set<ConfigurationProperty> boundProperties = (Set) ValidationBindHandler.this.boundProperties.stream().filter(property -> {
                return this.name.isAncestorOf(property.getName());
            }).collect(Collectors.toCollection(LinkedHashSet::new));
            return new ValidationErrors(this.name, boundProperties, getAllErrors());
        }
    }
}
