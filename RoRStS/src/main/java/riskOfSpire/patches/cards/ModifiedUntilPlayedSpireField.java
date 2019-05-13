package riskOfSpire.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class ModifiedUntilPlayedSpireField {
    public static SpireField<Boolean> isCostModifiedUntilPlayed = new SpireField<>(() -> false);
}

