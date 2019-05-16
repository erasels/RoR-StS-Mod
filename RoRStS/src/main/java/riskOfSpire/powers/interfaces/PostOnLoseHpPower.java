package riskOfSpire.powers.interfaces;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface PostOnLoseHpPower {
    //Triggers after all onLoseHp and betetr onLoseHP powers have triggered
    int postOnLoseHp(DamageInfo info, int damageAmount);
}
