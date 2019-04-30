package riskOfSpire.util;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Interfaces.ModifyRarityRateRelic;
import riskOfSpire.rewards.LinkedRewardItem;

import java.util.ArrayList;
import java.util.List;

public class RiskOfRainRelicHelper {
    public static Random RiskOfRainRelicRng = new Random(); //This is saved and loaded in patches in RelicData class.
    private static int incrementLater = 0;

    public static AbstractRelic getRandomRelic(boolean rare, boolean changeCounter)
    {
        return getRandomRelic(rare, changeCounter, 1.0f);
    }
    public static AbstractRelic getRandomRelic(boolean rare, boolean changeCounter, float rateModifier)
    {
        rateModifier *= getFinalRateModifier();
        String id = "";
        if (rare)
        {
            if (!changeCounter)
            {
                RiskOfRainRelicRng.counter -= 1;
                incrementLater += 1;
            }

            id = RiskOfSpire.rorRareRelics.get(RiskOfRainRelicRng.random(RiskOfSpire.rorRareRelics.size() - 1));
        }
        else
        {
            if (!changeCounter)
            {
                RiskOfRainRelicRng.counter -= 2;
                incrementLater += 2;
            }
            int pool = RiskOfRainRelicRng.random(100);

            if (pool >= 95 * rateModifier)
            {
                id = RiskOfSpire.rorRareRelics.get(RiskOfRainRelicRng.random(RiskOfSpire.rorRareRelics.size() - 1));
            }
            else if (pool > 75 * rateModifier)
            {
                id = RiskOfSpire.rorUncommonRelics.get(RiskOfRainRelicRng.random(RiskOfSpire.rorUncommonRelics.size() - 1));
            }
            else
            {
                id = RiskOfSpire.rorCommonRelics.get(RiskOfRainRelicRng.random(RiskOfSpire.rorCommonRelics.size() - 1));
            }
        }

        return RelicLibrary.getRelic(id).makeCopy();
    }

    public static void modifyCombatRewards(CombatRewardScreen __instance)
    {
        List<RewardItem> cardRewards = new ArrayList<>();
        for (RewardItem reward : __instance.rewards) {
            if (reward.type == RewardItem.RewardType.CARD) {
                cardRewards.add(reward);
            }
        }

        for (RewardItem reward : cardRewards) {
            AbstractRelic rorRelic = getRandomRelic((boolean) ReflectionHacks.getPrivate(reward, RewardItem.class, "isBoss"), false);

            if (rorRelic != null) {
                LinkedRewardItem replaceReward = new LinkedRewardItem(reward);
                LinkedRewardItem newReward = new LinkedRewardItem(replaceReward, rorRelic);

                int indexOf = __instance.rewards.indexOf(reward);
                // Insert after existing reward
                __instance.rewards.add(indexOf + 1, newReward);
                // Replace original
                __instance.rewards.set(indexOf, replaceReward);
            }
        }
    }

    public static void updateCounter()
    {
        RiskOfRainRelicRng.counter += incrementLater;
        incrementLater = 0;
    }

    private static float getFinalRateModifier()
    {
        float mod = 1.0f;
        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            if (r instanceof ModifyRarityRateRelic)
            {
                mod = ((ModifyRarityRateRelic) r).modifyRarity(mod);
            }
        }
        return mod;
    }
}
