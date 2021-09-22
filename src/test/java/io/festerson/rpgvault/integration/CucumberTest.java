package io.festerson.rpgvault.integration;

import io.cucumber.spring.CucumberContextConfiguration;
import io.festerson.rpgvault.Application;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberTest {

}
