package riskOfSpire.relics.Interfaces;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface TargetingRelic {
    default void targetAction(AbstractPlayer p) {}
    default void targetAction(AbstractMonster m) {}
}
