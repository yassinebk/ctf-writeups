package org.springframework.boot.env;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.boot.origin.Origin;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.boot.origin.TextResourceOrigin;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/env/OriginTrackedYamlLoader.class */
public class OriginTrackedYamlLoader extends YamlProcessor {
    private final Resource resource;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OriginTrackedYamlLoader(Resource resource) {
        this.resource = resource;
        setResources(resource);
    }

    @Override // org.springframework.beans.factory.config.YamlProcessor
    protected Yaml createYaml() {
        BaseConstructor constructor = new OriginTrackingConstructor();
        Representer representer = new Representer();
        DumperOptions dumperOptions = new DumperOptions();
        LimitedResolver resolver = new LimitedResolver();
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setAllowDuplicateKeys(false);
        return new Yaml(constructor, representer, dumperOptions, loaderOptions, resolver);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Map<String, Object>> load() {
        List<Map<String, Object>> result = new ArrayList<>();
        process(properties, map -> {
            result.add(getFlattenedMap(map));
        });
        return result;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/env/OriginTrackedYamlLoader$OriginTrackingConstructor.class */
    private class OriginTrackingConstructor extends Constructor {
        private OriginTrackingConstructor() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.yaml.snakeyaml.constructor.BaseConstructor
        public Object constructObject(Node node) {
            if (node instanceof ScalarNode) {
                if (!(node instanceof KeyScalarNode)) {
                    return constructTrackedObject(node, super.constructObject(node));
                }
            } else if (node instanceof MappingNode) {
                replaceMappingNodeKeys((MappingNode) node);
            }
            return super.constructObject(node);
        }

        private void replaceMappingNodeKeys(MappingNode node) {
            node.setValue((List) node.getValue().stream().map(KeyScalarNode::get).collect(Collectors.toList()));
        }

        private Object constructTrackedObject(Node node, Object value) {
            Origin origin = getOrigin(node);
            return OriginTrackedValue.of(getValue(value), origin);
        }

        private Object getValue(Object value) {
            return value != null ? value : "";
        }

        private Origin getOrigin(Node node) {
            Mark mark = node.getStartMark();
            TextResourceOrigin.Location location = new TextResourceOrigin.Location(mark.getLine(), mark.getColumn());
            return new TextResourceOrigin(OriginTrackedYamlLoader.this.resource, location);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/env/OriginTrackedYamlLoader$KeyScalarNode.class */
    public static class KeyScalarNode extends ScalarNode {
        KeyScalarNode(ScalarNode node) {
            super(node.getTag(), node.getValue(), node.getStartMark(), node.getEndMark(), node.getScalarStyle());
        }

        static NodeTuple get(NodeTuple nodeTuple) {
            Node keyNode = nodeTuple.getKeyNode();
            Node valueNode = nodeTuple.getValueNode();
            return new NodeTuple(get(keyNode), valueNode);
        }

        private static Node get(Node node) {
            if (node instanceof ScalarNode) {
                return new KeyScalarNode((ScalarNode) node);
            }
            return node;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/env/OriginTrackedYamlLoader$LimitedResolver.class */
    private static class LimitedResolver extends Resolver {
        private LimitedResolver() {
        }

        @Override // org.yaml.snakeyaml.resolver.Resolver
        public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
            if (tag == Tag.TIMESTAMP) {
                return;
            }
            super.addImplicitResolver(tag, regexp, first);
        }
    }
}
