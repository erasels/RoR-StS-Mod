package riskOfSpire.util;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.patches.ForUsableRelics.UsableRelicSlot;
import riskOfSpire.relics.Interfaces.ModifyRarityRateRelic;
import riskOfSpire.rewards.ExpensiveLinkedReward;
import riskOfSpire.rewards.LinkedRewardItem;

import java.util.ArrayList;
import java.util.List;

public class RiskOfRainRelicHelper {
    public static Random RiskOfRainRelicRng = new Random(); //This is saved and loaded in patches in RelicData class.
    private static int incrementLater = 0;

    public static AbstractRelic getRandomRelic(boolean rare, boolean changeCounter) {
        return getRandomRelic(rare, changeCounter, 1.0f);
    }

    public static AbstractRelic getRandomRelic(boolean rare, boolean changeCounter, float rateModifier)
    {
        int rolls = 0;
        rateModifier *= getFinalRateModifier();
        ArrayList<AbstractRelic> validPool = new ArrayList<>();
        AbstractRelic toCopy;

        if (rare) {
            for (String s : RiskOfSpire.rorRareRelicPool) {
                AbstractRelic r = RelicLibrary.getRelic(s);
                if (r != null && r.canSpawn())
                    validPool.add(r);
            }

            if (validPool.isEmpty()) { //no circlets.
                toCopy = RelicLibrary.getRelic(RiskOfSpire.rorRareRelics.get(RiskOfRainRelicRng.random(RiskOfSpire.rorRareRelics.size() - 1)));
                if (!RiskOfSpire.rorRareRelicPool.contains(toCopy.relicId))
                    RiskOfSpire.rorRareRelicPool.add(toCopy.relicId);
                rolls += 1;
            }
            else
            {
                toCopy = validPool.get(RiskOfRainRelicRng.random(validPool.size() - 1));
                rolls += 1;
                if (addToPool(toCopy.tier))
                    rolls += 1;
            }

            if (!changeCounter) {
                RiskOfRainRelicRng.counter -= rolls;
                incrementLater += rolls;
            }

        }
        else
        {
            AbstractRelic.RelicTier tier;
            ArrayList<String> sourceList;
            int pool = RiskOfRainRelicRng.random(100);
            rolls += 1;

            if (pool >= 95 * rateModifier) {
                sourceList = RiskOfSpire.rorRareRelicPool;
                tier = AbstractRelic.RelicTier.RARE;
            }
            else if (pool > 75 * rateModifier) {
                sourceList = RiskOfSpire.rorUncommonRelicPool;
                tier = AbstractRelic.RelicTier.UNCOMMON;
            }
            else {
                sourceList = RiskOfSpire.rorCommonRelicPool;
                tier = AbstractRelic.RelicTier.COMMON;
            }

            for (String s : sourceList)
            {
                AbstractRelic r = RelicLibrary.getRelic(s);
                if (r != null) {
                    if (r.canSpawn())
                        validPool.add(r);
                }
            }

            if (validPool.isEmpty()) { //no circlets.
                ArrayList<String> failurePool;
                switch (tier)
                {
                    case UNCOMMON:
                        failurePool = RiskOfSpire.rorUncommonRelics;
                        break;
                    case RARE:
                        failurePool = RiskOfSpire.rorRareRelics;
                        break;
                    default:
                        failurePool = RiskOfSpire.rorCommonRelics;
                        break;
                }
                toCopy = RelicLibrary.getRelic(failurePool.get(RiskOfRainRelicRng.random(failurePool.size() - 1)));
                if (!sourceList.contains(toCopy.relicId))
                    sourceList.add(toCopy.relicId);
                rolls += 1;
            }
            else
            {
                toCopy = validPool.get(RiskOfRainRelicRng.random(validPool.size() - 1));
                rolls += 1;
                if (addToPool(toCopy.tier))
                    rolls += 1;
            }


            if (!changeCounter)
            {
                RiskOfRainRelicRng.counter -= rolls;
                incrementLater += rolls;
            }
        }

        return toCopy.makeCopy();
    }

    public static AbstractRelic getRandomLunarRelic() {
        ArrayList<AbstractRelic> validPool = new ArrayList<>();
        AbstractRelic toCopy;
        ArrayList<AbstractRelic> failurePool = new ArrayList<>();
        ArrayList<String> sourceList = RiskOfSpire.rorLunarRelics;

        for (String s : sourceList) {
            AbstractRelic r = RelicLibrary.getRelic(s);
            if (r != null) {
                failurePool.add(r);
                if (r.canSpawn())
                    validPool.add(r);
            }
        }

        if (validPool.isEmpty()) { //no circlets.
            toCopy = failurePool.get(RiskOfRainRelicRng.random(failurePool.size() - 1));
        } else {
            toCopy = validPool.get(RiskOfRainRelicRng.random(validPool.size() - 1));
        }

        RiskOfRainRelicRng.counter -= 1;
        incrementLater += 1;

        return toCopy.makeCopy();
    }

    public static void modifyCombatRewards(CombatRewardScreen __instance) {
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
                LinkedRewardItem newReward = new ExpensiveLinkedReward(replaceReward, rorRelic);
                RiskOfRainRelicRng.counter--; //ExpensiveLinkedReward gets one random value.
                incrementLater++;


                int indexOf = __instance.rewards.indexOf(reward);
                // Insert after existing reward
                __instance.rewards.add(indexOf + 1, newReward);
                // Replace original
                __instance.rewards.set(indexOf, replaceReward);
            }
        }
    }

    private static boolean addToPool(AbstractRelic.RelicTier tier)
    {
        ArrayList<String> targetPool;
        ArrayList<String> sourcePool;

        switch (tier)
        {
            case UNCOMMON:
                sourcePool = RiskOfSpire.rorUncommonRelics;
                targetPool = RiskOfSpire.rorUncommonRelicPool;
                break;
            case RARE:
                sourcePool = RiskOfSpire.rorRareRelics;
                targetPool = RiskOfSpire.rorRareRelicPool;
                break;
            default:
                sourcePool = RiskOfSpire.rorCommonRelics;
                targetPool = RiskOfSpire.rorCommonRelicPool;
                break;
        }

        sourcePool.removeAll(targetPool);
        if (AbstractDungeon.player != null && UsableRelicSlot.usableRelic.get(AbstractDungeon.player) != null)
        {
            sourcePool.remove(UsableRelicSlot.usableRelic.get(AbstractDungeon.player).relicId);
        }

        if (sourcePool.size() == 1)
        {
            targetPool.add(sourcePool.get(0));
        }
        if (sourcePool.size() > 1)
        {
            targetPool.add(sourcePool.get(RiskOfRainRelicRng.random(sourcePool.size() - 1)));
            return true;
        }
        return false;
    }
    public static void removeFromPool(AbstractRelic r)
    {
        switch (r.tier)
        {
            case COMMON:
                RiskOfSpire.rorCommonRelicPool.remove(r.relicId);
                break;
            case UNCOMMON:
                RiskOfSpire.rorUncommonRelicPool.remove(r.relicId);
                break;
            case RARE:
                RiskOfSpire.rorRareRelicPool.remove(r.relicId);
                break;
        }
    }

    public static void updateCounter()
    {
        RiskOfRainRelicRng.counter += incrementLater;
        incrementLater = 0;
    }

    private static float getFinalRateModifier() {
        float mod = 1.0f;
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof ModifyRarityRateRelic) {
                mod = ((ModifyRarityRateRelic) r).modifyRarity(mod);
            }
        }
        return mod;
    }
}
