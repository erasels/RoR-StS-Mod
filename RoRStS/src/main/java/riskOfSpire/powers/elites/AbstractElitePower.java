package riskOfSpire.powers.elites;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import riskOfSpire.powers.abstracts.RoRStSPower;
import riskOfSpire.util.helpers.RiskOfRainRelicHelper;

public abstract class AbstractElitePower extends RoRStSPower {
    //Tint color and name variable
    protected Color tC;
    protected String mName;

    @Override
    public void onDeath() {
        RiskOfRainRelicHelper.dropUsable = true;
    }

    @Override
    public void onInitialApplication() {
        owner.name = mName + " " + owner.name;
        AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new RoomTintEffect(tC, 0.2f, 999f, true), 0.0F));
    }
}
