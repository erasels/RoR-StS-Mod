package riskOfSpire.patches.ForUsableRelics;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import riskOfSpire.relics.Abstracts.UsableRelic;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = SpirePatch.CLASS
)
public class UsableRelicSlot {
    public static SpireField<UsableRelic> usableRelic = new SpireField<>(()->null);
}
