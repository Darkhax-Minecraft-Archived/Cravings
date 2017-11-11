package net.darkhax.cravings;

import net.darkhax.bookshelf.lib.LoggingHelper;
import net.darkhax.bookshelf.lib.WeightedSelector;
import net.darkhax.cravings.craving.ICraving;
import net.darkhax.cravings.handler.ConfigurationHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "cravings", name = "Cravings", version = "@VERSION@", dependencies = "", certificateFingerprint = "@FINGERPRINT@")
public class Cravings {

    /**
     * Logger for the mod. Other mods should not use this!
     */
    public static final LoggingHelper LOG = new LoggingHelper("Cravings");

    /**
     * The weighted registry for cravings. All cravings should be registered here. Cravings can
     * be registered at any time, including game/world runtime, however the init phase is the
     * recommended time to do this.
     */
    public static final WeightedSelector<ICraving> CRAVING_REGISTRY = new WeightedSelector<>();

    /**
     * Configuration instance. Can be used to check fields, but they're usually static.
     */
    public static ConfigurationHandler config;

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        config = new ConfigurationHandler(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init (FMLInitializationEvent event) {

        // Register stuff here
    }

    @EventHandler
    public void onFingerprintViolation (FMLFingerprintViolationEvent event) {

        LOG.warn("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }
}