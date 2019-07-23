package riskOfSpire.relics.Uncommon;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.commons.lang3.math.NumberUtils;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.patches.relics.HappiestMaskPatches;
import riskOfSpire.powers.SurroundedPower;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.relics.Interfaces.OnMonsterSpawn;

public class HappiestMask extends StackableRelic implements OnMonsterSpawn {
    public static final String ID = RiskOfSpire.makeID("HappiestMask");
    private static final float PERC_MOD = 0.2f;
    //Me be stupid, need to change getDec and tmp2 in here and SurroundPower when changing second variable

    private float combatMonsterX;

    public HappiestMask() {
        super(ID, "HappiestMask.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        int tmp1 = MathUtils.round((getInc()-1f)*100);
        int tmp2 = MathUtils.round((1f-getDec())*100);
        return DESCRIPTIONS[0] + tmp1 + DESCRIPTIONS[1] + tmp2 + DESCRIPTIONS[2];
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if(m.drawX>combatMonsterX) {
            combatMonsterX = m.drawX;
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(m, this));

        }
        for(AbstractMonster mon: AbstractDungeon.getMonsters().monsters) {
            flipEmCarefully(mon);
        }
    }

    @Override
    public void onMonsterSpawn(AbstractMonster m) {
        if(m.drawX<combatMonsterX) {
            flipEmCarefully(m);
        }
    }

    public float getInc() {
        return 1f+(PERC_MOD*relicStack);
    }

    public float getDec() {
        return NumberUtils.max(1f-((PERC_MOD/2)*relicStack), 0.5f);
    }

    @Override
    public void atPreBattle() {
        HappiestMaskPatches.victoryMet = false;
    }

    @Override
    public void onVictory() {
        HappiestMaskPatches.victoryMet = true;
        AbstractDungeon.getMonsters().monsters.forEach(AbstractMonster::dispose);
        combatMonsterX = 0;
    }

    @Override
    public void onEquip() {
        HappiestMaskPatches.hasHM = true;
    }

    @Override
    public void onUnequip() {
        HappiestMaskPatches.hasHM = false;
    }

    @Override
    public void onLoad(Integer i) {
        super.onLoad(i);
        HappiestMaskPatches.hasHM = true;
    }

    private void flipEmCarefully(AbstractMonster mon) {
        if(combatMonsterX>mon.drawX && !mon.isDeadOrEscaped() && !mon.hasPower(SurroundedPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mon, AbstractDungeon.player , new SurroundedPower(mon, this)));
            mon.flipHorizontal = true;
        }
    }

    public AbstractRelic makeCopy() {
        return new HappiestMask();
    }

}

//Add to Mask event?