package riskOfSpire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.CtBehavior;
import riskOfSpire.rewards.LunarCoinReward;
import riskOfSpire.util.RiskOfRainRelicHelper;

@SpirePatch(
        clz = CombatRewardScreen.class,
        method = "setupItemReward",
        paramtypez = {}
)
public class SpawnRelicRewards {
    @SpireInsertPatch (
            locator = Locator.class
    )
    public static void modifyRewards(CombatRewardScreen __instance)
    {
        RiskOfRainRelicHelper.modifyCombatRewards(__instance);

        if(CardCrawlGame.isInARun()){
            if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
                AbstractDungeon.combatRewardScreen.rewards.add(new LunarCoinReward(AbstractDungeon.miscRng.random(3) + 2));
            }
            else if(AbstractDungeon.miscRng.randomBoolean(0.07f)) {
                AbstractDungeon.combatRewardScreen.rewards.add(new LunarCoinReward(AbstractDungeon.miscRng.random(2) + 1));
            }
        }

        //Give Lunar coins on entering heart fight
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "positionRewards");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}