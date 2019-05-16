package riskOfSpire.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class ModifiedGainBlockAction extends AbstractGameAction {
    private static final float DUR = 0.25f;
    private static final float Fast_DUR = 0.1f;

    public ModifiedGainBlockAction(AbstractCreature target, AbstractCreature source, int amount) {
        setValues(target, source, amount);
        this.actionType = AbstractGameAction.ActionType.BLOCK;
        if(Settings.FAST_MODE) {
            this.duration = Fast_DUR;
            this.startDuration = Fast_DUR;
        }else {
            this.duration = DUR;
            this.startDuration = DUR;
        }
    }

    public void update() {
        if ((!this.target.isDying) && (!this.target.isDead) && (this.duration == this.startDuration)) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.SHIELD));
            this.target.addBlock(this.amount);
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                c.applyPowers();
            }
        }
        tickDuration();
    }
}