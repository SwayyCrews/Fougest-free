package ru.fougest.client.events.impl.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.item.Item;
import ru.fougest.client.events.Event;

/**
 * @author dedinside
 * @since 09.06.2023
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EventCalculateCooldown extends Event {

    public Item itemStack;
    public float cooldown;

    public EventCalculateCooldown(Item item) {
        this.itemStack = item;
    }
}

