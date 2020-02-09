package riskOfSpire.relics.Common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.relics.Interfaces.ModifyDamageRelic;

public class ArmorPiercingRounds extends StackableRelic implements ModifyDamageRelic {
    public static final String ID = RiskOfSpire.makeID("ArmorPiercingRounds");

    public ArmorPiercingRounds() {
        super(ID, "ArmorPiercingRounds.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + relicStack + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped() && (m.type == AbstractMonster.EnemyType.BOSS || m.type == AbstractMonster.EnemyType.ELITE)) {
                //AbstractDungeon.actionManager.addToBottom(new DumbApplyPowerAction(m, AbstractDungeon.player, new ArmorPiercingRoundsPower(m), -1, true));
                if (!pulse)
                    this.beginLongPulse();
            }
        }
    }

    @Override
    public float calculateCardDamageRelic(float damage, AbstractCard c, AbstractMonster m) {
        if(c.damageTypeForTurn == DamageInfo.DamageType.NORMAL && (m.type == AbstractMonster.EnemyType.BOSS || m.type == AbstractMonster.EnemyType.ELITE)) {
            damage += relicStack;
        }
        return damage;
    }

    @Override
    public void onVictory() {
        this.stopPulse();
    }

    public AbstractRelic makeCopy() {
        return new ArmorPiercingRounds();
    }
}
