package riskOfSpire.relics.Uncommon;

import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.unique.RandomLightningStrikesAction;
import riskOfSpire.relics.Abstracts.StackableRelic;

import java.util.ArrayList;

public class Ukulele extends StackableRelic implements OnAfterUseCardRelic {
    public static final String ID = RiskOfSpire.makeID("Ukulele");

    private static int BASE_DMG = 3;

    public Ukulele() {
        super(ID, "Ukulele.png", RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BASE_DMG + DESCRIPTIONS[1] + relicStack + DESCRIPTIONS[2];
    }

    @Override
    public void onAfterUseCard(AbstractCard c, UseCardAction uac) {
        if (c.target == AbstractCard.CardTarget.ENEMY && c.type == AbstractCard.CardType.ATTACK) {
            ArrayList<AbstractMonster> tmp = new ArrayList<>(AbstractDungeon.getCurrRoom().monsters.monsters);
            tmp.removeIf(m -> m == uac.target);
            AbstractDungeon.actionManager.addToBottom(new RandomLightningStrikesAction(tmp, new DamageInfo(AbstractDungeon.player, BASE_DMG, DamageInfo.DamageType.THORNS), relicStack, AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Ukulele();
    }


}
