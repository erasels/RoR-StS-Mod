package riskOfSpire.relics.Boss;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
    public static final double MTPL = 0.3;

    public BrilliantBehemoth() {
        super(ID, "BrilliantBehemoth.png", RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            double FinalMtpl = Math.pow((1.0 + MTPL), ((double) relicStack));
            int FinalDmg = (int) Math.floor(card.damage * FinalMtpl);
            AbstractDungeon.actionManager.addToBottom(new SpawnTolerantDamageAllEnemiesAction(AbstractDungeon.player, FinalDmg, true, false, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BrilliantBehemoth();
    }

    public String getUpdatedDescription() {
        double FinalMtpl = Math.pow((1.0 + MTPL), ((double) relicStack));
        return DESCRIPTIONS[0] + ((100 * FinalMtpl) - 100) + DESCRIPTIONS[1];
    }
}
