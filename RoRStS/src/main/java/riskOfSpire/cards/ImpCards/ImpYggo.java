package riskOfSpire.cards.ImpCards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.AblativeThornsPower;

import static riskOfSpire.RiskOfSpire.makeCardPath;

public class ImpYggo extends AbstractImpCard {
    public static final String ID = RiskOfSpire.makeID("ImpYggo");
    public static final String IMG = makeCardPath("ImpYggo.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 0;

    private static final int MN = 1;
    private static final int UPGR_MN = 1;

    public ImpYggo() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MN;
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new VerticalAuraEffect(Color.GREEN, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.0F));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,new PlatedArmorPower(p, magicNumber), magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,new AblativeThornsPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGR_MN);
            initializeDescription();
        }
    }
}
