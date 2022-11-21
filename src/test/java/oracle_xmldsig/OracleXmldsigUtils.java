package oracle_xmldsig;

import com.github.jinahya.org.w3.xmldsig.core1.bind.JaxbUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Objects;

final class OracleXmldsigUtils {

    static Document loadDocument(final String name) throws IOException, ParserConfigurationException, SAXException {
        try (var resource = OracleXmldsigUtils.class.getResourceAsStream(name)) {
            assert resource != null : "null resource from " + name;
            final var factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            final var builder = factory.newDocumentBuilder();
            return builder.parse(resource);
        }
    }

    static <T> T unmarshal(final String resourceName, final Class<T> typeClass) throws Exception {
        Objects.requireNonNull(resourceName, "resourceName is null");
        Objects.requireNonNull(typeClass, "typeClass is null");
        try (var resource = OracleXmldsigUtils.class.getResourceAsStream(resourceName)) {
            assert resource != null : "null resource from " + resourceName;
            return JaxbUtils.unmarshal(() -> resource, typeClass);
        }
    }

    private OracleXmldsigUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
