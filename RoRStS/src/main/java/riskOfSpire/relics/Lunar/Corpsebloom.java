package riskOfSpire.relics.Lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.relics.Interfaces.OnFinalHealRelic;

public class Corpsebloom extends StackableRelic implements OnFinalHealRelic {
    public static final String ID = RiskOfSpire.makeID("Corpsebloom");
    private static final float HEAL_PER_ROOM = 0.05f;

    private boolean isCorpsebloomHeal;

    public Corpsebloom() {
        super(ID, "GestureOfTheDrowned.png", RelicTier.SPECIAL, LandingSound.MAGICAL);
        isCorpsebloomHeal = false;
        this.counter = 0;
    }

    @Override
    public int onFinalHeal(int healAmount) {
        if (isCorpsebloomHeal)
        {
            isCorpsebloomHeal = false;
            return healAmount;
        }
        this.counter += healAmount * (relicStack + 1);
        this.flash();
        return 0;
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (this.counter > 0)
        {
            int heal = Math.min(this.counter, MathUtils.floor(AbstractDungeon.player.maxHealth * HEAL_PER_ROOM * relicStack));
            this.counter -= heal;
            isCorpsebloomHeal = true;
            AbstractDungeon.player.heal(heal, true);
        }
    }

    @Override
    public String getUpdatedDescription() {
        //Healing is x% more effective, but is applied over time as up to x% of your max HP whenever you enter a room.
        return DESCRIPTIONS[0] + relicStack * 100 + DESCRIPTIONS[1] + MathUtils.floor(HEAL_PER_ROOM *  relicStack * 100) + DESCRIPTIONS[2];
    }

    public AbstractRelic makeCopy() {
        return new Corpsebloom();
    }
}