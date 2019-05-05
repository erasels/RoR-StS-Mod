package riskOfSpire.actions.general;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import riskOfSpire.RiskOfSpire;

public class MakeTempEchoInHandAction extends MakeTempCardInHandAction {
    private UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(RiskOfSpire.makeID("Echo"));

    private static final float PADDING;

    private AbstractCard card;
    private boolean isOtherCardInCenter;
    private int reduction = 0;

    public MakeTempEchoInHandAction(AbstractCard card, boolean isOtherCardInCenter) {
        super(card, isOtherCardInCenter);
    }

    public MakeTempEchoInHandAction(AbstractCard card) {
        this(card, 1);
    }

    public MakeTempEchoInHandAction(AbstractCard card, int amount) {
        super(card, amount);
    }
    public MakeTempEchoInHandAction(AbstractCard card, int amount, int reduction) {
        super(card, amount);
        this.reduction = reduction;
    }

    public MakeTempEchoInHandAction(AbstractCard card, int amount, boolean isOtherCardInCenter) {
        super(card, amount, isOtherCardInCenter);
    }

    public void update() {
        isOtherCardInCenter = (boolean)ReflectionHacks.getPrivate(this, MakeTempCardInHandAction.class, "isOtherCardInCenter");
        this.card = (AbstractCard) ReflectionHacks.getPrivate(this, MakeTempCardInHandAction.class, "c");
        if (this.amount == 0) {
            this.isDone = true;
        } else {
            int discardAmount = 0;
            int handAmount = this.amount;
            if (this.amount + AbstractDungeon.player.hand.size() > 10) {
                AbstractDungeon.player.createHandIsFullDialog();
                discardAmount = this.amount + AbstractDungeon.player.hand.size() - 10;
                handAmount -= discardAmount;
            }

            this.addToHand(handAmount);
            this.addToDiscard(discardAmount);
            if (this.amount > 0) {
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.8F));
            }

            this.isDone = true;
        }
    }

    private void addToHand(int handAmt) {
        int i;
        switch(this.amount) {
            case 0:
                break;
            case 1:
                if (handAmt == 1) {
                    if (isOtherCardInCenter) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(makeEcho(), (float) Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT / 2.0F));
                    } else {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(makeEcho()));
                    }
                }
                break;
            case 2:
                if (handAmt == 1) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(makeEcho(), (float)Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH * 0.5F), (float)Settings.HEIGHT / 2.0F));
                } else if (handAmt == 2) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(makeEcho(), (float)Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT / 2.0F));
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(makeEcho(), (float)Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT / 2.0F));
                }
                break;
            case 3:
                if (handAmt == 1) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(makeEcho(), (float)Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT / 2.0F));
                } else if (handAmt == 2) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(makeEcho(), (float)Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT / 2.0F));
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(makeEcho(), (float)Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT / 2.0F));
                } else if (handAmt == 3) {
                    for(i = 0; i < this.amount; ++i) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(makeEcho()));
                    }
                }
                break;
            default:
                for(i = 0; i < handAmt; ++i) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(makeEcho(), MathUtils.random((float)Settings.WIDTH * 0.2F, (float)Settings.WIDTH * 0.8F), MathUtils.random((float)Settings.HEIGHT * 0.3F, (float)Settings.HEIGHT * 0.7F)));
                }
        }

    }

    private void addToDiscard(int discardAmt) {
        switch(this.amount) {
            case 0:
                break;
            case 1:
                if (discardAmt == 1) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(makeEcho(), (float)Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT / 2.0F));
                }
                break;
            case 2:
                if (discardAmt == 1) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(makeEcho(), (float)Settings.WIDTH * 0.5F - (PADDING + AbstractCard.IMG_WIDTH * 0.5F), (float)Settings.HEIGHT * 0.5F));
                } else if (discardAmt == 2) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(makeEcho(), (float)Settings.WIDTH * 0.5F - (PADDING + AbstractCard.IMG_WIDTH * 0.5F), (float)Settings.HEIGHT * 0.5F));
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(makeEcho(), (float)Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH * 0.5F, (float)Settings.HEIGHT * 0.5F));
                }
                break;
            case 3:
                if (discardAmt == 1) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(makeEcho(), (float)Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT * 0.5F));
                } else if (discardAmt == 2) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(makeEcho(), (float)Settings.WIDTH * 0.5F, (float)Settings.HEIGHT * 0.5F));
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(makeEcho(), (float)Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT * 0.5F));
                } else if (discardAmt == 3) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(makeEcho(), (float)Settings.WIDTH * 0.5F, (float)Settings.HEIGHT * 0.5F));
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(makeEcho(), (float)Settings.WIDTH * 0.5F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT * 0.5F));
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(makeEcho(), (float)Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT * 0.5F));
                }
                break;
            default:
                for(int i = 0; i < discardAmt; ++i) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(makeEcho(), MathUtils.random((float)Settings.WIDTH * 0.2F, (float)Settings.WIDTH * 0.8F), MathUtils.random((float)Settings.HEIGHT * 0.3F, (float)Settings.HEIGHT * 0.7F)));
                }
        }

    }

    private AbstractCard makeEcho()
    {
        AbstractCard echo = this.card.makeStatEquivalentCopy();
        echo.exhaust = true;
        echo.isEthereal = true;
        echo.name = uiStrings.TEXT[0] + this.card.name;
        if (reduction != 0)
            echo.modifyCostForCombat(-reduction);
        echo.rawDescription = uiStrings.TEXT[1] + echo.rawDescription + uiStrings.TEXT[2];
        echo.initializeDescription();

        return echo;
    }

    static {
        PADDING = 25.0F * Settings.scale;
    }
}