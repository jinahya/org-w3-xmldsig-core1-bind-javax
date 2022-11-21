package com.github.jinahya.org.w3._2000._09.xmldsig_;

import org.w3._2000._09.xmldsig_.KeyInfoType;
import org.w3._2000._09.xmldsig_.KeyValueType;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class KeyInfoTypeHelper {

    static Stream<KeyValueType> getKeyValueTypeStream(final KeyInfoType keyInfoType) {
        Objects.requireNonNull(keyInfoType, "keyInfoType is null");
        return keyInfoType.getContent()
                .stream()
                .filter(JAXBElement.class::isInstance)
                .map(e -> ((JAXBElement<?>) e).getValue())
                .filter(KeyValueType.class::isInstance)
                .map(KeyValueType.class::cast);
    }

    public static <R, A> R getKeyValueTypeCollection(final KeyInfoType keyInfoType,
                                              final Collector<? super KeyValueType, A, R> collector) {
        Objects.requireNonNull(collector, "collector is null");
        return getKeyValueTypeStream(keyInfoType)
                .collect(collector);
    }

    public static List<KeyValueType> getKeyValueTypeList(final KeyInfoType keyInfoType) {
        return getKeyValueTypeCollection(keyInfoType, Collectors.toList());
    }

//    public static List<DSAKeyValueType> getDSAKeyValueTypeList(final KeyInfoType keyInfoType) {
//        return collectKeyValueType(keyInfoType, DSAKeyValueType.class, Collectors.toList());
//    }

    private KeyInfoTypeHelper() {
        throw new AssertionError("instantiation is not allowed");
    }
}
