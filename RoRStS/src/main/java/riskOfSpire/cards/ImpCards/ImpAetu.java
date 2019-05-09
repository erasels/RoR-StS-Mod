package riskOfSpire.cards.ImpCards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import riskOfSpire.RiskOfSpire;

import static riskOfSpire.RiskOfSpire.makeCardPath;

public class ImpAetu extends AbstractImpCard {
    public static final String ID = RiskOfSpire.makeID("ImpAetu");
    public static final String IMG = makeCardPath("ImpAetu.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 0;

    private static final int BLOCK = 7;

    private static final int GOLD_THRESH = 50;
    private static final int UPGR_GOLD_THRESH = -20;

    public ImpAetu() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = GOLD_THRESH;
        this.exhaust = true;
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int tmp = MathUtils.floor((float)AbstractDungeon.player.gold/(float)magicNumber);
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block+tmp));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGR_GOLD_THRESH);
            initializeDescription();
        }
    }
}
