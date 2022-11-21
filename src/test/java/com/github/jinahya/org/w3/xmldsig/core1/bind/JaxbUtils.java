package com.github.jinahya.org.w3.xmldsig.core1.bind;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Supplier;

public final class JaxbUtils {

    public static JAXBContext context() throws JAXBException {
        return JAXBContext.newInstance("org.w3._2000._09.xmldsig_");
    }

    @SuppressWarnings({
            "unchecked"
    })
    static <T> T unmarshal(final Supplier<? extends InputStream> supplier) throws Exception {
        Objects.requireNonNull(supplier, "supplier is null");
        try (var resource = supplier.get()) {
            assert resource != null;
            final var factory = XMLInputFactory.newFactory();
            final var reader = factory.createFilteredReader(
                    factory.createXMLStreamReader(new StreamSource(resource)),
                    reader1 -> {
                        if (reader1.getEventType() == XMLStreamReader.CHARACTERS) {
                            return !reader1.getText().isBlank();
                        }
                        return true;
                    }
            );
            final var context = context();
            final var unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(reader);
        }
    }

    private JaxbUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
