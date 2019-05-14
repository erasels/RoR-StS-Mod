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
import riskOfSpire.RiskOfSpire;
import riskOfSpire.util.RiskOfRainRelicHelper;
import riskOfSpire.util.RiskOfSpireSavedata;

import java.util.ArrayList;
import java.util.HashMap;

import static riskOfSpire.RiskOfSpire.logger;

public class RelicData {
    //Keys
    private static final String rorRelicRngCountID = "RISK_OF_SPIRE_RELIC_RNG_COUNT";
    private static final String rorCommonPoolID = "RISK_OF_SPIRE_COMMON_POOL";
    private static final String rorUncommonPoolID = "RISK_OF_SPIRE_UNCOMMON_POOL";
    private static final String rorRarePoolID = "RISK_OF_SPIRE_RARE_POOL";

    //Data
    public static int rorRelicRngCount = 0;
    public static ArrayList<String> rorCommonPool = new ArrayList<>();
    public static ArrayList<String> rorUncommonPool = new ArrayList<>();
    public static ArrayList<String> rorRarePool = new ArrayList<>();

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
            rorRelicRngCount = RiskOfRainRelicHelper.RiskOfRainRelicRng.counter;
            rorCommonPool.clear();
            rorCommonPool.addAll(RiskOfSpire.rorCommonRelicPool);
            rorUncommonPool.clear();
            rorUncommonPool.addAll(RiskOfSpire.rorUncommonRelicPool);
            rorRarePool.clear();
            rorRarePool.addAll(RiskOfSpire.rorRareRelicPool);
            logger.info("Saved Risk Of Rain Relic RNG Counter: " + rorRelicRngCount);

            StringBuilder sb = new StringBuilder("Saved RiskOfSpire Relic pools:");
            sb.append("\nCommon relics: ");
            for (String s : rorCommonPool)
            {
                sb.append(s).append(" ");
            }
            sb.append("\nUncommon relics: ");
            for (String s : rorUncommonPool)
            {
                sb.append(s).append(" ");
            }
            sb.append("\nRare relics: ");
            for (String s : rorRarePool)
            {
                sb.append(s).append(" ");
            }
            logger.info(sb.toString());
        }
    }

    //Ensure data is loaded/generated
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "loadSave"
    )
    public static class loadPools
    {
        @SpirePostfixPatch
        public static void loadRelicPools(AbstractDungeon __instance, SaveFile file)
        {
            RiskOfSpire.rorCommonRelicPool = new ArrayList<>(rorCommonPool);
            RiskOfSpire.rorUncommonRelicPool = new ArrayList<>(rorUncommonPool);
            RiskOfSpire.rorRareRelicPool = new ArrayList<>(rorRarePool);

            StringBuilder sb = new StringBuilder("Loaded RiskOfSpire Relic pools:");
            sb.append("\nCommon relics: ");
            for (String s : rorCommonPool)
            {
                sb.append(s).append(" ");
            }
            sb.append("\nUncommon relics: ");
            for (String s : rorUncommonPool)
            {
                sb.append(s).append(" ");
            }
            sb.append("\nRare relics: ");
            for (String s : rorRarePool)
            {
                sb.append(s).append(" ");
            }
            logger.info(sb.toString());
        }
    }

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
            params.put(rorCommonPoolID, rorCommonPool);
            params.put(rorUncommonPoolID, rorUncommonPool);
            params.put(rorRarePoolID, rorRarePool);
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
                rorCommonPool = data.RISK_OF_SPIRE_COMMON_POOL;
                rorUncommonPool = data.RISK_OF_SPIRE_UNCOMMON_POOL;
                rorRarePool = data.RISK_OF_SPIRE_RARE_POOL;

                if (rorCommonPool == null)
                    rorCommonPool = new ArrayList<>();
                if (rorUncommonPool == null)
                    rorUncommonPool = new ArrayList<>();
                if (rorRarePool == null)
                    rorRarePool = new ArrayList<>();
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
