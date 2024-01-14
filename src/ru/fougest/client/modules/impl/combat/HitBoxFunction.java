package ru.fougest.client.modules.impl.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import ru.fougest.client.events.Event;
import ru.fougest.client.events.impl.render.EventRender;
import ru.fougest.client.modules.Function;
import ru.fougest.client.modules.FunctionAnnotation;
import ru.fougest.client.modules.Type;
import ru.fougest.client.modules.settings.imp.BooleanOption;
import ru.fougest.client.modules.settings.imp.SliderSetting;

/**
 * @author dedinside
 * @since 07.06.2023
 */
@FunctionAnnotation(name = "HitBox", type = Type.Fight)
public class HitBoxFunction extends Function {

    public final SliderSetting size = new SliderSetting("������", 0.2f, 0.f, 3.f, 0.05f);
    public final BooleanOption invisible = new BooleanOption("���������", false);

    public HitBoxFunction() {
        addSettings(size, invisible);
    }

    @Override
    public void onEvent(final Event event) {
        handleEvent(event);
    }

    /**
     * ������������ �������.
     */
    private void handleEvent(Event event) {
        // ��������, �������� �� ������� ����� EventRender � �������� �� 3D-�����������
        if (!(event instanceof EventRender && ((EventRender) event).isRender3D()))
            return;

        // ��������, ������� �� ����� �����������
        if (invisible.get())
            return;

        // ���������� ������������� ������ �������� ��� �������
        adjustBoundingBoxesForPlayers();
    }

    /**
     * ����������� ������� ������ ��� ��������� ������.
     */
    private void adjustBoundingBoxesForPlayers() {
        // ������� ���� ������� � ����
        for (PlayerEntity player : mc.world.getPlayers()) {
            // ��������, ����� �� ���������� ������� ������ ��� ������������� ��������
            if (shouldSkipPlayer(player))
                continue;

            // ���������� ��������� ������� � ��������� ������ �������� ��� ������
            float sizeMultiplier = this.size.getValue().floatValue() * 2.5F;
            setBoundingBox(player, sizeMultiplier);
        }
    }

    /**
     * �������� �� ��������� ������
     */
    private boolean shouldSkipPlayer(PlayerEntity player) {
        // ��������, ����� �� ���������� ������� ������ ��� ������������� ��������
        // ����� ������������, ���� ��� ������� ����� (mc.player) ��� ���� ����� �����
        return player == mc.player || !player.isAlive();
    }

    /**
     * ������������� ����� ������ ��� ��������
     */
    private void setBoundingBox(Entity entity, float size) {
        // ���������� ������ �������� ��� �������� � ��������� ��
        AxisAlignedBB newBoundingBox = calculateBoundingBox(entity, size);
        entity.setBoundingBox(newBoundingBox);
    }

    /**
     * ���������� ��������� ����������� � ������������ ����� �������� ��� �������� � ��������
     * � ����������� ������ �������� ��������
     */
    private AxisAlignedBB calculateBoundingBox(Entity entity, float size) {
        // ���������� ��������� ����������� � ������������ ����� �������� ��� ��������
        double minX = entity.getPosX() - size;
        double minY = entity.getBoundingBox().minY;
        double minZ = entity.getPosZ() - size;
        double maxX = entity.getPosX() + size;
        double maxY = entity.getBoundingBox().maxY;
        double maxZ = entity.getPosZ() + size;

        // �������� � ����������� ������ �������� ��������
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}

