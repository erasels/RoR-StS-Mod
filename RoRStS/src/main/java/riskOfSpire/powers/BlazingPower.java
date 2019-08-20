package riskOfSpire.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.elites.AbstractElitePower;

public class BlazingPower extends AbstractElitePower implements CloneablePowerInterface {
    public static final String POWER_ID = RiskOfSpire.makeID("Blazing");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BlazingPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        updateDescription();
        loadRegion("confusion");
        this.type = AbstractPower.PowerType.BUFF;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BlazingPower(owner);
    }

    @Override
    public void onInitialApplication() {
        owner.name = NAME + " " + owner.name;
    }
}
