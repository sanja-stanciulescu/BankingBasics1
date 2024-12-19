package org.poo.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IBANRegistry {
    private Map<String, List<String>> ibanAliases;

    public IBANRegistry() {
        ibanAliases = new HashMap<String, List<String>>();
    }

    public void registerIBAN(String iban, String alias) {
        if (iban == null || iban.trim().isEmpty() || alias == null || alias.trim().isEmpty()) {
            System.out.println("IBAN cannot be null or empty");
            return;
        }

        ibanAliases.computeIfAbsent(alias, k -> new ArrayList<String>()).add(iban);
    }

    public boolean updateAlias(String currentIdentifier, String newAlias) {
        if (currentIdentifier == null || newAlias == null) {
            return false;
        }
        System.out.println("Updating IBAN: " + currentIdentifier + " to " + newAlias);
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
        if (ibanAliases.containsKey(identifier)) {
            System.out.println("Alias is " + identifier);
            return ibanAliases.get(identifier).getLast();
        }

        for (Map.Entry<String, List<String>> entry : ibanAliases.entrySet()) {
            if (entry.getValue().contains(identifier)) {
                System.out.println("Iban is " + identifier);
                return identifier;
            }
        }

        return null;
    }

    public boolean removeIBAN(String identifier) {
        if (identifier == null) {
            return false;
        }

        for (Map.Entry<String, List<String>> entry : ibanAliases.entrySet()) {
            if (entry.getValue().remove(identifier)) {
                if (entry.getValue().isEmpty()) {
                    ibanAliases.remove(entry.getKey());
                }
                return true;
            }
        }

        return false;
    }


}
