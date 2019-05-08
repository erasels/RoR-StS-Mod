package riskOfSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import riskOfSpire.util.LunarCoinHelper;

@SpirePatch(
        clz = CorruptHeart.class,
        method = "usePreBattleAction"
)
public class LunarCoinOnHeartFightPatch {
    @SpirePostfixPatch
    public static void Postfix(CorruptHeart __instance) {
        LunarCoinHelper.manipLunarCoins(LunarCoinHelper.HEART_ENTER_COIN_AMT, true);
    }
}