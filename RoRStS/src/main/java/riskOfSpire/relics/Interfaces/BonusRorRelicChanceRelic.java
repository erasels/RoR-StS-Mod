package riskOfSpire.relics.Interfaces;

import com.megacrit.cardcrawl.screens.CombatRewardScreen;

public interface BonusRorRelicChanceRelic {
    //Triggers before bRCM
    default float flatBonusRelicChanceModifier(float currentChance) {
        return currentChance;
    }

    default float bonusRelicChanceModifier(float currentChance, CombatRewardScreen screenInfo) {
        return currentChance;
    }

    //After difficulty modifier is applied
    default float lunarCacheChanceModifier(float currentChance) {
        return currentChance;
    }
}
