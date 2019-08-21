package riskOfSpire.powers.elites;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.unique.OverloadingAction;

public class OverloadingPower extends AbstractElitePower implements CloneablePowerInterface {
    public static final String POWER_ID = RiskOfSpire.makeID("Overloading");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final float OVERLOAD_PERCENTAGE = 0.2f;

    public OverloadingPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        updateDescription();
        setImage("84_Overloading.png", "32_Overloading.png");
        this.type = AbstractPower.PowerType.BUFF;
        priority = 0;
        mName = NAME;
        tC = Color.YELLOW.cpy();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        owner.decreaseMaxHealth(owner.maxHealth/2);
        AbstractDungeon.actionManager.addToTop(new OverloadingAction(owner));
        AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(owner.drawX, owner.drawY), Settings.FAST_MODE ? 0.0F : 0.1F));
    }

    @Override
    public void atEndOfRound() {
        AbstractDungeon.actionManager.addToBottom(new OverloadingAction(owner));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + MathUtils.floor(OVERLOAD_PERCENTAGE*100) + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new OverloadingPower(owner);
    }
}
