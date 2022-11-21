package com.github.jinahya.org.w3.xmldsig.core1.bind;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Utilities for Jakarta XML binding.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class JaxbUtils {

    private static final String CONTEXT_PATH = "org.w3._2000._09.xmldsig_" + ':' + "org.w3._2009.xmldsig11_";

    public static JAXBContext newJaxbContext() throws JAXBException {
        return JAXBContext.newInstance(CONTEXT_PATH);
    }

    @SuppressWarnings({
            "java:S3077" // volatile
    })
    private static volatile JAXBContext jaxbContext = null; // NOSONAR

    static JAXBContext getJaxbContext() throws JAXBException {
        JAXBContext result = jaxbContext;
        if (result == null) {
            result = jaxbContext = newJaxbContext();
        }
        return result;
    }

    @SuppressWarnings({
            "unchecked",
            "java:S2755" // XmlInputFactory.newFactory
    })
    public static <T> T unmarshal(final Supplier<? extends InputStream> resourceSupplier, final Class<T> typeClass)
            throws IOException, XMLStreamException, JAXBException {
        Objects.requireNonNull(resourceSupplier, "resourceSupplier is null");
        Objects.requireNonNull(typeClass, "typeClass is null");
        try (InputStream resource = resourceSupplier.get()) {
            assert resource != null;
            final XMLInputFactory factory = XMLInputFactory.newFactory(); // NOSONAR
            final XMLStreamReader reader = factory.createFilteredReader(
                    factory.createXMLStreamReader(new StreamSource(resource)),
                    reader1 -> {
                        if (reader1.getEventType() == XMLStreamConstants.CHARACTERS) {
                            return !reader1.getText().trim().isEmpty();
                        }
                        return true;
                    }
            );
            final Unmarshaller unmarshaller = newJaxbContext().createUnmarshaller();
            return unmarshaller.unmarshal(reader, typeClass).getValue();
        }
    }

    private JaxbUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
