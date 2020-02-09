package riskOfSpire.relics.Interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface ModifyDamageRelic {
    default float calculateCardDamageRelic(float damage, AbstractCard c, AbstractMonster target) {
        return damage;
    }
}