package riskOfSpire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.util.helpers.RiskOfRainRelicHelper;

import java.util.ArrayList;
import java.util.Collections;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "initializeRelicList"
)
public class InitializeRelicPools {
    @SpirePostfixPatch
    public static void init(AbstractDungeon __instance) {
        RiskOfSpire.rorCommonRelicPool.clear();
        RiskOfSpire.rorUncommonRelicPool.clear();
        RiskOfSpire.rorRareRelicPool.clear();

        ArrayList<String> cpy = new ArrayList<>(RiskOfSpire.rorCommonRelics);
        Collections.shuffle(cpy, new java.util.Random(RiskOfRainRelicHelper.RiskOfRainRelicRng.randomLong()));
        for (int i = 0; i < Math.min(RiskOfSpire.BASE_COMMONS, cpy.size()); ++i) {
            RiskOfSpire.rorCommonRelicPool.add(cpy.get(i));
        }

        cpy.clear();
        cpy.addAll(RiskOfSpire.rorUncommonRelics);
        Collections.shuffle(cpy, new java.util.Random(RiskOfRainRelicHelper.RiskOfRainRelicRng.randomLong()));
        for (int i = 0; i < Math.min(RiskOfSpire.BASE_UNCOMMONS, cpy.size()); ++i) {
            RiskOfSpire.rorUncommonRelicPool.add(cpy.get(i));
        }

        cpy.clear();
        cpy.addAll(RiskOfSpire.rorRareRelics);
        Collections.shuffle(cpy, new java.util.Random(RiskOfRainRelicHelper.RiskOfRainRelicRng.randomLong()));
        for (int i = 0; i < Math.min(RiskOfSpire.BASE_RARES, cpy.size()); ++i) {
            RiskOfSpire.rorRareRelicPool.add(cpy.get(i));
        }
    }
}
