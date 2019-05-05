package riskOfSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import riskOfSpire.RiskOfSpire;

@SpirePatch(
        clz = CorruptHeart.class,
        method = "usePreBattleAction"
)
public class LunarCoinOnHeartFightPatch {
    @SpirePostfixPatch
    public static void Postfix(CorruptHeart __instance) {
        RiskOfSpire.manipLunarCoins(4, true);
    }
}