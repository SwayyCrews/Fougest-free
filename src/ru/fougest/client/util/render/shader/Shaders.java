package ru.fougest.client.util.render.shader;

import ru.fougest.client.util.render.shader.core.Shader;
import ru.fougest.client.util.render.shader.impl.AlphaShader;

import java.util.ArrayList;
import java.util.List;

public class Shaders {

    public List<Shader> shaderList = new ArrayList<>();

    public Shaders() {

        shaderList.addAll(List.of(
           new AlphaShader("")
        ));

    }

}
