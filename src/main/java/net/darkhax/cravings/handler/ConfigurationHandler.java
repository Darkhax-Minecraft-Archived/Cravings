package net.darkhax.cravings.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import net.darkhax.cravings.Cravings;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ConfigurationHandler {

    public static Configuration config;

    private static final String[] DEFAULT_SATISFIED_EFFECTS = { "minecraft:speed, 60000", "minecraft:haste, 6000", "minecraft:, 6000", "minecraft:regeneration, 100, 2" };
    private static final String[] DEFUALT_UNSATISFIED_EFFECTS = { "minecraft:slowness", "minecraft:nausea", "minecraft:hunger, 100", "minecraft:weakness, 6000" };

    public static List<PotionEffect> satisfiedEffects = new ArrayList<>();
    public static List<PotionEffect> unsatisfiedEffects = new ArrayList<>();

    public static boolean applySatisfiedEffects = true;
    public static boolean applyUnsatisfiedEffects = true;
    public static int timeToSatisfy = 12000;
    public static float cravingChance = 0.05f;
    public static int ticksTillCravingAttempt = 24000;

    public ConfigurationHandler (File file) {

        config = new Configuration(file);
        this.syncConfigData();
    }

    private void syncConfigData () {

        satisfiedEffects.clear();
        unsatisfiedEffects.clear();

        for (final String entry : config.getStringList("cravingSatisfied", Configuration.CATEGORY_GENERAL, DEFAULT_SATISFIED_EFFECTS, "This list contains potion id info for the potion effects that can potentially be given to a player when they satisfy a craving. There are three total arguments, but only the first one is needed. The first argument is the id of the potion effect, this is the string id and not the numeric one! The second argument is the amount of time the player will get the buff for in ticks, default is 200 ticks (10 seconds), the last argument is the amplifier for the potion effect, default is 0. Arguments are split up using a , character and a space.")) {

            satisfiedEffects.add(this.getEffectFromString(entry));
        }

        for (final String entry : config.getStringList("cravingUnsatisfied", Configuration.CATEGORY_GENERAL, DEFUALT_UNSATISFIED_EFFECTS, "This list contains potion id info for the potion effects that can potentially be given to a player when they fail to satisfy a craving. There are three total arguments, but only the first one is needed. The first argument is the id of the potion effect, this is the string id and not the numeric one! The second argument is the amount of time the player will get the buff for in ticks, default is 200 ticks (10 seconds), the last argument is the amplifier for the potion effect, default is 0. Arguments are split up using a , character and a space.")) {

            unsatisfiedEffects.add(this.getEffectFromString(entry));
        }

        applySatisfiedEffects = config.getBoolean("applySatisfiedEffects", Configuration.CATEGORY_GENERAL, true, "If enabled, players will get benefits for satisfying cravings.");
        applyUnsatisfiedEffects = config.getBoolean("applyUnsatisfiedEffects", Configuration.CATEGORY_GENERAL, true, "If enabled, players will get a penalty for not satisfying cravings.");
        timeToSatisfy = config.getInt("timeToSatisfy", Configuration.CATEGORY_GENERAL, 12000, 0, Integer.MAX_VALUE, "The amount of time in ticks that a player has to satisfy a craving. Setting to 0 will give an infinite amount of time.");
        cravingChance = config.getFloat("cravingChance", Configuration.CATEGORY_GENERAL, 0.05f, 0f, 1f, "The % chance that a player will get a craving. 0 means the player will never get cravings, 1 means they will always get cravings, 0.67 means they have a 67% chance of getting a craving.");
        ticksTillCravingAttempt = config.getInt("ticksTillCravingAttempt", Configuration.CATEGORY_CLIENT, 24000, 0, Integer.MAX_VALUE, "The amount of ticks until the player has another chance at recieving a craving. This time will start ticking down when a player has no active craving.");

        if (config.hasChanged()) {
            config.save();
        }
    }

    private PotionEffect getEffectFromString (String effectString) {

        final String[] args = effectString.split(", ");

        // Minimum args check
        if (args.length == 1) {

            Cravings.LOG.noticableWarning(false, Arrays.asList("The config line " + effectString + " is not valid. At least two arguments are needed. Use , and a space to split args."));
            return null;
        }

        // Potion id validation
        if (!ForgeRegistries.POTIONS.containsKey(new ResourceLocation(args[0]))) {

            Cravings.LOG.noticableWarning(false, Arrays.asList("No potion found for id " + args[0]));
            return null;
        }

        final Potion effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(args[0]));

        // If duration argument is given, try to set it. Otherwise default to 200 (10 seconds)
        final int duration = args.length == 2 && NumberUtils.isCreatable(args[1]) ? NumberUtils.createInteger(args[1]) : 200;

        // If amplifier arg is given, try to set it. Otherwise default to 0 (level 1)
        final int amplifier = args.length == 3 && NumberUtils.isCreatable(args[2]) ? NumberUtils.createInteger(args[2]) : 0;

        return new PotionEffect(effect, duration, amplifier);
    }
}