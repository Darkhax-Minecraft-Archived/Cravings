package net.darkhax.cravings;

import net.darkhax.bookshelf.lib.LoggingHelper;
import net.darkhax.bookshelf.lib.WeightedSelectorRegistry;
import net.darkhax.bookshelf.network.NetworkHandler;
import net.darkhax.cravings.craving.ICraving;
import net.darkhax.cravings.handler.ConfigurationHandler;
import net.darkhax.cravings.handler.CravingDataHandler;
import net.darkhax.cravings.network.PacketRequestClientSync;
import net.darkhax.cravings.network.PacketSyncClient;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "cravings", name = "Cravings", version = "@VERSION@", dependencies = "", certificateFingerprint = "@FINGERPRINT@")
public class Cravings {

    /**
     * Logger for the mod. Other mods should not use this!
     */
    public static final LoggingHelper LOG = new LoggingHelper("Cravings");

    /**
     * The network handler for cravings.
     */
    public static final NetworkHandler NETWORK = new NetworkHandler("cravings");

    /**
     * The weighted registry for cravings. All cravings should be registered here. Cravings can
     * be registered at any time, including game/world runtime, however the init phase is the
     * recommended time to do this.
     */
    public static final WeightedSelectorRegistry<ICraving> CRAVING_REGISTRY = new WeightedSelectorRegistry<>();

    /**
     * Configuration instance. Can be used to check fields, but they're usually static.
     */
    public static ConfigurationHandler config;

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        NETWORK.register(PacketRequestClientSync.class, Side.SERVER);
        NETWORK.register(PacketSyncClient.class, Side.CLIENT);
        config = new ConfigurationHandler(event.getSuggestedConfigurationFile());
        CravingDataHandler.init();
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