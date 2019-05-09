package riskOfSpire.cards.ImpCards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.SpawnTolerantDamageAllEnemiesAction;

import static riskOfSpire.RiskOfSpire.makeCardPath;

public class ImpBava extends AbstractImpCard {
    public static final String ID = RiskOfSpire.makeID("ImpBava");
    public static final String IMG = makeCardPath("ImpBava.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 0;

    private static final int DMG = 9;
    private static final int UPGR_DMG = 6;

    public ImpBava() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DMG;
        this.isMultiDamage = true;
        this.exhaust = true;
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SpawnTolerantDamageAllEnemiesAction(p, damage, false, false, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE, false));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGR_DMG);
            initializeDescription();
        }
    }
}
