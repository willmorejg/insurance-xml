/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

James G Willmore - LJ Computing - (C) 2023
*/
package net.ljcomputing.insurancexml.configuration;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.Marshaller;
import net.ljcomputing.insurancexml.validation.GlobalEventHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class OxmConfiguration {
    @Value("classpath:schema/policy.xsd")
    private Resource policySchema;

    @Bean
    public Map<String, Object> jaxbMarshallerProperties() {
        final Map<String, Object> map = new HashMap<>();
        map.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        return map;
    }

    @Bean
    public Jaxb2Marshaller policyMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("net.ljcomputing.insurancexml.domain");
        marshaller.setMarshallerProperties(jaxbMarshallerProperties());
        marshaller.setValidationEventHandler(new GlobalEventHandler());
        marshaller.setSchema(policySchema);
        return marshaller;
    }
}
