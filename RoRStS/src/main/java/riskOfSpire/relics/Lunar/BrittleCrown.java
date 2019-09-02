package riskOfSpire.relics.Lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Ectoplasm;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.vfx.combat.LoseGoldEffect;
import riskOfSpire.vfx.combat.QuietGainPennyEffect;

import java.util.ArrayList;

public class BrittleCrown extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("BrittleCrown");
    private static final float CONVERT_PERC = 0.5f;
    private static final float LOSE_PERC = 0.15f;

    private ArrayList<AbstractMonster> antiScumList = new ArrayList<>();


    public BrittleCrown() {
        super(ID, "BrittleCrown.png", RelicTier.SPECIAL, LandingSound.FLAT);
        isLunar = true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MathUtils.round(getConvertPerc()*100) + DESCRIPTIONS[1] + MathUtils.round(getLossPerc()*100) + DESCRIPTIONS[2];
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (!antiScumList.contains(m)) {
            flash();
            AbstractDungeon.player.gainGold(getConvert(m));
            AbstractPlayer p = AbstractDungeon.player;
            if (!AbstractDungeon.player.hasRelic(Ectoplasm.ID)) {
                for (int i = 0; i < getConvert(m); i++) {
                    AbstractDungeon.effectList.add(new QuietGainPennyEffect(p, m.hb.cX, m.hb.cY, p.hb.cX, p.hb.cY, true));
                }
            }
            antiScumList.add(m);
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_FROST_EVOKE", MathUtils.random(0.75f, 1.5f)));
            AbstractDungeon.player.loseGold(getLoss());
            AbstractDungeon.effectsQueue.add(new LoseGoldEffect(getLoss()));
        }
        return damageAmount;
    }

    @Override
    public void onVictory() {
        antiScumList.clear();
    }

    public float getLossPerc() {
        return LOSE_PERC;
    }

    public int getLoss() {
        return MathUtils.floor((float) AbstractDungeon.player.gold * getLossPerc());
    }

    public float getConvertPerc() {
        return (CONVERT_PERC / 2) + ((CONVERT_PERC / 2) * relicStack);
    }

    public int getConvert(AbstractMonster m) {
        return MathUtils.floor((float) m.maxHealth * getConvertPerc());
    }

    public AbstractRelic makeCopy() {
        return new BrittleCrown();
    }
}
