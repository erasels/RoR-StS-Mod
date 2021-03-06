package riskOfSpire.relics.Uncommon;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;


public class MonsterTooth extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("MonsterTooth");
    private static final int HEAL = 2;

    public MonsterTooth() {
        super(ID, "MonsterTooth.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m.currentHealth == 0) {
            this.flash();
            if (!(m.hasPower(MinionPower.POWER_ID))) {
                AbstractDungeon.player.heal(HEAL * relicStack);
            }
        }
    }

    public AbstractRelic makeCopy() {
        return new MonsterTooth();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HEAL*relicStack + DESCRIPTIONS[1];
    }
}
