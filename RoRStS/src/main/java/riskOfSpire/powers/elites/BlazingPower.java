package riskOfSpire.powers.elites;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import riskOfSpire.RiskOfSpire;

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
        setImage("84_Blazing.png", "32_Blazing.png");
        this.type = AbstractPower.PowerType.BUFF;
        priority = 0;
        mName = NAME;
        tC = Color.FIREBRICK.cpy();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public void onInitialApplication() {
        super.onInitialApplication();
        AbstractDungeon.actionManager.addToBottom(new VFXAction(owner, new ScreenOnFireEffect(), 1.0F));
    }

    @Override
    public AbstractPower makeCopy() {
        return new BlazingPower(owner);
    }
}
