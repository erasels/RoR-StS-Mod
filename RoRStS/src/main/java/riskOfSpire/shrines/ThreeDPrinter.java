package riskOfSpire.shrines;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.util.helpers.RiskOfRainRelicHelper;

import java.util.ArrayList;

public class ThreeDPrinter extends AbstractShrineEvent {
    public static final String ID = RiskOfSpire.makeID("ThreeDPrinter");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final int MIN_RELIC_AMT = 3;

    private CurrentScreen curScreen = CurrentScreen.INTRO;

    private enum CurrentScreen {
        INTRO
    }

    public ThreeDPrinter() {
        super(NAME, DESCRIPTIONS[0], RiskOfSpire.makeEventPath("ThreeDPrinter.jpg"));
        imageEventText.setDialogOption(DESCRIPTIONS[0]);
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
                        //imageEventText.updateDialogOption(0, getDialog());
                        break;
                    case 1:
                        imageEventText.clearRemainingOptions();
                        super.buttonEffect(buttonPressed);
                }
        }
    }

    private StackableRelic getPrinterRelic() {
        ArrayList<StackableRelic> cRelics = new ArrayList<>(), uRelics = new ArrayList<>(), rRelics = new ArrayList<>();

        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof StackableRelic) {
                switch (r.tier) {
                    case COMMON:
                        cRelics.add((StackableRelic) r);
                        break;
                    case UNCOMMON:
                        uRelics.add((StackableRelic) r);
                        break;
                    case RARE:
                        rRelics.add((StackableRelic) r);
                        break;
                }
            }
        }

        AbstractRelic tmp;
        if(!(rRelics.size() >= MIN_RELIC_AMT || uRelics.size() >= MIN_RELIC_AMT || cRelics.size() >= MIN_RELIC_AMT)) {
            return (StackableRelic)RiskOfRainRelicHelper.getRandomRelic(false, true, true, 1.0f);
        }
        while (true) {
            tmp = RiskOfRainRelicHelper.getRandomRelic(false, true, true, 1.0f);
            switch(tmp.tier) {
                case RARE:
                    if (rRelics.size() >= MIN_RELIC_AMT) return (StackableRelic) tmp;
                    break;
                case UNCOMMON:
                    if (uRelics.size() >= MIN_RELIC_AMT) return (StackableRelic) tmp;
                    break;
                case COMMON:
                    if (cRelics.size() >= MIN_RELIC_AMT) return (StackableRelic) tmp;
                    break;
            }
        }
    }

    @Override
    public AbstractShrineEvent makeCopy() {
        return new ThreeDPrinter();
    }

    @Override
    public boolean canSpawn() {
        int c = 0, u = 0, r = 0;
        for (AbstractRelic aR : AbstractDungeon.player.relics) {
            if (aR instanceof StackableRelic) {
                switch (aR.tier) {
                    case COMMON:
                        c++;
                        break;
                    case UNCOMMON:
                        u++;
                        break;
                    case RARE:
                        r++;
                        break;
                }
            }
        }
        return c > MIN_RELIC_AMT || u > MIN_RELIC_AMT || r > MIN_RELIC_AMT;
    }
}