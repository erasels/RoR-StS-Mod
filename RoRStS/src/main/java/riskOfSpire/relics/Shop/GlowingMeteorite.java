package riskOfSpire.relics.Shop;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.GuaranteedDamageRandomEnemyAction;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.util.StringManipulationUtilities;
import riskOfSpire.vfx.combat.MeteorRainEffect;

import java.util.ArrayList;

public class GlowingMeteorite extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("GlowingMeteorite");
    private static final int TURN_AMT = 3;
    private static final int DMG_AMT = 15;

    //TODO: What to do about Shop relics, these don't spawn like normal. New merchant? Or just readd them and make them scale really fast
    public GlowingMeteorite() {
        super(ID, "GlowingMeteorite.png", RelicTier.SHOP, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        if (relicStack == 1) {
            return DESCRIPTIONS[0] + StringManipulationUtilities.ordinalNaming(TURN_AMT) + DESCRIPTIONS[1] + DMG_AMT + DESCRIPTIONS[2] + relicStack + DESCRIPTIONS[3];
        } else {
            return DESCRIPTIONS[0] + StringManipulationUtilities.ordinalNaming(TURN_AMT) + DESCRIPTIONS[1] + (DMG_AMT * relicStack) + DESCRIPTIONS[2] + relicStack + DESCRIPTIONS[4];
        }
    }

    @Override
    public void onPlayerEndTurn() {
        setCounter(GameActionManager.turn % TURN_AMT);
        if (counter == 0) {
            for (int i = 0; i < relicStack; i++) {
                int entities = 1;
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDeadOrEscaped()) {
                        entities++;
                    }
                }
                entities++;

                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new MeteorRainEffect(relicStack, AbstractDungeon.getMonsters().shouldFlipVfx()), 0.25F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new MeteorRainEffect(relicStack, AbstractDungeon.getMonsters().shouldFlipVfx()), 1.0F));
                }

                if (AbstractDungeon.cardRandomRng.random(entities) == 0) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, DMG_AMT * relicStack, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE, true));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new GuaranteedDamageRandomEnemyAction(AbstractDungeon.player, DMG_AMT * relicStack, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
                }
            }
        }
    }

    public AbstractRelic makeCopy() {
        return new GlowingMeteorite();
    }
}
