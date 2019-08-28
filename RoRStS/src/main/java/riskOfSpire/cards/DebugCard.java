package riskOfSpire.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.cards.ImpCards.AbstractImpCard;
import riskOfSpire.powers.elites.GlacialPower;

import static riskOfSpire.RiskOfSpire.makeCardPath;

public class DebugCard extends AbstractImpCard {
    public static final String ID = RiskOfSpire.makeID("DebugCard");
    public static final String IMG = makeCardPath("ImpAetu.png");
    public static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    private static final int COST = 0;


    public DebugCard() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new GlacialPower(m)));
    }

    @Override
    public void upgrade() {
    }
}