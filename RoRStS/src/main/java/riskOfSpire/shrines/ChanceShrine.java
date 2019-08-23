package riskOfSpire.shrines;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.util.helpers.RiskOfRainRelicHelper;

import java.util.Random;

public class ChanceShrine extends AbstractShrineEvent {
    public static final String ID = RiskOfSpire.makeID("BloodShrine");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private int amount_pressed = 0;

    private static final int MIN_COST = 25;
    private static final float MAX_CHANCE = 0.4f;

    private static final float ASC_CHANCE_DECREASE = 0.07f;

    private CurrentScreen curScreen = CurrentScreen.INTRO;

    private enum CurrentScreen {
        INTRO
    }

    public ChanceShrine() {
        super(NAME, DESCRIPTIONS[0], RiskOfSpire.makeEventPath("ChanceShrine.jpg"));
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
                        CardCrawlGame.sound.play("CEILING_BOOM_1", new Random().nextFloat());
                        int g = getGoldCost();
                        AbstractDungeon.player.loseGold(g);
                        if(RiskOfRainRelicHelper.RiskOfRainRelicRng.randomBoolean(getRelicChance(g))) {
                            AbstractRelic r = RiskOfRainRelicHelper.getRandomRelic(false, true, true, 1.0f);
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, r);
                            imageEventText.updateBodyText(DESCRIPTIONS[2] + FontHelper.colorString(r.name, "g") + DESCRIPTIONS[3]);
                        } else {
                            imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        }
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
        int tmp = getGoldCost();
        return OPTIONS[1] + tmp + OPTIONS[2] + getRelicChance(tmp) + OPTIONS[3];
    }

    private int getGoldCost() {
        return MathUtils.floor(MIN_COST + (MIN_COST*amount_pressed));
    }

    private int getRelicChance(int goldCost) {
        //TODO:Refine this formula
        float tmp = (MAX_CHANCE) - (MAX_CHANCE - (goldCost/200f));
        if (AbstractDungeon.ascensionLevel >= 15) tmp -= Math.min(ASC_CHANCE_DECREASE, 0.0f);
        return MathUtils.floor(tmp*100f);
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.gold > 50;
    }

    @Override
    public AbstractShrineEvent makeCopy() {
        return new ChanceShrine();
    }
}