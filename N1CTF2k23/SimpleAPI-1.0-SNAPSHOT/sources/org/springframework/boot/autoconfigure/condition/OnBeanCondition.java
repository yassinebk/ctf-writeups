package org.springframework.boot.autoconfigure.condition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.FilteringSpringBootCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotationCollectors;
import org.springframework.core.annotation.MergedAnnotationPredicates;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
@Order(Integer.MAX_VALUE)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/condition/OnBeanCondition.class */
class OnBeanCondition extends FilteringSpringBootCondition implements ConfigurationCondition {
    OnBeanCondition() {
    }

    @Override // org.springframework.context.annotation.ConfigurationCondition
    public ConfigurationCondition.ConfigurationPhase getConfigurationPhase() {
        return ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN;
    }

    @Override // org.springframework.boot.autoconfigure.condition.FilteringSpringBootCondition
    protected final ConditionOutcome[] getOutcomes(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        ConditionOutcome[] outcomes = new ConditionOutcome[autoConfigurationClasses.length];
        for (int i = 0; i < outcomes.length; i++) {
            String autoConfigurationClass = autoConfigurationClasses[i];
            if (autoConfigurationClass != null) {
                Set<String> onBeanTypes = autoConfigurationMetadata.getSet(autoConfigurationClass, "ConditionalOnBean");
                outcomes[i] = getOutcome(onBeanTypes, ConditionalOnBean.class);
                if (outcomes[i] == null) {
                    Set<String> onSingleCandidateTypes = autoConfigurationMetadata.getSet(autoConfigurationClass, "ConditionalOnSingleCandidate");
                    outcomes[i] = getOutcome(onSingleCandidateTypes, ConditionalOnSingleCandidate.class);
                }
            }
        }
        return outcomes;
    }

    private ConditionOutcome getOutcome(Set<String> requiredBeanTypes, Class<? extends Annotation> annotation) {
        List<String> missing = filter(requiredBeanTypes, FilteringSpringBootCondition.ClassNameFilter.MISSING, getBeanClassLoader());
        if (!missing.isEmpty()) {
            ConditionMessage message = ConditionMessage.forCondition(annotation, new Object[0]).didNotFind("required type", "required types").items(ConditionMessage.Style.QUOTE, missing);
            return ConditionOutcome.noMatch(message);
        }
        return null;
    }

    @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConditionMessage matchMessage = ConditionMessage.empty();
        MergedAnnotations annotations = metadata.getAnnotations();
        if (annotations.isPresent(ConditionalOnBean.class)) {
            Spec<ConditionalOnBean> spec = new Spec<>(context, metadata, annotations, ConditionalOnBean.class);
            MatchResult matchResult = getMatchingBeans(context, spec);
            if (!matchResult.isAllMatched()) {
                String reason = createOnBeanNoMatchReason(matchResult);
                return ConditionOutcome.noMatch(spec.message().because(reason));
            }
            matchMessage = spec.message(matchMessage).found("bean", DefaultBeanDefinitionDocumentReader.NESTED_BEANS_ELEMENT).items(ConditionMessage.Style.QUOTE, matchResult.getNamesOfAllMatches());
        }
        if (metadata.isAnnotated(ConditionalOnSingleCandidate.class.getName())) {
            Spec<ConditionalOnSingleCandidate> spec2 = new SingleCandidateSpec(context, metadata, annotations);
            MatchResult matchResult2 = getMatchingBeans(context, spec2);
            if (!matchResult2.isAllMatched()) {
                return ConditionOutcome.noMatch(spec2.message().didNotFind("any beans").atAll());
            }
            if (!hasSingleAutowireCandidate(context.getBeanFactory(), matchResult2.getNamesOfAllMatches(), spec2.getStrategy() == SearchStrategy.ALL)) {
                return ConditionOutcome.noMatch(spec2.message().didNotFind("a primary bean from beans").items(ConditionMessage.Style.QUOTE, matchResult2.getNamesOfAllMatches()));
            }
            matchMessage = spec2.message(matchMessage).found("a primary bean from beans").items(ConditionMessage.Style.QUOTE, matchResult2.getNamesOfAllMatches());
        }
        if (metadata.isAnnotated(ConditionalOnMissingBean.class.getName())) {
            Spec<ConditionalOnMissingBean> spec3 = new Spec<>(context, metadata, annotations, ConditionalOnMissingBean.class);
            MatchResult matchResult3 = getMatchingBeans(context, spec3);
            if (matchResult3.isAnyMatched()) {
                String reason2 = createOnMissingBeanNoMatchReason(matchResult3);
                return ConditionOutcome.noMatch(spec3.message().because(reason2));
            }
            matchMessage = spec3.message(matchMessage).didNotFind("any beans").atAll();
        }
        return ConditionOutcome.match(matchMessage);
    }

    protected final MatchResult getMatchingBeans(ConditionContext context, Spec<?> spec) {
        ClassLoader classLoader = context.getClassLoader();
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        boolean considerHierarchy = spec.getStrategy() != SearchStrategy.CURRENT;
        Set<Class<?>> parameterizedContainers = spec.getParameterizedContainers();
        if (spec.getStrategy() == SearchStrategy.ANCESTORS) {
            BeanFactory parent = beanFactory.getParentBeanFactory();
            Assert.isInstanceOf(ConfigurableListableBeanFactory.class, parent, "Unable to use SearchStrategy.ANCESTORS");
            beanFactory = (ConfigurableListableBeanFactory) parent;
        }
        MatchResult result = new MatchResult();
        Set<String> beansIgnoredByType = getNamesOfBeansIgnoredByType(classLoader, beanFactory, considerHierarchy, spec.getIgnoredTypes(), parameterizedContainers);
        for (String type : spec.getTypes()) {
            Collection<String> typeMatches = getBeanNamesForType(classLoader, considerHierarchy, beanFactory, type, parameterizedContainers);
            typeMatches.removeAll(beansIgnoredByType);
            if (typeMatches.isEmpty()) {
                result.recordUnmatchedType(type);
            } else {
                result.recordMatchedType(type, typeMatches);
            }
        }
        for (String annotation : spec.getAnnotations()) {
            Set<String> annotationMatches = getBeanNamesForAnnotation(classLoader, beanFactory, annotation, considerHierarchy);
            annotationMatches.removeAll(beansIgnoredByType);
            if (annotationMatches.isEmpty()) {
                result.recordUnmatchedAnnotation(annotation);
            } else {
                result.recordMatchedAnnotation(annotation, annotationMatches);
            }
        }
        for (String beanName : spec.getNames()) {
            if (beansIgnoredByType.contains(beanName) || !containsBean(beanFactory, beanName, considerHierarchy)) {
                result.recordUnmatchedName(beanName);
            } else {
                result.recordMatchedName(beanName);
            }
        }
        return result;
    }

    private Set<String> getNamesOfBeansIgnoredByType(ClassLoader classLoader, ListableBeanFactory beanFactory, boolean considerHierarchy, Set<String> ignoredTypes, Set<Class<?>> parameterizedContainers) {
        Set<String> result = null;
        for (String ignoredType : ignoredTypes) {
            Collection<String> ignoredNames = getBeanNamesForType(classLoader, considerHierarchy, beanFactory, ignoredType, parameterizedContainers);
            result = addAll(result, ignoredNames);
        }
        return result != null ? result : Collections.emptySet();
    }

    private Set<String> getBeanNamesForType(ClassLoader classLoader, boolean considerHierarchy, ListableBeanFactory beanFactory, String type, Set<Class<?>> parameterizedContainers) throws LinkageError {
        try {
            return getBeanNamesForType(beanFactory, considerHierarchy, resolve(type, classLoader), parameterizedContainers);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return Collections.emptySet();
        }
    }

    private Set<String> getBeanNamesForType(ListableBeanFactory beanFactory, boolean considerHierarchy, Class<?> type, Set<Class<?>> parameterizedContainers) {
        Set<String> result = collectBeanNamesForType(beanFactory, considerHierarchy, type, parameterizedContainers, null);
        return result != null ? result : Collections.emptySet();
    }

    private Set<String> collectBeanNamesForType(ListableBeanFactory beanFactory, boolean considerHierarchy, Class<?> type, Set<Class<?>> parameterizedContainers, Set<String> result) {
        Set<String> result2 = addAll(result, beanFactory.getBeanNamesForType(type, true, false));
        for (Class<?> container : parameterizedContainers) {
            ResolvableType generic = ResolvableType.forClassWithGenerics(container, type);
            result2 = addAll(result2, beanFactory.getBeanNamesForType(generic, true, false));
        }
        if (considerHierarchy && (beanFactory instanceof HierarchicalBeanFactory)) {
            BeanFactory parent = ((HierarchicalBeanFactory) beanFactory).getParentBeanFactory();
            if (parent instanceof ListableBeanFactory) {
                result2 = collectBeanNamesForType((ListableBeanFactory) parent, considerHierarchy, type, parameterizedContainers, result2);
            }
        }
        return result2;
    }

    private Set<String> getBeanNamesForAnnotation(ClassLoader classLoader, ConfigurableListableBeanFactory beanFactory, String type, boolean considerHierarchy) throws LinkageError {
        Set<String> result = null;
        try {
            result = collectBeanNamesForAnnotation(beanFactory, resolveAnnotationType(classLoader, type), considerHierarchy, null);
        } catch (ClassNotFoundException e) {
        }
        return result != null ? result : Collections.emptySet();
    }

    private Class<? extends Annotation> resolveAnnotationType(ClassLoader classLoader, String type) throws ClassNotFoundException {
        return resolve(type, classLoader);
    }

    private Set<String> collectBeanNamesForAnnotation(ListableBeanFactory beanFactory, Class<? extends Annotation> annotationType, boolean considerHierarchy, Set<String> result) {
        Set<String> result2 = addAll(result, beanFactory.getBeanNamesForAnnotation(annotationType));
        if (considerHierarchy) {
            BeanFactory parent = ((HierarchicalBeanFactory) beanFactory).getParentBeanFactory();
            if (parent instanceof ListableBeanFactory) {
                result2 = collectBeanNamesForAnnotation((ListableBeanFactory) parent, annotationType, considerHierarchy, result2);
            }
        }
        return result2;
    }

    private boolean containsBean(ConfigurableListableBeanFactory beanFactory, String beanName, boolean considerHierarchy) {
        if (considerHierarchy) {
            return beanFactory.containsBean(beanName);
        }
        return beanFactory.containsLocalBean(beanName);
    }

    private String createOnBeanNoMatchReason(MatchResult matchResult) {
        StringBuilder reason = new StringBuilder();
        appendMessageForNoMatches(reason, matchResult.getUnmatchedAnnotations(), "annotated with");
        appendMessageForNoMatches(reason, matchResult.getUnmatchedTypes(), "of type");
        appendMessageForNoMatches(reason, matchResult.getUnmatchedNames(), "named");
        return reason.toString();
    }

    private void appendMessageForNoMatches(StringBuilder reason, Collection<String> unmatched, String description) {
        if (!unmatched.isEmpty()) {
            if (reason.length() > 0) {
                reason.append(" and ");
            }
            reason.append("did not find any beans ");
            reason.append(description);
            reason.append(" ");
            reason.append(StringUtils.collectionToDelimitedString(unmatched, ", "));
        }
    }

    private String createOnMissingBeanNoMatchReason(MatchResult matchResult) {
        StringBuilder reason = new StringBuilder();
        appendMessageForMatches(reason, matchResult.getMatchedAnnotations(), "annotated with");
        appendMessageForMatches(reason, matchResult.getMatchedTypes(), "of type");
        if (!matchResult.getMatchedNames().isEmpty()) {
            if (reason.length() > 0) {
                reason.append(" and ");
            }
            reason.append("found beans named ");
            reason.append(StringUtils.collectionToDelimitedString(matchResult.getMatchedNames(), ", "));
        }
        return reason.toString();
    }

    private void appendMessageForMatches(StringBuilder reason, Map<String, Collection<String>> matches, String description) {
        if (!matches.isEmpty()) {
            matches.forEach(key, value -> {
                if (reason.length() > 0) {
                    reason.append(" and ");
                }
                reason.append("found beans ");
                reason.append(description);
                reason.append(" '");
                reason.append(key);
                reason.append("' ");
                reason.append(StringUtils.collectionToDelimitedString(value, ", "));
            });
        }
    }

    private boolean hasSingleAutowireCandidate(ConfigurableListableBeanFactory beanFactory, Set<String> beanNames, boolean considerHierarchy) {
        return beanNames.size() == 1 || getPrimaryBeans(beanFactory, beanNames, considerHierarchy).size() == 1;
    }

    private List<String> getPrimaryBeans(ConfigurableListableBeanFactory beanFactory, Set<String> beanNames, boolean considerHierarchy) {
        List<String> primaryBeans = new ArrayList<>();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = findBeanDefinition(beanFactory, beanName, considerHierarchy);
            if (beanDefinition != null && beanDefinition.isPrimary()) {
                primaryBeans.add(beanName);
            }
        }
        return primaryBeans;
    }

    private BeanDefinition findBeanDefinition(ConfigurableListableBeanFactory beanFactory, String beanName, boolean considerHierarchy) {
        if (beanFactory.containsBeanDefinition(beanName)) {
            return beanFactory.getBeanDefinition(beanName);
        }
        if (considerHierarchy && (beanFactory.getParentBeanFactory() instanceof ConfigurableListableBeanFactory)) {
            return findBeanDefinition((ConfigurableListableBeanFactory) beanFactory.getParentBeanFactory(), beanName, considerHierarchy);
        }
        return null;
    }

    private static Set<String> addAll(Set<String> result, Collection<String> additional) {
        if (CollectionUtils.isEmpty(additional)) {
            return result;
        }
        Set<String> result2 = result != null ? result : new LinkedHashSet<>();
        result2.addAll(additional);
        return result2;
    }

    private static Set<String> addAll(Set<String> result, String[] additional) {
        if (ObjectUtils.isEmpty((Object[]) additional)) {
            return result;
        }
        Set<String> result2 = result != null ? result : new LinkedHashSet<>();
        Collections.addAll(result2, additional);
        return result2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/condition/OnBeanCondition$Spec.class */
    public static class Spec<A extends Annotation> {
        private final ClassLoader classLoader;
        private final Class<? extends Annotation> annotationType;
        private final Set<String> names;
        private final Set<String> types;
        private final Set<String> annotations;
        private final Set<String> ignoredTypes;
        private final Set<Class<?>> parameterizedContainers;
        private final SearchStrategy strategy;

        Spec(ConditionContext context, AnnotatedTypeMetadata metadata, MergedAnnotations annotations, Class<A> annotationType) {
            MultiValueMap<String, Object> attributes = (MultiValueMap) annotations.stream(annotationType).filter(MergedAnnotationPredicates.unique((v0) -> {
                return v0.getMetaTypes();
            })).collect(MergedAnnotationCollectors.toMultiValueMap(MergedAnnotation.Adapt.CLASS_TO_STRING));
            MergedAnnotation<A> annotation = annotations.get(annotationType);
            this.classLoader = context.getClassLoader();
            this.annotationType = annotationType;
            this.names = extract(attributes, "name");
            this.annotations = extract(attributes, "annotation");
            this.ignoredTypes = extract(attributes, "ignored", "ignoredType");
            this.parameterizedContainers = resolveWhenPossible(extract(attributes, "parameterizedContainer"));
            this.strategy = (SearchStrategy) annotation.getValue("search", SearchStrategy.class).orElse(null);
            Set<String> types = extractTypes(attributes);
            BeanTypeDeductionException deductionException = null;
            if (types.isEmpty() && this.names.isEmpty()) {
                try {
                    types = deducedBeanType(context, metadata);
                } catch (BeanTypeDeductionException ex) {
                    deductionException = ex;
                }
            }
            this.types = types;
            validate(deductionException);
        }

        protected Set<String> extractTypes(MultiValueMap<String, Object> attributes) {
            return extract(attributes, "value", "type");
        }

        private Set<String> extract(MultiValueMap<String, Object> attributes, String... attributeNames) {
            if (attributes.isEmpty()) {
                return Collections.emptySet();
            }
            Set<String> result = new LinkedHashSet<>();
            for (String attributeName : attributeNames) {
                List<Object> values = (List) attributes.getOrDefault(attributeName, Collections.emptyList());
                for (Object value : values) {
                    if (value instanceof String[]) {
                        merge(result, (String[]) value);
                    } else if (value instanceof String) {
                        merge(result, (String) value);
                    }
                }
            }
            return result.isEmpty() ? Collections.emptySet() : result;
        }

        private void merge(Set<String> result, String... additional) {
            Collections.addAll(result, additional);
        }

        private Set<Class<?>> resolveWhenPossible(Set<String> classNames) {
            if (classNames.isEmpty()) {
                return Collections.emptySet();
            }
            Set<Class<?>> resolved = new LinkedHashSet<>(classNames.size());
            for (String className : classNames) {
                try {
                    resolved.add(FilteringSpringBootCondition.resolve(className, this.classLoader));
                } catch (ClassNotFoundException | NoClassDefFoundError e) {
                }
            }
            return resolved;
        }

        protected void validate(BeanTypeDeductionException ex) {
            if (!hasAtLeastOneElement(this.types, this.names, this.annotations)) {
                String message = getAnnotationName() + " did not specify a bean using type, name or annotation";
                if (ex == null) {
                    throw new IllegalStateException(message);
                }
                throw new IllegalStateException(message + " and the attempt to deduce the bean's type failed", ex);
            }
        }

        private boolean hasAtLeastOneElement(Set<?>... sets) {
            for (Set<?> set : sets) {
                if (!set.isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        protected final String getAnnotationName() {
            return "@" + ClassUtils.getShortName(this.annotationType);
        }

        private Set<String> deducedBeanType(ConditionContext context, AnnotatedTypeMetadata metadata) {
            if ((metadata instanceof MethodMetadata) && metadata.isAnnotated(Bean.class.getName())) {
                return deducedBeanTypeForBeanMethod(context, (MethodMetadata) metadata);
            }
            return Collections.emptySet();
        }

        private Set<String> deducedBeanTypeForBeanMethod(ConditionContext context, MethodMetadata metadata) {
            try {
                Class<?> returnType = getReturnType(context, metadata);
                return Collections.singleton(returnType.getName());
            } catch (Throwable ex) {
                throw new BeanTypeDeductionException(metadata.getDeclaringClassName(), metadata.getMethodName(), ex);
            }
        }

        private Class<?> getReturnType(ConditionContext context, MethodMetadata metadata) throws ClassNotFoundException, LinkageError {
            ClassLoader classLoader = context.getClassLoader();
            Class<?> returnType = FilteringSpringBootCondition.resolve(metadata.getReturnTypeName(), classLoader);
            if (isParameterizedContainer(returnType)) {
                returnType = getReturnTypeGeneric(metadata, classLoader);
            }
            return returnType;
        }

        private boolean isParameterizedContainer(Class<?> type) {
            for (Class<?> parameterizedContainer : this.parameterizedContainers) {
                if (parameterizedContainer.isAssignableFrom(type)) {
                    return true;
                }
            }
            return false;
        }

        private Class<?> getReturnTypeGeneric(MethodMetadata metadata, ClassLoader classLoader) throws ClassNotFoundException, LinkageError {
            Class<?> declaringClass = FilteringSpringBootCondition.resolve(metadata.getDeclaringClassName(), classLoader);
            Method beanMethod = findBeanMethod(declaringClass, metadata.getMethodName());
            return ResolvableType.forMethodReturnType(beanMethod).resolveGeneric(new int[0]);
        }

        private Method findBeanMethod(Class<?> declaringClass, String methodName) {
            Method method = ReflectionUtils.findMethod(declaringClass, methodName);
            if (isBeanMethod(method)) {
                return method;
            }
            Method[] candidates = ReflectionUtils.getAllDeclaredMethods(declaringClass);
            for (Method candidate : candidates) {
                if (candidate.getName().equals(methodName) && isBeanMethod(candidate)) {
                    return candidate;
                }
            }
            throw new IllegalStateException("Unable to find bean method " + methodName);
        }

        private boolean isBeanMethod(Method method) {
            return method != null && MergedAnnotations.from(method, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).isPresent(Bean.class);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public SearchStrategy getStrategy() {
            return this.strategy != null ? this.strategy : SearchStrategy.ALL;
        }

        Set<String> getNames() {
            return this.names;
        }

        Set<String> getTypes() {
            return this.types;
        }

        Set<String> getAnnotations() {
            return this.annotations;
        }

        Set<String> getIgnoredTypes() {
            return this.ignoredTypes;
        }

        Set<Class<?>> getParameterizedContainers() {
            return this.parameterizedContainers;
        }

        ConditionMessage.Builder message() {
            return ConditionMessage.forCondition(this.annotationType, this);
        }

        ConditionMessage.Builder message(ConditionMessage message) {
            return message.andCondition(this.annotationType, this);
        }

        public String toString() {
            boolean hasNames = !this.names.isEmpty();
            boolean hasTypes = !this.types.isEmpty();
            boolean hasIgnoredTypes = !this.ignoredTypes.isEmpty();
            StringBuilder string = new StringBuilder();
            string.append("(");
            if (hasNames) {
                string.append("names: ");
                string.append(StringUtils.collectionToCommaDelimitedString(this.names));
                string.append(hasTypes ? " " : "; ");
            }
            if (hasTypes) {
                string.append("types: ");
                string.append(StringUtils.collectionToCommaDelimitedString(this.types));
                string.append(hasIgnoredTypes ? " " : "; ");
            }
            if (hasIgnoredTypes) {
                string.append("ignored: ");
                string.append(StringUtils.collectionToCommaDelimitedString(this.ignoredTypes));
                string.append("; ");
            }
            string.append("SearchStrategy: ");
            string.append(this.strategy.toString().toLowerCase(Locale.ENGLISH));
            string.append(")");
            return string.toString();
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/condition/OnBeanCondition$SingleCandidateSpec.class */
    private static class SingleCandidateSpec extends Spec<ConditionalOnSingleCandidate> {
        private static final Collection<String> FILTERED_TYPES = Arrays.asList("", Object.class.getName());

        SingleCandidateSpec(ConditionContext context, AnnotatedTypeMetadata metadata, MergedAnnotations annotations) {
            super(context, metadata, annotations, ConditionalOnSingleCandidate.class);
        }

        @Override // org.springframework.boot.autoconfigure.condition.OnBeanCondition.Spec
        protected Set<String> extractTypes(MultiValueMap<String, Object> attributes) {
            Set<String> types = super.extractTypes(attributes);
            types.removeAll(FILTERED_TYPES);
            return types;
        }

        @Override // org.springframework.boot.autoconfigure.condition.OnBeanCondition.Spec
        protected void validate(BeanTypeDeductionException ex) {
            Assert.isTrue(getTypes().size() == 1, () -> {
                return getAnnotationName() + " annotations must specify only one type (got " + StringUtils.collectionToCommaDelimitedString(getTypes()) + ")";
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/condition/OnBeanCondition$MatchResult.class */
    public static final class MatchResult {
        private final Map<String, Collection<String>> matchedAnnotations;
        private final List<String> matchedNames;
        private final Map<String, Collection<String>> matchedTypes;
        private final List<String> unmatchedAnnotations;
        private final List<String> unmatchedNames;
        private final List<String> unmatchedTypes;
        private final Set<String> namesOfAllMatches;

        private MatchResult() {
            this.matchedAnnotations = new HashMap();
            this.matchedNames = new ArrayList();
            this.matchedTypes = new HashMap();
            this.unmatchedAnnotations = new ArrayList();
            this.unmatchedNames = new ArrayList();
            this.unmatchedTypes = new ArrayList();
            this.namesOfAllMatches = new HashSet();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordMatchedName(String name) {
            this.matchedNames.add(name);
            this.namesOfAllMatches.add(name);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordUnmatchedName(String name) {
            this.unmatchedNames.add(name);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordMatchedAnnotation(String annotation, Collection<String> matchingNames) {
            this.matchedAnnotations.put(annotation, matchingNames);
            this.namesOfAllMatches.addAll(matchingNames);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordUnmatchedAnnotation(String annotation) {
            this.unmatchedAnnotations.add(annotation);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordMatchedType(String type, Collection<String> matchingNames) {
            this.matchedTypes.put(type, matchingNames);
            this.namesOfAllMatches.addAll(matchingNames);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordUnmatchedType(String type) {
            this.unmatchedTypes.add(type);
        }

        boolean isAllMatched() {
            return this.unmatchedAnnotations.isEmpty() && this.unmatchedNames.isEmpty() && this.unmatchedTypes.isEmpty();
        }

        boolean isAnyMatched() {
            return (this.matchedAnnotations.isEmpty() && this.matchedNames.isEmpty() && this.matchedTypes.isEmpty()) ? false : true;
        }

        Map<String, Collection<String>> getMatchedAnnotations() {
            return this.matchedAnnotations;
        }

        List<String> getMatchedNames() {
            return this.matchedNames;
        }

        Map<String, Collection<String>> getMatchedTypes() {
            return this.matchedTypes;
        }

        List<String> getUnmatchedAnnotations() {
            return this.unmatchedAnnotations;
        }

        List<String> getUnmatchedNames() {
            return this.unmatchedNames;
        }

        List<String> getUnmatchedTypes() {
            return this.unmatchedTypes;
        }

        Set<String> getNamesOfAllMatches() {
            return this.namesOfAllMatches;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/condition/OnBeanCondition$BeanTypeDeductionException.class */
    public static final class BeanTypeDeductionException extends RuntimeException {
        private BeanTypeDeductionException(String className, String beanMethodName, Throwable cause) {
            super("Failed to deduce bean type for " + className + "." + beanMethodName, cause);
        }
    }
}
