package riskOfSpire.relics.Lunar;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.patches.relics.TranscendencePatches;
import riskOfSpire.relics.Abstracts.StackableRelic;

//Basically Bottled heart, thanks for your code, Kio
public class Transcendence extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("Transcendence");
    private static final float HP_BASE = 0.5f;
    private static final float HP_INCREMENTS = 0.25f;


    private int mhp;
    private int chp;

    public Transcendence() {
        super(ID, "HopooFeather.png", RelicTier.SPECIAL, LandingSound.MAGICAL);
        isLunar = true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MathUtils.round(getVal()*100) + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        flash();
        mhp = AbstractDungeon.player.maxHealth;
        chp = AbstractDungeon.player.currentHealth;
        AbstractDungeon.player.decreaseMaxHealth(AbstractDungeon.player.maxHealth - 1);

        AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, MathUtils.floor(mhp*getVal())));
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public void onVictory() {
        AbstractDungeon.player.maxHealth = mhp+(AbstractDungeon.player.maxHealth-1);
        AbstractDungeon.player.currentHealth = chp;
    }

    @Override
    public void onEquip() {
        TranscendencePatches.TranscendenceField.hasTranscendence.set(AbstractDungeon.topPanel, true);
    }

    @Override
    public void onUnequip() {
        TranscendencePatches.TranscendenceField.hasTranscendence.set(AbstractDungeon.topPanel, false);
    }

    public float getVal() {
        return HP_BASE + (HP_INCREMENTS*(relicStack-1));
    }

    @Override
    public void onLoad(Integer amt) {
        super.onLoad(amt);
        TranscendencePatches.TranscendenceField.hasTranscendence.set(AbstractDungeon.topPanel, true);
    }

    public AbstractRelic makeCopy() {
        return new Transcendence();
    }
}