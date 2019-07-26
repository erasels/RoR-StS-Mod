package riskOfSpire.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.util.helpers.LunarCoinHelper;

import static org.apache.commons.lang3.math.NumberUtils.max;

@SpirePatch(
        clz = CorruptHeart.class,
        method = "usePreBattleAction"
)
public class LunarCoinOnHeartFightPatch {
    @SpirePostfixPatch
    public static void Postfix(CorruptHeart __instance) {
        int tmp = max(LunarCoinHelper.HEART_ENTER_COIN_AMT, MathUtils.round(LunarCoinHelper.HEART_ENTER_COIN_AMT* RiskOfSpire.DifficultyMeter.getDifficultyMod()));
        LunarCoinHelper.manipLunarCoins(tmp, true);
    }
}