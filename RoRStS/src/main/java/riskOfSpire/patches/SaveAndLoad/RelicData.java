package riskOfSpire.patches.SaveAndLoad;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CtBehavior;
import riskOfSpire.util.RiskOfRainRelicHelper;
import riskOfSpire.util.RiskOfSpireSavedata;

import java.util.HashMap;

import static riskOfSpire.RiskOfSpire.logger;

public class RelicData {
    //Keys
    private static final String rorRelicRngCountID = "RISK_OF_SPIRE_RELIC_RNG_COUNT";

    //Data
    public static int rorRelicRngCount = 0;

    //Save data whenever SaveFile is constructed
    @SpirePatch(
            clz = SaveFile.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = { SaveFile.SaveType.class }
    )
    public static class SaveTheSaveData
    {
        @SpirePostfixPatch
        public static void saveAllTheSaveData(SaveFile __instance, SaveFile.SaveType type)
        {
            /* Load save data from custom relic pools
            SaveData.rorCommonRelics =
            */
            rorRelicRngCount = RiskOfRainRelicHelper.RiskOfRainRelicRng.counter;
            logger.info("Saved Risk Of Rain Relic RNG Counter: " + rorRelicRngCount);
        }
    }

    //Ensure seed is loaded/generated along with other seeds
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "loadSeeds"
    )
    public static class loadRandom
    {
        @SpirePostfixPatch
        public static void loadSeed(SaveFile file)
        {
            //Actual data is loaded in LoadDataFromFile; this just sets it afterwards to ensure it is saved again properly.
            RiskOfRainRelicHelper.RiskOfRainRelicRng = new Random(Settings.seed, rorRelicRngCount);
            logger.info("Loaded Risk Of Rain Relic RNG Counter: " + rorRelicRngCount);
        }
    }
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "generateSeeds"
    )
    public static class generateSeeds
    {
        @SpirePostfixPatch
        public static void generate()
        {
            logger.info("Generated Risk of Rain Relic Random with seed " + Settings.seed.toString());
            RiskOfRainRelicHelper.RiskOfRainRelicRng = new Random(Settings.seed);
        }
    }

    @SpirePatch(
            clz=SaveAndContinue.class,
            method="save",
            paramtypez={ SaveFile.class }
    )
    public static class SaveDataToFile
    {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = { "params" }
        )
        public static void addCustomSaveData(SaveFile save, HashMap<Object, Object> params)
        {
            params.put(rorRelicRngCountID, rorRelicRngCount);
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GsonBuilder.class, "create");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz=SaveAndContinue.class,
            method="loadSaveFile",
            paramtypez = { String.class }
    )
    public static class LoadDataFromFile
    {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = { "gson", "savestr" }
        )
        public static void loadCustomSaveData(String path, Gson gson, String savestr)
        {
            try
            {
                RiskOfSpireSavedata data = gson.fromJson(savestr, RiskOfSpireSavedata.class);

                rorRelicRngCount = data.RISK_OF_SPIRE_RELIC_RNG_COUNT;
            }
            catch (Exception e)
            {
                logger.error("Failed to load RoRStS savedata: " + e.toString());
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Gson.class, "fromJson");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
