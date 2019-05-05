package riskOfSpire.relics.Common;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class PaulsHoof extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("PaulsHoof");
    private static final int BLOCK_GAIN = 1;

    public PaulsHoof() {
        super(ID, "PaulsHoof.png", RelicTier.COMMON, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + getVal() + DESCRIPTIONS[1];
    }

    public int onPlayerGainedBlock(float blockAmount) {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, getVal()), getVal()));

        return MathUtils.floor(blockAmount);
    }

    private int getVal() {
        return BLOCK_GAIN*relicStack;
    }

    public AbstractRelic makeCopy() {
        return new PaulsHoof();
    }
}
