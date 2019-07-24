package riskOfSpire.relics.Usable;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import javafx.util.Pair;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.UsableRelic;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static riskOfSpire.RiskOfSpire.logger;

public class EccentricVase extends UsableRelic {
    public static final String ID = RiskOfSpire.makeID("EccentricVase");

    private static final int COOLDOWN = 16;

    public EccentricVase() {
        super(ID, "EccentricVase.png", RelicTier.SHOP, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + getCooldownString();
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public int getBaseCooldown() {
        return COOLDOWN;
    }

    @Override
    public void onRightClick() {
        if (this.counter == 0)
        {
            if (AbstractDungeon.getCurrRoom() == null)
            {
                return;
            }
            else if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                return;
            }

            this.stopPulse();
            this.flash();
            this.activateCooldown();

            ArrayList<Pair<Integer, Integer>> possibleDestinations = new ArrayList<>();
            ArrayList<Pair<Integer, MapRoomNode>> myPath = new ArrayList<>();

            MapRoomNode node = AbstractDungeon.getCurrMapNode();

            for (int a = AbstractDungeon.map.size() - 1; a >= 0; --a)
            {
                for (int b = AbstractDungeon.map.get(a).size() - 1; b >= 0; --b)
                {
                    MapRoomNode n = AbstractDungeon.map.get(a).get(b);
                    if (n != null && n.hasEdges()) //based on RoomTypeAssigner.lastMinuteNodeChecker
                    {
                        if (n.equals(node))
                        {
                            myPath.add(new Pair<>(a, n));
                            continue; //don't want to add current room to possible destinations
                        }
                        else if (n.taken)
                        {
                            myPath.add(new Pair<>(a, n));
                        }

                        possibleDestinations.add(new Pair<>(a, b));
                    }
                }
            }

            if (!(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss))
            {
                logger.info("Can teleport to boss.");
                possibleDestinations.add(new Pair<>(-1, -1)); //Boss room, will be created upon teleportation if this is chosen
            }

            if (!possibleDestinations.isEmpty())
            {
                Pair<Integer, Integer> destination = possibleDestinations.get(AbstractDungeon.miscRng.random(possibleDestinations.size() - 1));

                if (destination.getValue() == -1 && destination.getKey() == -1) //we going to boss
                {
                    logger.info("Oh boy, off the boss we go.");

                    node.taken = true;

                    CardCrawlGame.music.fadeOutTempBGM();
                    MapRoomNode bossNode = new MapRoomNode(-1, 15);
                    bossNode.room = new MonsterRoomBoss();
                    AbstractDungeon.nextRoom = bossNode;
                    if (AbstractDungeon.pathY.size() > 1) {
                        AbstractDungeon.pathX.add(AbstractDungeon.pathX.get(AbstractDungeon.pathX.size() - 1));
                        AbstractDungeon.pathY.add(AbstractDungeon.pathY.get(AbstractDungeon.pathY.size() - 1) + 1);
                    } else {
                        AbstractDungeon.pathX.add(1);
                        AbstractDungeon.pathY.add(15);
                    }
                }
                else //we not going to boss
                {
                    MapRoomNode destinationNode = AbstractDungeon.map.get(destination.getKey()).get(destination.getValue());
                    logger.info("Teleporting to node (" + destination.getValue() + ", " + destination.getKey() + ")");

                    for (Pair<Integer, MapRoomNode> n : myPath)
                    {
                        if (n.getKey() >= destination.getKey())
                        {
                            n.getValue().taken = false;

                            for (MapEdge e : n.getValue().getEdges())
                            {
                                e.taken = false;
                                e.color = new Color(0.34F, 0.34F, 0.34F, 1.0F);
                            }

                            AbstractRoom r = n.getValue().room;

                            if (r != null) //shouldn't be
                            {
                                try
                                {
                                    n.getValue().setRoom(r.getClass().newInstance());
                                }
                                catch (Exception e)
                                {
                                    logger.error("Failed to re-initialize room: " + r.getClass().getName());
                                }
                            }
                        }
                        else
                        {
                            n.getValue().taken = true;
                        }
                    }

                    if (destinationNode.getRoom().phase == AbstractRoom.RoomPhase.COMPLETE)
                    {
                        try
                        {
                            destinationNode.setRoom(destinationNode.getRoom().getClass().newInstance());
                        }
                        catch (Exception e)
                        {
                            logger.error("Failed to re-initialize room: " + destinationNode.getRoom().getClass().getName());
                        }
                    }

                    //Ensure monster list is not completely emptied
                    if (AbstractDungeon.monsterList.size() <= 1 && CardCrawlGame.dungeon != null)
                    {
                        try
                        {
                            Method m = AbstractDungeon.class.getDeclaredMethod("generateStrongEnemies", int.class);

                            m.setAccessible(true);

                            m.invoke(CardCrawlGame.dungeon, 12);

                            if (AbstractDungeon.monsterList.size() == 1) //this did nothing. Add a copy.
                            {
                                String theOnlyMonster = "" + AbstractDungeon.monsterList.get(0);
                                AbstractDungeon.monsterList.add(theOnlyMonster);
                            }
                        }
                        catch (Exception e)
                        {
                            logger.error("Failed to repopulate monster list. Crash is likely to occur shortly.");
                        }
                    }

                    //MapEdge edge = new MapEdge(node.x, node.y, node.offsetX, node.offsetY, destinationNode.x, destinationNode.y, destinationNode.offsetX, destinationNode.offsetY, false);
                    //node.addEdge(edge);
                    //edge.markAsTaken();
                    //This edge thing really messes things up if you teleport backwards and then can traverse these edges and anyways it's just bad.
                    destinationNode.taken = true;
                    AbstractDungeon.nextRoom = destinationNode;
                    AbstractDungeon.pathX.add(destinationNode.x);
                    AbstractDungeon.pathY.add(destinationNode.y);
                    CardCrawlGame.metricData.path_taken.add(AbstractDungeon.nextRoom.getRoom().getMapSymbol());

                    AbstractDungeon.nextRoomTransitionStart();
                    CardCrawlGame.music.fadeOutTempBGM();

                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;

                    if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP)
                    {
                        AbstractDungeon.dungeonMapScreen.open(false);
                    }
                }

                if (!AbstractDungeon.firstRoomChosen)
                {
                    AbstractDungeon.firstRoomChosen = true;
                }
            }
            else
            {
                logger.error("NOWHERE TO TELEPORT??????????????????");
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EccentricVase();
    }
}
