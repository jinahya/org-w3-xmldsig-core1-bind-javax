package com.github.jinahya.org.w3.xmldsig.core1.bind;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A factory class for obtaining schema instances related to this module.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class XmlDsigCore1SchemaFactory {

    /**
     * The name for getting the schema targeting both {@value XmlDsigCore1XmlConstants#XML_NS_URI_XMLDSIG} and
     * {@value XmlDsigCore1XmlConstants#XML_NS_URI_XMLDSIG11}.
     */
    private static final String NAME_XMLDSIG1 = "xmldsig1";

    /**
     * The name for getting the schema targeting {@value XmlDsigCore1XmlConstants#XML_NS_URI_XMLDSIG}.
     */
    private static final String NAME_XMLDSIG_CORE = "xmldsig-core";

    static final Map<String, String> RESOURCE_NAMES;

    static {
        final Map<String, String> names = new HashMap<>();
        names.put(NAME_XMLDSIG1, "/xmldsig1-schema.xsd");
        names.put(NAME_XMLDSIG_CORE, "/xmldsig-core-schema.xsd");
        RESOURCE_NAMES = Collections.unmodifiableMap(names);
    }

    private static final Map<URI, Schema> SCHEMA_INSTANCES = new ConcurrentHashMap<>();

    /**
     * Returns the schema of specified name.
     *
     * @param name the name of the schema.
     * @return the schema of the {@code name}.
     * @see #NAME_XMLDSIG1
     * @see #NAME_XMLDSIG_CORE
     */
    @SuppressWarnings({
            "java:S112" // RuntimeException
    })
    public static Schema getSchema(final String name) {
        final String resourceName = RESOURCE_NAMES.get(Objects.requireNonNull(name, "name is null"));
        if (resourceName == null) {
            throw new IllegalArgumentException("unknown name: " + name);
        }
        final URL resource = XmlDsigCore1SchemaFactory.class.getResource(resourceName);
        assert resource != null;
        try {
            return SCHEMA_INSTANCES.computeIfAbsent(resource.toURI(), k -> {
                // The SchemaFactory class is not thread-safe!
                final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                try {
                    return factory.newSchema(k.toURL()); // immutable
                } catch (final MalformedURLException murle) {
                    throw new RuntimeException("failed to convert " + k + " to a URL", murle);
                } catch (final SAXException saxe) {
                    throw new RuntimeException("failed to create a schema from " + k, saxe); // NOSONAR
                }
            });
        } catch (final URISyntaxException urise) {
            throw new RuntimeException("failed to convert " + resource + " to a URI", urise);
        }
    }

    private XmlDsigCore1SchemaFactory() {
        throw new AssertionError("instantiation is not allowed");
    }
}
