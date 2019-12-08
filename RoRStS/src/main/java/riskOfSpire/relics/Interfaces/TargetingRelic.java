package riskOfSpire.relics.Interfaces;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface TargetingRelic {
    default void targetAction(AbstractCreature c, boolean isPlayer) {}
    default void onTargetingCancelled() {}
}
