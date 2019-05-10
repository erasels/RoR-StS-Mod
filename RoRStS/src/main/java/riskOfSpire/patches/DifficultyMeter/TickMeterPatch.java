package riskOfSpire.patches.DifficultyMeter;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import riskOfSpire.RiskOfSpire;

@SpirePatch(clz = CardCrawlGame.class, method = "update")
public class TickMeterPatch {
    @SpirePostfixPatch
    public static void patch(CardCrawlGame __instance) {
        RiskOfSpire.DifficultyMeter.tick();
    }
}
