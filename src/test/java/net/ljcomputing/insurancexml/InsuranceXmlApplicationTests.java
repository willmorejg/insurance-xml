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
package net.ljcomputing.insurancexml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.ljcomputing.insurancexml.domain.Address;
import net.ljcomputing.insurancexml.domain.AddressType;
import net.ljcomputing.insurancexml.domain.Addresses;
import net.ljcomputing.insurancexml.domain.Driver;
import net.ljcomputing.insurancexml.domain.Drivers;
import net.ljcomputing.insurancexml.domain.DweillingPropertyRisk;
import net.ljcomputing.insurancexml.domain.Insured;
import net.ljcomputing.insurancexml.domain.PersonalAutoRisk;
import net.ljcomputing.insurancexml.domain.Policy;
import net.ljcomputing.insurancexml.domain.Risk;
import net.ljcomputing.insurancexml.domain.Risks;
import net.ljcomputing.insurancexml.domain.USState;
import net.ljcomputing.insurancexml.domain.Vehicle;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class InsuranceXmlApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(InsuranceXmlApplicationTests.class);

    @Autowired private Jaxb2Marshaller policyMarshaller;

    private Policy getPolicy() {
        Address physicalAddr = new Address();
        physicalAddr.setStreet1("1 Salem Road");
        physicalAddr.setCity("Smithville");
        physicalAddr.setState(USState.NJ);
        physicalAddr.setZipCode("09190");

        Address billingAddr = new Address();
        billingAddr.setType(AddressType.BILLING);
        billingAddr.setStreet1("1 Salem Road");
        billingAddr.setCity("Smithville");
        billingAddr.setState(USState.NJ);
        billingAddr.setZipCode("09190");

        Addresses addresses = new Addresses();
        addresses.getAddresses().add(physicalAddr);
        addresses.getAddresses().add(billingAddr);

        DweillingPropertyRisk dpRisk = new DweillingPropertyRisk();
        dpRisk.setAddress(physicalAddr);

        Risk risk = new Risk();
        risk.setName("DP Risk");
        risk.setDweillingPropertyRisk(dpRisk);

        Vehicle vehicle = new Vehicle();
        vehicle.setMake("Subaru");
        vehicle.setModel("Outback");
        vehicle.setYear(2000);
        vehicle.setVin("5GAKRDED0CJ396612");

        Driver driver = new Driver();
        driver.setGivenName("James");
        driver.setSurname("Willmore");
        driver.setBirthdate(LocalDate.of(1968, 1, 22));
        driver.setDlNumber("DL123 45678 90000");
        driver.setDlState(USState.NJ);

        Drivers drivers = new Drivers();
        drivers.getDrivers().add(driver);

        PersonalAutoRisk paRisk = new PersonalAutoRisk();
        paRisk.setDrivers(drivers);
        paRisk.setVehicle(vehicle);

        Risk risk2 = new Risk();
        risk2.setName("PA Risk");
        risk2.setPersonalAutoRisk(paRisk);

        Risks risks = new Risks();
        risks.getRisks().add(risk);
        risks.getRisks().add(risk2);

        Insured insured = new Insured();
        insured.setGivenName("James");
        insured.setSurname("Willmore");
        insured.setBirthdate(LocalDate.of(1968, 1, 22));

        insured.setAddresses(addresses);

        Policy policy = new Policy();
        policy.setInsured(insured);
        policy.setRisks(risks);

        return policy;
    }

    private Policy getBadPolicy() {
        Policy policy = getPolicy();
        policy.getInsured().getAddresses().getAddresses().get(0).setZipCode("AA");
        return policy;
    }

    @Test
    @Order(1)
    void marshalPolicy() {
        Policy policy = getPolicy();

        try {
            policyMarshaller.marshal(
                    policy, new StreamResult(new FileWriter("src/test/resources/out/policy.xml")));
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    @Order(2)
    void marshalBadPolicy() {
        log.info(
                "========================= {} =========================",
                "marshalBadPolicy - START");
        Policy policy = getBadPolicy();

        try {
            policyMarshaller.marshal(policy, new StreamResult(new StringWriter()));
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        } finally {
            log.info(
                    "========================= {} =========================",
                    "marshalBadPolicy -   END");
        }
    }

    @Test
    @Order(10)
    void unmarshalPolicy() {
        try {
            Policy results =
                    (Policy)
                            policyMarshaller.unmarshal(
                                    new StreamSource(
                                            new File("src/test/resources/out/policy.xml")));
            assertEquals(getPolicy(), results);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
