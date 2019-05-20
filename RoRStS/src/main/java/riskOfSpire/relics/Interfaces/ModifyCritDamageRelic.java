package riskOfSpire.relics.Interfaces;

public interface ModifyCritDamageRelic {
    default float modifyCrit(float critMod) {
        return critMod;
    }

    default void afterCrit() {
    }
}
