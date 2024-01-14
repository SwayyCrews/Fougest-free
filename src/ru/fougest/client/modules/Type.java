package ru.fougest.client.modules;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Type {
    Fight("Aura, AutoTotem, Other..", "A",0f),
    Movement("Strafe, Speed, Other..", "B", 0f),
    Render("Tracers, ESP, Other...", "C", 0f),
    Player("Auto Potion, Other...", "D", 0f),
    Util("MCF, MCP, Other...", "E", 0f),
    Theme("Установить тему чита", "F", 0f);

    public final String description;
    public final String image;
    public double anim;
}
