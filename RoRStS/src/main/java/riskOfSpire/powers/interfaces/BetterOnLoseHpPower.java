package riskOfSpire.powers.interfaces;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface BetterOnLoseHpPower {
    //Triggers after onLoseHP
    //Only triggers if damage is greater than 0
    int betterOnLoseHp(DamageInfo info, int damageAmount);
}