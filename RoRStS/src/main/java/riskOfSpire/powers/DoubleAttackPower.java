package riskOfSpire.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.abstracts.RoRStSPower;
import riskOfSpire.relics.Interfaces.OnDoubleAttackRelic;

public class DoubleAttackPower extends RoRStSPower implements CloneablePowerInterface {
    public static final String POWER_ID = RiskOfSpire.makeID("DoubleAttack");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean dontRemove;

    public DoubleAttackPower(AbstractCreature owner, boolean dontRemove) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        isTurnBased = false;
        amount = -1;
        type = AbstractPower.PowerType.BUFF;
        this.dontRemove = dontRemove;
        setImage("84_DoubleAttack.png", "32_DoubleAttack.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if ((!card.purgeOnUse) && (card.type == AbstractCard.CardType.ATTACK)) {
            for(AbstractRelic r : AbstractDungeon.player.relics) {
                if(r instanceof OnDoubleAttackRelic) {
                    ((OnDoubleAttackRelic) r).beforeDoubleAttack(card, action);
                }
            }
            flash();
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster) action.target;
            }
            AbstractCard tmp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (Settings.WIDTH / 2.0F - 300.0F * Settings.scale);
            tmp.target_y = (Settings.HEIGHT / 2.0F);
            tmp.freeToPlayOnce = true;
            if (m != null) {
                tmp.calculateCardDamage(m);
            }
            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, m, card.energyOnUse));
            if (!dontRemove) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
            }

        }
    }

    @Override
    public void stackPower(int i) {
    }

    @Override
    public AbstractPower makeCopy() {
        return new DoubleAttackPower(owner, dontRemove);
    }
}