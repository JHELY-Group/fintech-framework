package org.jhely.money.base.service.payments;

import org.jhely.money.base.domain.BridgeAgreement;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Simple in-memory broadcaster for Bridge agreement capture events.
 * View instances register per userId; callback controller broadcasts
 * when a new agreement is saved. Replaced polling with server push.
 */
@Component
public class BridgeAgreementBroadcaster {

    public interface Listener {
        void onAgreement(BridgeAgreement agreement);
    }

    private final Map<String, List<Listener>> listeners = new ConcurrentHashMap<>();

    public void register(String userId, Listener listener) {
        listeners.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public void unregister(String userId, Listener listener) {
        List<Listener> list = listeners.get(userId);
        if (list != null) {
            list.remove(listener);
            if (list.isEmpty()) listeners.remove(userId);
        }
    }

    public void broadcast(BridgeAgreement agreement) {
        if (agreement == null) return;
        List<Listener> list = listeners.get(agreement.getUserId());
        if (list != null) {
            for (Listener l : list) {
                try { l.onAgreement(agreement); } catch (Exception ignore) {}
            }
        }
    }
}
