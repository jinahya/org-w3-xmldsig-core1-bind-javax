package com.github.jinahya.org.w3.xmldsig.core1.bind;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class XmlDsigCore1SchemaFactoryTest {

    private static Set<String> nameSet() {
        return XmlDsigCore1SchemaFactory.RESOURCE_NAMES.keySet();
    }

    @MethodSource("nameSet")
    @ParameterizedTest
    void getSchema__(final String name) {
        final var schema = XmlDsigCore1SchemaFactory.getSchema(name);
        assertThat(schema).isNotNull();
    }
}
