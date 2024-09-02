package dev.ftb.mods.ftbmaterials.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.function.Supplier;

public enum ModIntegrations {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(ModIntegrations.class);

    private boolean setup = false;
    private HashMap<String, Supplier<ModIntegration>> integrations = new HashMap<>();

    public void init() {
        addIntegration("thermal", Thermal::new);
        addIntegration("modern_industrialization", ModernIndustrialization::new);
        addIntegration("mekanism", Mek::new);

        setupIntegrations();
    }

    private void setupIntegrations() {
        if (setup) throw new IllegalStateException("Already setup!");
        setup = true;

        integrations.forEach((modid, integration) -> {


            try {
                integration.get().setup();
            } catch (Exception ex) {
                LOGGER.error("Failed to setup integration for mod {}", modid, ex);
            }
        });
    }

    public void addIntegration(String modid, Supplier<ModIntegration> integration) {
        integrations.put(modid, integration);
    }
}
