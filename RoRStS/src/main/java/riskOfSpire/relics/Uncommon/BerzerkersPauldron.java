package riskOfSpire.relics.Uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.relicPowers.BPDAttackPower;
import riskOfSpire.powers.relicPowers.BPDSkillPower;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class BerzerkersPauldron extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("BerzerkersPauldron");
    private static final int DOUBLE_PLAY_AMT = 1;

    public BerzerkersPauldron() {
        super(ID, "BerzerkersPauldron.png", RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public void atBattleStart() {
        if(counter == -2) {
            stopPulse();
            flash();
            counter++;
            atb(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BPDAttackPower(AbstractDungeon.player, getVal()), getVal()));
            atb(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BPDSkillPower(AbstractDungeon.player, getVal()), getVal()));
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom r) {
        if(!(r instanceof MonsterRoom)) {
            counter = -1;
            stopPulse();
        }
    }

    @Override
    public void onVictory() {
        counter = -2;
        beginLongPulse();
    }

    @Override
    public void onLoad(Integer i) {
        super.onLoad(i);
        if(counter == -2) {
            beginLongPulse();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + (getVal() == 1? DESCRIPTIONS[1]:(getVal() + DESCRIPTIONS[2]));
    }

    public int getVal() {
        return DOUBLE_PLAY_AMT + (relicStack-1);
    }

    public AbstractRelic makeCopy() {
        return new BerzerkersPauldron();
    }
}