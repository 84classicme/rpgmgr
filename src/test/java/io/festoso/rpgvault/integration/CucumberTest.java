package io.festoso.rpgvault.integration;

import io.cucumber.spring.CucumberContextConfiguration;
import io.festoso.rpgvault.Application;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberTest {
}
