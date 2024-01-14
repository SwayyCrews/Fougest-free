package ru.fougest.client.modules.settings.imp;


import ru.fougest.client.modules.settings.Setting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class BooleanOption extends Setting {
    private boolean value;
    private String desc;
    public float anim;

    public BooleanOption(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public BooleanOption(String name, String desc, boolean value) {
        super(name);
        this.value = value;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public boolean get() {
        return value;
    }

    public void toggle() {
        value = !value;
    }

    public void set(boolean value) {
        this.value = value;
    }

    public BooleanOption setVisible(Supplier<Boolean> bool) {
        visible = bool;
        return this;
    }

    @Override
    public SettingType getType() {
        return SettingType.BOOLEAN_OPTION;
    }

    public List<BooleanOption> getValues() {
        return Arrays.asList(this);
    }

    public boolean getValue() {
        return value;
    }
}