package riskOfSpire.util.helpers;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.patches.ForUsableRelics.UsableRelicSlot;
import riskOfSpire.relics.Abstracts.BaseRelic;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.relics.Interfaces.BonusRorRelicChanceRelic;
import riskOfSpire.relics.Interfaces.ModifyRarityRateRelic;
import riskOfSpire.rewards.ExpensiveLinkedReward;
import riskOfSpire.rewards.LinkedRewardItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RiskOfRainRelicHelper {
    public static Random RiskOfRainRelicRng = new Random(); //This is saved and loaded in patches in RelicData class.
    private static int incrementLater = 0;

    public static float FINAL_COST_MOD = 0.5f;
    public static boolean dropUsable;

    public static AbstractRelic getRandomRelic(boolean rare, boolean changeCounter) {
        return getRandomRelic(rare, changeCounter, 1.0f);
    }

    public static AbstractRelic getRandomRelic(boolean rare, boolean changeCounter, float rateModifier) {
        return getRandomRelic(rare, changeCounter, false, rateModifier);
    }

    public static AbstractRelic getRandomRelic(boolean rare, boolean changeCounter, boolean onlyStackable, float rateModifier)
    {
        int rolls = 0;
        rateModifier *= getFinalRateModifier();
        ArrayList<AbstractRelic> validPool = new ArrayList<>();
        AbstractRelic toCopy;

        if (rare) {
            for (String s : RiskOfSpire.rorRareRelicPool) {
                AbstractRelic r = RelicLibrary.getRelic(s);
                if (r != null && r.canSpawn() && (!onlyStackable || r instanceof StackableRelic))
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

            if (pool >= 90 * rateModifier) {
                sourceList = RiskOfSpire.rorRareRelicPool;
                tier = AbstractRelic.RelicTier.RARE;
            }
            else if (pool > 65 * rateModifier) {
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
                    if (r.canSpawn()  && (!onlyStackable || r instanceof StackableRelic))
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

        return toCopy.makeCopy();
    }

    private static AbstractRelic getRandomUsableRelic() {
        float rateModifier = getFinalRateModifier();
        AbstractRelic.RelicTier tier;
        int pool = RiskOfRainRelicRng.random(100);

        if (pool >= 85 * rateModifier) {
            tier = AbstractRelic.RelicTier.RARE;
        }
        else if (pool > 50 * rateModifier) {
            tier = AbstractRelic.RelicTier.UNCOMMON;
        }
        else {
            tier = AbstractRelic.RelicTier.COMMON;
        }

        return getRandomUsableRelic(tier);
    }

    private static AbstractRelic getRandomUsableRelic(AbstractRelic.RelicTier tier) {
        ArrayList<AbstractRelic> tmp = RiskOfSpire.rorUsableRelics.stream()
                .filter(t -> RelicLibrary.getRelic(t).tier == tier && RelicLibrary.getRelic(t).canSpawn())
                .map(RelicLibrary::getRelic)
                .collect(Collectors.toCollection(ArrayList::new));
        return tmp.get(RiskOfRainRelicHelper.RiskOfRainRelicRng.random(tmp.size() - 1)).makeCopy();
    }

    public static StackableRelic loseRelicStack(Random rng, AbstractRelic.RelicTier tier) {
        ArrayList<StackableRelic> tmp = new ArrayList<>();
        for(AbstractRelic r : AbstractDungeon.player.relics) {
            if(r instanceof  StackableRelic && r.tier == tier) {
                tmp.add((StackableRelic)r);
            }
        }
        StackableRelic unstacked = tmp.get(rng.random(tmp.size()-1));
        unstacked.onUnstack();
        return unstacked;
    }

    public static StackableRelic loseRelicStack(Random rng, StackableRelic replacerRelic) {
        ArrayList<StackableRelic> tmp = new ArrayList<>();
        for(AbstractRelic r : AbstractDungeon.player.relics) {
            if(r instanceof  StackableRelic && r.tier == replacerRelic.tier && !r.relicId.equals(replacerRelic.relicId)) {
                tmp.add((StackableRelic)r);
            }
        }
        StackableRelic unstacked = tmp.get(rng.random(tmp.size()-1));
        unstacked.onUnstack();
        return unstacked;
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

        float bonusRelicChance = 0.01f;
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof BonusRorRelicChanceRelic) {
                bonusRelicChance = ((BonusRorRelicChanceRelic) r).flatBonusRelicChanceModifier(bonusRelicChance);
                bonusRelicChance = ((BonusRorRelicChanceRelic) r).bonusRelicChanceModifier(bonusRelicChance, __instance);
            }
        }

        if (RiskOfRainRelicRng.randomBoolean(Math.min(bonusRelicChance, 1.0f))) {
            __instance.rewards.add(new RewardItem(getRandomRelic(false, false)));
        }

        if(dropUsable) {
            __instance.rewards.add(new RewardItem(getRandomUsableRelic()));
            //Is set to false in RoomTransitionDropUsableReset
        }
    }

    private static boolean addToPool(AbstractRelic.RelicTier tier)
    {
        ArrayList<String> targetPool;
        ArrayList<String> sourcePool = new ArrayList<>();

        switch (tier)
        {
            case UNCOMMON:
                sourcePool.addAll(RiskOfSpire.rorUncommonRelics);
                targetPool = RiskOfSpire.rorUncommonRelicPool;
                break;
            case RARE:
                sourcePool.addAll(RiskOfSpire.rorRareRelics);
                targetPool = RiskOfSpire.rorRareRelicPool;
                break;
            default:
                sourcePool.addAll(RiskOfSpire.rorCommonRelics);
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

    public static boolean hasUsableRelic() {
        return UsableRelicSlot.usableRelic.get(AbstractDungeon.player) != null;
    }

    public static boolean hasCritRelic() {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if ((r instanceof BaseRelic && ((BaseRelic) r).isCritical)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasLunarRelic() {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if ((r instanceof BaseRelic && ((BaseRelic) r).isLunar)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasTempHPRelic() {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if ((r instanceof BaseRelic && ((BaseRelic) r).isTempHP)) {
                return true;
            }
        }
        return false;
    }

}
