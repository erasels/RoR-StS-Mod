package riskOfSpire.powers.elites;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.GainDexPower;

public class GlacialPower extends AbstractElitePower implements CloneablePowerInterface {
    public static final String POWER_ID = RiskOfSpire.makeID("Glacial");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final int DRAW_DOWN = 2;

    public GlacialPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        updateDescription();
        loadRegion("confusion");
        this.type = AbstractPower.PowerType.BUFF;
        mName = NAME;
        tC = Color.SKY.cpy();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            if(damageAmount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new DrawReductionPower(AbstractDungeon.player, DRAW_DOWN), DRAW_DOWN));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new DexterityPower(AbstractDungeon.player, -1), -1));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, owner, new GainDexPower(AbstractDungeon.player, 1), 1));
            }
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + DRAW_DOWN + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new GlacialPower(owner);
    }
}
