package riskOfSpire.patches.relics;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import riskOfSpire.relics.Uncommon.TougherTimes;

public class TougherTimesPatch
{
    private static float reduceDamage(AbstractCreature target, float damage, DamageInfo.DamageType damageType)
    {
        if (target.isPlayer && AbstractDungeon.player.hasRelic(TougherTimes.ID)) {
            TougherTimes relic = (TougherTimes) AbstractDungeon.player.getRelic(TougherTimes.ID);
            damage = relic.reduceDamage(damage, damageType);
        }
        return damage;
    }

    @SpirePatch(
            clz=DamageInfo.class,
            method="applyPowers"
    )
    public static class DamageInfoPatch
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"tmp"}
        )
        public static void Insert(DamageInfo __instance, AbstractCreature owner, AbstractCreature target, @ByRef float[] tmp)
        {
            tmp[0] = reduceDamage(target, tmp[0], __instance.type);
            if (__instance.base != (int) tmp[0]) {
                __instance.isModified = true;
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz=AbstractMonster.class,
            method="calculateDamage"
    )
    public static class AbstractMonsterPatch
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"target", "tmp"}
        )
        public static void Insert(AbstractMonster __instance, int dmg, AbstractPlayer target, @ByRef float[] tmp)
        {
            tmp[0] = reduceDamage(target, tmp[0], DamageInfo.DamageType.NORMAL);
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
