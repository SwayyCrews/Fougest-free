package ru.fougest.client.events.impl.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.entity.Entity;
import ru.fougest.client.events.Event;

/**
 * @author dedinside
 * @since 24.06.2023
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class EventInteractEntity extends Event {
    private Entity entity;

}
