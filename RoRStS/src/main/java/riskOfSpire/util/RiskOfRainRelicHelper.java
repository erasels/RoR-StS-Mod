package riskOfSpire.util;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.rewards.LinkedRewardItem;

import java.util.ArrayList;
import java.util.List;

public class RiskOfRainRelicHelper {
    public static Random RiskOfRainRelicRng = new Random(); //This is saved and loaded in patches in RelicData class.
    private static int incrementLater = 0;

    public static String getRandomRelic(boolean rare, boolean changeCounter)
    {
        if (rare)
        {
            if (!changeCounter)
            {
                RiskOfRainRelicRng.counter -= 1;
                incrementLater += 1;
            }

            return RiskOfSpire.rorRareRelics.get(RiskOfRainRelicRng.random(RiskOfSpire.rorRareRelics.size() - 1));
        }
        else
        {
            if (!changeCounter)
            {
                RiskOfRainRelicRng.counter -= 2;
                incrementLater += 2;
            }
            int pool = RiskOfRainRelicRng.random(100);

            if (pool >= 95)
            {
                return RiskOfSpire.rorRareRelics.get(RiskOfRainRelicRng.random(RiskOfSpire.rorRareRelics.size() - 1));
            }
            else if (pool > 75)
            {
                return RiskOfSpire.rorUncommonRelics.get(RiskOfRainRelicRng.random(RiskOfSpire.rorUncommonRelics.size() - 1));
            }
            else
            {
                return RiskOfSpire.rorCommonRelics.get(RiskOfRainRelicRng.random(RiskOfSpire.rorCommonRelics.size() - 1));
            }
        }
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
            AbstractRelic rorRelic = RelicLibrary.getRelic(getRandomRelic((boolean) ReflectionHacks.getPrivate(reward, RewardItem.class, "isBoss"), false)).makeCopy();

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
}
