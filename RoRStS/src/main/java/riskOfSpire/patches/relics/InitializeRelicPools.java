package riskOfSpire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.UsableRelic;
import riskOfSpire.util.RiskOfRainRelicHelper;

import java.util.ArrayList;
import java.util.Collections;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "initializeRelicList"
)
public class InitializeRelicPools {
    @SpirePrefixPatch
    public static void init(AbstractDungeon __instance)
    {
        RiskOfSpire.rorCommonRelicPool.clear();
        RiskOfSpire.rorUncommonRelicPool.clear();
        RiskOfSpire.rorRareRelicPool.clear();

        boolean hasUsable = false;

        ArrayList<String> cpy = new ArrayList<>(RiskOfSpire.rorCommonRelics);
        Collections.shuffle(cpy, new java.util.Random(RiskOfRainRelicHelper.RiskOfRainRelicRng.randomLong()));
        for (int i = 0; i < Math.min(RiskOfSpire.BASE_COMMONS, cpy.size()); ++i)
        {
            if (RelicLibrary.getRelic(cpy.get(i)) instanceof UsableRelic)
            {
                hasUsable = true;
            }
            RiskOfSpire.rorCommonRelicPool.add(cpy.get(i));
        }

        cpy.clear();
        cpy.addAll(RiskOfSpire.rorUncommonRelics);
        Collections.shuffle(cpy, new java.util.Random(RiskOfRainRelicHelper.RiskOfRainRelicRng.randomLong()));
        for (int i = 0; i < Math.min(RiskOfSpire.BASE_UNCOMMONS, cpy.size()); ++i)
        {
            if (RelicLibrary.getRelic(cpy.get(i)) instanceof UsableRelic)
            {
                hasUsable = true;
            }
            RiskOfSpire.rorUncommonRelicPool.add(cpy.get(i));
        }

        cpy.clear();
        cpy.addAll(RiskOfSpire.rorRareRelics);
        Collections.shuffle(cpy, new java.util.Random(RiskOfRainRelicHelper.RiskOfRainRelicRng.randomLong()));
        for (int i = 0; i < Math.min(RiskOfSpire.BASE_RARES, cpy.size()); ++i)
        {
            if (RelicLibrary.getRelic(cpy.get(i)) instanceof UsableRelic)
            {
                hasUsable = true;
            }
            RiskOfSpire.rorRareRelicPool.add(cpy.get(i));
        }

        if (!hasUsable) //ensure there's always at least one usable relic in pool at start
        {
            AbstractRelic r = getRandomUsableRelic();
            switch (r.tier)
            {
                case COMMON:
                    RiskOfSpire.rorCommonRelicPool.add(r.relicId);
                    break;
                case UNCOMMON:
                    RiskOfSpire.rorUncommonRelicPool.add(r.relicId);
                    break;
                case RARE:
                    RiskOfSpire.rorRareRelicPool.add(r.relicId);
                    break;
            }
        }
    }

    private static AbstractRelic getRandomUsableRelic()
    {
        return RelicLibrary.getRelic(RiskOfSpire.rorUsableRelics.get(RiskOfRainRelicHelper.RiskOfRainRelicRng.random(RiskOfSpire.rorUsableRelics.size() - 1))).makeCopy();
    }
}
