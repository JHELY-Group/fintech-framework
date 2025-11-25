package org.jhely.money.base.service.payments;

import org.springframework.stereotype.Component;

@Component
public class BridgeWebhookKeyProvider {
    private volatile String publicKeyPem;

    public void setPublicKeyPem(String pem) {
        this.publicKeyPem = pem;
    }

    public String getPublicKeyPem() {
        return publicKeyPem;
    }
}
