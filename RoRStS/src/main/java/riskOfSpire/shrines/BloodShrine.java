package riskOfSpire.shrines;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.vfx.event.BloodShrineRain;

public class BloodShrine extends AbstractShrineEvent {
    public static final String ID = RiskOfSpire.makeID("BloodShrine");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private int amount_pressed = 0;

    private static final float PERDAMAGE = 0.1f;
    private static final float DAMAGE_MULTI = 0.075f;
    private static final int GOLD_MULTI = 6;
    private static final int ASC_GOLD_MULTI = 4;

    private CurrentScreen curScreen = CurrentScreen.INTRO;

    private enum CurrentScreen {
        INTRO
    }

    public BloodShrine() {
        super(NAME, DESCRIPTIONS[0], RiskOfSpire.makeEventPath("BloodShrine.jpg"));
        imageEventText.setDialogOption(getDialog());
        imageEventText.setDialogOption(OPTIONS[0]); //Leave
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        //Code here
        switch (curScreen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        int g = getHealthCost();
                        AbstractDungeon.effectsQueue.add(new BloodShrineRain(g));
                        AbstractDungeon.player.damage(new DamageInfo(null, g, DamageInfo.DamageType.HP_LOSS));
                        AbstractDungeon.player.gainGold(getMoneyReward(g));
                        amount_pressed++;
                        imageEventText.updateDialogOption(0, getDialog());
                        break;
                    case 1:
                        imageEventText.clearRemainingOptions();
                        super.buttonEffect(buttonPressed);
                }
        }
    }

    private String getDialog() {
        int tmp = getHealthCost();
        return OPTIONS[1] + tmp + OPTIONS[2] + getMoneyReward(tmp) + OPTIONS[3];
    }

    private int getHealthCost() {
        return MathUtils.floor(AbstractDungeon.player.maxHealth * (PERDAMAGE + (amount_pressed > 0 ? (DAMAGE_MULTI * (float)amount_pressed) : 0)));
    }

    private int getMoneyReward(int hpCost) {
        if (AbstractDungeon.ascensionLevel >= 15) {
            return hpCost * ASC_GOLD_MULTI;
        } else {
            return hpCost * GOLD_MULTI;
        }
    }

    @Override
    public AbstractShrineEvent makeCopy() {
        return new BloodShrine();
    }
}
