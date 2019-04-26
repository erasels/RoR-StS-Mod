package riskOfSpire.relics.Uncommon;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.RaisePlayerMaxHPAction;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class Infusion extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("Infusion");
    private static final int HP_PER = 15;

    public Infusion() {
        super(ID, "Infusion.png", RelicTier.UNCOMMON, LandingSound.CLINK);
        this.counter = 0;
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m.currentHealth == 0) { //idk gremlin horn does it so I will too
            if (counter < HP_PER * relicStack) {
                counter++;
                this.flash();
                if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                    AbstractDungeon.player.increaseMaxHp(1, true);
                } else {
                    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(m, this));
                    AbstractDungeon.actionManager.addToBottom(new RaisePlayerMaxHPAction(1, true));
                    //Add a little red ball that flies from monster to player? Since that's the visual effect in RoR.
                }
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        //Upon killing an enemy, gain 1 Max HP, up to x per stack.
        //Should it display the final amount on this? Currently gained amount? It will be shown as counter, so is it needed?
        return DESCRIPTIONS[0] + HP_PER + DESCRIPTIONS[1];
    }

    public AbstractRelic makeCopy() {
        return new Infusion();
    }
}