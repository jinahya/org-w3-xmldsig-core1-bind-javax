package com.github.jinahya.org.w3.xmldsig.core1.bind;

import javax.xml.bind.JAXBContext;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.util.Objects;

final class XmlUtils {

    static <T> T unmarshal(final String name) throws Exception {
        Objects.requireNonNull(name, "name is null");
        try (var resource = XmlUtils.class.getResourceAsStream(name)) {
            assert resource != null : "null resource from " + name;
            final var factory = XMLInputFactory.newFactory();
            final var reader = factory.createFilteredReader(
                    factory.createXMLStreamReader(new StreamSource(resource)),
                    reader1 -> {
                        if (reader1.getEventType() == XMLStreamReader.CHARACTERS) {
                            return reader1.getText().trim().length() > 0;
                        }
                        return true;
                    }
            );
            final var context = JAXBContext.newInstance("org.w3._2000._09.xmldsig_");
            final var unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(reader);
        }
    }

    private XmlUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
