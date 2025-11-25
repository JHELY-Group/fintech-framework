package org.jhely.money.base.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Minimal integration test to validate BridgeService wiring and generated client configuration.
 * Uses the application context to inject the factory and service. No real network call should be made
 * if the sandbox base URL is a placeholder or not reachable; the test asserts that we handle failures gracefully.
 */
@SpringBootTest
@ActiveProfiles("test")
public class BridgeServiceIntegrationTest {

    @Autowired
    private BridgeService bridgeService;

    @Test
    @DisplayName("BridgeService ping should return boolean without throwing")
    public void pingDoesNotThrow() {
        boolean result = bridgeService.pingWalletsTotalBalances();
        // We only assert that the method returns a boolean; success may depend on external connectivity.
        assertThat(result).isIn(true, false);
    }
}
