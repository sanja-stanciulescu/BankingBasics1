package org.poo.app;

import java.util.HashMap;
import java.util.Map;

public class IBANRegistry {
    private final Map<String, String> ibanAliases = new HashMap<String, String>();

    public void registerIBAN(String iban, String alias) {
        if (iban == null || iban.trim().isEmpty() || alias == null || alias.trim().isEmpty()) {
            System.out.println("IBAN cannot be null or empty");
            return;
        }

        ibanAliases.put(alias, iban);
    }

    public boolean updateAlias(String currentIdentifier, String newAlias) {
        if (currentIdentifier == null || newAlias == null) {
            return false;
        }

        String existingIban = getIBAN(currentIdentifier);

        if (existingIban == null) {
            return false;
        }

        if (!removeIBAN(currentIdentifier)) {
            return false;
        }
        registerIBAN(existingIban, newAlias);

        return true;
    }

    public String getIBAN(String identifier) {
        if (ibanAliases.containsValue(identifier)) {
            return identifier;
        }

        if (ibanAliases.containsKey(identifier)) {
            return ibanAliases.get(identifier);
        }

        return null;
    }

    public boolean removeIBAN(String identifier) {
        if (identifier == null) {
            return false;
        }

        return ibanAliases.remove(identifier) != null;
    }


}
