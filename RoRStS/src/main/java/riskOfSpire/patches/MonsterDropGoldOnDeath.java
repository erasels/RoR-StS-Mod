package riskOfSpire.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import riskOfSpire.RiskOfSpire;

import java.util.ArrayList;

@SpirePatch(clz = AbstractRoom.class, method = "endBattle")
public class MonsterDropGoldOnDeath {
    @SpireInsertPatch(locator = Locator.class)
    public static void Insert(AbstractRoom __instance) {
        if (RiskOfSpire.DifficultyMeter.getDifficultyMod() > 0 && RiskOfSpire.difficultyCostSetting) {
            float toAdd = 0;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.hasPower(MinionPower.POWER_ID)) {
                    toAdd += (float) m.maxHealth / 10F;
                }
            }
            AbstractDungeon.getCurrRoom().addGoldToRewards(MathUtils.floor(toAdd));
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "onVictory");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }
}

/*@SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class GoldField {
        public static SpireField<Boolean> hasDroppedGold = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractMonster.class, method = "update")
    public static class MonsterDropGoldOnDeathPatch {
        @SpirePrefixPatch
        public static void patch(AbstractMonster __instance) {
            if (!GoldField.hasDroppedGold.get(__instance) && __instance.isDying && RiskOfSpire.DifficultyMeter.getDifficultyMod() > 0 && RiskOfSpire.difficultyCostSetting) {
                GoldField.hasDroppedGold.set(__instance, true);
                int maxHp = __instance.maxHealth / 10;
                if (!__instance.hasPower(MinionPower.POWER_ID)) {
                    AbstractDungeon.player.gainGold(maxHp);
                    for (int i = 0; i < maxHp; i++) {
                        AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player, __instance.hb.cX, __instance.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));
                    }
                }
            }
        }
    }*/