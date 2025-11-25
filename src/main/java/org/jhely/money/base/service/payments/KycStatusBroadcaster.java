package org.jhely.money.base.service.payments;

import org.jhely.money.base.domain.BridgeCustomer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class KycStatusBroadcaster {

    public interface Listener {
        void onUpdate(BridgeCustomer customer);
    }

    private final Map<String, List<Listener>> listenersByUser = new ConcurrentHashMap<>();

    public void register(String userId, Listener listener) {
        listenersByUser.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public void unregister(String userId, Listener listener) {
        var list = listenersByUser.get(userId);
        if (list != null) {
            list.remove(listener);
            if (list.isEmpty()) listenersByUser.remove(userId);
        }
    }

    public void broadcast(BridgeCustomer customer) {
        if (customer == null) return;
        var list = listenersByUser.get(customer.getUserId());
        if (list != null) {
            for (Listener l : list) {
                try { l.onUpdate(customer); } catch (Exception ignore) {}
            }
        }
    }
}
