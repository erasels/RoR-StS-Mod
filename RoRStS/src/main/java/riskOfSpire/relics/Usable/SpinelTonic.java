package riskOfSpire.relics.Usable;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.PlayerLoseMaxHPAction;
import riskOfSpire.relics.Abstracts.UsableRelic;

public class SpinelTonic extends UsableRelic {
    public static final String ID = RiskOfSpire.makeID("SpinelTonic");

    private static final int BUFF_AMT = 3;
    private static final int HP_COST = 10;
    private static final int COOLDOWN = 0;

    public SpinelTonic() {
        super(ID, "SpinelTonic.png", RelicTier.SPECIAL, LandingSound.CLINK);
        this.counter = -1;
        isLunar = true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HP_COST + DESCRIPTIONS[1] + BUFF_AMT + DESCRIPTIONS[2] + getCooldownString();
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public int getBaseCooldown() {
        return COOLDOWN;
    }

    @Override
    public void atPreBattle() {
        System.out.println("P: " + pulse);
        beginLongPulse();
        System.out.println("P: " + pulse);
    }

    @Override
    public void onRightClickInCombat() {
        System.out.println("Clicked");
        if (pulse && AbstractDungeon.player.maxHealth > HP_COST) {
            stopPulse();
            flash();
            AbstractPlayer p = AbstractDungeon.player;
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(p, this));
            AbstractDungeon.actionManager.addToBottom(new PlayerLoseMaxHPAction(HP_COST));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, BUFF_AMT), BUFF_AMT, true, AbstractGameAction.AttackEffect.POISON));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, BUFF_AMT), BUFF_AMT, true, AbstractGameAction.AttackEffect.POISON));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FocusPower(p, BUFF_AMT), BUFF_AMT, true, AbstractGameAction.AttackEffect.POISON));
        }
    }

    @Override
    public void onVictory() {
        stopPulse();
        System.out.println("SP");
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SpinelTonic();
    }
}
