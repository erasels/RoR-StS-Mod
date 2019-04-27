package riskOfSpire.relics.Common;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.unique.DumbApplyPowerAction;
import riskOfSpire.powers.ArmorPiercingRoundsPower;
import riskOfSpire.relics.Abstracts.OnMonsterSpawn;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class ArmorPiercingRounds extends StackableRelic implements OnMonsterSpawn {
    public static final String ID = RiskOfSpire.makeID("ArmorPiercingRounds");
    public static final int BASE_ADD_DMG = 1; //Will always be atleast 2 because of relicstack satrting at 1

    public ArmorPiercingRounds() {
        super(ID, "ArmorPiercingRounds.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + (BASE_ADD_DMG + relicStack) + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped() && (m.type == AbstractMonster.EnemyType.BOSS || m.type == AbstractMonster.EnemyType.ELITE)) {
                AbstractDungeon.actionManager.addToBottom(new DumbApplyPowerAction(m, AbstractDungeon.player, new ArmorPiercingRoundsPower(m), -1, true));
            }
        }
    }

    @Override
    public void onMonsterSpawn(AbstractMonster m) {
        if ((m.type == AbstractMonster.EnemyType.BOSS || m.type == AbstractMonster.EnemyType.ELITE)) {
            AbstractDungeon.actionManager.addToBottom(new DumbApplyPowerAction(m, AbstractDungeon.player, new ArmorPiercingRoundsPower(m), -1, true));
        }
    }

    public AbstractRelic makeCopy() {
        return new ArmorPiercingRounds();
    }
}
