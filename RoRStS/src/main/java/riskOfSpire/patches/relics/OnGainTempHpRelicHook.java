package riskOfSpire.patches.relics;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.relics.Interfaces.OnGainTempHpRelic;

public class OnGainTempHpRelicHook {
    @SpirePatch(
            clz = AddTemporaryHPAction.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class GainTempHPListener {
        public static void Postfix(AddTemporaryHPAction __instance, AbstractCreature target, AbstractCreature source, int amount) {
            for(AbstractRelic r : AbstractDungeon.player.relics) {
                if(r instanceof OnGainTempHpRelic) {
                    __instance.amount = ((OnGainTempHpRelic) r).onGainTempHp(target, amount);
                }
            }
        }
    }
}
