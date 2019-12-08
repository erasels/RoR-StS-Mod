package riskOfSpire.powers.elites;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseTempHpPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.unique.OverloadingAction;

import java.util.Random;

public class OverloadingPower extends AbstractElitePower implements CloneablePowerInterface, OnLoseTempHpPower {
    public static final String POWER_ID = RiskOfSpire.makeID("Overloading");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final float OVERLOAD_PERCENTAGE = 0.4f;
    private boolean triggered =false;

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
        owner.decreaseMaxHealth(owner.maxHealth / 2);
        AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(owner, owner, owner.maxHealth));
        Random rng = new Random();
        for(int i = 0; i< 20; i++) {
            AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(rng.nextFloat()*Settings.HEIGHT, rng.nextFloat()*Settings.WIDTH), Settings.FAST_MODE ? 0.0F : 0.01F));
        }
        AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(owner.drawX, owner.drawY), Settings.FAST_MODE ? 0.0F : 0.1F));
    }

    @Override
    public int onLoseTempHp(DamageInfo damageInfo, int i) {
        triggered = true;
        return i;
    }

    @Override
    public void atStartOfTurn() {
        if(!triggered) {
            AbstractDungeon.actionManager.addToBottom(new OverloadingAction(owner));
        }
        triggered = false;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + MathUtils.floor(OVERLOAD_PERCENTAGE * 100) + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new OverloadingPower(owner);
    }
}
