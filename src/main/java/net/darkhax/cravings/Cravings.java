package net.darkhax.cravings;

import java.io.File;

import net.darkhax.bookshelf.BookshelfRegistry;
import net.darkhax.bookshelf.lib.LoggingHelper;
import net.darkhax.bookshelf.lib.WeightedSelectorRegistry;
import net.darkhax.bookshelf.network.NetworkHandler;
import net.darkhax.cravings.command.CommandCravingTree;
import net.darkhax.cravings.craving.CravingRandomFood;
import net.darkhax.cravings.craving.ICraving;
import net.darkhax.cravings.handler.ConfigurationHandler;
import net.darkhax.cravings.handler.CravingDataHandler;
import net.darkhax.cravings.handler.SatisfactionHandler;
import net.darkhax.cravings.network.PacketRequestClientSync;
import net.darkhax.cravings.network.PacketSyncClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "cravings", name = "Cravings", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.3.511,);", certificateFingerprint = "@FINGERPRINT@")
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
        CravingDataHandler.init();
        MinecraftForge.EVENT_BUS.register(new SatisfactionHandler());
        BookshelfRegistry.addCommand(new CommandCravingTree());

        CRAVING_REGISTRY.addEntry(new CravingRandomFood(), 25, "random");
    }

    @EventHandler
    public void init (FMLInitializationEvent event) {

        config = new ConfigurationHandler(new File("cravings.cfg"));
    }

    @EventHandler
    public void onFingerprintViolation (FMLFingerprintViolationEvent event) {

        LOG.warn("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }
}