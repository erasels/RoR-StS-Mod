package riskOfSpire.relics.Common;

import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.relicPowers.BurningPower;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class Gasoline extends StackableRelic implements OnAfterUseCardRelic {
    public static final String ID = RiskOfSpire.makeID("Gasoline");
    private static final int BURN_DMG = 5;

    public Gasoline() {
        super(ID, "Gasoline.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        if (relicStack == 1) {
            return DESCRIPTIONS[0] + BURN_DMG + DESCRIPTIONS[1] + relicStack + DESCRIPTIONS[3];
        }
        return DESCRIPTIONS[0] + BURN_DMG + DESCRIPTIONS[1] + relicStack + DESCRIPTIONS[2];
    }

    @Override
    public void atBattleStart() {
        beginLongPulse();
    }

    @Override
    public void onAfterUseCard(AbstractCard c, UseCardAction uac) {
        if (c.target == AbstractCard.CardTarget.ALL_ENEMY && pulse) {
            AbstractPlayer p = AbstractDungeon.player;
            stopPulse();
            flash();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(m, this));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new BurningPower(m, relicStack, BURN_DMG), relicStack));
                }
            }
        }
    }

    public AbstractRelic makeCopy() {
        return new Gasoline();
    }
}
