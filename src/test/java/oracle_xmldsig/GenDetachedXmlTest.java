package oracle_xmldsig;

import com.github.jinahya.org.w3._2000._09.xmldsig_.KeyInfoTypeHelper;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.w3._2000._09.xmldsig_.DSAKeyValueType;
import org.w3._2000._09.xmldsig_.KeyValueType;
import org.w3._2000._09.xmldsig_.SignatureType;
import org.w3._2000._09.xmldsig_.SignatureValueType;

import javax.xml.bind.JAXBElement;

import static org.assertj.core.api.Assertions.assertThat;

class GenDetachedXmlTest {

    @Test
    void unmarshal__() throws Exception {
        final var signature = OracleXmldsigUtils.unmarshal("GenDetached.xml", SignatureType.class);
        assertThat(signature).isNotNull().satisfies(s -> {
            assertThat(s.getSignedInfo()).as("//:signedInfo").isNotNull().satisfies(si -> {
                assertThat(si.getCanonicalizationMethod().getAlgorithm())
                        .isEqualTo("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
                assertThat(si.getSignatureMethod().getAlgorithm())
                        .isEqualTo("http://www.w3.org/2000/09/xmldsig#dsa-sha1");
                assertThat(si.getReference())
                        .singleElement()
                        .satisfies(r -> {
                            assertThat(r.getURI()).isEqualTo("http://www.w3.org/TR/xml-stylesheet");
                            assertThat(r.getDigestMethod().getAlgorithm()).isEqualTo("http://www.w3.org/2000/09/xmldsig#sha1");
                            assertThat(r.getDigestValue())
                                    .asBase64Encoded()
                                    .isEqualTo("ITIkrxsTd8x8DE+Ui4ZehAiisH4=");
                        });
            });
            assertThat(s.getSignatureValue())
                    .isNotNull()
                    .extracting(SignatureValueType::getValue, InstanceOfAssertFactories.BYTE_ARRAY)
                    .asBase64Encoded()
                    .isEqualTo("QLR8WiDQ6w9zLfdpe4SBDuj9KRUIGcGg+RsHRwPG6v7lQXVt2SEtdg==");
            assertThat(s.getKeyInfo()).isNotNull().satisfies(ki -> {
                assertThat(KeyInfoTypeHelper.getKeyValueTypeList(ki))
                        .singleElement()
                        .extracting(KeyValueType::getContent, InstanceOfAssertFactories.LIST)
                        .singleElement()
                        .satisfies(se -> {
                            assertThat(((JAXBElement<DSAKeyValueType>) se).getValue()).satisfies(dsakv -> {
                                assertThat(dsakv.getP())
                                        .isNotNull().asBase64Encoded()
                                        .isEqualTo("/KaCzo4Syrom78z3EQ5SbbB4sF7ey80etKII864WF64B81uRpH5t9jQTxeEu0ImbzRMqzVDZkVG9xD7nN1kuFw==");
                                assertThat(dsakv.getQ())
                                        .isNotNull().asBase64Encoded()
                                        .isEqualTo("li7dzDacuo67Jg7mtqEm2TRuOMU=");
                                assertThat(dsakv.getG())
                                        .isNotNull().asBase64Encoded()
                                        .isEqualTo("Z4Rxsnqc9E7pGknFFH2xqaryRPBaQ01khpMdLRQnG541Awtx/XPaF5Bpsy4pNWMOHCBiNU0NogpsQW5QvnlMpA==");
                                assertThat(dsakv.getY())
                                        .isNotNull().asBase64Encoded()
                                        .isEqualTo("0LHXqLr4xyaImiEbwc9qPpsG5uDEAQWvjVJhf5DaSsFSuLqIzyA5d3U/G6XBcl4M0pwWqVYMfC+QXYLC9Axa7w==");
                            });
                        });
            });
        });
    }
}
