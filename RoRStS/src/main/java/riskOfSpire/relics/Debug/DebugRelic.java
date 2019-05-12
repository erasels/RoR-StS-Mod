package riskOfSpire.relics.Debug;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.util.TextureLoader;

import static riskOfSpire.RiskOfSpire.makeRelicOutlinePath;
import static riskOfSpire.RiskOfSpire.makeRelicPath;

public class DebugRelic extends CustomRelic implements ClickableRelic {
    public static final String ID = RiskOfSpire.makeID("DebugRelic");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ArmorPiercingRounds.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ArmorPiercingRounds.png"));

    public DebugRelic() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onRightClick() {
        RiskOfSpire.DifficultyMeter.setDifficulty(RiskOfSpire.DifficultyMeter.getDifficulty() + 40);
        this.counter = (int) (RiskOfSpire.DifficultyMeter.getDifficultyMod() * 2);
    }

    public AbstractRelic makeCopy() {
        return new DebugRelic();
    }
}
