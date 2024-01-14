package ru.fougest.client.modules.impl.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.joml.Vector2d;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.player.EventMotion;
import ru.fougest.client.events.impl.render.EventRender;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.util.math.PlayerPositionTracker;
import ru.fougest.client.util.render.BloomHelper;
import ru.fougest.client.util.render.ColorUtil;
import ru.fougest.client.util.render.ProjectionUtils;
import ru.fougest.client.util.render.RenderUtil;
import ru.fougest.client.util.render.animation.AnimationMath;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static ru.fougest.client.util.math.PlayerPositionTracker.isInView;
import static ru.fougest.client.util.world.WorldUtil.TotemUtil.getSphere;

@FunctionAnnotation(name = "Particles", type = Type.Fight)
public class Particles extends Function {

    public Particles() {
        super();
    }

    CopyOnWriteArrayList<Point> points = new CopyOnWriteArrayList<>();

    @Override
    public void onEvent(Event event) {
//        if (event instanceof EventPacket e) {
//            if (e.getPacket() instanceof CUseEntityPacket use) {
//                if (use.getAction() == ATTACK) {
//                    Entity entity = use.getEntityFromWorld(mc.world);
//                    if (mc.world != null && entity != null)
//                        createPoints(entity.getPositionVec().add(0,1,0));
//                }
//            }
//        }
        if (event instanceof EventMotion e) {
            for (Entity entity : mc.world.getAllEntities()) {
                if (entity instanceof LivingEntity l) {
                    if (l.hurtTime == 9) {
                        createPoints(l.getPositionVec().add(0,1,0));
                    }
                }
                if (entity instanceof EnderPearlEntity p) {
                    points.add(new Point(p.getPositionVec()));
                }
            }
        }
        if (event instanceof EventRender e) {
            if (e.isRender2D()) {
                if (points.size() > 100) {
                    points.remove(0);
                }
                for (Point point : points) {
                    long alive = (System.currentTimeMillis() - point.createdTime);
                    if (alive > point.aliveTime || !mc.player.canVectorBeSeenFixed(point.position) || !PlayerPositionTracker.isInView(point.position)) {
                        points.remove(point);
                        continue;
                    }

                    Vector2d pos = ProjectionUtils.project(point.position.x, point.position.y, point.position.z);

                    if (pos != null) {
                        float sizeDefault = point.size;

                        point.update();

                        float size = 1 - (float) alive / point.aliveTime;

                        BloomHelper.registerRenderCall(() -> {
                            RenderUtil.Render2D.drawRoundCircle((float) pos.x, (float) pos.y, (sizeDefault + 1) * size, Color.BLACK.getRGB());
                            RenderUtil.Render2D.drawRoundCircle((float) pos.x, (float) pos.y, sizeDefault * size, ColorUtil.getColorStyle(points.indexOf(point)));
                        });
                        RenderUtil.Render2D.drawRoundCircle((float) pos.x, (float) pos.y, (sizeDefault + 1) * size, Color.BLACK.getRGB());
                        RenderUtil.Render2D.drawRoundCircle((float) pos.x, (float) pos.y, sizeDefault * size, ColorUtil.getColorStyle(points.indexOf(point)));
                    }
                }
            }
        }
    }

    private void createPoints(Vector3d position) {
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(5, 20); i++) {
            points.add(new Point(position));
        }
    }

    private final class Point {
        public Vector3d position;
        public Vector3d motion;
        public Vector3d animatedMotion;

        public long aliveTime;
        public float size;

        public long createdTime = System.currentTimeMillis();

        public Point(Vector3d position) {
            this.position = new Vector3d(position.x,position.y,position.z);
            this.motion = new Vector3d(ThreadLocalRandom.current().nextFloat(-0.01f, 0.01f), 0, ThreadLocalRandom.current().nextFloat(-0.01f, 0.01f));
            this.animatedMotion = new Vector3d(0,0,0);
            size = ThreadLocalRandom.current().nextFloat(4, 7);
            aliveTime = ThreadLocalRandom.current().nextLong(3000, 10000);
        }


        public void update() {
            if (isGround()) {
                motion.y = 1;
                motion.x *= 1.05;
                motion.z *= 1.05;
            } else {
                motion.y = -0.01;
                motion.y *= 2;
            }


            animatedMotion.x = AnimationMath.fast((float) animatedMotion.x, (float) (motion.x), 3);
            animatedMotion.y = AnimationMath.fast((float) animatedMotion.y, (float) (motion.y), 3);
            animatedMotion.z = AnimationMath.fast((float) animatedMotion.z, (float) (motion.z), 3);

            position = position.add(animatedMotion);


        }

        boolean isGround() {
            Vector3d position = this.position.add(animatedMotion);
            AxisAlignedBB bb = new AxisAlignedBB(position.x - 0.1, position.y - 0.1, position.z - 0.1, position.x + 0.1, position.y + 0.1, position.z + 0.1);
            return getSphere(new BlockPos(position), 2, 4, false, true, 0)
                    .stream()
                    .anyMatch(blockPos -> !mc.world.getBlockState(blockPos).isAir() &&
                            bb.intersects(new AxisAlignedBB(blockPos)) &&
                            AxisAlignedBB.calcSideHit(new AxisAlignedBB(blockPos.add(0, 1, 0)), position, new double[]{
                2D
            },null, 0.1f, 0.1f,0.1f) == Direction.DOWN);
        }


    }


}
