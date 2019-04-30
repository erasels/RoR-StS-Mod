package riskOfSpire.relics.Rare;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnLoseHpRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.unique.MissileStrikeAction;
import riskOfSpire.actions.unique.WobblyMissileAction;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class CeremonialDagger extends StackableRelic implements BetterOnLoseHpRelic {
    public static final String ID = RiskOfSpire.makeID("CeremonialDagger");
    private static final int KNIFE_AMT = 3;

    private int storedDamage = 0;

    public CeremonialDagger() {
        super(ID, "CeremonialDagger.png", RelicTier.RARE, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + KNIFE_AMT * relicStack + DESCRIPTIONS[1];
    }

    //TODO: Maybe change effect, current one ranges from kinda meh to hilariously broken (AKA: BALANCE PLS)
    @Override
    public int betterOnLoseHp(DamageInfo damageInfo, int i) {
        if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            storedDamage += i;
            setCounter(storedDamage);
        }
        return i;
    }

    @Override
    public void atTurnStart() {
        if (storedDamage > 1) {
            int tmp = MathUtils.ceilPositive((storedDamage/2F)/(float)KNIFE_AMT);
            for (int j = 0; j < KNIFE_AMT; j++) {
                AbstractDungeon.actionManager.addToTop(new WobblyMissileAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true), AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, tmp, DamageInfo.DamageType.THORNS), relicStack, 0.1f, Color.BLACK.cpy(), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            }
        }
        setCounter(-1);
        storedDamage = 0;
    }

    public AbstractRelic makeCopy() {
        return new CeremonialDagger();
    }
}
