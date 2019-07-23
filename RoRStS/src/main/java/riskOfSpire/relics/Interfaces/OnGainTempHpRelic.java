package riskOfSpire.relics.Interfaces;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnGainTempHpRelic {
    int onGainTempHp(AbstractCreature receiver, int amount);
}
