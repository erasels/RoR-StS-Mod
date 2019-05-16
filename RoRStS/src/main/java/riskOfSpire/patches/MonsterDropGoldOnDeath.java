package riskOfSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import riskOfSpire.RiskOfSpire;

public class MonsterDropGoldOnDeath {
    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class GoldField {
        public static SpireField<Boolean> hasDroppedGold = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractMonster.class, method = "update")
    public static class MonsterDropGoldOnDeathPatch {
        @SpirePrefixPatch
        public static void patch(AbstractMonster __instance) {
            if (!GoldField.hasDroppedGold.get(__instance) && __instance.isDying && RiskOfSpire.DifficultyMeter.getDifficultyMod() > 0) {
                GoldField.hasDroppedGold.set(__instance, true);
                int maxhp = __instance.maxHealth / 10;
                if (!__instance.hasPower(MinionPower.POWER_ID)) {
                    AbstractDungeon.player.gainGold(maxhp);
                    for (int i = 0; i < maxhp; i++) {
                        AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player, __instance.hb.cX, __instance.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));
                    }
                }
            }
        }
    }
}
