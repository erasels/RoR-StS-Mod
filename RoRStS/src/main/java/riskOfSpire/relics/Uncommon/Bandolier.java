package riskOfSpire.relics.Uncommon;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.ReduceRandomCostAction;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class Bandolier extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("Bandolier");

    private static final int REDUCTION = 1;

    public Bandolier() {
        super(ID, "Bandolier.png", RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    //Upon killing an enemy, reduce the cost of x random card in your hand by 1 this combat.
    /*@Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m.currentHealth == 0) { //idk gremlin horn does it so I will too
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(m, this));
                AbstractDungeon.actionManager.addToTop(new ReduceRandomCostAction(this.relicStack, REDUCTION));
                this.flash();
            }
        }
    }*/

    //Whenever you shuffle your discard pile, reduce the cost of a random card in your hand by 1 until played.
     @Override
     public void onShuffle() {
         AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
         AbstractDungeon.actionManager.addToBottom(new ReduceRandomCostAction(getValue(), REDUCTION, true));
         this.flash();
     }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + getValue() + DESCRIPTIONS[2];
    }

    public int getValue() {
         return relicStack+1;
    }

    public AbstractRelic makeCopy() {
        return new Bandolier();
    }
}