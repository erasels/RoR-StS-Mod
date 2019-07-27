package riskOfSpire.shrines;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
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
    private static final float RELIC_X = Settings.WIDTH * 0.25f;
    private static final float RELIC_Y = Settings.HEIGHT * 0.45f;

    private CurrentScreen curScreen = CurrentScreen.INTRO;
    private StackableRelic printerRelic;

    private enum CurrentScreen {
        INTRO
    }

    public ThreeDPrinter() {
        super(NAME, DESCRIPTIONS[0], RiskOfSpire.makeEventPath("3DPrinter.jpg"));
        printerRelic = getPrinterRelic();
        printerRelic.currentX = RELIC_X;
        printerRelic.currentY = RELIC_Y;
        imageEventText.setDialogOption(OPTIONS[1] + FontHelper.colorString(printerRelic.name, "g"));
        imageEventText.setDialogOption(OPTIONS[0]); //Leave
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        //Code here
        switch (curScreen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(RELIC_X, RELIC_Y, printerRelic.makeCopy());
                        StackableRelic lost = RiskOfRainRelicHelper.loseRelicStack(RiskOfRainRelicHelper.RiskOfRainRelicRng, printerRelic);
                        imageEventText.updateBodyText(DESCRIPTIONS[2] + FontHelper.colorString(lost.name, "r") + DESCRIPTIONS[1]);
                        //Check if no relics available and disable option
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
        if (!(checkRelicAmt(rRelics) || checkRelicAmt(uRelics) || checkRelicAmt(cRelics))) {
            return (StackableRelic) RiskOfRainRelicHelper.getRandomRelic(false, true, true, 1.0f);
        }
        while (true) {
            tmp = RiskOfRainRelicHelper.getRandomRelic(false, true, true, 1.0f);
            switch (tmp.tier) {
                case RARE:
                    if (checkRelicAmt(rRelics)) return (StackableRelic) tmp;
                    break;
                case UNCOMMON:
                    if (checkRelicAmt(uRelics)) return (StackableRelic) tmp;
                    break;
                case COMMON:
                    if (checkRelicAmt(cRelics)) return (StackableRelic) tmp;
                    break;
            }
        }
    }

    private boolean checkRelicAmt(ArrayList<StackableRelic> rl) {
        int counter = 0;
        for (StackableRelic r : rl) {
            counter += r.relicStack;
        }
        return counter >= MIN_RELIC_AMT;
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

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (printerRelic != null) {
            //printerRelic.renderOutline(sb, false);
            //printerRelic.render(sb);
            sb.setColor(Color.WHITE);
            sb.draw(printerRelic.img, RELIC_X - 64.0F, RELIC_Y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, printerRelic.scale, printerRelic.scale, 0, 0, 0, 128, 128, false, false);
        }
    }
}