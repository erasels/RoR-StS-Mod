package riskOfSpire.powers.relicPowers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.abstracts.RoRStSPower;

public class PlayerFlightPower extends RoRStSPower implements CloneablePowerInterface {
    public static final String POWER_ID = RiskOfSpire.makeID("PlayerFlight");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final float DMG_MULTI = 0.5F;
    private static final float FLY_HEIGHT = 0.1F;
    private static final float HOVER_THRESHOLD = 30f;
    private float hover_test = HOVER_THRESHOLD;
    private static final float COOLDOWN_AMT = 0.01F;
    private float cooldown = COOLDOWN_AMT;

    private float initialPlayerHeight;


    public PlayerFlightPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = AbstractPower.PowerType.BUFF;
        updateDescription();
        loadRegion("flight");
        this.priority = 50;
    }

    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_FLIGHT", 0.05f);
    }

    @Override
    public void onInitialApplication() {
        if (AbstractDungeon.player != null) {
            if (AbstractDungeon.player.state != null) AbstractDungeon.player.state.setTimeScale(3);
            initialPlayerHeight = AbstractDungeon.player.drawY;
            AbstractDungeon.player.drawY += Settings.HEIGHT * FLY_HEIGHT * Settings.scale;
        }
    }

    @Override
    public void onRemove() {
        if (AbstractDungeon.player != null) {
            AbstractDungeon.player.drawY = initialPlayerHeight;
            if (AbstractDungeon.player.state != null) AbstractDungeon.player.state.setTimeScale(1);
        }
    }

    @Override
    public void onVictory() {
        onRemove();
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        return calculateDamageTakenAmount(damage, type);
    }

    private float calculateDamageTakenAmount(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.HP_LOSS && type != DamageInfo.DamageType.THORNS) {
            return damage * DMG_MULTI;
        }
        return damage;
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS) {
            flash();
            decreasePower();
        }
    }

    private void decreasePower() {
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
        updateDescription();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    public static String getDesc() {
        return DESCRIPTIONS[0] + "X" + DESCRIPTIONS[1];
    }

    @Override
    public void update(int slot) {
        super.update(slot);

        cooldown -= Gdx.graphics.getDeltaTime();
        if (cooldown < 0.0f) {
            cooldown = COOLDOWN_AMT;
            owner.drawY += hover_test*0.05f;
            hover_test -= 1f;
            if (hover_test < -HOVER_THRESHOLD) {
                hover_test = HOVER_THRESHOLD;
            }
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new PlayerFlightPower(owner, amount);
    }
}