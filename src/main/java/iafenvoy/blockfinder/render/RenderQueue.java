package iafenvoy.blockfinder.render;

import net.minecraft.client.util.math.MatrixStack;

import java.util.*;
import java.util.function.Consumer;

public class RenderQueue {

    private final static RenderQueue INSTANCE = new RenderQueue();

    private final Map<String, List<Consumer<MatrixStack>>> typeRunnableMap = new HashMap<>();
    private MatrixStack matrixStack = null;

    public static RenderQueue get() {
        return INSTANCE;
    }

    public void add(String type, Consumer<MatrixStack> runnable) {
        assert type != null;
        assert runnable != null;
        if (!this.typeRunnableMap.containsKey(type))
            this.typeRunnableMap.put(type, new ArrayList<>());
        List<Consumer<MatrixStack>> runnableList = this.typeRunnableMap.get(type);
        runnableList.add(runnable);
    }

    public void setTrackRender(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
    }

    public void onRender(String type) {
        if (this.matrixStack == null || !this.typeRunnableMap.containsKey(type)) return;
        this.typeRunnableMap.get(type).forEach(r -> r.accept(this.matrixStack));
    }

}
