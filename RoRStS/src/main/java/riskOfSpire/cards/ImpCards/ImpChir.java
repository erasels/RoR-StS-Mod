package riskOfSpire.cards.ImpCards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.unique.ChirAction;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static riskOfSpire.RiskOfSpire.makeCardPath;

public class ImpChir extends AbstractImpCard{
    public static final String ID = RiskOfSpire.makeID("ImpChir");
    public static final String IMG = makeCardPath("ImpChir.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPG_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 0;

    private static final int MN = 2;

    public ImpChir() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = MN;
        this.exhaust = true;
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ChirAction(magicNumber, languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION[0]));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isEthereal = false;
            retain = true;
            rawDescription = UPG_DESCRIPTION;
            initializeDescription(); //NPE
        }
    }
}
