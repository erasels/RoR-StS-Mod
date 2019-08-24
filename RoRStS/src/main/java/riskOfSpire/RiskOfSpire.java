package riskOfSpire;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomSavable;
import basemod.abstracts.CustomSavableRaw;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.clapper.util.classutil.*;
import riskOfSpire.cards.ImpCards.*;
import riskOfSpire.patches.RewardItemTypeEnumPatch;
import riskOfSpire.patches.StartingScreen.BgChanges;
import riskOfSpire.relics.Abstracts.BaseRelic;
import riskOfSpire.relics.Abstracts.UsableRelic;
import riskOfSpire.relics.Interfaces.OnBlockClearRelic;
import riskOfSpire.rewards.LunarCacheReward;
import riskOfSpire.rewards.LunarCoinReward;
import riskOfSpire.ui.DifficultyButton;
import riskOfSpire.ui.DifficultyMeter;
import riskOfSpire.ui.LunarCoinDisplay;
import riskOfSpire.util.IDCheckDontTouchPls;
import riskOfSpire.util.RelicFilter;
import riskOfSpire.util.TextureLoader;
import riskOfSpire.util.helpers.RiskOfRainRelicHelper;
import riskOfSpire.util.helpers.RoRShrineHelper;
import riskOfSpire.vfx.titlescreen.CustomSlowTitleCloud;
import riskOfSpire.vfx.titlescreen.CustomTitleCloud;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpireInitializer
public class RiskOfSpire implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        RelicGetSubscriber,
        PostInitializeSubscriber,
        PostDungeonInitializeSubscriber,
        PostUpdateSubscriber,
        PreStartGameSubscriber,
        OnPlayerLoseBlockSubscriber{
    public static final Logger logger = LogManager.getLogger(RiskOfSpire.class.getName());
    public static final String BADGE_IMAGE = "riskOfSpireResources/images/Badge.png";
    private static final String MODNAME = "Risk Of Spire";
    private static final String AUTHOR = "erasels / Alchyr / Kiooeht / Lobbien";
    private static final String DESCRIPTION = "A mod to add the items from Risk of Rain in the context of Slay the Spire relics.";
    public static Properties ModSettings = new Properties();
    public static boolean difficultyCostSetting = true;
    public static DifficultyMeter DifficultyMeter;
    public static ArrayList<Color> COLORS = new ArrayList<>(Arrays.asList(Color.MAGENTA.cpy(), Color.WHITE.cpy(), Color.BLUE.cpy(), Color.CHARTREUSE.cpy(), Color.CORAL.cpy(), Color.CYAN.cpy(), Color.FIREBRICK.cpy(), Color.FOREST.cpy(), Color.GOLD.cpy(), Color.VIOLET.cpy()));
    //No need to track shop or boss relics.
    public static ArrayList<String> rorCommonRelics = new ArrayList<>();
    public static ArrayList<String> rorUncommonRelics = new ArrayList<>();
    public static ArrayList<String> rorRareRelics = new ArrayList<>();
    public static ArrayList<String> rorLunarRelics = new ArrayList<>();
    public static ArrayList<String> rorUsableRelics = new ArrayList<>();

    public static ArrayList<String> rorCommonRelicPool = new ArrayList<>();
    public static ArrayList<String> rorUncommonRelicPool = new ArrayList<>();
    public static ArrayList<String> rorRareRelicPool = new ArrayList<>();

    public static final int BASE_COMMONS = 3;
    public static final int BASE_UNCOMMONS = 2;
    public static final int BASE_RARES = 2;
    public static ShaderProgram GlacialShader;
    public static int lunarCoinAmount = 0;
    public static LunarCoinDisplay lCD;
    public static boolean lCacheTrigger = false;
    public static boolean clearPowers = false;
    private static String modID;

    public static final String DIFFICULTY_RELIC_COST_MOD_SETTING = "diffMoneyMod";

    public RiskOfSpire() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);

        setModID("riskOfSpire");

        logger.info("Done subscribing");

        logger.info("Adding mod settings");
        ModSettings.setProperty(DIFFICULTY_RELIC_COST_MOD_SETTING, "TRUE");
        try {
            SpireConfig config = new SpireConfig("riskOfSpire", "riskOfSpireConfig", ModSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            difficultyCostSetting = config.getBool(DIFFICULTY_RELIC_COST_MOD_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");
        //All relics are Shared relics, not character specific, so I can just do this.
        try {
            autoAddRelics();
        } catch (URISyntaxException | IllegalAccessException | InstantiationException | NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }

        logger.info("Done adding relics!");
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new ImpAetu());
        BaseMod.addCard(new ImpBava());
        BaseMod.addCard(new ImpYggo());
        BaseMod.addCard(new ImpUgorn());
        BaseMod.addCard(new ImpChir());
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        //String keywordStrings = Gdx.files.internal(assetPath("loc/" + languageSupport() + "/" +"aspiration-KeywordStrings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        String keywordStrings = Gdx.files.internal(getModID() + "Resources/localization/eng/Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {
        }.getType();

        Map<String, Keyword> keywords = gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k, v) -> {
            // Keyword word = (Keyword)v;
            logger.info("Adding Keyword - " + v.NAMES[0]);
            BaseMod.addKeyword((getModID().toLowerCase() + ":"), v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
    }

    private static void autoAddRelics() throws URISyntaxException, IllegalAccessException, InstantiationException, NotFoundException, CannotCompileException {
        ClassFinder finder = new ClassFinder();
        URL url = RiskOfSpire.class.getProtectionDomain().getCodeSource().getLocation();
        finder.add(new File(url.toURI()));

        ClassFilter filter =
                new AndClassFilter(
                        new NotClassFilter(new InterfaceOnlyClassFilter()),
                        new NotClassFilter(new AbstractClassFilter()),
                        new ClassModifiersClassFilter(Modifier.PUBLIC),
                        new RelicFilter()
                );
        Collection<ClassInfo> foundClasses = new ArrayList<>();
        finder.findClasses(foundClasses, filter);

        for (ClassInfo classInfo : foundClasses) {
            CtClass cls = Loader.getClassPool().get(classInfo.getClassName());

            boolean isRelic = false;
            CtClass superCls = cls;
            while (superCls != null) {
                superCls = superCls.getSuperclass();
                if (superCls == null) {
                    break;
                }
                if (superCls.getName().equals(AbstractRelic.class.getName())) {
                    isRelic = true;
                    break;
                }
            }
            if (!isRelic) {
                continue;
            }

            AbstractRelic r = (AbstractRelic) Loader.getClassPool().toClass(cls).newInstance();
            if (r instanceof UsableRelic && (r.tier == AbstractRelic.RelicTier.COMMON || r.tier == AbstractRelic.RelicTier.UNCOMMON || r.tier == AbstractRelic.RelicTier.RARE)) {
                rorUsableRelics.add(r.relicId);
            } else {
                switch (r.tier) {
                    case COMMON:
                        rorCommonRelics.add(r.relicId);
                        break;
                    case UNCOMMON:
                        rorUncommonRelics.add(r.relicId);
                        break;
                    case RARE:
                        rorRareRelics.add(r.relicId);
                        break;
                    case SPECIAL:
                        if ((r instanceof BaseRelic && ((BaseRelic) r).isLunar)) {
                            rorLunarRelics.add(r.relicId);
                        }
                        break;
                }
            }
            logger.info("Adding " + r.tier.name().toLowerCase() + " relic: " + r.name);

            BaseMod.addRelic(r, RelicType.SHARED);
        }

        rorCommonRelics.sort(String::compareTo);
        rorUncommonRelics.sort(String::compareTo);
        rorRareRelics.sort(String::compareTo);
        rorLunarRelics.sort(String::compareTo);
        rorUsableRelics.sort(String::compareTo);
    }

    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        UIStrings mSStrings = CardCrawlGame.languagePack.getUIString(RiskOfSpire.makeID("ModSettings"));

        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton(mSStrings.TEXT[0], 350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, difficultyCostSetting, settingsPanel,
                (label) -> {
                },
                (button) -> {
                    difficultyCostSetting = button.enabled;
                    try {
                        SpireConfig config = new SpireConfig("riskOfSpire", "riskOfSpireConfig", ModSettings);
                        config.setBool(DIFFICULTY_RELIC_COST_MOD_SETTING, difficultyCostSetting);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        //Lunar Coins
        loadData();

        BaseMod.registerCustomReward(
                RewardItemTypeEnumPatch.LUNAR_COIN,
                (rewardSave) -> new LunarCoinReward(rewardSave.amount),
                (customReward) -> new RewardSave(customReward.type.toString(), null, ((LunarCoinReward) customReward).amountOfCoins, 0)
        );

        BaseMod.registerCustomReward(
                RewardItemTypeEnumPatch.LUNAR_CACHE,
                (rewardSave) -> new LunarCacheReward(rewardSave.amount),
                (customReward) -> new RewardSave(customReward.type.toString(), null, ((LunarCacheReward) customReward).goldAmt, 0)
        );

        if (lCD == null) {
            lCD = new LunarCoinDisplay();
        }
        BaseMod.addTopPanelItem(lCD);

        DifficultyMeter = new DifficultyMeter();
        BaseMod.addSaveField("RoRDifficulty", new CustomSavableRaw() {
            @Override
            public JsonElement onSaveRaw() {
                Gson coolG = new Gson();
                logger.info("Risk of Spire successfully saved run values.");
                return coolG.toJsonTree(DifficultyMeter.getDifficulty());
            }

            @Override
            public void onLoadRaw(JsonElement jsonElement) {
                if (jsonElement != null) {
                    Gson coolG = new Gson();
                    DifficultyMeter.setDifficulty(coolG.fromJson(jsonElement, Integer.class));
                    logger.info("Risk of Spire successfully loaded run values.");
                }
            }
        });
        BaseMod.addSaveField("RoRDifficultyMod", new CustomSavable<Float>() {
            @Override
            public Float onSave() {
                return DifficultyMeter.getDifficultyMod();
            }

            @Override
            public void onLoad(Float Float) {
                DifficultyMeter.setDifficultyMod(Float);
            }
        });
        DifficultyButton B = new DifficultyButton("riskOfSpireResources/images/ui/PeacefulButton.png", Settings.WIDTH / Settings.scale - 230/*- 175.0F*/, 50.0F, 0.0F, CardCrawlGame.languagePack.getTutorialString("DifficultyButton").TEXT[0]);
        DifficultyButton C = new DifficultyButton("riskOfSpireResources/images/ui/EasyButton.png", Settings.WIDTH / Settings.scale - 170/*235.0F*/, 50.0F, 0.5F, CardCrawlGame.languagePack.getTutorialString("DifficultyButton").TEXT[1]);
        DifficultyButton D = new DifficultyButton("riskOfSpireResources/images/ui/MediumButton.png", Settings.WIDTH / Settings.scale - 110/*295.0F*/, 50.0F, 1.0F, CardCrawlGame.languagePack.getTutorialString("DifficultyButton").TEXT[2]);
        DifficultyButton E = new DifficultyButton("riskOfSpireResources/images/ui/HardButton.png", Settings.WIDTH / Settings.scale - 50/*355.0F*/, 50.0F, 1.5F, CardCrawlGame.languagePack.getTutorialString("DifficultyButton").TEXT[3]);
        B.setSelected();
        DifficultyButton.Buttons.add(B);
        DifficultyButton.Buttons.add(C);
        DifficultyButton.Buttons.add(D);
        DifficultyButton.Buttons.add(E);
        for (int i = 0; i < 11; i++) {
            BgChanges.VfxClouds.add(new CustomTitleCloud((Settings.WIDTH / 9) * i - 400 * Settings.scale));
        }
        for (int i = 0; i < 11; i++) {
            BgChanges.SlowVfxClouds.add(new CustomSlowTitleCloud((Settings.WIDTH / 9) * i - 400 * Settings.scale));
        }
        GlacialShader = new ShaderProgram(Gdx.files.internal("riskOfSpireResources/rorstsshaders/GlacialShader/vertexShader.vs").readString(), Gdx.files.internal("riskOfSpireResources/rorstsshaders/GlacialShader/fragShader.fs").readString());
        /*logger.info(GlacialShader.getLog());
        RiskOfSpire.logger.info(Gdx.files.internal("riskOfSpireResources/rorstsshaders/GlacialShader/vertexShader.vs").readString());
        RiskOfSpire.logger.info(Gdx.files.internal("riskOfSpireResources/rorstsshaders/GlacialShader/fragShader.fs").readString());*/
        BaseMod.addSaveField("RoRUsableDrop", new CustomSavable<Boolean>() {
            @Override
            public Boolean onSave() {
                return RiskOfRainRelicHelper.dropUsable;
            }

            @Override
            public void onLoad(Boolean b) {
                RiskOfRainRelicHelper.dropUsable = b;
            }
        });

        BaseMod.addSaveField("RoRShrineSpawnMisses", new CustomSavable<Integer>() {
            @Override
            public Integer onSave() {
                return RoRShrineHelper.shrineSpawnMiss;
            }

            @Override
            public void onLoad(Integer i) {
                RoRShrineHelper.shrineSpawnMiss = i;
            }
        });

        logger.info("Done loading badge Image and mod options");
    }

    @Override
    public void receivePostUpdate() {
        if (!CardCrawlGame.isInARun()) return;
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            if (lCacheTrigger) {
                lCacheTrigger = false;
                AbstractDungeon.combatRewardScreen.hasTakenAll = false;
                AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(RiskOfRainRelicHelper.getRandomLunarRelic()));
                AbstractDungeon.combatRewardScreen.positionRewards();
            }
        }
        if(clearPowers) {
            clearPowers = false;
            AbstractDungeon.player.powers.clear();
        }
    }

    @Override
    public int receiveOnPlayerLoseBlock(int i) {
        int tmp = i;
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof OnBlockClearRelic) {
                tmp = ((OnBlockClearRelic) r).onBlockClear(tmp);
            }
        }
        return tmp;
    }


    @Override
    public void receivePreStartGame() {
        DifficultyMeter.setDifficulty(0);
        RiskOfRainRelicHelper.dropUsable = false;
        RoRShrineHelper.shrineSpawnMiss = 0;
    }

    @Override
    public void receivePostDungeonInitialize() {
        AbstractDungeon.commonRelicPool.removeAll(rorCommonRelics);
        AbstractDungeon.uncommonRelicPool.removeAll(rorUncommonRelics);
        AbstractDungeon.rareRelicPool.removeAll(rorRareRelics);

        if (DifficultyMeter.getDifficultyMod() == 0f) {
            DifficultyMeter.hideHitbox();
        } else {
            DifficultyMeter.unhideHitbox();
        }
    }

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/Power-Strings.json");

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/Relic-Strings.json");

        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/Event-Strings.json");

        // UI Strings
        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/eng/UI-Strings.json");
        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/Orb-Strings.json");
        //TutorialStrings
        BaseMod.loadCustomStringsFile(TutorialStrings.class,
                getModID() + "Resources/localization/eng/Tutorial-Strings.json");
        logger.info("Done editing strings");
    }

    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH

    public static String assetPath(String path) {
        return getModID() + "Resources/" + path;
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    public static void saveData() {
        logger.info("Risk of Spire | Saving Data...");
        try {
            SpireConfig config = new SpireConfig("riskOfSpire", "riskOfSpireConfig");

            config.setInt("lunarCoinAmt", lunarCoinAmount);
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = RiskOfSpire.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO

    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NNOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = RiskOfSpire.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = RiskOfSpire.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO

    @SuppressWarnings("unused")
    public static void initialize() {
        RiskOfSpire riskOfSpire = new RiskOfSpire();
    }

    @Override
    public void receiveRelicGet(AbstractRelic rel) {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof BaseRelic) {
                ((BaseRelic) r).onRelicGet(rel);
            }
        }
    }

    public static void clearData() {
        logger.info("Risk of Spire | Clearing Saved Data...");
        saveData();
    }

    public static void loadData() {
        logger.info("Risk of Spire | Loading Data...");
        try {
            SpireConfig config = new SpireConfig("riskOfSpire", "riskOfSpireConfig");
            config.load();
            if (config.has("lunarCoinAmt")) {
                lunarCoinAmount = config.getInt("lunarCoinAmt");
            } else {
                lunarCoinAmount = 0;
            }
        } catch (IOException | NumberFormatException e) {
            logger.error("Failed to load Risk of Spire data!");
            e.printStackTrace();
            clearData();
        }
    }
}
