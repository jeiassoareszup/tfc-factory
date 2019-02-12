package com.example.tfc.factory.resolver.component;

import java.util.HashMap;

public final class ComponentResolverManager {

    private static HashMap<String, ComponentResolver> resolvers = new HashMap<>();

    static {
        resolvers.put("com.ibm.dse.gui.extensions.BSCHLabel", new BSCHLabelComponentResolver());
        resolvers.put("com.ibm.dse.gui.extensions.BSCHOperationPanel", new BSCHOperationPanelComponentResolver());
        resolvers.put("com.ibm.dse.gui.extensions.BSCHTextField", new BSCHTextFieldComponentResolver());
        resolvers.put("com.ibm.dse.gui.extensions.BSCHButton", new BSCHButtonComponentResolver());
        resolvers.put("com.ibm.dse.gui.extensions.BSCHTable", new BSCHTableComponentResolver());
        resolvers.put("NOT_FOUND", new NotFoundComponentResolver());
    }

    public static ComponentResolver getResolver(String component) {
        return resolvers.computeIfAbsent(component, k -> getNotFoundResolver());
    }

    private static ComponentResolver getNotFoundResolver() {
        return resolvers.get("NOT_FOUND");
    }
}
