package riskOfSpire.patches.relics;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.Skeleton;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_L;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_L;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.reflect.Field;


public class HappiestMaskPatches {
    public static boolean hasHM;
    public static boolean victoryMet;
    private static Field skelField;

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "render"
    )
    public static class RenderNormallyPls {
        @SpirePostfixPatch
        public static void Postfix(AbstractMonster __instance, SpriteBatch sb) throws NoSuchFieldException, IllegalAccessException {
            if (!victoryMet && (__instance.isDead || __instance.isDying) && !__instance.id.equals(SlimeBoss.ID) && !__instance.id.equals(AcidSlime_L.ID) && !__instance.id.equals(SpikeSlime_L.ID)) {
                if (hasHM) {
                    if (skelField == null) {
                        skelField = getField(AbstractMonster.class, "skeleton");//AbstractCreature.class.getDeclaredField("skeleton");
                    }
                    skelField.setAccessible(true);
                    Skeleton sk = (Skeleton) skelField.get(__instance);
                    if (sk != null) {
                        __instance.state.update(Gdx.graphics.getDeltaTime());
                        __instance.state.apply(sk);
                        sk.updateWorldTransform();
                        sk.setPosition(__instance.drawX + __instance.animX, __instance.drawY + __instance.animY + AbstractDungeon.sceneOffsetY);
                        Color c = Color.LIGHT_GRAY.cpy();
                        c.a = 0.6f;
                        sk.setColor(c);
                        sk.setFlip(__instance.flipHorizontal, __instance.flipVertical);
                        sb.end();
                        CardCrawlGame.psb.begin();
                        AbstractMonster.sr.draw(CardCrawlGame.psb, sk);
                        CardCrawlGame.psb.end();
                        sb.begin();
                        sb.setBlendFunction(770, 771);
                    } else {
                        Texture img = (Texture) ReflectionHacks.getPrivate(__instance, AbstractMonster.class, "img");
                        if (img != null) {
                            sb.draw(img, __instance.drawX - img.getWidth() * Settings.scale / 2.0f + __instance.animX, __instance.drawY + __instance.animY + AbstractDungeon.sceneOffsetY, img.getWidth() * Settings.scale, img.getHeight() * Settings.scale, 0, 0, img.getWidth(), img.getHeight(), __instance.flipHorizontal, __instance.flipVertical);
                        }
                    }
                }
            }
        }
    }

    private static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "updateDeathAnimation"
    )
    public static class DontMakeThemBlackPls {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractMonster.class.getName()) && m.getMethodName().equals("dispose")) {
                        m.replace("{" +
                                "if(!" + AbstractDungeon.class.getName() + ".player.hasRelic(\"riskOfSpire:HappiestMask\"))" +
                                "$proceed();" +
                                "}");
                    }
                }
            };
        }
    }
}
