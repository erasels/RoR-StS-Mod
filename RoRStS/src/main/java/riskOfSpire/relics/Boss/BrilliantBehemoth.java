package riskOfSpire.relics.Boss;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.SpawnTolerantDamageAllEnemiesAction;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class BrilliantBehemoth extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("BrilliantBehemoth");
    public static final float MTPL = 0.3F;

    public BrilliantBehemoth() {
        super(ID, "BrilliantBehemoth.png", RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && card.damage > 0) {
            float finalMult = relicStack * MTPL;
            int finalDmg = MathUtils.ceil(card.damage * finalMult);
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new SpawnTolerantDamageAllEnemiesAction(AbstractDungeon.player, finalDmg, true, false, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BrilliantBehemoth();
    }

    public String getUpdatedDescription() {
        float finalMult = relicStack * MTPL;
        return DESCRIPTIONS[0] + MathUtils.floor(100 * finalMult) + DESCRIPTIONS[1];
    }
}
